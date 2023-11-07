import { Injectable } from '@angular/core';
import {BoardManagerService} from "../clients/board-manager/board-manager.service";
import {Board} from "../clients/board-manager/model/board";
import {EMPTY, Observable} from "rxjs";
import {Solution} from "../clients/board-manager/model/solution/solution";

@Injectable({
  providedIn: 'root'
})
export class GameResolverService {

  constructor(private boardManager: BoardManagerService) { }

  resolve(playerId: string, board: Board | null) : Observable<Solution>{
    if(board != null) {
      return this.boardManager.resolve(playerId, board);
    } else {
      return EMPTY;
    }
  }
}
