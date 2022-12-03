import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {HttpClientModule} from '@angular/common/http';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {BoardComponent} from './board-ui/board/board.component';
import {FieldComponent} from './board-ui/field/field.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatButtonModule} from "@angular/material/button";
import {BoardManagerComponent} from './board-ui/board-manager/board-manager.component';
import {MatTableModule} from "@angular/material/table";
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatPaginatorModule} from "@angular/material/paginator";
import {RackComponent} from './board-ui/rack/rack.component';
import {DragDropModule} from "@angular/cdk/drag-drop";
import {GamePanelComponent} from './board-ui/game-panel/game-panel.component';
import {BlankDialogComponent} from './board-ui/blank-dialog/blank-dialog.component';
import {MatDialogModule} from "@angular/material/dialog";
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import {ScrollingModule} from "@angular/cdk/scrolling";
import {TableVirtualScrollModule} from 'ng-table-virtual-scroll';
import {MatTooltipModule} from '@angular/material/tooltip';

@NgModule({
  declarations: [
    AppComponent,
    BoardComponent,
    FieldComponent,
    BoardManagerComponent,
    RackComponent,
    GamePanelComponent,
    BlankDialogComponent
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
    MatTooltipModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
