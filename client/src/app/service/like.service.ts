import { HttpClient } from '@angular/common/http';
import { EventEmitter, Injectable } from '@angular/core';
import { LikePayload, Post } from '../model/models';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LikeService {

  constructor(private httpClient: HttpClient) { }
  
  likePost(likePayload: LikePayload): Observable<Post> {
    return this.httpClient.post<Post>('/api/likes', likePayload)
  }


}
