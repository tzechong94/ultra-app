import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CreateProjectModel, EditProjectModel, Project } from '../model/models';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {

  currentProject!: Project

  constructor(private httpClient: HttpClient) { }

  getAllProjectsByUser(userId: number) : Observable<Project[]> {
    return this.httpClient.get<Project[]>("/api/project/user/" + userId.toString())
  }

  getProjectById(id: number) : Observable<Project> {
    return this.httpClient.get<Project>("/api/project/" + id.toString())
  }

  createProject(createProjectModel: CreateProjectModel): Observable<CreateProjectModel> {
    return this.httpClient.post<CreateProjectModel>('/api/project', createProjectModel)
  }

  updateProject(id: number, editProjectModel: EditProjectModel) {
    return this.httpClient.put<CreateProjectModel>('/api/project/'+id+'/edit', editProjectModel)
  }

  deleteProjectById(id: number) {
    return this.httpClient.delete('/api/project/'+id)
  }




}
