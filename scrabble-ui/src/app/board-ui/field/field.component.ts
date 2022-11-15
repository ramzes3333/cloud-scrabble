import {Component, Input, OnInit} from '@angular/core';
import {Bonus} from "../../clients/board-manager/model/bonus";
import {CdkDragDrop, moveItemInArray, transferArrayItem} from "@angular/cdk/drag-drop";
import {Letter} from "../model/letter";
import {MovableField} from "../model/movable-field";
import {Move} from "../model/move";
import {MoveType} from "../model/move-type";
import {GameService, GameUpdate, GameUpdateType} from "../../services/game.service";
import {MatDialog} from "@angular/material/dialog";
import {BlankDialogComponent} from "../blank-dialog/blank-dialog.component";

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
  invalid: boolean = false;
  movableFields!: MovableField[];

  constructor(private gameService: GameService, private dialog: MatDialog) { }

  ngOnInit(): void {
    this.movableFields = [];
    this.gameService.updateBoardEvent.subscribe(gameUpdate => {
      if (gameUpdate.gameUpdateType == GameUpdateType.MOVE_CONFIRMED) {
        this.moveConfirmed();
      } else if (gameUpdate.gameUpdateType == GameUpdateType.MOVE_PERFORMED) {
        this.movePerformed();
      } else if (gameUpdate.gameUpdateType == GameUpdateType.INVALID_WORD) {
        this.moveInvalid(gameUpdate);
      } else if (gameUpdate.gameUpdateType == GameUpdateType.ORPHAN) {
        this.moveInvalid(gameUpdate);
      }
    });
  }

  private moveInvalid(gameUpdate: GameUpdate) {
    if (this.x == gameUpdate.x && this.y == gameUpdate.y) {
      if (this.movableFields.length > 0) {
        this.movableFields[0].invalid = true;
      } else {
        this.invalid = true;
      }
    }
  }

  private movePerformed() {
    if (this.movableFields.length > 0) {
      this.movableFields[0].invalid = false;
    } else {
      this.invalid = false;
    }
  }

  private moveConfirmed() {
    if (this.movableFields.length > 0) {
      this.letter = this.movableFields[0].letter.letter;
      this.blank = this.movableFields[0].letter.blank;
      this.points = this.movableFields[0].letter.points;
      this.movableFields = [];
    }
  }

  canDropLetter = (): boolean => {
    return !this.isLetter();
  }

  dropped(event: CdkDragDrop<MovableField[]>) {
    if (event.previousContainer.data[event.previousIndex].letter.letter == ' ') {
      this.gameService.getCharset().subscribe(charset => {
        const dialogRef = this.dialog.open(BlankDialogComponent, {
          width: '400px',
          disableClose: true,
          data: {charset: charset},
        });

        dialogRef.afterClosed().subscribe(result => {
          this.movableFields[0].letter.letter = result;
          this.gameService.selectedLetterForBlank(this.x, this.y, result);
        });
      });
    }

    this.performMove(event);
  }

  private performMove(event: CdkDragDrop<MovableField[]>) {
    let fromX = event.previousContainer.data[event.previousIndex].x;
    let fromY = event.previousContainer.data[event.previousIndex].y;

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

    this.gameService.move(this.extractMoveFromDropEvent(fromX, fromY, event));
  }

  private extractMoveFromDropEvent(fromX: number, fromY: number | null, event: CdkDragDrop<MovableField[]>) {
    return new Move(
      this.getMoveType(fromY),
      fromX,
      fromY,
      new MovableField(
        this.x, this.y,
        new Letter(
          event.container.data[event.currentIndex].letter.letter,
          event.container.data[event.currentIndex].letter.blank,
          event.container.data[event.currentIndex].letter.points
        )
      )
    );
  }

  private getMoveType(fromY: number | null) {
    return fromY != null ? MoveType.FROM_BOARD : MoveType.FROM_RACK;
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
    if (this.movableFields.length > 0 && this.movableFields[0].invalid || this.invalid) {
      return 'invalid';
    } else if (this.isBlankMovableField() || this.isBlankLetter()) {
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

  getLetterPoints() {
    return this.points != undefined
      ? this.points : this.movableFields.length > 0
        ? this.movableFields[0].letter.points : '';
  }

  isDragDisabled() {
    return this.movableFields.length == 0;
  }
}
