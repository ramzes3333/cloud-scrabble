import { Component, OnInit } from '@angular/core';
import {GameService} from "../../services/game.service";
import {Word} from "../model/word";
import {Element} from "../../clients/board-manager/model/solution/element";
import {Element as GuiElement} from "../model/element";

@Component({
  selector: 'app-game-panel',
  templateUrl: './game-panel.component.html',
  styleUrls: ['./game-panel.component.css']
})
export class GamePanelComponent implements OnInit {

  displayedColumns: string[] = ['words', 'points'];
  words: Word[] = [];

  constructor(private gameService: GameService) { }

  ngOnInit(): void {
    this.gameService.solutionEvent.subscribe(solution => {
      this.words = [];
      for (const w of solution.words) {
        let elements: Element[] = [];
        let word: Word = new Word(w.points, elements);

        for (const el of w.elements) {
          elements.push(this.convertElement(el));
        }
        this.words.push(word);
      }
    });
  }

  startGame() {
    this.gameService.startGame();
  }

  confirmMove() {
    this.gameService.confirmMove();
  }

  resolve() {
    this.gameService.resolve();
  }

  isNotStarted() {
    return !this.gameService.isStarted();
  }

  isStarted() {
    return this.gameService.isStarted();
  }

  private convertElement(el: Element): GuiElement {
    return new GuiElement(el.x, el.y, el.letter, el.points, el.onBoard);
  }
}
