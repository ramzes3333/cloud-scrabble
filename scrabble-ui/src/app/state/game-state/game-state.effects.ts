import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from "@ngrx/effects";
import {Store} from "@ngrx/store";
import {AppState} from "../app.state";
import {GameCreatorService} from "../../services/game-creator.service";
import {
  confirm,
  create,
  createSuccess,
  failure,
  init, initGameLoaded, initSuccess,
  preview,
  previewSuccess,
  resolve,
  resolveSuccess, validateError, validateSuccess
} from './game-state.actions';
import {catchError, from, of, switchMap, withLatestFrom} from "rxjs";
import {map} from "rxjs/operators";
import {GameResolverService} from "../../services/game-resolver.service";
import {selectBoard} from "./game-state.selectors";
import {BoardService} from "../../services/board.service";
import {HttpErrorResponse} from "@angular/common/http";
import {
  BoardValidationResult
} from "../../clients/board-manager/model/board-validation-result";
import {GameService} from "../../services/game.service";

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
      withLatestFrom(this.store.select(selectBoard)),
      switchMap(([action, board]) =>
        from(this.gameResolverService.resolve(board!)).pipe(
          map((solution) => resolveSuccess(solution)),
          catchError((error) => of(failure({error})))
        )
      )
    )
  );

  confirm$ = createEffect(() =>
    this.actions$.pipe(
      ofType(confirm),
      withLatestFrom(this.store.select(selectBoard)),
      switchMap(([action, board]) =>
        from(this.boardService.validateBoard(board!)).pipe(
          map((validationResult) => validateSuccess()),
          catchError((error: HttpErrorResponse) => {
            if (error.status === 409) {
              const validationResult: BoardValidationResult = error.error;
              return of(validateError(validationResult));
            }
            // Obsługa innych błędów, jeśli jest potrzebna
            return of(failure({error: error.message}));  // Inna akcja dla innych błędów
          })
        )
      )
    )
  );
}
