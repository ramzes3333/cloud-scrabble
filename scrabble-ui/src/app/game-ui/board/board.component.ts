import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Store} from "@ngrx/store";
import {GameState} from "../../state/game-state/game-state";
import {init, preview} from "../../state/game-state/game-state.actions";
import {Subscription} from "rxjs";
import {BoardElement} from "../model/board-element";
import {selectBoardParameters} from "../../state/game-state/game-state.selectors";

@Component({
  selector: 'app-board',
  templateUrl: './board.component.html',
  styleUrls: ['./board.component.css']
})
export class BoardComponent implements OnInit {

  public boardParameters$ = this.store.select(selectBoardParameters);

  private routeSubscription?: Subscription;

  constructor(private route: ActivatedRoute, private store: Store<{ gameState: GameState }>) { }

  ngOnInit(): void {
    this.routeSubscription = this.route.params.subscribe(params => {
      const gameId = params['id'];
      if(gameId) {
        this.store.dispatch(init({ gameId: gameId }));
      } else {
        this.store.dispatch(preview());
      }
    });
  }

  ngOnDestroy() {
    if (this.routeSubscription) {
      this.routeSubscription.unsubscribe();
    }
  }

  protected readonly MovableFieldSource = BoardElement;
}
