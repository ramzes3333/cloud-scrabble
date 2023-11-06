import {Component, Input, OnInit} from '@angular/core';
import {Letter as BoardLetter} from "../../clients/board-manager/model/letter";
import {Letter as GuiLetter} from "../model/letter";
import {CdkDrag, CdkDragDrop, CdkDropList, moveItemInArray, transferArrayItem} from "@angular/cdk/drag-drop";
import {MovableField, MovableFieldSource} from "../model/movable-field";
import {Move} from "../model/move";
import {MoveType} from "../model/move-type";
import {select, Store} from "@ngrx/store";
import {GameState} from "../../state/game-state/game-state";
import {combineLatest, Subscription} from "rxjs";
import {selectActualPlayerId, selectBoard, selectStartedFlag} from "../../state/game-state/game-state.selectors";
import {move} from "../../state/game-state/game-state.actions";
import {MatSnackBar} from "@angular/material/snack-bar";
import {map} from "rxjs/operators";

@Component({
  selector: 'app-rack',
  templateUrl: './rack.component.html',
  styleUrls: ['./rack.component.css']
})
export class RackComponent implements OnInit {

  @Input() rackNumber!: number;
  @Input() movableFieldSource!: MovableFieldSource;

  gameStarted$ = this.store.pipe(select(selectStartedFlag));
  actualPlayerId$ = this.store.pipe(select(selectActualPlayerId));

  isRackDisabled: boolean = true;
  playerId?: string;
  movableFields: MovableField[] = [];
  private subscriptions: Subscription[] = [];

  constructor(private snackBar: MatSnackBar, private store: Store<{ gameState: GameState }>) { }

  ngOnInit(): void {
    this.subscriptions.push(
      this.store.pipe(select(selectBoard)).subscribe((board) => {
        if (board && board.racks && board.racks.length >= this.rackNumber && board.racks[this.rackNumber].letters) {
          this.movableFields = this.convertBoardLettersToMovableFields(0, board.racks[this.rackNumber].letters);
          this.playerId = board.racks[this.rackNumber].playerId;
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

  isRackDisabled$ = combineLatest([this.gameStarted$, this.actualPlayerId$]).pipe(
    map(([gameStarted, actualPlayerId]) => {
      const isGameStarted = gameStarted !== null ? gameStarted : false;
      const isActualPlayer = actualPlayerId !== null ? actualPlayerId === this.playerId : false;
      return !isGameStarted || !isActualPlayer;
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
    let fromSource = event.previousContainer.data[event.previousIndex].source;

    if(fromSource > 0 && fromSource != this.movableFieldSource) {
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

  ngOnDestroy() {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }
}
