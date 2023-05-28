import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CreatePostPayload, Project } from 'src/app/model/models';
import { PostService } from 'src/app/service/post.service';
import { ProjectService } from 'src/app/service/project.service';

@Component({
  selector: 'app-create-post',
  templateUrl: './create-post.component.html',
  styleUrls: ['./create-post.component.css']
})
export class CreatePostComponent implements OnInit {

  selectedProject!: Project
  projectId?: number
  createPostPayload: CreatePostPayload
  blob!: Blob
  file!: File;
  latLngString!: string
  lat!: string
  lng!: string
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
    // console.log(this.id, "ID")

    this.selectedProject = this.projectService.currentProject
    // this.projectService.getProjectById(this.id).subscribe(data => {
    //   this.selectedProject = data
    // })
    console.log("SELECTED PROJECT ", this.selectedProject)
    
    navigator.geolocation.getCurrentPosition((position) => {
      this.lat = position.coords.latitude.toString()
      this.lng = position.coords.longitude.toString()
      this.latLngString = this.lat + "," + this.lng
  })
  
  }

  createForm() {
    return this.fb.group({
      postName: this.fb.control<string>('', [ Validators.required]),
      postReflections: this.fb.control<string>('', [Validators.required]),
      imageFile: this.fb.control('')
    })
  }

  postSubmit() {
    const formData = new FormData()
    // console.log("ID: ", this.selectedProject.id.toString())
    // console.log("postname: ", this.form.get('postName')?.value)
    // console.log("postreflection: ", this.form.get('postReflections')?.value)
    console.log('file ', this.file)
    formData.set('id', this.selectedProject.id.toString())
    formData.set("postName", this.form.get('postName')?.value)
    formData.set("postReflections", this.form.get('postReflections')?.value)
    if (this.file) {
      formData.set("mediaFile", this.file)
    }
    if (this.latLngString) {
        formData.set("latLngString", this.latLngString)
      }
    
    console.log(formData, " formData")

    // this.createPostPayload.id = this.selectedProject?.id
    // console.log(this.createPostPayload.id, "IDDD")
    // this.createPostPayload.postName = this.form.get('postName')?.value
    // console.log("POSTNAME ", this.form.get('postName')?.value)
    // this.createPostPayload.postReflections = this.form.get('postReflections')?.value
    // console.log("reflections ", this.form.get('postReflections')?.value)

    this.postService.createPost(formData).subscribe((data: any) => {
      this.router.navigate(['/'])
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

// export interface CreatePostPayload {
//   id: number, //projectId
//   postName: string,
//   postReflections: string,
// }


