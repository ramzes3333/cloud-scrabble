import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import {Observable} from "rxjs";
import {Board} from "./model/board";
import {map} from 'rxjs/operators';
import {BoardValidationResult} from "./model/board-validation-result";
import {Solution} from "./model/solution/solution";
import {BoardPreview} from "./model/board-preview";

@Injectable({
  providedIn: 'root'
})
export class BoardManagerService {

  constructor(private http: HttpClient) { }

  createBoard(): Observable<Board> {
    return this.http.post<Board>("board-manager-service/api/boards",
      {},
      {
        observe: 'response'
      }
    ).pipe(
      map(response => response.body!)
    );
  }

  updateBoard(board: Board): Observable<Board> {
    return this.http.put<Board>("board-manager-service/api/boards", board);
  }

  getBoards(): Observable<Board[]> {
    return this.http.get<Board[]>("board-manager-service/api/boards",
      {
        observe: 'response'
      }
    ).pipe(
      map(response => response.body ?? [])
    );
  }

  getBoard(uuid: string): Observable<Board> {
    return this.http.get<Board>(`board-manager-service/api/boards/${uuid}`,
      {
        observe: 'response'
      }
    ).pipe(
      map(response => response.body!)
    );
  }

  getBoardPreview(): Observable<BoardPreview> {
    return this.http.get<Board>(`board-manager-service/api/boards/preview`,
      {
        observe: 'response'
      }
    ).pipe(
      map(response => response.body!)
    );
  }

  validateBoard(board: Board): Observable<BoardValidationResult> {
    return this.http.post<BoardValidationResult>(`board-manager-service/api/boards/validate`,
      board,
      {
        observe: 'response'
      }
    ).pipe(
      map(response => response.body!)
    );
  }

  resolve(board: Board): Observable<Solution> {
    return this.http.post<Solution>(`board-manager-service/api/boards/resolve`,
      board,
      {
        observe: 'response'
      }
    ).pipe(
      map(response => response.body!)
    );
  }
}
