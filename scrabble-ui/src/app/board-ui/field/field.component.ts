import {Component, Input, OnInit} from '@angular/core';
import {Bonus} from "../../clients/board-manager/model/bonus";
import {CdkDragDrop, moveItemInArray, transferArrayItem} from "@angular/cdk/drag-drop";
import {Letter} from "../model/letter";
import {MovableField} from "../model/movable-field";
import {Move} from "../model/move";
import {MoveType} from "../model/move-type";
import {GameService, GameUpdateType} from "../../services/game.service";

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
  @Input() points?: number;
  movableFields!: MovableField[];

  constructor(private gameService: GameService) { }

  ngOnInit(): void {
    this.movableFields = [];
    if (this.letter != null && this.points != null) {
      this.movableFields.push(new MovableField(true, this.x, this.y,
        new Letter(
          this.letter, this.points
        )))
    }
    this.gameService.updateBoardEvent.subscribe(gameUpdate => {
      if (gameUpdate.gameUpdateType == GameUpdateType.MOVE_CONFIRMED) {
        if (this.movableFields.length > 0) {
          this.movableFields[0].locked = true;
          this.movableFields[0].invalid = false;
        }
      } else if (gameUpdate.gameUpdateType == GameUpdateType.MOVE_PERFORMED) {
        if (this.movableFields.length > 0) {
          this.movableFields[0].invalid = false;
        }
      } else if (gameUpdate.gameUpdateType == GameUpdateType.INVALID_WORD
        || gameUpdate.gameUpdateType == GameUpdateType.ORPHAN) {
        if (this.movableFields.length > 0 &&
          this.movableFields[0].x == gameUpdate.x &&
          this.movableFields[0].y == gameUpdate.y) {
          this.movableFields[0].invalid = true;
        }
      }
    });
  }

  canDropLetter = (): boolean => {
    return this.movableFields.length == 0;
  }

  isDragDisabled() {
    return this.movableFields.length > 0 && this.movableFields[0].locked;
  }

  dropped(event: CdkDragDrop<MovableField[]>) {
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
      new MovableField(false,
        this.x, this.y,
        new Letter(
          event.container.data[event.currentIndex].letter.letter,
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
    if (this.movableFields.length > 0 && this.movableFields[0].invalid) {
      return 'invalid';
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
    return this.movableFields.length > 0 ? this.movableFields[0].letter.letter : '';
  }

  isLetter() {
    return this.movableFields.length > 0;
  }
}
