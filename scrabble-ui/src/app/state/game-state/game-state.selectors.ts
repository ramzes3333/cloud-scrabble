import {AppState} from "../app.state";
import {createSelector} from "@ngrx/store";
import {GameState} from "./game-state";
import {CharacterWithPosition} from "../../clients/board-manager/model/board-validation-result";

export const selectGameState = (state: AppState) => state.gameState;

export const selectBoard = createSelector(
  selectGameState, (state: GameState) => state.board);

export const selectCharset = createSelector(
  selectGameState, (state: GameState) => state.charset);

export const selectBoardId = createSelector(
  selectGameState, (state: GameState) => state.boardId);

export const selectStartedFlag = createSelector(
  selectGameState, (state: GameState) => state.started);

export const selectActualPlayerId = createSelector(
  selectGameState, (state: GameState) => state.actualPlayerId);

export const selectSolution = createSelector(
  selectGameState, (state: GameState) => state.solution);

export const selectIncorrectFields = createSelector(
  selectGameState, (gameState: GameState) => gameState.incorrectFields);

export const selectValidationErrorsForCoordinates = (x: number, y: number) => {
  return createSelector(
    selectIncorrectFields,
    (incorrectFields: CharacterWithPosition[]) =>
      incorrectFields.filter(incorrectField => incorrectField.x === x && incorrectField.y === y)
  );
};
