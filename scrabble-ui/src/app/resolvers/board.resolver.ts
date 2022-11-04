import { Injectable } from '@angular/core';
import {
  Router, Resolve,
  RouterStateSnapshot,
  ActivatedRouteSnapshot
} from '@angular/router';
import { Observable, of } from 'rxjs';
import {Board} from "../clients/board-manager/model/board";
import {BoardManagerService} from "../clients/board-manager/board-manager.service";

@Injectable({
  providedIn: 'root'
})
export class BoardResolver implements Resolve<Board> {

  constructor(private boardManager: BoardManagerService) {

  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Board> | Promise<Board> | Board {
    const boardId = route.params['uuid'];
    return this.boardManager.getBoard(boardId);
  }
}
