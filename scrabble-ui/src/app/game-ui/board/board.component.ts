import {Component, Input, OnInit} from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import {Store} from "@ngrx/store";
import {GameState} from "../../state/game-state/game-state";
import {init, preview} from "../../state/game-state/game-state.actions";
import {selectBoard} from "../../state/game-state/game-state.selectors";
import {Subscription} from "rxjs";
import {MovableFieldSource} from "../model/movable-field";

@Component({
  selector: 'app-board',
  templateUrl: './board.component.html',
  styleUrls: ['./board.component.css']
})
export class BoardComponent implements OnInit {

  public board$ = this.store.select(selectBoard);

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

  protected readonly MovableFieldSource = MovableFieldSource;
}
