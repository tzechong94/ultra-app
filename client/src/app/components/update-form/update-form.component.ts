import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Project } from 'src/app/model/models';
import { UltraService } from 'src/app/service/ultra.service';

@Component({
  selector: 'app-update-form',
  templateUrl: './update-form.component.html',
  styleUrls: ['./update-form.component.css']
})
export class UpdateFormComponent implements OnInit {

  selectedProject?: Project
  projectId?: number

  form!: FormGroup
  projectName!: string;
  projectDuration!: number;
  updateName!: string;
  updateReflections!: string;
  dateCreated!: Date;

  constructor(private ultraSvc: UltraService, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.selectedProject = this.ultraSvc.selectedProject
    this.form = this.createForm()
  }

  createForm() {
    return this.fb.group({
      projectName: this.fb.control<string>(this.selectedProject?.name ?? "", [Validators.required]),
      projectDuration: this.fb.control<number>(this.selectedProject?.projectDuration ?? 1, Validators.required),
      updateName: this.fb.control<string>('', [ Validators.required]),
      updateReflections: this.fb.control<string>('', [ Validators.required]),
    })
  }

  updateSubmit() {
    this.projectId = this.selectedProject?.id
    this.projectName = this.form.value.projectName
    this.projectDuration = this.form.value.projectDuration
    this.updateName = this.form.value.updateName
    this.updateReflections = this.form.value.updateReflections
    this.dateCreated = new Date()
    
    console.log(this.projectId, "projectId")
    this.ultraSvc.upsertUpdateByProjectId(this.projectId, this.projectName, this.projectDuration, this.updateName, this.updateReflections)

  }
}
