import { Component, OnInit } from '@angular/core';
import {GameService} from "../../services/game.service";
import {Word} from "../../clients/board-manager/model/solution/word";
import {Word as GuiWord} from "../model/word";
import {Element} from "../../clients/board-manager/model/solution/element";
import {Element as GuiElement} from "../model/element";

@Component({
  selector: 'app-game-panel',
  templateUrl: './game-panel.component.html',
  styleUrls: ['./game-panel.component.css']
})
export class GamePanelComponent implements OnInit {

  displayedColumns: string[] = ['words', 'points'];
  words: GuiWord[] = [];
  isLoading: boolean = false;

  constructor(private gameService: GameService) { }

  ngOnInit(): void {
    this.gameService.solutionEvent.subscribe(solution => {
      this.words = [];
      for (const w of solution.words) {
        let elements: Element[] = [];
        let relatedWords: GuiWord[] = [];
        let word: GuiWord = new GuiWord(w.points, elements, relatedWords);

        for (const el of w.elements) {
          elements.push(this.convertElement(el));
        }
        for (const rw of w.relatedWords) {
          relatedWords.push(this.convertRelatedWord(rw));
        }
        this.words.push(word);
      }
      this.isLoading = false;
    });
  }

  startGame() {
    this.gameService.startGame();
    this.words = [];
  }

  confirmMove() {
    this.gameService.confirmMove();
    this.words = [];
  }

  resolve() {
    this.words = [];
    this.isLoading = true;
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

  private convertRelatedWord(rw: Word): GuiWord {
    let elements: Element[] = [];
    let relatedWords: Word[] = [];
    let word: GuiWord = new GuiWord(rw.points, elements, relatedWords);

    for (const el of rw.elements) {
      elements.push(this.convertElement(el));
    }
    return word;
  }

  showPotentialWord(word: GuiWord) {
    this.gameService.showPotentialWord(word);
  }

  clearPotentialWord() {
    this.gameService.clearPotentialWord();
  }
}
