import {Component, Input, OnInit} from '@angular/core';
import {Letter as BoardLetter} from "../../clients/board-manager/model/letter";
import {Letter, Letter as GuiLetter} from "../model/letter";
import {CdkDrag, CdkDragDrop, CdkDropList, moveItemInArray, transferArrayItem} from "@angular/cdk/drag-drop";
import {MovableField} from "../model/movable-field";
import {Move} from "../model/move";
import {select, Store} from "@ngrx/store";
import {GameState} from "../../state/game-state/game-state";
import {combineLatest, Subscription} from "rxjs";
import {
  selectActualPlayer,
  selectRacks,
  selectStartedFlag
} from "../../state/game-state/game-state.selectors";
import {move} from "../../state/game-state/game-state.actions";
import {MatSnackBar} from "@angular/material/snack-bar";
import {map} from "rxjs/operators";
import {BoardElement} from "../model/board-element";
import {Type} from "../../clients/game-manager/model/game";

@Component({
  selector: 'app-rack',
  templateUrl: './rack.component.html',
  styleUrls: ['./rack.component.css']
})
export class RackComponent implements OnInit {

  @Input() rackNumber!: number;
  @Input() movableFieldSource!: BoardElement;

  gameStarted$ = this.store.pipe(select(selectStartedFlag));
  actualPlayer$ = this.store.pipe(select(selectActualPlayer));

  isRackDisabled: boolean = true;
  playerId?: string;
  playerName?: string;
  points?: number;
  movableFields: MovableField[] = [];
  private subscriptions: Subscription[] = [];

  constructor(private snackBar: MatSnackBar, private store: Store<{ gameState: GameState }>) { }

  ngOnInit(): void {
    this.subscriptions.push(
      this.store.pipe(select(selectRacks)).subscribe((racks) => {
        if (racks && racks.length >= this.rackNumber && racks[this.rackNumber].letters) {
          this.movableFields = this.convertBoardLettersToMovableFields(0, racks[this.rackNumber].letters);
          this.playerId = racks[this.rackNumber].playerId;
          this.playerName = racks[this.rackNumber].playerName;
          this.points = racks[this.rackNumber].points;
        } else {
          this.movableFields = [];
        }
      })
    );
    this.subscriptions.push(
      this.isRackDisabled$.subscribe((value) => {
        this.isRackDisabled = value;
      })
    );
  }

  isRackDisabled$ = combineLatest([this.gameStarted$, this.actualPlayer$]).pipe(
    map(([gameStarted, actualPlayer]) => {
      const isGameStarted = gameStarted ? gameStarted : false;
      const isActualPlayer = actualPlayer ? actualPlayer.id === this.playerId : false;
      return actualPlayer?.type === Type.BOT || !isGameStarted || !isActualPlayer;
    })
  );

  canDrop = (drag: CdkDrag, drop: CdkDropList) => {
    return !this.isRackDisabled;
  };

  private convertBoardLettersToMovableFields(startIndex: number, letters: BoardLetter[]): MovableField[] {
    let movableFields: MovableField[] = [];
    for (const letter of letters) {
      movableFields.push(new MovableField(startIndex++, null,
        this.convert(letter.letter, letter.blank, letter.points),
        this.movableFieldSource));
    }
    return movableFields;
  }

  private convert(letter: string, blank: boolean, points: number): GuiLetter {
    return new GuiLetter(letter, blank, points);
  }

  dropped(event: CdkDragDrop<MovableField[]>) {
    let fromX = event.previousContainer.data[event.previousIndex].x;
    let fromY = event.previousContainer.data[event.previousIndex].y;
    let from = event.previousContainer.data[event.previousIndex].source;

    if(from > 0 && from != this.movableFieldSource) {
      this.snackBar.open('Nie możesz przenosić liter między stojakami', 'Zamknij', {
        duration: 5000,
        panelClass: ['background-red'],
        horizontalPosition: 'right',
        verticalPosition: 'top'
      });
      return;
    }

    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    } else {
      transferArrayItem(event.previousContainer.data,
        event.container.data,
        event.previousIndex,
        event.currentIndex);
    }
    event.container.data[event.currentIndex].x = event.currentIndex;
    event.container.data[event.currentIndex].y = null;
    event.container.data[event.currentIndex].source = this.movableFieldSource;
    if(event.container.data[event.currentIndex].letter.blank) {
      event.container.data[event.currentIndex].letter.letter = " ";
    }

    this.store.dispatch(move(this.extractMoveFromDropEvent(fromX, fromY, from, event)));
  }

  private extractMoveFromDropEvent(fromX: number, fromY: number | null, from: BoardElement, event: CdkDragDrop<MovableField[]>) {
    return new Move(
      new Letter(
        event.container.data[event.currentIndex].letter.letter,
        event.container.data[event.currentIndex].letter.blank,
        event.container.data[event.currentIndex].letter.points
      ),
      fromX,
      fromY,
      from,
      event.container.data[event.currentIndex].x,
      null,
      event.container.data[event.currentIndex].source
    );
  }

  ngOnDestroy() {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }
}
