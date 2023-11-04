import {Component, Input, OnInit} from '@angular/core';
import {Letter as BoardLetter} from "../../clients/board-manager/model/letter";
import {Letter as TileLetter} from "../../clients/tile-manager/model/letter";
import {Letter as GuiLetter} from "../model/letter";
import {CdkDragDrop, moveItemInArray, transferArrayItem} from "@angular/cdk/drag-drop";
import {GameService} from "../../services/game.service";
import {MovableField, MovableFieldSource} from "../model/movable-field";
import {Move} from "../model/move";
import {MoveType} from "../model/move-type";
import {select, Store} from "@ngrx/store";
import {GameState} from "../../state/game-state/game-state";
import {from, Subscription} from "rxjs";
import {selectBoard, selectStartedFlag} from "../../state/game-state/game-state.selectors";
import {move} from "../../state/game-state/game-state.actions";
import {MatSnackBar, MatSnackBarHorizontalPosition} from "@angular/material/snack-bar";

@Component({
  selector: 'app-rack',
  templateUrl: './rack.component.html',
  styleUrls: ['./rack.component.css']
})
export class RackComponent implements OnInit {

  @Input() rackNumber!: number;
  @Input() movableFieldSource!: MovableFieldSource;

  gameStarted$ = this.store.pipe(select(selectStartedFlag));

  private _rack: Rack = new Rack([]);
  private subscriptions: Subscription[] = [];

  constructor(private snackBar: MatSnackBar, private store: Store<{ gameState: GameState }>) { }

  ngOnInit(): void {
    this.subscriptions.push(
      this.store.pipe(select(selectBoard)).subscribe((board) => {
        if (board && board.racks && board.racks.length >= this.rackNumber && board.racks[this.rackNumber].letters) {
          this._rack = new Rack(this.convertBoardLettersToMovableFields(0, board.racks[this.rackNumber].letters));
        } else {
          this._rack = new Rack([]);
        }
      })
    );
  }

  private convertTileLettersToMovableFields(startIndex: number, letters: TileLetter[]): MovableField[] {
    let movableFields: MovableField[] = [];
    for (const letter of letters) {
      movableFields.push(new MovableField(startIndex++, null,
        this.convert(letter.letter, letter.letter == ' ', letter.points),
        this.movableFieldSource));
    }
    return movableFields;
  }

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
    let fromSource = event.previousContainer.data[event.previousIndex].source;

    if(fromSource > 0) {
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

    this.store.dispatch(move(this.extractMoveFromDropEvent(fromX, fromY, fromSource, event)));
  }

  private extractMoveFromDropEvent(fromX: number, fromY: number | null, fromSource: MovableFieldSource, event: CdkDragDrop<MovableField[]>) {

    return new Move(
      this.getMoveType(fromY),
      fromX,
      fromY,
      fromSource,
      new MovableField(
        event.container.data[event.currentIndex].x, null,
        new GuiLetter(
          event.container.data[event.currentIndex].letter.letter,
          event.container.data[event.currentIndex].letter.blank,
          event.container.data[event.currentIndex].letter.points
        ),
        event.container.data[event.currentIndex].source
      )
    );
  }

  private getMoveType(fromY: number | null) {
    return fromY != null ? MoveType.FROM_BOARD : MoveType.FROM_RACK;
  }

  get rack(): Rack {
    return this._rack;
  }

  ngOnDestroy() {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }
}

export class Rack {
  public movableFields: MovableField[];

  constructor(movableFields: MovableField[]) {
    this.movableFields = movableFields;
  }
}
