import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from "@ngrx/effects";
import {Store} from "@ngrx/store";
import {AppState} from "../app.state";
import {GameCreatorService} from "../../services/game-creator.service";
import {
  makeMove,
  create,
  createSuccess,
  failure,
  init, initGameLoaded, initSuccess, makeMoveSuccess,
  preview,
  previewSuccess, refreshBoard,
  resolve,
  resolveSuccess, setCharset, moveValidateError, moveValidateSuccess
} from './game-state.actions';
import {catchError, from, mergeMap, of, switchMap, withLatestFrom} from "rxjs";
import {map} from "rxjs/operators";
import {GameResolverService} from "../../services/game-resolver.service";
import {selectActualPlayerId, selectBoard, selectBoardId, selectGameState} from "./game-state.selectors";
import {BoardService} from "../../services/board.service";
import {HttpErrorResponse} from "@angular/common/http";
import {
  BoardValidationResult
} from "../../clients/board-manager/model/board-validation-result";
import {GameMoveRequest, GameService} from "../../services/game.service";

@Injectable()
export class GameEffects {
  constructor(
    private actions$: Actions,
    private store: Store<AppState>,

    private gameCreatorService: GameCreatorService,
    private gameResolverService: GameResolverService,
    private boardService: BoardService,
    private gameService: GameService
  ) {
  }

  create$ = createEffect(() =>
    this.actions$.pipe(
      ofType(create),
      switchMap((request) =>
        from(this.gameCreatorService.createGame(request)).pipe(
          map((response) => createSuccess(response)),
          catchError((error) => of(failure({error})))
        )
      )
    )
  );

  preview$ = createEffect(() =>
    this.actions$.pipe(
      ofType(preview),
      switchMap(() =>
        from(this.boardService.getBoardPreview()).pipe(
          map((response) => previewSuccess(response)),
          catchError((error) => of(failure({error})))
        )
      )
    )
  );

  initGetGame$ = createEffect(() =>
    this.actions$.pipe(
      ofType(init),
      switchMap((request) =>
        from(this.gameService.getGame(request.gameId)).pipe(
          map((game) => initGameLoaded(game)),
          catchError((error) => of(failure({error})))
        )
      )
    )
  );

  initCharset = createEffect(() =>
    this.actions$.pipe(
      ofType(initGameLoaded),
      switchMap((game) =>
        from(this.gameService.getCharset(game.boardId)).pipe(
          map((charset) => setCharset({charset: charset})),
          catchError((error) => of(failure({error})))
        )
      )
    )
  );

  initGetBoard$ = createEffect(() =>
    this.actions$.pipe(
      ofType(initGameLoaded),
      switchMap((game) =>
        from(this.boardService.getBoard(game.boardId)).pipe(
          map((board) => initSuccess({game: game, board: board})),
          catchError((error) => of(failure({error})))
        )
      )
    )
  );

  resolve$ = createEffect(() =>
    this.actions$.pipe(
      ofType(resolve),
      withLatestFrom(this.store.select(selectActualPlayerId), this.store.select(selectBoard)),
      switchMap(([action, actualPlayerId, board]) => {
        if (actualPlayerId && board) {
          return from(this.gameResolverService.resolve(actualPlayerId!, board!)).pipe(
            map((solution) => resolveSuccess(solution)),
            catchError((error) => of(failure({error})))
          )
        } else {
          return of(failure({error: 'Board or actual player is undefined'}));
        }
      })
    )
  );

  confirm$ = createEffect(() =>
    this.actions$.pipe(
      ofType(makeMove),
      withLatestFrom(this.store.select(selectBoard)),
      switchMap(([action, board]) =>
        from(this.boardService.validateBoard(board!)).pipe(
          map((validationResult) => moveValidateSuccess()),
          catchError((error: HttpErrorResponse) => {
            if (error.status === 409) {
              const validationResult: BoardValidationResult = error.error;
              return of(moveValidateError(validationResult));
            }
            return of(failure({error: error.message}));
          })
        )
      )
    )
  );

  makeMove$ = createEffect(() =>
    this.actions$.pipe(
      ofType(moveValidateSuccess),
      withLatestFrom(this.store.select(selectGameState)),
      switchMap(([action, gameState]) => {
        const movableFields = gameState.board?.fields.filter(field => field.letter?.movable) || [];
        const tiles = movableFields.map(field => ({
          x: field.x,
          y: field.y,
          letter: field.letter!.letter,
          points: field.letter!.points,
          blank: field.letter!.blank
        }));

        const gameMoveRequest: GameMoveRequest = {
          gameId: gameState.gameId!,
          playerId: gameState.actualPlayerId!,
          tiles: tiles
        };

        return this.gameService.makeMove(gameMoveRequest).pipe(
          map(moveResult => makeMoveSuccess(moveResult)),
          catchError(error => of(failure({error})))
        );
      })
    )
  );

  makeMoveSuccess$ = createEffect(() =>
    this.actions$.pipe(
      ofType(makeMoveSuccess),
      withLatestFrom(this.store.select(selectBoardId)),
      switchMap(([moveResult, boardId]) => {
        if (boardId) {
          return from(this.boardService.getBoard(boardId)).pipe(
            map((board) => refreshBoard(board)),
            catchError((error) => of(failure({error})))
          );
        } else {
          return of(failure({error: 'Board ID is undefined'}));
        }
      })
    )
  );
}
