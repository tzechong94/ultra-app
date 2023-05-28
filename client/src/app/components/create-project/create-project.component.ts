import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { catchError, tap, throwError } from 'rxjs';
import { CreateProjectModel, Project } from 'src/app/model/models';
import { ProjectService } from 'src/app/service/project.service';

@Component({
  selector: 'app-create-project',
  templateUrl: './create-project.component.html',
  styleUrls: ['./create-project.component.css']
})
export class CreateProjectComponent implements OnInit {

  createProjectForm!: FormGroup
  createProjectModel: CreateProjectModel
  // currentProject!: Project
  
  constructor(private fb: FormBuilder, private projectService: ProjectService,
      private router: Router) {
    this.createProjectModel = {
      name: '',
      description: '',
      projectDuration: 1,
      completed: false
    }
  }

  ngOnInit(): void {
      this.createProjectForm = this.createForm()
  }

  createForm() {
    return this.fb.group({
      name: this.fb.control<string>('', [Validators.required]),
      description: this.fb.control<string>('', [Validators.required]),
      projectDuration: this.fb.control<number>(6, [Validators.min(1), Validators.max(24), Validators.required])
    })
  }

  createProjectSubmit() {
    this.createProjectModel.name = this.createProjectForm.get('name')!.value
    this.createProjectModel.description = this.createProjectForm.get('description')?.value
    this.createProjectModel.projectDuration = this.createProjectForm.get('projectDuration')?.value
    this.projectService.createProject(this.createProjectModel).subscribe((data: any) => {
      this.router.navigate(['/project/'+ data['id']])
      console.log('Data ', data)
      this.projectService.currentProject = data
      console.log('CURRENT PROJECT ', this.projectService.currentProject)
    })
    // .pipe(
    //   tap((data:any) => {
    //     this.router.navigate(['/project/'+ data['id']]);
    //     console.log(data)
    //   }),
    //   catchError((error: any) => {
    //     return throwError(new Error(error));
    //   })
    // )
    // // .subscribe();
    }

}
