import {EventEmitter, Injectable} from '@angular/core';
import * as SockJS from 'sockjs-client';
import {CompatClient, Frame, Stomp} from '@stomp/stompjs';
import {KeycloakService} from "keycloak-angular";
import {Store} from "@ngrx/store";
import {GameState} from "../state/game-state/game-state";
import {addSolutionWord} from "../state/game-state/game-state.actions";
import {Word} from "../clients/board-manager/model/solution/word";

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {

  private stompClient: CompatClient | null = null;

  public solutionWordEvent = new EventEmitter<Word>();

  constructor(private keycloakService: KeycloakService, private store: Store<{ gameState: GameState }>) {
  }

  //TODO To add refreshing connection
  async initializeWebSocketConnection() {
    try {
      const token = await this.keycloakService.getToken();
      const wsUrl = `/board-manager-websocket/websocket`;
      const ws = new SockJS(wsUrl);

      const headers = {
        'X-Authorization': `Bearer ${token}`
      };

      this.stompClient = Stomp.over(ws);
      this.stompClient.debug = () => {};
      this.stompClient.connect(headers, (frame: Frame) => {
        this.subscribeToUserNotifications();
      });
    } catch (error) {
      console.error('Token acquire error', error);
    }
  }

  subscribeToUserNotifications() {
    if (this.stompClient) {
      this.stompClient.subscribe('/user/queue/solution-words', message => {
        const word: Word = JSON.parse(message.body);
        this.solutionWordEvent.emit(word);
      });
    } else {
      console.error('WebSocket is not connected.');
    }
  }
}
