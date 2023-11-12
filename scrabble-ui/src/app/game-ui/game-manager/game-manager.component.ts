import {Component, OnInit} from '@angular/core';
import {BoardManagerService} from "../../clients/board-manager/board-manager.service";
import {Board} from "../../clients/board-manager/model/board";
import {Router} from "@angular/router";
import {GameCreatorDialogComponent} from "../game-creator-dialog/game-creator-dialog.component";
import {MatDialog} from "@angular/material/dialog";
import {GamePlayer} from "../game-creator-dialog/model/game-player";
import {
  BotPlayer,
  CreateGameResponse,
  GameCreatorService,
  HumanPlayer,
  Level
} from "../../services/game-creator.service";
import {Game} from "../../clients/game-manager/model/game";
import {GameManagerService} from "../../clients/game-manager/game-manager.service";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-game-manager',
  templateUrl: './game-manager.component.html',
  styleUrls: ['./game-manager.component.css']
})
export class GameManagerComponent implements OnInit {

  title = 'Scrabble';

  createDisabled: boolean = false;

  displayedColumns: string[] = ['id'];
  games: GameBoard[] = [];

  private subscriptions: Subscription[] = [];

  constructor(private boardManager: BoardManagerService,
              private gameManager: GameManagerService,
              private gameCreatorService: GameCreatorService,
              private router: Router, private dialog: MatDialog) { }

  ngOnInit(): void {
    this.refreshBoardsTable();
  }

  createGame() {
    const dialogRef = this.dialog.open(GameCreatorDialogComponent, {
      width: '650px',
      height: '570px'
    });

    dialogRef.afterClosed().subscribe((data) => {
      let players: GamePlayer[] = data.gamePlayers;
      let botPlayers: BotPlayer[] = [];
      let humanPlayers: HumanPlayer[] = [];
      for (const player of players) {
        if(player.player)
          switch (player.playerType) {
            case 'me':
              humanPlayers.push({});
              break;
            case 'bot':
              botPlayers.push({level: player.playerBotLevel as unknown as Level});
              break;
          }
      }

      this.gameCreatorService.createGame({botPlayers, humanPlayers}).subscribe((response: CreateGameResponse) => {
        this.refreshBoardsTable();
        this.router.navigate(["game", response.id])
      });
    });
  }

  getGame(id: string) {
    this.router.navigate(["game", id]);
  }

  refreshBoardsTable() {
    this.gameManager.getAllGames().subscribe((result: Game[]) => {
      this.games = result.map(game => ({
        game: game,
        board: undefined
      }));
      for (const game of this.games) {
        this.loadBoardForGame(game);
      }
    });
  }

  private loadBoardForGame(game: GameBoard) {
    this.subscriptions.push(this.boardManager.getBoard(game.game.boardId).subscribe(board => (game.board = board)));
  }

  ngOnDestroy() {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }
}

export interface GameBoard {
  game: Game,
  board?: Board
}
