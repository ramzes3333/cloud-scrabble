import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {Letter as BoardLetter} from "../../clients/board-manager/model/letter";
import {Letter as TileLetter} from "../../clients/tile-manager/model/letter";
import {Letter as GuiLetter} from "../model/letter";
import {CdkDragDrop, moveItemInArray, transferArrayItem} from "@angular/cdk/drag-drop";
import {ActivatedRoute} from "@angular/router";
import {GameService} from "../../services/game.service";
import {MovableField} from "../model/movable-field";
import {Move} from "../model/move";
import {MoveType} from "../model/move-type";

@Component({
  selector: 'app-rack',
  templateUrl: './rack.component.html',
  styleUrls: ['./rack.component.css']
})
export class RackComponent implements OnInit {

  private _rack: Rack = new Rack([]);

  @Output() movePerformed = new EventEmitter<Move>();

  constructor(private route: ActivatedRoute, private gameService: GameService) { }

  ngOnInit(): void {
    this.route.data
      .subscribe((data) => {
        if(data['board']['racks'].length > 0) {
          this._rack = new Rack(this.convertBoardLettersToMovableFields(0, data['board']['racks'][0].letters));
        } else {
          this._rack = new Rack([]);
        }
      });

    this.gameService.fillRackEvent.subscribe(letters => {
      this._rack.movableFields = this._rack.movableFields.concat(this.convertTileLettersToMovableFields(this._rack.movableFields.length, letters));
    })

    this.gameService.updateBoardEvent.subscribe(gameUpdateType => {
      this.updateRackCoordinates();
    });
  }

  private convertTileLettersToMovableFields(startIndex: number, letters: TileLetter[]): MovableField[] {
    let movableFields: MovableField[] = [];
    for (const letter of letters) {
      movableFields.push(new MovableField(startIndex++, null, this.convert(letter.letter, letter.letter == ' ', letter.points)));
    }
    return movableFields;
  }

  private convertBoardLettersToMovableFields(startIndex: number, letters: BoardLetter[]): MovableField[] {
    let movableFields: MovableField[] = [];
    for (const letter of letters) {
      movableFields.push(new MovableField(startIndex++, null, this.convert(letter.letter, letter.blank, letter.points)));
    }
    return movableFields;
  }

  private convert(letter: string, blank: boolean, points: number): GuiLetter {
    return new GuiLetter(letter, blank, points);
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
    event.container.data[event.currentIndex].x = event.currentIndex;
    event.container.data[event.currentIndex].y = null;
    if(event.container.data[event.currentIndex].letter.blank) {
      event.container.data[event.currentIndex].letter.letter = " ";
    }

    this.gameService.move(this.extractMoveFromDropEvent(fromX, fromY, event));
  }

  private updateRackCoordinates() {
    let counter = 0;
    for(let movableField of this._rack.movableFields) {
      movableField.x = counter++;
    }
  }

  private extractMoveFromDropEvent(fromX: number, fromY: number | null, event: CdkDragDrop<MovableField[]>) {
    return new Move(
      this.getMoveType(fromY),
      fromX,
      fromY,
      new MovableField(
        event.currentIndex, null,
        new GuiLetter(
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

  get rack(): Rack {
    return this._rack;
  }

  isGameNotStarted() {
    return !this.gameService.isStarted();
  }
}

export class Rack {
  public movableFields: MovableField[];

  constructor(movableFields: MovableField[]) {
    this.movableFields = movableFields;
  }
}
