import { Injectable } from '@angular/core';
import {Observable} from "rxjs";
import {GameManagerService} from "../clients/game-manager/game-manager.service";
import {Player} from "../model/player";

@Injectable({
  providedIn: 'root'
})
export class GameCreatorService {

  constructor(private gameManagerService: GameManagerService) { }

  createGame(req: GameStartRequest) : Observable<GameStartResponse> {
    return this.gameManagerService.createGame(req);
  }
}

export interface GameStartRequest {
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

export interface GameStartResponse {
  id: number;
  boardId: string;
  players: Player[];
}


