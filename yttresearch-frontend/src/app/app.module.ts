import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';

import { AppRoutingModule } from './app-routing.module';

//High charts
import { Ng2HighchartsModule } from 'ng2-highcharts';


import { AppComponent } from './app.component';
import { NavigationComponent } from './navigation/navigation.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { VideoAnalysisComponent } from './video-analysis/video-analysis.component';
import { VideoCategoriesComponent } from './dashboard/video-categories/video-categories.component';


//Services
//import {VideoCategoriesService} from './dashboard/video-categories/video-categories.service';

@NgModule({
  declarations: [
    AppComponent,
    NavigationComponent,
    NavigationComponent,
    DashboardComponent,
    VideoAnalysisComponent,
    VideoCategoriesComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    AppRoutingModule,
    Ng2HighchartsModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
