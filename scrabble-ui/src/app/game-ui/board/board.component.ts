import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {select, Store} from "@ngrx/store";
import {GameState} from "../../state/game-state/game-state";
import {init, preview} from "../../state/game-state/game-state.actions";
import {Subscription} from "rxjs";
import {BoardElement} from "../model/board-element";
import {selectActualPlayer, selectBoardParameters, selectWinner} from "../../state/game-state/game-state.selectors";
import {Type} from "../../clients/game-manager/model/game";

@Component({
  selector: 'app-board',
  templateUrl: './board.component.html',
  styleUrls: ['./board.component.css']
})
export class BoardComponent implements OnInit {

  public boardParameters$ = this.store.select(selectBoardParameters);

  protected readonly MovableFieldSource = BoardElement;

  public winner?: string;

  private subscriptions: Subscription[] = [];

  constructor(private route: ActivatedRoute, private store: Store<{ gameState: GameState }>) { }

  ngOnInit(): void {
    this.winner = undefined;
    this.subscriptions.push(this.route.params.subscribe(params => {
      const gameId = params['id'];
      if(gameId) {
        this.store.dispatch(init({ gameId: gameId }));
      } else {
        this.store.dispatch(preview());
      }
    }));
    this.subscriptions.push(this.store.pipe(select(selectWinner)).subscribe(winner => {
      if(winner) {
        this.winner = winner?.type === Type.HUMAN
          ? `Player: ${winner?.parameters.get('login') || 'Unknown'}`
          : `Bot: ${winner?.parameters.get('level') || 'Unknown'}`;
      } else {
        this.winner = undefined;
      }
    }));
  }

  ngOnDestroy() {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }
}
