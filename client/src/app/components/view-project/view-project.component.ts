import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { EditProjectModel, Post, Project } from 'src/app/model/models';
import { AuthService } from 'src/app/service/auth.service';
import { PostService } from 'src/app/service/post.service';
import { ProjectService } from 'src/app/service/project.service';

@Component({
  selector: 'app-view-project',
  templateUrl: './view-project.component.html',
  styleUrls: ['./view-project.component.css']
})
export class ViewProjectComponent {
  
  posts$: Array<Post> = []
  // postCount: number = 0
  projectId!: number
  project: Project
  currentUser!: string
  editProjectModel: EditProjectModel
  confirmString!: string

  constructor(private postService: PostService, private projectService: ProjectService,
      private activatedRoute: ActivatedRoute, private authService: AuthService, private router: Router) {
        this.project = {
          id: 0,
          name: '',
          description: '',
          username: '',
          projectDuration: 0,
          postCount: 0,
          duration: '',
          createdDate: new Date(),
          completed: false
        },
        this.editProjectModel = {
          id: 0,
          name: "",
          description: "",
          projectDuration: 0,
          completed: false
        }
        
  }

  ngOnInit(): void {
    this.projectId = this.activatedRoute.snapshot.params['id']
    this.postService.getAllPostsByProjectId(this.projectId).subscribe(post => {
      this.posts$ = post
      this.posts$.reverse()
      this.project.postCount = this.posts$.length
    })
    
    this.projectService.getProjectById(this.projectId).subscribe(project => {
      this.project = project
      this.projectService.currentProject = project
    })

    this.currentUser = this.authService.getUsername()

  }

  editProject(projectId: number) {
    this.router.navigateByUrl("/project/"+projectId+"/edit")
  }

  deleteProject(projectId: number) {
    if (confirm("Project will be permanently deleted. All posts under this project will be deleted as well. Proceed?")) {
      this.projectService.deleteProjectById(projectId).subscribe(data => data)
      alert("Project Deleted.")
      this.router.navigate(['/'])
    }
  }

  completeProject(projectId: number) {
    if (this.project.completed===false){
      this.confirmString = "Mark Project as Completed?"
    } else {
      this.confirmString = "Mark Project as In Progress?"
    }
    if (confirm(this.confirmString)) {
      this.editProjectModel.id = projectId
      this.editProjectModel.name = this.project.name
      this.editProjectModel.description = this.project.description
      this.editProjectModel.projectDuration = this.project.projectDuration
      this.editProjectModel.completed = !this.project.completed
      this.projectService.updateProject(projectId, this.editProjectModel).subscribe(data => {
        this.projectService.getProjectById(projectId).subscribe(projectResponse => {
          this.project = projectResponse
        })
      })
      this.router.navigate(['/'])
    }

  }

}