import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import {DashboardComponent} from './dashboard/dashboard.component';
import {VideoAnalysisComponent} from './video-analysis/video-analysis.component';
import {GroupsAnalysisComponent} from './groups-analysis/groups-analysis.component';

const routes: Routes = [
   { path : "dashboard", component: DashboardComponent, },
   { path : "video-analysis", component: VideoAnalysisComponent, },
   { path : "groups-analysis", component: GroupsAnalysisComponent, },
   { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
  providers: []
})
export class AppRoutingModule { }
