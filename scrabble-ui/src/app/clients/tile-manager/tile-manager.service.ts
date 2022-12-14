import { Injectable } from '@angular/core';
import {Observable} from "rxjs";
import {Letter} from "./model/letter";
import {map} from "rxjs/operators";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class TileManagerService {

  constructor(private http: HttpClient) { }

  getTiles(uuid: string, numberOfItems: number): Observable<Letter[]> {
    return this.http.post<Letter[]>(`tile-manager-service/api/boards/${uuid}/tiles/${numberOfItems}`,
      {},
      {
        observe: 'response'
      }
    ).pipe(
      map(response => response.body ?? [])
    );
  }

  getCharset(uuid: string): Observable<string[]> {
    return this.http.get<string[]>(`tile-manager-service/api/boards/${uuid}/charset?withoutBlank=true`,
      {
        observe: 'response'
      }
    ).pipe(
      map(response => response.body ?? [])
    );
  }
}
