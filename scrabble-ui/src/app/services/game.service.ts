import {EventEmitter, Injectable} from '@angular/core';
import {BoardManagerService} from "../clients/board-manager/board-manager.service";
import {TileManagerService} from "../clients/tile-manager/tile-manager.service";
import {Board} from "../clients/board-manager/model/board";
import {Letter as BoardLetter} from "../clients/board-manager/model/letter";
import {Letter as TileLetter} from "../clients/tile-manager/model/letter";
import {Element as GuiElement} from "../game-ui/model/element";
import {Word as GuiWord} from "../game-ui/model/word";
import {EMPTY, Observable} from "rxjs";
import {GameManagerService} from "../clients/game-manager/game-manager.service";
import {Game} from "../clients/game-manager/model/game";

const maximumRackSize = 7;

@Injectable({
  providedIn: 'root'
})
export class GameService {

  public fillRackEvent = new EventEmitter<TileLetter[]>();
  public updateBoardEvent = new EventEmitter<GameUpdate>();
  public potentialWordLetterEvent = new EventEmitter<GuiElement>();

  constructor(private boardManager: BoardManagerService, private tileManager: TileManagerService, private gameManagerService: GameManagerService) {
  }

  getGame(id: string): Observable<Game> {
    return this.gameManagerService.getGame(id);
  }

  getCharset(): Observable<string[]> {
    //TODO migrate to ngrx effect
    /*if (this.boardUUID !== undefined) {
      return this.tileManager.getCharset(this.boardUUID);
    } else {
      return EMPTY;
    }*/

    return EMPTY;
  }

  selectedLetterForBlank(x: number, y: number, letter: string) {
    //TODO migrate to ngrx reducer
    /*for (const field of this.board!.fields) {
      if (field.x == x && field.y == y) {
        field.letter!.letter = letter;
      }
    }*/
  }

  private fillRack(board: Board) {
    //TODO to implement in game services
    /*let rackSize: number = board.racks.length > 0 ? board.racks[0].letters?.length! : 0;
    if (rackSize != maximumRackSize) {
      this.tileManager.getTiles(this.boardUUID!, maximumRackSize - rackSize).subscribe(value => {
        if (board.racks?.[0] == undefined) {
          board.racks[0] = new Rack("0", []); // TODO set proper playerId
        }
        board.racks[0].letters = board.racks[0].letters!.concat(this.convertTileLettersToBoardLetters(value));
        this.boardManager.updateBoard(board).subscribe();
        this.fillRackEvent.emit(value);
      });
    }*/
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
