import {Board} from "../../clients/board-manager/model/board";

export interface GameState {
    boardUUID: string | null;
    board: Board | null;
    started: boolean;
}
