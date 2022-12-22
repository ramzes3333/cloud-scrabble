import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {MatDialogRef} from "@angular/material/dialog";
import {Level} from "../model/level";
import {GamePlayer} from "./model/game-player";

@Component({
  selector: 'app-game-creator-dialog',
  templateUrl: './game-creator-dialog.component.html',
  styleUrls: ['./game-creator-dialog.component.css'],
  encapsulation: ViewEncapsulation.None,
})
export class GameCreatorDialogComponent implements OnInit {
  gamePlayers: GamePlayer[] = [];

  eLevel = Object.keys(Level).filter(l => isNaN(+l));

  constructor(public dialogRef: MatDialogRef<GameCreatorDialogComponent>) {
    this.gamePlayers.push(new GamePlayer(true, "me", ""));
    this.gamePlayers.push(new GamePlayer(true, "bot", "NEWBIE"));
  }

  ngOnInit(): void {

  }

  capitalizeFirstLetter(value: string) {
    return value.charAt(0).toUpperCase() + value.slice(1).toLowerCase();
  }

  createGame() {
    this.dialogRef.close({ gamePlayers: this.gamePlayers });
  }
}
