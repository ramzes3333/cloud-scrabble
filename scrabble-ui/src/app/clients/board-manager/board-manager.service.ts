import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import {Observable} from "rxjs";
import {Board} from "./model/board";
import {catchError, map} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class BoardManagerService {

  constructor(private http: HttpClient) { }

  createBoard(): Observable<Board> {
    return this.http.post<Board>("board-manager/api/boards",
      {},
      {
        observe: 'response'
      }
    ).pipe(
      map(response => response.body!)
    );
  }

  updateBoard(board: Board): Observable<Board> {
    return this.http.put<Board>("board-manager/api/boards", board);
  }

  getBoards(): Observable<Board[]> {
    return this.http.get<Board[]>("board-manager/api/boards",
      {
        observe: 'response'
      }
    ).pipe(
      map(response => response.body ?? [])
    );
  }

  getBoard(uuid: string): Observable<Board> {
    return this.http.get<Board>(`board-manager/api/boards/${uuid}`,
      {
        observe: 'response'
      }
    ).pipe(
      map(response => response.body!)
    );
  }
}
