import {Injectable} from '@angular/core';
import {Observable} from "rxjs";
import {map} from "rxjs/operators";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class ExternalServices {

  constructor(private http: HttpClient) { }

  getWordDefinition(word: string): Observable<WordDefinition> {
    let code: string = 'pl';
    return this.http.get<WordDefinition>(`external-services/api/word-definition/language/${code}/word/${word}`,
      {
        observe: 'response'
      }
    ).pipe(
      map(response => response.body!)
    );
  }
}

export interface WordDefinition {
  definition: string;
}
