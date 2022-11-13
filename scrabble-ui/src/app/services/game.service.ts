import {EventEmitter, Injectable} from '@angular/core';
import {BoardManagerService} from "../clients/board-manager/board-manager.service";
import {TileManagerService} from "../clients/tile-manager/tile-manager.service";
import {Board} from "../clients/board-manager/model/board";
import {Letter as BoardLetter} from "../clients/board-manager/model/letter";
import {Rack} from "../clients/board-manager/model/rack";
import {Move} from "../board-ui/model/move";
import {BoardValidationResult} from "../clients/board-manager/model/board-validation-result";

const maximumRackSize = 7;

@Injectable({
  providedIn: 'root'
})
export class GameService {

  public fillRackEvent = new EventEmitter<BoardLetter[]>();
  public updateBoardEvent = new EventEmitter<GameUpdate>();

  private boardUUID?: string;
  private board?: Board;
  private started: boolean = false;

  constructor(private boardManager: BoardManagerService, private tileManager: TileManagerService) {
  }

  init(boardUUID: string) {
    this.boardUUID = boardUUID;
    this.started = false;
  }

  startGame() {
    if (this.boardUUID !== undefined) {
      this.boardManager.getBoard(this.boardUUID).subscribe(board => {
        this.board = board;
        this.started = true;
        this.fillRack(this.board);
      })
    }
  }

  isStarted() {
    return this.started;
  }

  move(move: Move) {
    if (this.started) {
      console.log(move.field.x + ' ' + move.field.y + ' ' + move.field.letter!.letter + ' ' + move.field.letter!.points);
      this.updateField(move.fromX, move.fromY, move.field.x, move.field.y, move.field.letter!.letter, move.field.letter!.points);
      this.updateRack(move.fromX, move.fromY, move.field.x, move.field.y, move.field.letter!.letter, move.field.letter!.points);
      this.updateBoardEvent.emit(new GameUpdate(GameUpdateType.MOVE_PERFORMED, null, null));
    }
  }

  confirmMove() {
    if (this.board) {
      this.lockBoard();
      this.boardManager.validateBoard(this.board).subscribe(validationResult => {
        if (this.board) {
          if (!this.hasErrors(validationResult)) {
            this.boardManager.updateBoard(this.board).subscribe();
            this.fillRack(this.board);
            this.updateBoardEvent.emit(new GameUpdate(GameUpdateType.MOVE_CONFIRMED, null, null));
          } else {
            for(const incorrectWord of validationResult.incorrectWords) {
              for(const ch of incorrectWord.characters) {
                this.updateBoardEvent.emit(new GameUpdate(GameUpdateType.INVALID_WORD, ch.x, ch.y));
              }
            }
            for(const ch of validationResult.orphans) {
                this.updateBoardEvent.emit(new GameUpdate(GameUpdateType.ORPHAN, ch.x, ch.y));
            }
          }
        }
        this.unlockBoard();
      });
    }
  }

  private hasErrors(validationResult: BoardValidationResult) {
    return validationResult.incorrectWords.length > 0 || validationResult.orphans.length > 0;
  }

  private lockBoard() {

  }

  private unlockBoard() {

  }

  private updateField(fromX: number, fromY: number | null, toX: number, toY: number | null, letter: string, points: number) {
    for (const field of this.board!.fields) {
      if (fromY !== null && field.x == fromX && field.y == fromY) {
        field.letter = undefined;
      }
      if (toY !== null && field.x == toX && field.y == toY) {
        field.letter = new BoardLetter(letter, points);
      }
    }
  }

  private fillRack(board: Board) {
    let rackSize: number = board.racks.length > 0 ? board.racks[0].letters?.length! : 0;
    if (rackSize != maximumRackSize) {
      this.tileManager.getTiles(this.boardUUID!, maximumRackSize - rackSize).subscribe(value => {
        if (board.racks?.[0] == undefined) {
          let rack = new Rack();
          rack.letters = [];
          board.racks[0] = rack;
        }
        board.racks[0].letters = board.racks[0].letters!.concat(value);
        this.boardManager.updateBoard(board).subscribe();
        this.fillRackEvent.emit(value);
      });
    }
  }

  private updateRack(fromX: number, fromY: number | null, toX: number, toY: number | null, letter: string, points: number) {
    if (this.board && this.board.racks.length > 0) {
      let rack = this.board.racks[0];
      if (fromY == null && rack.letters?.at(fromX)?.letter == letter) {
        rack.letters!.splice(fromX, 1);
      }
      if (toY == null) {
        rack.letters!.splice(toX, 0, new BoardLetter(letter, points));
      }
    }
  }
}

export enum GameUpdateType {
  MOVE_PERFORMED,
  MOVE_CONFIRMED,
  INVALID_WORD,
  ORPHAN
}

export class GameUpdate {
  public gameUpdateType: GameUpdateType;

  public x: number | null;
  public y: number | null;

  constructor(gameUpdateType: GameUpdateType, x: number | null, y: number | null) {
    this.gameUpdateType = gameUpdateType;
    this.x = x;
    this.y = y;
  }
}
