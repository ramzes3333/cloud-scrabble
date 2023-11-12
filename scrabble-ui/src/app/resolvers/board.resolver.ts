import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, RouterStateSnapshot} from '@angular/router';
import {Observable} from 'rxjs';
import {Board} from "../clients/board-manager/model/board";
import {BoardManagerService} from "../clients/board-manager/board-manager.service";

@Injectable({
  providedIn: 'root'
})
export class BoardResolver  {

  constructor(private boardManager: BoardManagerService) {

  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Board> | Promise<Board> | Board {
    const boardId = route.params['uuid'];
    return this.boardManager.getBoard(boardId);
  }
}
