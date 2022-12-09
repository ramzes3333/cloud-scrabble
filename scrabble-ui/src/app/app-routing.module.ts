import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {BoardComponent} from "./board-ui/board/board.component";
import {BoardResolver} from "./resolvers/board.resolver";
import {RackComponent} from "./board-ui/rack/rack.component";
import {AuthGuard} from "./guard/auth.guard";
import {MainComponent} from "./board-ui/main/main.component";

const routes: Routes = [
  {
    path: '', component: MainComponent, canActivate: [AuthGuard], children: [
      {
        path: 'board/:uuid',
        component: BoardComponent,
        resolve: {board: BoardResolver},
        children: [
          {
            path: 'rack',
            component: RackComponent
          }
        ]
      }]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
