import {Component, Input, OnInit} from '@angular/core';
import {CdkDragDrop, moveItemInArray, transferArrayItem} from "@angular/cdk/drag-drop";
import {Letter} from "../model/letter";
import {MovableField} from "../model/movable-field";
import {Move} from "../model/move";
import {GameService} from "../../services/game.service";
import {MatDialog} from "@angular/material/dialog";
import {BlankDialogComponent} from "../blank-dialog/blank-dialog.component";
import {Bonus} from "../model/bonus";
import {select, Store} from "@ngrx/store";
import {GameState} from "../../state/game-state/game-state";
import {move, updateBlankLetter} from "../../state/game-state/game-state.actions";
import {selectValidationErrorsForCoordinates} from "../../state/game-state/game-state.selectors";
import {Subject, takeUntil, tap} from "rxjs";
import {BoardElement} from "../model/board-element";

@Component({
  selector: 'app-field',
  templateUrl: './field.component.html',
  styleUrls: ['./field.component.css']
})
export class FieldComponent implements OnInit {

  @Input() x!: number;
  @Input() y!: number;
  @Input() bonus!: string;
  @Input() letter?: string;
  @Input() blank?: boolean;
  @Input() points?: number;
  @Input() movable?: boolean;
  @Input() suggested?: boolean;
  @Input() invalid?: boolean;
  @Input() movableFieldSource!: BoardElement;

  movableFields!: MovableField[];
  potentialLetter: Letter | null = null;

  private readonly destroy$ = new Subject<void>();

  constructor(private gameService: GameService, private store: Store<{ gameState: GameState }>, private dialog: MatDialog) { }

  ngOnInit(): void {
    this.movableFields = [];
    if(this.movable) {
      this.addMovableField();
    }

    this.gameService.potentialWordLetterEvent.subscribe(element => {
      if (element.x == this.x && element.y == this.y && !element.onBoard) {
        this.potentialLetter = new Letter(element.letter, false, element.points)
      } else if (element.x == -1 && element.y == -1) {
        this.potentialLetter = null;
      }
    });

    this.store.pipe(
      select(selectValidationErrorsForCoordinates(this.x, this.y)),
      tap(errors => {
        if (errors && errors.length > 0) {
          this.setInvalidParam(true);
        }
      }),
      takeUntil(this.destroy$)
    ).subscribe();
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
    } else if (this.isBlankLetter()) {
      return 'blank';
    } else {
      return '';
    }
  }

  private isBlankMovableField() {
    return this.movableFields.length > 0 && this.movableFields[0].letter.blank;
  }

  private isBlankLetter() {
    return this.blank;
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

  isPotentialLetter() {
    return this.potentialLetter != null;
  }

  getLetterPoints() {
    return this.points != undefined
      ? this.points : this.movableFields.length > 0
        ? this.movableFields[0].letter.points : '';
  }

  isDragDisabled() {
    return this.movableFields.length == 0;
  }

  getPotentialLetter() {
    if(this.potentialLetter != null) {
      return this.potentialLetter.letter.toUpperCase();
    }
    return null;
  }

  getPotentialLetterPoints() {
    if(this.potentialLetter != null) {
      return this.potentialLetter.points;
    }
    return null;
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
