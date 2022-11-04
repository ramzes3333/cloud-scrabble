import {Component, Input, OnInit} from '@angular/core';
import {Board} from "../../clients/board-manager/model/board";
import { ActivatedRoute, ParamMap } from '@angular/router';
import {GameService} from "../../services/game.service";
import {Move} from "../model/move";

@Component({
  selector: 'app-board',
  templateUrl: './board.component.html',
  styleUrls: ['./board.component.css']
})
export class BoardComponent implements OnInit {

  _board?: Board;

  constructor(private route: ActivatedRoute, private gameService: GameService) { }

  ngOnInit(): void {
    this.route.data.subscribe(data => {
      this._board = data['board'];
      this.gameService.init(data['board'].id);
    });
  }

  get board(): Board | undefined {
    return this._board;
  }
}
