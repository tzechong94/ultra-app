import { NgModule, isDevMode } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatIconModule} from '@angular/material/icon';
import {MatButtonModule} from '@angular/material/button';
import { NavbarComponent } from './components/navbar/navbar.component';
import { HomepageComponent } from './components/homepage/homepage.component';
import { ProjectComponent } from './components/project/project.component';
import { UpdateFormComponent } from './components/update-form/update-form.component';
import {MatCardModule} from '@angular/material/card';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { LoginComponent } from './components/login/login.component';
import { SignupComponent } from './components/signup/signup.component';
import {NgxWebstorageModule} from 'ngx-webstorage';
import { ToastrModule } from 'ngx-toastr';
import { PostComponent } from './components/post/post.component';
import { CreateProjectComponent } from './components/create-project/create-project.component';
import { CreatePostComponent } from './components/create-post/create-post.component';
import { TokenInterceptor } from './token-interceptor';
import { PathLocationStrategy, LocationStrategy } from '@angular/common';
import { ProfileComponent } from './components/profile/profile.component';
import { ViewPostComponent } from './components/view-post/view-post.component';
import { ViewProjectComponent } from './components/view-project/view-project.component';
import {MatMenuModule} from '@angular/material/menu';
import { EditProjectComponent } from './components/edit-project/edit-project.component';
import { EditPostComponent } from './components/edit-post/edit-post.component';
import { ServiceWorkerModule } from '@angular/service-worker';

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    HomepageComponent,
    ProjectComponent,
    UpdateFormComponent,
    LoginComponent,
    SignupComponent,
    PostComponent,
    CreateProjectComponent,
    CreatePostComponent,
    ProfileComponent,
    ViewPostComponent,
    ViewProjectComponent,
    EditProjectComponent,
    EditPostComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    MatToolbarModule,
    MatIconModule,
    MatButtonModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    HttpClientModule,
    NgxWebstorageModule.forRoot(),
    ToastrModule.forRoot(), // ToastrModule added
    MatMenuModule, ServiceWorkerModule.register('ngsw-worker.js', {
  enabled: !isDevMode(),
  // Register the ServiceWorker as soon as the application is stable
  // or after 30 seconds (whichever comes first).
  registrationStrategy: 'registerWhenStable:30000'
}),

  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true
    },
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
