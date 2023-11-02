import { Injectable } from '@angular/core';
import {Observable} from "rxjs";
import {BoardManagerService} from "../clients/board-manager/board-manager.service";
import {BoardValidationResult} from "../clients/board-manager/model/board-validation-result";
import {Board} from "../model/board";
import {map} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class BoardService {

  constructor(private boardManager: BoardManagerService) { }

  getBoard(uuid: string) : Observable<Board>{
    return this.boardManager.getBoard(uuid).pipe(
      map(response => response as Board)
    );
  }

  getBoardPreview() : Observable<BoardPreview>{
    return this.boardManager.getBoardPreview();
  }

  validateBoard(board: Board) : Observable<BoardValidationResult> {
    return this.boardManager.validateBoard(board);
  }
}

export interface BoardPreview {
  fields: EmptyField[];
  boardParameters: BoardParameters;
}

export interface EmptyField {
  x: number;
  y: number;
  bonus: string;
}

export interface BoardParameters {
  horizontalSize: number;
  verticalSize: number;
}
