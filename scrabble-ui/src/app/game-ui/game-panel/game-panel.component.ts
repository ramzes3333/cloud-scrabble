import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
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
  asyncResolve,
  clearSuggestedWord,
  makeMove,
  putSuggestedWord,
  resolve,
  showSuggestedWord,
  start
} from "../../state/game-state/game-state.actions";
import {MatTableDataSource} from "@angular/material/table";
import {
  BehaviorSubject,
  bufferTime,
  debounceTime,
  fromEvent,
  Observable, of,
  scan,
  startWith,
  Subject,
  Subscription
} from "rxjs";
import {WebSocketService} from "../../services/web-socket.service";
import {ExternalServices} from "../../clients/external-services/external-services.service";
import {map} from "rxjs/operators";

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
  async: boolean = false;

  definitions = new Map<string, string>();
  loadingDefinitions = new Set<string>();

  moveHistory$ = this.store.select(selectMoveHistory);
  solution$ = this.store.select(selectSolution);
  gameStarted$ = this.store.pipe(select(selectStartedFlag));

  private subscriptions: Subscription[] = [];

  constructor(private store: Store<{ gameState: GameState }>,
              private webSocketService: WebSocketService,
              private externalServices: ExternalServices) { }

  ngOnInit(): void {
    this.webSocketService.initializeWebSocketConnection();

    this.subscriptions.push(this.moveHistory$.subscribe(moveHistory => {
      this.moveHistory.data = moveHistory.slice().reverse();
    }));

    this.subscriptions.push(this.solution$.subscribe(solution => {
      if(solution && !this.async) {
        for (const w of solution.words) {
          let elements: Element[] = [];
          let relatedWords: GuiWord[] = [];
          let bonuses: GuiBonus[] = [];
          let word: GuiWord = new GuiWord(w.points, elements, relatedWords, bonuses);

          for (const el of w.elements) {
            elements.push(this.convertElement(el));
          }
          for (const rw of w.relatedWords || []) {
            relatedWords.push(this.convertRelatedWord(rw));
          }
          for (const b of w.bonuses || []) {
            bonuses.push(this.convertBonus(b));
          }
          this.words.data = [...this.words.data, word];
        }
        this.isLoading = false;
      }
    }));

    this.subscriptions.push(new Observable<Word>(subscriber => {
        this.webSocketService.solutionWordEvent.subscribe(event => {
          subscriber.next(event);
        });
      })
      .pipe(bufferTime(300))
      .subscribe((words: Word[]) => {
        if (words && words.length > 0 && this.async) {
          for (const w of words) {
            let elements: Element[] = [];
            let relatedWords: GuiWord[] = [];
            let bonuses: GuiBonus[] = [];
            let word: GuiWord = new GuiWord(w.points, elements, relatedWords, bonuses);

            for (const el of w.elements) {
              elements.push(this.convertElement(el));
            }
            for (const rw of w.relatedWords || []) {
              relatedWords.push(this.convertRelatedWord(rw));
            }
            for (const b of w.bonuses || []) {
              bonuses.push(this.convertBonus(b));
            }
            this.words.data = [...this.words.data, word].sort((a, b) => b.points - a.points);
          }
          this.isLoading = false;
        }
      })
    );
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
    if(this.async) {
      this.store.dispatch(asyncResolve());
    } else {
      this.store.dispatch(resolve());
    }
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

  getDefinition(word: GuiWord) {
    const wordString = word.getWordAsString();

    if (!this.definitions.has(wordString) && !this.loadingDefinitions.has(wordString)) {
      this.loadingDefinitions.add(wordString);

      this.externalServices.getWordDefinition(wordString).subscribe(
        (definition) => {
          this.definitions.set(wordString, definition.definition);
          this.loadingDefinitions.delete(wordString);
        },
        error => {
          this.definitions.set(wordString, 'Definicja niedostępna');
          this.loadingDefinitions.delete(wordString);
        }
      );
    }
  }

  tooltipForWord(wordString: string): string {
    if (this.loadingDefinitions.has(wordString)) {
      return 'Ładowanie definicji...';
    }
    return this.definitions.get(wordString) || 'Definicja niedostępna';
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

  ngOnDestroy() {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }
}
