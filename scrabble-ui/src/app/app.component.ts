import { Component } from '@angular/core';
import {Board} from "./clients/board-manager/model/board";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Scrabble';
}
