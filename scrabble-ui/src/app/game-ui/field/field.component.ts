import {Component, Input, OnInit} from '@angular/core';
import {CdkDragDrop, moveItemInArray, transferArrayItem} from "@angular/cdk/drag-drop";
import {Letter} from "../model/letter";
import {MovableField} from "../model/movable-field";
import {Move} from "../model/move";
import {MatDialog} from "@angular/material/dialog";
import {BlankDialogComponent} from "../blank-dialog/blank-dialog.component";
import {Bonus} from "../model/bonus";
import {Store} from "@ngrx/store";
import {GameState} from "../../state/game-state/game-state";
import {move, updateBlankLetter} from "../../state/game-state/game-state.actions";
import {selectField, selectValidationErrorsForCoordinates} from "../../state/game-state/game-state.selectors";
import {Subscription} from "rxjs";
import {BoardElement} from "../model/board-element";

@Component({
  selector: 'app-field',
  templateUrl: './field.component.html',
  styleUrls: ['./field.component.css']
})
export class FieldComponent implements OnInit {

  @Input() x!: number;
  @Input() y!: number;

  bonus!: string;
  letter?: string;
  blank?: boolean;
  points?: number;
  movable?: boolean;
  suggested?: boolean;
  invalid?: boolean;
  movableFieldSource!: BoardElement;

  movableFields!: MovableField[];

  private subscriptions: Subscription[] = [];

  constructor(private store: Store<{ gameState: GameState }>, private dialog: MatDialog) { }

  ngOnInit(): void {
    this.movableFields = [];
    if(this.movable) {
      this.addMovableField();
    }

    this.subscriptions.push(this.store.select(selectValidationErrorsForCoordinates(this.x, this.y)).subscribe(errors => {
      if (errors && errors.length > 0) {
        this.setInvalidParam(true);
      }
    }));

    this.subscriptions.push(this.store.select(selectField(this.x, this.y)).subscribe(field => {
      if (field) {
        this.bonus = field.bonus;
        if (field.letter) {
          this.letter = field.letter.letter;
          this.blank = field.letter.blank;
          this.points = field.letter.points;
          this.movable = field.letter.movable;
          this.suggested = field.letter.suggested;
          this.invalid = field.letter.invalid;
          if(this.movable) {
            this.addMovableField();
          }
        } else {
          this.letter = undefined;
          this.blank = undefined;
          this.points = undefined;
          this.movable = undefined;
          this.suggested = undefined;
          this.invalid = undefined;
        }
      }
    }));
  }

  private addMovableField() {
    this.movableFields[0] = {
      x: this.x,
      y: this.y,
      letter: {
        letter: this.letter!,
        blank: this.blank!,
        points: this.points!
      },
      source: this.movableFieldSource
    }
  }

  private setInvalidParam(value: boolean) {
      this.invalid = value;
  }

  canDropLetter = (): boolean => {
    return !this.isLetter();
  }

  dropped(event: CdkDragDrop<MovableField[]>) {
    if (event.previousContainer.data[event.previousIndex].letter.letter == ' ') {
      const dialogRef = this.dialog.open(BlankDialogComponent, {
        width: '400px',
        disableClose: true
      });

      dialogRef.afterClosed().subscribe(result => {
        this.store.dispatch(updateBlankLetter({x: this.x, y: this.y, letter: result}));
      });
    }

    this.performMove(event);
  }

  private performMove(event: CdkDragDrop<MovableField[]>) {
    let fromX = event.previousContainer.data[event.previousIndex].x;
    let fromY = event.previousContainer.data[event.previousIndex].y;
    let from = event.previousContainer.data[event.previousIndex].source;

    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    } else {
      transferArrayItem(event.previousContainer.data,
        event.container.data,
        event.previousIndex,
        event.currentIndex);
    }
    event.container.data[event.currentIndex].x = this.x;
    event.container.data[event.currentIndex].y = this.y;
    event.container.data[event.currentIndex].source = this.movableFieldSource;

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
      event.container.data[event.currentIndex].y,
      event.container.data[event.currentIndex].source
    );
  }

  getBackgroundColor(): string {
    if (this.bonus === Bonus[Bonus.DoubleWordScore]) {
      return 'board-field-dws';
    } else if (this.bonus === Bonus[Bonus.TripleWordScore]) {
      return 'board-field-tws';
    } else if (this.bonus === Bonus[Bonus.DoubleLetterScore]) {
      return 'board-field-dls';
    } else if (this.bonus === Bonus[Bonus.TripleLetterScore]) {
      return 'board-field-tls';
    } else if (this.bonus === Bonus[Bonus.None]) {
      return '';
    } else {
      return '';
    }
  }

  getLetterColor(): string {
    if (this.movableFields.length > 0 && this.invalid) {
      return 'invalid';
    } else if (this.suggested) {
      return 'potential';
    } else if (this.blank) {
      return 'blank';
    } else {
      return '';
    }
  }

  getBackgroundContent(): string {
    if (this.bonus === Bonus[Bonus.DoubleWordScore]) {
      return 'DOUBLE WORD SCORE';
    } else if (this.bonus === Bonus[Bonus.TripleWordScore]) {
      return 'TRIPLE WORD SCORE';
    } else if (this.bonus === Bonus[Bonus.DoubleLetterScore]) {
      return 'DOUBLE LETTER SCORE';
    } else if (this.bonus === Bonus[Bonus.TripleLetterScore]) {
      return 'TRIPLE LETTER SCORE';
    } else if (this.bonus === Bonus[Bonus.None]) {
      return '';
    } else {
      return '';
    }
  }

  getLetter() {
    if(this.letter != undefined) {
      return this.letter;
    } else if(this.movableFields.length > 0) {
      return this.movableFields[0].letter.letter;
    } else {
      return " ";
    }
  }

  isLetter() {
    return this.letter != undefined || this.movableFields.length > 0;
  }

  getLetterPoints() {
    return this.points != undefined
      ? this.points : this.movableFields.length > 0
        ? this.movableFields[0].letter.points : '';
  }

  isDragDisabled() {
    return this.movableFields.length == 0;
  }

  ngOnDestroy() {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }
}
