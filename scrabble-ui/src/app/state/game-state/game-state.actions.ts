import {createAction, props} from '@ngrx/store';
import {CreateGameResponse} from "../../services/game-creator.service";
import {Solution} from "../../clients/board-manager/model/solution/solution";
import {BoardPreview} from "../../services/board.service";
import {Board} from "../../clients/board-manager/model/board";
import {BoardValidationResult} from "../../clients/board-manager/model/board-validation-result";
import {Move} from "../../game-ui/model/move";
import {Game} from "../../clients/game-manager/model/game";
import {MoveResult} from "../../clients/game-manager/model/move-result";

/* CREATE GAME ACTIONS */
export const create = createAction('[Game State Component] Create game', props<CreateGameRequest>());
export const createSuccess = createAction('[Game State Component] Create game success', props<CreateGameResponse>());

/* SOLUTIONS */
export const resolve = createAction('[Game State Component] Resolve board');
export const resolveSuccess = createAction('[Game State Component] Resolve board success', props<Solution>());

/* INIT */
export const init = createAction('[Game State Component] Init game', props<{ gameId: string }>());
export const initGameLoaded = createAction('[Game State Component] Init game - game loaded success', props<Game>());
export const setCharset = createAction('[Game State Component] Init game - set charset', props<{charset: string[]}>());
export const initSuccess = createAction('[Game State Component] Init game success', props<{game: Game, board: Board}>());

/* PREVIEW */
export const preview = createAction('[Game State Component] Get board preview');
export const previewSuccess = createAction('[Game State Component] Get board preview success', props<BoardPreview>());

export const start = createAction('[Game State Component] Start game');

export const move = createAction('[Game State Component] Move', props<Move>());

export const updateBlankLetter = createAction('[Game State Component] Update blank letter', props<{x: number, y: number, letter: string}>());

export const confirm = createAction('[Game State Component] Confirm');
export const validateSuccess = createAction('[Game State Component] Confirm - board validation success');
export const validateError = createAction('[Game State Component] Confirm - board validation error', props<BoardValidationResult>());
export const makeMoveSuccess = createAction('[Game State Component] Confirm - make move success', props<MoveResult>());

export const refreshBoard = createAction('[Game State Component] Refresh board', props<Board>());

export const failure = createAction('[Game State Component] Common failure', props<{ error: string }>());

export interface CreateGameRequest {
  botPlayers: BotPlayer[];
  humanPlayers: HumanPlayer[];
}

export interface BotPlayer {
  level: Level;
}

export interface HumanPlayer {
}

export enum Level {
  NEWBIE,
  BEGINNER,
  ADVANCED,
  EXPERT,
  LEGEND
}
