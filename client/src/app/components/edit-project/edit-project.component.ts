import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CreateProjectModel, EditProjectModel, Project } from 'src/app/model/models';
import { ProjectService } from 'src/app/service/project.service';

@Component({
  selector: 'app-edit-project',
  templateUrl: './edit-project.component.html',
  styleUrls: ['./edit-project.component.css']
})
export class EditProjectComponent implements OnInit{

  editProjectForm!: FormGroup
  editProjectModel: EditProjectModel
  currentProject!: Project
  projectId: number = 0
  
  constructor(private fb: FormBuilder, private projectService: ProjectService,
      private router: Router, private activatedRoute: ActivatedRoute) {
    this.editProjectModel = {
      id: 0,
      name: '',
      description: '',
      projectDuration: 1,
      completed: false
    }
  }

  ngOnInit(): void {
      this.editProjectForm = this.createForm()
      this.projectId = this.activatedRoute.snapshot.params['id']
      this.projectService.getProjectById(this.projectId).subscribe(project => {
        this.currentProject = project
        this.editProjectForm.setValue({
          name: project.name,
          description: project.description,
          projectDuration: project.projectDuration,
          completed: project.completed
        })
      })
  }

  createForm() {
    return this.fb.group({
      name: this.fb.control<string>('', [Validators.required]),
      description: this.fb.control<string>('', [Validators.required]),
      projectDuration: this.fb.control<number>(6, [Validators.min(1), Validators.max(24), Validators.required])
    })
  }

  editProjectSubmit() {
    this.editProjectModel.id = this.currentProject.id
    this.editProjectModel.name = this.editProjectForm.get('name')!.value
    this.editProjectModel.description = this.editProjectForm.get('description')?.value
    this.editProjectModel.projectDuration = this.editProjectForm.get('projectDuration')?.value
    this.projectService.updateProject(this.projectId, this.editProjectModel).subscribe((data: any) => {
      this.router.navigate(['/project/'+ data['id']])
      console.log('Data ', data)
      this.projectService.currentProject = data
      console.log('CURRENT PROJECT ', this.projectService.currentProject)
    })
    }

}
