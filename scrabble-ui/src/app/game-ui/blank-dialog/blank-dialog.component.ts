import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA} from '@angular/material/dialog';
import {select, Store} from "@ngrx/store";
import {selectBoardId, selectCharset} from "../../state/game-state/game-state.selectors";
import {Subscription} from "rxjs";
import {GameState} from "../../state/game-state/game-state";
import {GameService} from "../../services/game.service";

@Component({
  selector: 'app-blank-dialog',
  templateUrl: './blank-dialog.component.html',
  styleUrls: ['./blank-dialog.component.css']
})
export class BlankDialogComponent implements OnInit {

  public charset: string[] = [];

  private subscriptions: Subscription[] = [];

  constructor(private store: Store<{ gameState: GameState }>,
              private gameService: GameService) { }

  ngOnInit(): void {
    this.subscriptions.push(
      this.store.pipe(select(selectCharset)).subscribe((charset) => {
        if(charset) {
            this.charset = charset;
        }
      })
    );
  }

  ngOnDestroy() {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }
}
