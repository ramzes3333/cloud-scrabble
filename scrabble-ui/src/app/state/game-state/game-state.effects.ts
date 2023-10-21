import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from "@ngrx/effects";
import {Store} from "@ngrx/store";
import {AppState} from "../app.state";
import {GameCreatorService} from "../../services/game-creator.service";
import { create } from './game-state.actions';

@Injectable()
export class GameEffects {
  constructor(
    private actions$: Actions,
    private store: Store<AppState>,
    private gameCreatorService: GameCreatorService
  ) {
  }

  createGame$ = createEffect(() =>
    this.actions$.pipe(
      ofType(create),
      switchMap(() =>
        // Call the getTodos method, convert it to an observable
        from(this.todoService.getTodos()).pipe(
          // Take the returned value and return a new success action containing the todos
          map((todos) => loadTodosSuccess({todos: todos})),
          // Or... if it errors return a new failure action containing the error
          catchError((error) => of(loadTodosFailure({error})))
        )
      )
    )
  );
}
