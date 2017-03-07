import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';

//High charts
import { Ng2HighchartsModule } from 'ng2-highcharts';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavigationComponent } from './navigation/navigation.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { GroupsAnalysisComponent } from './groups-analysis/groups-analysis.component';

import {EndpointsServiceService} from './utils/endpoints-service.service';
import { GroupsStatsComponent } from './groups-analysis/groups-stats/groups-stats.component';

@NgModule({
  declarations: [
    AppComponent,
    NavigationComponent,
    DashboardComponent,
    GroupsAnalysisComponent,
    GroupsStatsComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    AppRoutingModule,
    Ng2HighchartsModule
  ],
  providers: [EndpointsServiceService],
  bootstrap: [AppComponent]
})
export class AppModule { }
