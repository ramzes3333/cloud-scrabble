import {Component, OnInit} from '@angular/core';
import {BoardManagerService} from "../../clients/board-manager/board-manager.service";
import {Board} from "../../clients/board-manager/model/board";
import {Router} from "@angular/router";
import {GameCreatorDialogComponent} from "../game-creator-dialog/game-creator-dialog.component";
import {MatDialog} from "@angular/material/dialog";
import {GamePlayer} from "../game-creator-dialog/model/game-player";
import {
  BotPlayer,
  GameCreatorService,
  GameStartResponse,
  HumanPlayer,
  Level
} from "../../services/game-creator.service";

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
              private gameCreatorService: GameCreatorService,
              private router: Router, private dialog: MatDialog) { }

  ngOnInit(): void {
    this.refreshBoardsTable();
  }

  createBoard() {
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
            humanPlayers.push({});
            break;
          case 'bot':
            botPlayers.push({level: player.playerBotLevel as unknown as Level});
            break;
        }
      }

      this.gameCreatorService.createGame({botPlayers, humanPlayers}).subscribe((response: GameStartResponse) => {
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
