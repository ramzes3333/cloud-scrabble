import {createAction, props} from '@ngrx/store';

export const create = createAction('[Game State Component] Create', props<CreateGameRequest>());
export const init = createAction('[Game State Component] Init', props<{ boardUUID: string }>());
export const start = createAction('[Game State Component] Start game', props<{ boardUUID: string }>());
export const move = createAction('[Game State Component] Move', props<{ boardUUID: string }>());
export const confirm = createAction('[Game State Component] Confirm', props<{ boardUUID: string }>());
export const resolve = createAction('[Game State Component] Resolve', props<{ boardUUID: string }>());

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
