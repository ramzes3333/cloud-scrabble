import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {BoardManagerService} from "../../clients/board-manager/board-manager.service";
import {Board} from "../../clients/board-manager/model/board";
import {ActivatedRoute, Route, Router} from "@angular/router";

@Component({
  selector: 'app-board-manager',
  templateUrl: './board-manager.component.html',
  styleUrls: ['./board-manager.component.css']
})
export class BoardManagerComponent implements OnInit {

  createDisabled: boolean = false;

  displayedColumns: string[] = ['id'];
  boards: Board[] = [];

  constructor(private boardManager: BoardManagerService, private router: Router) { }

  ngOnInit(): void {
    this.refreshBoardsTable();
  }

  createBoard() {
    this.boardManager.createBoard().subscribe((result: Board) => {
      this.refreshBoardsTable();
      this.router.navigate(["board", result.id])
    });
  }

  getBoard(uuid: string) {
    this.router.navigate(["board", uuid]);
  }

  refreshBoardsTable() {
    this.boardManager.getBoards().subscribe((result: Board[]) => {
      this.boards = result;
    });
  }

}
