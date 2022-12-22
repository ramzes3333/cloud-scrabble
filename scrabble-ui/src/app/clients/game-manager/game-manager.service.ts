import { Injectable } from '@angular/core';
import {Observable} from "rxjs";
import {BoardValidationResult} from "../board-manager/model/board-validation-result";
import {map} from "rxjs/operators";
import {GameStartRequest} from "./model/game-start-request";
import {HttpClient} from "@angular/common/http";
import {GameStartResponse} from "./model/game-start-response";

@Injectable({
  providedIn: 'root'
})
export class GameManagerService {

  constructor(private http: HttpClient) { }

  createGame(req: GameStartRequest): Observable<GameStartResponse> {
    return this.http.post<GameStartResponse>(`game-service/api/game/start`,
      req,
      {
        observe: 'response'
      }
    ).pipe(
      map(response => response.body!)
    );
  }
}
