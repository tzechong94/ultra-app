import { Component, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { Post } from 'src/app/model/models';
import { PostService } from 'src/app/service/post.service';

@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.css']
})
export class HomepageComponent implements OnInit {

  posts$: Array<Post> = []
  
  constructor(private postService: PostService) {
  }

  ngOnInit(): void {
    this.postService.getAllPosts().subscribe(post => {
      this.posts$ = post
      this.posts$.reverse()
    })

  }


}
