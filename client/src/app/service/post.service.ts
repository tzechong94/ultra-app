import { HttpClient } from '@angular/common/http';
import { EventEmitter, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CreatePostPayload, Post, Project } from '../model/models';
import { ProjectService } from './project.service';

@Injectable({
  providedIn: 'root'
})
export class PostService {

  currentProject!: Project
  currentPost!: Post

  constructor(private httpClient: HttpClient, private projectService: ProjectService) { }


  getAllPosts(): Observable<Post[]> {
    return this.httpClient.get<Post[]>("/api/post")
  }

  getAllPostsByProjectId(id: number) : Observable<Post[]> {
    return this.httpClient.get<Post[]>('/api/post?projectId='+id)
  }

  createPost(formData: FormData) : Observable<any> {
    this.currentProject = this.projectService.currentProject
    console.log(formData,'POSTING')
    return this.httpClient.post("/api/post", formData)
  }

  getPostByPostId(postId: number): Observable<Post> {
    return this.httpClient.get<Post>("/api/post/"+postId)
  }

  updatePost(postId: number, formData: FormData) : Observable<any> {
    return this.httpClient.put('/api/post/'+postId+'/edit', formData)
  }

  deletePostById(postId: number) {
    return this.httpClient.delete('/api/post/'+postId)
  }


}
// createProject(createProjectModel: CreateProjectModel): Observable<CreateProjectModel> {
//   return this.httpClient.post<CreateProjectModel>('/api/project', createProjectModel)
// }