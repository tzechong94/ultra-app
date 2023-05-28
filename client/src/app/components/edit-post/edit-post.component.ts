import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CreatePostPayload, Post, Project } from 'src/app/model/models';
import { PostService } from 'src/app/service/post.service';
import { ProjectService } from 'src/app/service/project.service';

@Component({
  selector: 'app-edit-post',
  templateUrl: './edit-post.component.html',
  styleUrls: ['./edit-post.component.css']
})
export class EditPostComponent implements OnInit{
  selectedProject!: Project
  selectedPost!: Post
  projectId?: number
  createPostPayload: CreatePostPayload
  postId: number = 0
  blob!: Blob
  file!: File;

  // id: number = 0

  form!: FormGroup

  constructor(private projectService: ProjectService, private fb: FormBuilder,
    private postService: PostService, private router: Router, private activatedRoute: ActivatedRoute) {
      this.createPostPayload = 
        {
          id: 0,
          postName: '',
          postReflections: ''
        }
    }

  ngOnInit(): void {
    this.form = this.createForm()
    this.selectedProject = this.projectService.currentProject
    this.postId = this.activatedRoute.snapshot.params['postId']
    this.postService.getPostByPostId(this.postId).subscribe(post => {
      this.selectedPost = post
      this.form.setValue({
        postName: post.postName,
        postReflections: post.postReflections,
        imageFile: post.mediaUrl
      })

    })
    console.log("SELECTED PROJECT ", this.selectedProject)
  }

  createForm() {
    return this.fb.group({
      postName: this.fb.control<string>('', [ Validators.required]),
      postReflections: this.fb.control<string>('', [Validators.required]),
      imageFile: this.fb.control('')
    })
  }

  editPostSubmit() {
    const formData = new FormData()
    console.log('file ', this.file)
    formData.set('postId', this.selectedPost.id.toString())
    formData.set("postName", this.form.get('postName')?.value)
    formData.set("postReflections", this.form.get('postReflections')?.value)
    if (this.file) {
      formData.set("mediaFile", this.file)
    }
    console.log(formData, " formData")

    this.postService.updatePost(this.postId, formData).subscribe((data: any) => {
      this.router.navigate(['/project/'+this.selectedPost.id])
      console.log('Data ', data)
    })
  }

  onFilechange(event: any) {
    console.log(event.target.files[0])
    this.file = event.target.files[0]
  }
 
  dataURItoBlob(dataURI: string) {
    var byteString = atob(dataURI.split(',')[1])
    let mimeString = dataURI.split(',')[0].split(';')[0]
    var ab = new ArrayBuffer(byteString.length)

    var ia = new Uint8Array(ab)
    for (var i = 0; i < byteString.length; i++) {
      ia[i] = byteString.charCodeAt(i)
    }
    return new Blob([ab], {type: mimeString});
  }


}
