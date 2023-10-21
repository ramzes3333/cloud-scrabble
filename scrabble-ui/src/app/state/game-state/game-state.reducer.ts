import { createReducer, on } from '@ngrx/store';
import {GameState} from "./game-state";
import {init} from "./game-state.actions";
import {Board} from "../../clients/board-manager/model/board";

export const initialState : GameState = {
  boardUUID: null,
  board: null,
  started: false
};
export const gameStateReducer = createReducer(initialState,
    on(init, (state, {boardUUID}) => ({...state, boardUUID: boardUUID})));
