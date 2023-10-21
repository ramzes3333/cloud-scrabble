import {APP_INITIALIZER, NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {HttpClientModule} from '@angular/common/http';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {BoardComponent} from './game-ui/board/board.component';
import {FieldComponent} from './game-ui/field/field.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatButtonModule} from "@angular/material/button";
import {GameManagerComponent} from './game-ui/game-manager/game-manager.component';
import {MatTableModule} from "@angular/material/table";
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatPaginatorModule} from "@angular/material/paginator";
import {RackComponent} from './game-ui/rack/rack.component';
import {DragDropModule} from "@angular/cdk/drag-drop";
import {GamePanelComponent} from './game-ui/game-panel/game-panel.component';
import {BlankDialogComponent} from './game-ui/blank-dialog/blank-dialog.component';
import {MatDialogModule} from "@angular/material/dialog";
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import {ScrollingModule} from "@angular/cdk/scrolling";
import {TableVirtualScrollModule} from 'ng-table-virtual-scroll';
import {MatTooltipModule} from '@angular/material/tooltip';
import {KeycloakAngularModule, KeycloakService} from "keycloak-angular";
import {initializeKeycloak} from "./init/keycloak-init.factory";
import {MainComponent} from './game-ui/main/main.component';
import {HashLocationStrategy, LocationStrategy} from "@angular/common";
import {GameCreatorDialogComponent} from './game-ui/game-creator-dialog/game-creator-dialog.component';
import {MatSlideToggleModule} from "@angular/material/slide-toggle";
import {FormsModule} from "@angular/forms";
import {MatRadioModule} from "@angular/material/radio";
import {MatSelectModule} from "@angular/material/select";
import {MatCardModule} from "@angular/material/card";
import {MatChipsModule} from "@angular/material/chips";
import { StoreModule } from '@ngrx/store';
import {gameStateReducer} from "./state/game-state/game-state.reducer";

@NgModule({
  declarations: [
    AppComponent,
    BoardComponent,
    FieldComponent,
    GameManagerComponent,
    RackComponent,
    GamePanelComponent,
    BlankDialogComponent,
    MainComponent,
    GameCreatorDialogComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatButtonModule,
    MatTableModule,
    MatToolbarModule,
    MatPaginatorModule,
    DragDropModule,
    MatDialogModule,
    MatProgressSpinnerModule,
    ScrollingModule,
    TableVirtualScrollModule,
    MatTooltipModule,
    KeycloakAngularModule,
    MatSlideToggleModule,
    FormsModule,
    MatRadioModule,
    MatSelectModule,
    MatCardModule,
    MatChipsModule,
    StoreModule.forRoot({gameState: gameStateReducer}, {})
  ],
  providers: [
    {
      provide: APP_INITIALIZER,
      useFactory: initializeKeycloak,
      multi: true,
      deps: [KeycloakService]
    },
    {
      provide: LocationStrategy,
      useClass: HashLocationStrategy
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
