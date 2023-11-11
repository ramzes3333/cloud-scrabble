import {AppState} from "../app.state";
import {createSelector} from "@ngrx/store";
import {GameState} from "./game-state";
import {CharacterWithPosition} from "../../clients/board-manager/model/board-validation-result";

export const selectGameState = (state: AppState) => state.gameState;

export const selectBoardId = createSelector(
  selectGameState, (state: GameState) => state.boardId);

export const selectFields = createSelector(
  selectGameState, (state: GameState) => state.fields);

export const selectRacks = createSelector(
  selectGameState, (state: GameState) => state.racks);

export const selectBoardParameters = createSelector(
  selectGameState, (state: GameState) => state.boardParameters);

export const selectBoard = createSelector(
  selectBoardId, selectFields, selectRacks, selectBoardParameters, (id, fields, racks, boardParameters) => {
    if (id && fields && racks && boardParameters) {
      return {
        id,
        fields,
        racks,
        boardParameters
      }
    } else {
      return undefined;
    }
  }
);

export const selectField = (x: number, y: number) => createSelector(
  selectGameState,
  (state: GameState) => {
    if (state.fields) {
      return state.fields.find(field => field.x === x && field.y === y);
    }
    return undefined;
  }
);

export const selectCharset = createSelector(
  selectGameState, (state: GameState) => state.charset);

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
