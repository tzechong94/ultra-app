import { Component, Input } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Post, Project } from 'src/app/model/models';
import { AuthService } from 'src/app/service/auth.service';
import { PostService } from 'src/app/service/post.service';
import { ProjectService } from 'src/app/service/project.service';

@Component({
  selector: 'app-project',
  templateUrl: './project.component.html',
  styleUrls: ['./project.component.css']
})
export class ProjectComponent {

  posts$: Array<Post> = []
  projectId!: number

  @Input()
  project!: Project
  currentUser!: string
projects: any;

  constructor(private postService: PostService, private projectService: ProjectService,
      private activatedRoute: ActivatedRoute, private router: Router, private authService: AuthService) {
        this.projectId = this.activatedRoute.snapshot.params['id']
        // this.postService.getAllPostsByProjectId(this.projectId).subscribe(post => {
        //   this.posts$ = post
        // })
        // console.log("posts ", this.posts$)
        
        // this.project = this.projectService.currentProject
        // // this.projectService.getProjectById(this.projectId).subscribe(project => {
        // //   this.project = project
        // // })
        // console.log("project ", this.project)

        this.currentUser = this.authService.getUsername()
    
  }

  ngOnInit(): void {

}

// createPost(project: Project) {
//   this.projectService.currentProject = project
//   this.router.navigate(['create-post'])
// }

viewProject(id: number) {
  this.router.navigate(['project/'+id])
}

addProject() {
  throw new Error('Method not implemented.');
  }
  
  

}
