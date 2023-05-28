import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { throwError } from 'rxjs';
import { LoginRequestPayload } from 'src/app/model/models';
import { AuthService } from 'src/app/service/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  loginForm!: FormGroup
  loginRequestPayload!: LoginRequestPayload
  responseMessage!: string
  registerSuccessMessage: string = '';
  isError?: boolean;

  constructor(private fb: FormBuilder, private authService: AuthService,
    private activatedRoute: ActivatedRoute, private router: Router, private toastr: ToastrService) {
    this.loginRequestPayload = {
      username: '',
      password: ''
    }
  }

  ngOnInit(): void {
    this.loginForm = this.createForm()
  

  this.activatedRoute.queryParams
    .subscribe(params => {
      if (params['registered'] !== undefined && params['registered'] == 'true') {
        this.toastr.success('Signup Successful', 'Successful', {
          timeOut: 5000
        })
        this.registerSuccessMessage = 'Please check your inbox for activation email and activate account before you can login'
        this.toastr.warning(this.registerSuccessMessage, "Activate account", {
          timeOut: 7000
        })
      }
    })

  }
  loginSubmit() {
    this.loginRequestPayload.username = this.loginForm.get('username')?.value
    this.loginRequestPayload.password = this.loginForm.get('password')?.value
    // console.log(this.loginRequestPayload + "login request payload")
    this.authService.login(this.loginRequestPayload).subscribe(data => {
      this.isError = false
      this.router.navigateByUrl('')
      this.toastr.success('Login successful')
    }, error => {
      this.isError = true
      throwError(()=> new Error(error))
    }
    )

  }

  createForm() {
    return this.fb.group({
      username: this.fb.control<string>('', [Validators.required, Validators.pattern(/^\S.*\S$/)]),
      password: this.fb.control<string>('', Validators.required)
    })
  }
}
