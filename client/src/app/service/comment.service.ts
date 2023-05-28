import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Comment, CommentPayload } from '../model/models';

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  constructor(private httpClient: HttpClient) { }

  getAllCommentsForPost(postId: number): Observable<Comment[]>  {
    return this.httpClient.get<Comment[]>('/api/comments?postId='+postId)

  }

  postComment(commentPayload: CommentPayload): Observable<Comment> {
    console.log(commentPayload, "comment payload")
    return this.httpClient.post<Comment>('/api/comments', commentPayload)
  }

}
