import {EventEmitter, Injectable} from '@angular/core';
import {TileManagerService} from "../clients/tile-manager/tile-manager.service";
import {Element as GuiElement} from "../game-ui/model/element";
import {Word as GuiWord} from "../game-ui/model/word";
import {Observable} from "rxjs";
import {GameManagerService} from "../clients/game-manager/game-manager.service";
import {Game} from "../clients/game-manager/model/game";

@Injectable({
  providedIn: 'root'
})
export class GameService {

  public potentialWordLetterEvent = new EventEmitter<GuiElement>();

  constructor(private tileManager: TileManagerService, private gameManagerService: GameManagerService) {
  }

  getGame(id: string): Observable<Game> {
    return this.gameManagerService.getGame(id);
  }

  makeMove(move: GameMoveRequest): Observable<MoveResult> {
    return this.gameManagerService.makeMove(move);
  }

  getCharset(boardId: string): Observable<string[]> {
      return this.tileManager.getCharset(boardId);
  }

  showPotentialWord(word: GuiWord) {
    this.clearPotentialWord();
    for (const element of word.elements) {
      this.potentialWordLetterEvent.emit(element);
    }
  }

  clearPotentialWord() {
    this.potentialWordLetterEvent.emit(new GuiElement(-1, -1, "", -1, false, false));
  }
}

export interface GameMoveRequest {
  gameId: string;
  playerId: string;
  tiles: Tile[];
}

export interface Tile {
  x: number;
  y: number;
  letter: string;
  points: number;
  blank: boolean;
}

export interface MoveResult {
  actualPlayerId: string;
  playerMoves: PlayerMove[];
}

export interface PlayerMove {
  playerId: string;
  moveTiles: Tile[];
  movePoints: number;
  allPoints: number;
}
