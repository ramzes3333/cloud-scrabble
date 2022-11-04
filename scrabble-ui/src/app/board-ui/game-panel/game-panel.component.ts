import { Component, OnInit } from '@angular/core';
import {GameService} from "../../services/game.service";

@Component({
  selector: 'app-game-panel',
  templateUrl: './game-panel.component.html',
  styleUrls: ['./game-panel.component.css']
})
export class GamePanelComponent implements OnInit {

  constructor(private gameService: GameService) { }

  ngOnInit(): void {
  }

  startGame() {
    this.gameService.startGame();
  }

  confirmMove() {
    this.gameService.confirmMove();
  }

  isNotStarted() {
    return !this.gameService.isStarted();
  }

  isStarted() {
    return this.gameService.isStarted();
  }
}
