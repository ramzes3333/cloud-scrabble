import {Component, OnInit} from '@angular/core';
import {BoardManagerService} from "../../clients/board-manager/board-manager.service";
import {Board} from "../../clients/board-manager/model/board";
import {Router} from "@angular/router";
import {GameCreatorDialogComponent} from "../game-creator-dialog/game-creator-dialog.component";
import {MatDialog} from "@angular/material/dialog";
import {GamePlayer} from "../game-creator-dialog/model/game-player";
import {BotPlayer, GameStartRequest, HumanPlayer, Level} from "../../clients/game-manager/model/game-start-request";
import {GameManagerService} from "../../clients/game-manager/game-manager.service";
import {GameStartResponse} from "../../clients/game-manager/model/game-start-response";

@Component({
  selector: 'app-game-manager',
  templateUrl: './game-manager.component.html',
  styleUrls: ['./game-manager.component.css']
})
export class GameManagerComponent implements OnInit {

  createDisabled: boolean = false;

  displayedColumns: string[] = ['id'];
  boards: Board[] = [];

  constructor(private boardManager: BoardManagerService,
              private gameManager: GameManagerService,
              private router: Router, private dialog: MatDialog) { }

  ngOnInit(): void {
    this.refreshBoardsTable();
  }

  createBoard() {
    /*this.boardManager.createBoard().subscribe((result: Board) => {
      this.refreshBoardsTable();
      this.router.navigate(["main/board", result.id])
    });*/
    const dialogRef = this.dialog.open(GameCreatorDialogComponent, {
      width: '650px',
      height: '570px'
    });

    dialogRef.afterClosed().subscribe((data) => {
      let players: GamePlayer[] = data.gamePlayers;
      let botPlayers: BotPlayer[] = [];
      let humanPlayers: HumanPlayer[] = [];
      for (const player of players) {
        switch (player.playerType) {
          case 'me':
            humanPlayers.push(new HumanPlayer());
            break;
          case 'bot':
            botPlayers.push(new BotPlayer(player.playerBotLevel as unknown as Level));
            break;
        }
      }

      this.gameManager.createGame(new GameStartRequest(botPlayers, humanPlayers)).subscribe((response: GameStartResponse) => {
        this.refreshBoardsTable();
        this.router.navigate(["main/board", response.boardId])
      });
    });
  }

  getBoard(uuid: string) {
    this.router.navigate(["main/board", uuid]);
  }

  refreshBoardsTable() {
    this.boardManager.getBoards().subscribe((result: Board[]) => {
      this.boards = result;
    });
  }

}
