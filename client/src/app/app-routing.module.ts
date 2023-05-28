import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UpdateFormComponent } from './components/update-form/update-form.component';
import { SignupComponent } from './components/signup/signup.component';
import { LoginComponent } from './components/login/login.component';
import { HomepageComponent } from './components/homepage/homepage.component';
import { CreatePostComponent } from './components/create-post/create-post.component';
import { CreateProjectComponent } from './components/create-project/create-project.component';
import { ProjectComponent } from './components/project/project.component';
import { ViewPostComponent } from './components/view-post/view-post.component';
import { ProfileComponent } from './components/profile/profile.component';
import { ViewProjectComponent } from './components/view-project/view-project.component';
import { AuthGuard } from './auth/auth.guard';
import { EditProjectComponent } from './components/edit-project/edit-project.component';
import { EditPostComponent } from './components/edit-post/edit-post.component';

const routes: Routes = [
  { path: "", component: HomepageComponent, canActivate: [AuthGuard] },
  { path: "update", component: UpdateFormComponent, canActivate: [AuthGuard] },
  { path: "signup", component: SignupComponent },
  { path: "login", component: LoginComponent },
  { path: "create-post", component: CreatePostComponent, canActivate: [AuthGuard] },
  { path: "create-project", component: CreateProjectComponent, canActivate: [AuthGuard] },
  { path: "project/:id" , component: ViewProjectComponent, canActivate: [AuthGuard] },
  { path: "create-post", component: CreatePostComponent, canActivate: [AuthGuard] },
  { path: "post/:postId", component: ViewPostComponent, canActivate: [AuthGuard] },
  { path: "user/:username", component: ProfileComponent, canActivate: [AuthGuard] },
  { path: "project/:id/edit", component: EditProjectComponent, canActivate: [AuthGuard] },
  { path: "post/:postId/edit", component: EditPostComponent, canActivate: [AuthGuard] },

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
