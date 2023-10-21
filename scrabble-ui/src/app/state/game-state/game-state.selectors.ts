import {AppState} from "../app.state";
import {createSelector} from "@ngrx/store";
import {GameState} from "./game-state";

export const selectGameState = (state: AppState) => state.gameState;
export const selectBoardUUID = createSelector(
  selectGameState,
  (state: GameState) => state.boardUUID
);
