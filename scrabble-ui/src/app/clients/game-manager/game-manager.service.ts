import {Injectable} from '@angular/core';
import {Observable} from "rxjs";
import {map} from "rxjs/operators";
import {CreateGameRequest} from "./model/create-game-request";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Game} from "./model/game";
import {GameMoveRequest} from "./model/move-request";
import {MoveResult} from "./model/move-result";
import {PageableResponse} from "./model/pageable-response";

@Injectable({
  providedIn: 'root'
})
export class GameManagerService {

  constructor(private http: HttpClient) { }

  createGame(req: CreateGameRequest): Observable<Game> {
    return this.http.post<Game>(`game-service/api/games/create`,
      req,
      {
        observe: 'response'
      }
    ).pipe(
      map(response => response.body!)
    );
  }

  getGame(id: string): Observable<Game> {
    return this.http.get<Game>(`game-service/api/games/${id}`,
      {
        observe: 'response'
      }
    ).pipe(
      map(response => response.body!)
    );
  }

  getAllGames(page: number, size: number): Observable<PageableResponse<Game>> {
    return this.http.get<PageableResponse<Game>>(`game-service/api/games`,
      {
        params: new HttpParams()
          .set('page', page.toString())
          .set('size', size.toString()),
        observe: 'response'
      }
    ).pipe(
      map(response => response.body ?? {
        content: [],
        totalElements: 0,
        totalPages: 0,
        size: 0,
        number: 0
      })
    );
  }

  makeMove(req: GameMoveRequest): Observable<MoveResult> {
    return this.http.post<MoveResult>(`game-service/api/games/move`,
      req,
      {
        observe: 'response'
      }
    ).pipe(
      map(response => response.body!)
    );
  }
}
