import {Component, OnInit} from '@angular/core';
import {Word} from "../../clients/board-manager/model/solution/word";
import {Word as GuiWord} from "../model/word";
import {Element} from "../../clients/board-manager/model/solution/element";
import {Element as GuiElement} from "../model/element";
import {Bonus} from "../../clients/board-manager/model/bonus";
import {Bonus as GuiBonus} from "../model/bonus";
import {TableVirtualScrollDataSource} from "ng-table-virtual-scroll";
import {select, Store} from "@ngrx/store";
import {GameState, Move} from "../../state/game-state/game-state";
import {selectMoveHistory, selectSolution, selectStartedFlag} from "../../state/game-state/game-state.selectors";
import {
  clearSuggestedWord,
  makeMove,
  putSuggestedWord,
  resolve,
  showSuggestedWord,
  start
} from "../../state/game-state/game-state.actions";
import {PlayerMove} from "../../services/game.service";
import {MatTableDataSource} from "@angular/material/table";

@Component({
  selector: 'app-game-panel',
  templateUrl: './game-panel.component.html',
  styleUrls: ['./game-panel.component.css']
})
export class GamePanelComponent implements OnInit {

  solutionColumns: string[] = ['words', 'points'];
  moveHistoryColumns: string[] = ['moves', 'points'];
  words = new TableVirtualScrollDataSource<GuiWord>([]);
  moveHistory = new MatTableDataSource<Move>();
  isLoading: boolean = false;

  moveHistory$ = this.store.select(selectMoveHistory);
  solution$ = this.store.select(selectSolution);
  gameStarted$ = this.store.pipe(select(selectStartedFlag));

  constructor(private store: Store<{ gameState: GameState }>) { }

  ngOnInit(): void {
    this.moveHistory$.subscribe(moveHistory => {
      this.moveHistory.data = moveHistory.slice().reverse();
    });

    this.solution$.subscribe(solution => {
      this.words = new TableVirtualScrollDataSource<GuiWord>([]);
      if(solution)
        for (const w of solution.words) {
          let elements: Element[] = [];
          let relatedWords: GuiWord[] = [];
          let bonuses: GuiBonus[] = [];
          let word: GuiWord = new GuiWord(w.points, elements, relatedWords, bonuses);

          for (const el of w.elements) {
            elements.push(this.convertElement(el));
          }
          for (const rw of w.relatedWords) {
            relatedWords.push(this.convertRelatedWord(rw));
          }
          for (const b of w.bonuses) {
            bonuses.push(this.convertBonus(b));
          }
          //this.words.push(word);
          this.words.data.push(word);
        }
      this.isLoading = false;
    });
  }

  startGame() {
    this.words = new TableVirtualScrollDataSource<GuiWord>([]);
    this.store.dispatch(start());
  }

  confirmMove() {
    this.words = new TableVirtualScrollDataSource<GuiWord>([]);
    this.store.dispatch(makeMove());
  }

  resolve() {
    this.words = new TableVirtualScrollDataSource<GuiWord>([]);
    this.isLoading = true;
    this.store.dispatch(resolve());
  }

  getCharacters(value: string): string[] {
    return value.split('');
  }

  private convertElement(el: Element): GuiElement {
    return new GuiElement(el.x, el.y, el.letter, el.points, el.blank, el.onBoard);
  }

  private convertBonus(b: Bonus): GuiBonus {
    return b as GuiBonus;
  }

  private convertRelatedWord(rw: Word): GuiWord {
    let elements: GuiElement[] = [];
    let relatedWords: GuiWord[] = [];
    let bonuses: GuiBonus[] = [];
    let word: GuiWord = new GuiWord(rw.points, elements, relatedWords, bonuses);

    for (const el of rw.elements) {
      elements.push(this.convertElement(el));
    }
    for (const b of rw.bonuses) {
      bonuses.push(this.convertBonus(b));
    }
    return word;
  }

  showPotentialWord(word: GuiWord) {
    this.store.dispatch(showSuggestedWord(word));
  }

  clearPotentialWord() {
    this.store.dispatch(clearSuggestedWord());
  }

  putSuggestedWord(word: GuiWord) {
    this.store.dispatch(putSuggestedWord(word));
  }

  relatedWordsTooltip(word: GuiWord): string {
    let relatedWords: string = "";
    for (const rw of word.relatedWords) {
      relatedWords += rw.getWordAsString() + " (" + rw.points + ")\r\n";
    }
    return relatedWords;
  }

  bonusesTooltip(word: GuiWord): string {
    let bonuses: string = "";
    for (const b of word.bonuses) {
      switch (b.toString()) {
        case "DoubleWordScore":
          bonuses += "Double word score";
          break;
        case "TripleWordScore":
          bonuses += "Triple word score";
          break;
        case "DoubleLetterScore":
          bonuses += "Double letter score";
          break;
        case "TripleLetterScore":
          bonuses += "Triple letter score";
          break;
      }
      bonuses += "\r\n";
    }
    return bonuses;
  }
}
