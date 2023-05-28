import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Comment, CommentPayload, Post } from 'src/app/model/models';
import { AuthService } from 'src/app/service/auth.service';
import { CommentService } from 'src/app/service/comment.service';
import { PostService } from 'src/app/service/post.service';

@Component({
  selector: 'app-view-post',
  templateUrl: './view-post.component.html',
  styleUrls: ['./view-post.component.css']
})
export class ViewPostComponent implements OnInit{

  commentForm!: FormGroup
  selectedPost!: Post 
  postId: number
  comments$: Comment[] = []
  commentPayload: CommentPayload;

  // @Input()
  // post!: Post

  constructor(private fb: FormBuilder, private postService: PostService, private activatedRoute: ActivatedRoute,
    private commentService: CommentService, private authService: AuthService) {
      this.postId = this.activatedRoute.snapshot.params['postId']
    this.commentPayload = {
      username: '',
      text: '',
      postId: 0,
      duration: ''
    }
    this.postService.getPostByPostId(this.postId).subscribe(post => {
      this.selectedPost = post
      console.log(this.selectedPost.mediaUrl, "media URl")
      console.log(this.selectedPost , "selected post ")
      console.log(post.userId, "USER ID")  
      this.commentService.getAllCommentsForPost(this.postId).subscribe(comments => {
        this.comments$ = comments
      })  
    })

  }

  ngOnInit(): void {
    this.commentForm = this.createForm()
  }

  createForm() {
    return this.fb.group({
      text: this.fb.control<string>('', Validators.required)
    })
  }


  commentSubmit() {
    console.log(this.postId)
    this.commentPayload.postId = this.postId
    this.commentPayload.text = this.commentForm.get('text')?.value
    this.commentPayload.username = this.authService.getUsername()
    this.commentService.postComment(this.commentPayload).subscribe((data)=>{
      console.log(data)
      this.commentService.getAllCommentsForPost(this.postId).subscribe(comments => {
        this.comments$ = comments
        this.commentForm.get('text')?.setValue('') 
      })
      this.postService.getPostByPostId(this.postId).subscribe(post => {
        this.selectedPost = post  

    }
    )
  })
  }
  }
