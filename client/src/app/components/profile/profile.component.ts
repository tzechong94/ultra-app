import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Project, User } from 'src/app/model/models';
import { AuthService } from 'src/app/service/auth.service';
import { ProjectService } from 'src/app/service/project.service';
import { UserService } from 'src/app/service/user.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  user: any
  projects$: Project[] = []
  username: string = ''
  currentUser: string = ''

  constructor(private projectService: ProjectService, private authService: AuthService,
    private router: Router, private activatedRoute: ActivatedRoute, private userService: UserService) {
      this.user = {
        username: '',
        email: '',
        profileImageUrl: '',
        userCreated: new Date(),
        passwordHash: '',
      }
    }


  ngOnInit(): void {
    this.username = this.activatedRoute.snapshot.params['username']
    // console.log(this.username , " USERNAME From user page")
    this.userService.getUserByUsername(this.username).subscribe(user=>{
      this.user = user
      // console.log("USER ", this.user)
      this.projectService.getAllProjectsByUser(this.user.userId).subscribe((projects)=>{
        this.projects$ = projects
        // console.log("projects in profile ", this.projects$)
        }
      )  
    })
    this.currentUser = this.authService.getUsername()
    // console.log(this.currentUser, "CURRENT USEr")
  }

}
