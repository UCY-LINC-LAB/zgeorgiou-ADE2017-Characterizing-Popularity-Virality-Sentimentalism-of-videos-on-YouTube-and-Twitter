import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { AppRoutingModule } from './app-routing.module';

//High charts
import { Ng2HighchartsModule } from 'ng2-highcharts';


import { AppComponent } from './app.component';
import { NavigationComponent } from './navigation/navigation.component';
import { VideoAnalysisComponent } from './video-analysis/video-analysis.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { VideoCategoriesComponent } from './dashboard/video-categories/video-categories.component';
import { GroupsAnalysisComponent } from './groups-analysis/groups-analysis.component';
import { GroupsAnalysisFormComponent } from './groups-analysis/groups-analysis-form/groups-analysis-form.component';

//Services
import {EndpointsService} from './utils/endpoints.service';
import { GroupsGraphComponent } from './groups-analysis/groups-graph/groups-graph.component';

@NgModule({
  declarations: [
    AppComponent,
    NavigationComponent,
    VideoAnalysisComponent,
    DashboardComponent,
    VideoCategoriesComponent,
    GroupsAnalysisComponent,
    GroupsAnalysisFormComponent,
    GroupsGraphComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    AppRoutingModule,
    Ng2HighchartsModule,

  ],
  providers: [EndpointsService],
  bootstrap: [AppComponent]
})
export class AppModule { }
