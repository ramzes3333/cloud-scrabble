import {Injectable} from '@angular/core';
import {BoardManagerService} from "../clients/board-manager/board-manager.service";
import {EMPTY, Observable} from "rxjs";
import {Solution} from "../clients/board-manager/model/solution/solution";
import {Board} from "../model/board";

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
