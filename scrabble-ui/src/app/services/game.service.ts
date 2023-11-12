import {Injectable} from '@angular/core';
import {TileManagerService} from "../clients/tile-manager/tile-manager.service";
import {Observable} from "rxjs";
import {GameManagerService} from "../clients/game-manager/game-manager.service";
import {Game} from "../clients/game-manager/model/game";
import {MatSnackBar} from "@angular/material/snack-bar";

@Injectable({
  providedIn: 'root'
})
export class GameService {

  constructor(private tileManager: TileManagerService, private gameManagerService: GameManagerService,
              private snackBar: MatSnackBar) {
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

  showError(error: string) {
    this.snackBar.open(`${error}`, 'Zamknij', {
      duration: 5000,
      panelClass: ['background-red'],
      horizontalPosition: 'right',
      verticalPosition: 'top'
    });
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
