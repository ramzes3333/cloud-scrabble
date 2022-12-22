import {EventEmitter, Injectable} from '@angular/core';
import {BoardManagerService} from "../clients/board-manager/board-manager.service";
import {TileManagerService} from "../clients/tile-manager/tile-manager.service";
import {Board} from "../clients/board-manager/model/board";
import {Letter as BoardLetter} from "../clients/board-manager/model/letter";
import {Letter as TileLetter} from "../clients/tile-manager/model/letter";
import {Letter as GuiLetter} from "../clients/board-manager/model/letter";
import {Rack} from "../clients/board-manager/model/rack";
import {Move} from "../game-ui/model/move";
import {Element as GuiElement} from "../game-ui/model/element";
import {Word as GuiWord} from "../game-ui/model/word";
import {BoardValidationResult} from "../clients/board-manager/model/board-validation-result";
import {EMPTY, Observable} from "rxjs";
import {Solution} from "../clients/board-manager/model/solution/solution";

const maximumRackSize = 7;

@Injectable({
  providedIn: 'root'
})
export class GameService {

  public fillRackEvent = new EventEmitter<TileLetter[]>();
  public updateBoardEvent = new EventEmitter<GameUpdate>();
  public solutionEvent = new EventEmitter<Solution>();
  public potentialWordLetterEvent = new EventEmitter<GuiElement>();

  private boardUUID?: string;
  private board?: Board;
  private started: boolean = false;

  constructor(private boardManager: BoardManagerService, private tileManager: TileManagerService) {
  }

  init(boardUUID: string) {
    this.boardUUID = boardUUID;
    this.started = false;
    this.solutionEvent.emit(new Solution([]));
  }

  getCharset(): Observable<string[]> {
    if (this.boardUUID !== undefined) {
      return this.tileManager.getCharset(this.boardUUID);
    } else {
      return EMPTY;
    }
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
      this.updateField(move.fromX, move.fromY, move.field.x, move.field.y, move.field.letter);
      this.updateRack(move.fromX, move.fromY, move.field.x, move.field.y, move.field.letter);
      this.updateBoardEvent.emit(new GameUpdate(GameUpdateType.MOVE_PERFORMED, null, null));
    }
  }

  selectedLetterForBlank(x: number, y: number, letter: string) {
    for (const field of this.board!.fields) {
      if (field.x == x && field.y == y) {
        field.letter!.letter = letter;
      }
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

  private updateField(fromX: number, fromY: number | null, toX: number, toY: number | null,
                      letter: GuiLetter) {
    for (const field of this.board!.fields) {
      if (fromY !== null && field.x == fromX && field.y == fromY) {
        field.letter = undefined;
      }
      if (toY !== null && field.x == toX && field.y == toY) {
        field.letter = new BoardLetter(letter.letter, letter.blank, letter.points);
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
        board.racks[0].letters = board.racks[0].letters!.concat(this.convertTileLettersToBoardLetters(value));
        this.boardManager.updateBoard(board).subscribe();
        this.fillRackEvent.emit(value);
      });
    }
  }

  private convertTileLettersToBoardLetters(letters: TileLetter[]): BoardLetter[] {
    let result: BoardLetter[] = [];
    for (const letter of letters) {
      result.push(this.convertTileLetterToBoardLetter(letter));
    }
    return result;
  }

  private convertTileLetterToBoardLetter(letter: TileLetter): BoardLetter {
    return new BoardLetter(letter.letter, letter.letter == ' ', letter.points)
  }

  private updateRack(fromX: number, fromY: number | null, toX: number, toY: number | null, letter: GuiLetter) {
    if (this.board && this.board.racks.length > 0) {
      let rack = this.board.racks[0];
      if (fromY == null && rack.letters?.at(fromX)?.letter == letter.letter) {
        rack.letters!.splice(fromX, 1);
      }
      if (toY == null) {
        rack.letters!.splice(toX, 0, new BoardLetter(letter.letter, letter.blank, letter.points));
      }
    }
  }

  resolve() {
    if (this.board) {
      this.boardManager.resolve(this.board).subscribe(solution => {
        this.solutionEvent.emit(solution);
      });
    }
  }

  showPotentialWord(word: GuiWord) {
    this.clearPotentialWord();
    for (const element of word.elements) {
      this.potentialWordLetterEvent.emit(element);
    }
  }

  clearPotentialWord() {
    this.potentialWordLetterEvent.emit(new GuiElement(-1, -1, "", -1, false));
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
