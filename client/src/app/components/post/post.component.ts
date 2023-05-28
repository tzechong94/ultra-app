import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { Router } from '@angular/router';
import { LikePayload, Post } from 'src/app/model/models';
import { AuthService } from 'src/app/service/auth.service';
import { LikeService } from 'src/app/service/like.service';
import { PostService } from 'src/app/service/post.service';

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.css']
})
export class PostComponent implements OnInit {

  @Input()
  post!: any;

  currentUser!: string
  // mediaExists: boolean = false;

  hasLiked: boolean = false;
  // post: any

  likePayload: LikePayload
  updatedPost: any

  constructor(private router: Router, private likeService: LikeService, private postService:PostService,
    private authService: AuthService) {
    this.likePayload = {
      postId: 0
    }
  }

  viewPost(postId: number) {
    this.router.navigate(['/post/'+postId])
  }

  ngOnInit(): void {
    // console.log("post this is called before post gets passed here", this.post)
    this.currentUser = this.authService.getUsername()
    // if (this.post?.mediaUrl === null || this.post?.mediaUrl === 'null' || this.post?.mediaUrl.length<3){
    //   this.mediaExists = false
    //   console.log('media exist ', this.mediaExists)
    // } else {
    //   console.log('media exist ', this.mediaExists)
    //   this.mediaExists = true
    // }

  }

  viewProject(id: number) {
    this.router.navigate(['project/'+id])
  }

  likePost(postId: number) {
    this.likePayload.postId = postId
    console.log(this.likePayload)
    this.likeService.likePost(this.likePayload).subscribe((data: any)=>{
      console.log(data)
      this.postService.getPostByPostId(postId).subscribe((post)=>{
        this.post = post
      })
    })
  }

  deletePost(postId: number) {
    if (confirm("Post will be permanently deleted. All comments under this project will be deleted as well. Proceed?")) {
      this.postService.deletePostById(postId).subscribe()
      alert("Project Deleted.")
      this.router.navigate(['/']).then(() => {
          window.location.reload();
        });
    }
  }

  editPost(postId: number) {
    this.router.navigateByUrl("/post/"+postId+"/edit")
  }

  replaceString(postLocation:String) {
    return postLocation.replaceAll('"','')
  }
    
    
    
}
