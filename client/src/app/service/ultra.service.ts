import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { Comment, Project } from '../model/models';

@Injectable({
  providedIn: 'root'
})
export class UltraService {

  constructor(private http: HttpClient) { }

  selectedProject!: Project
  
  getProjectsByUserId(userId: number) {
    return firstValueFrom(this.http.get("/api/projects/"+userId))
  }

  getAllProjects() {
    return firstValueFrom(this.http.get("api/projects"))
  }

  getUserByUserId(userId: number) {
    return firstValueFrom(this.http.get("/api/users/" + userId))
  }

  getProjectByProjectId(userId: number, projectId: number) {
    return firstValueFrom(this.http.get("/api/projects/" + projectId))
  }

  getAllUpdatesByProjectId(projectId: number) {
    return firstValueFrom(this.http.get("/api/projects/" + projectId + "/updates"))
  }

  getAllUpdates() {
    return firstValueFrom(this.http.get("/api/updates"))
  }

  getUpdateByUpdateId(updateId: number) {
    return firstValueFrom(this.http.get("/api/updates/" + updateId))
  }


  upsertUpdateByProjectId(projectId: any, projectName:string, projectDuration: number, updateName: string, updateReflections:string) {
    let body = new HttpParams()
              .set("projectId", projectId)
              .set("projectName", projectName)
              .set("projectDuration", projectDuration)
              .set("updateName", updateName)
              .set("updateReflections", updateReflections)
    
    return firstValueFrom(this.http.post("/api/firstUpdate", body))

  }

  //comments
  getCommentsByUpdateId(updateId: number) {
    return firstValueFrom(this.http.get("/api/updates/"+updateId+"/comments"))
  }

  // postCommentByUpdateId(updateId: number, comment: Comment) {
  //   let body = new HttpParams()
  //             .set("comment", comment.comment)
  //             .set("byUserId", comment.byUserId)
  //             .set("", comment.)
  //   return firstValueFrom(this.http.get("/api/updates/"+updateId+"/comments"))
  // }

  // likes 
  getLikesByUpdateId(updateId: number) {
    return firstValueFrom(this.http.get("/api/updates/"+updateId+"/likes"))
  }

  postLikeByUpdateIdAndUserId(updateId: number, byUserId: number) {
    let body = new HttpParams()
            .set("byUserId", byUserId)

    return firstValueFrom(this.http.post("/api/updates/"+updateId+"/likes", body)
    )
  }



}
