import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { SignupRequestPayload } from 'src/app/model/models';
import { AuthService } from 'src/app/service/auth.service';


@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {

  signupRequestPayload: SignupRequestPayload
  signupForm!: FormGroup
  responseMessage!: string

  constructor(private fb: FormBuilder, private authService: AuthService,
    private router: Router, private toastr: ToastrService) {
    this.signupRequestPayload = {
      username: '',
      email: '',
      password: ''
    }
  }

  ngOnInit(): void {
    this.signupForm = this.createForm()
  }

  signupSubmit() {
    this.signupRequestPayload.email = this.signupForm.get('email')?.value
    this.signupRequestPayload.username = this.signupForm.get('username')?.value
    this.signupRequestPayload.password = this.signupForm.get('password')?.value
    // console.log(this.signupRequestPayload + "signup request payload")
    this.authService.signup(this.signupRequestPayload)
      .subscribe(data => {
        this.router.navigate(['/login'],
          { queryParams: { registered: 'true' } });
      }, error => {
        // console.log(error);
        this.toastr.error('Registration Failed! Please try again. Username or email already taken.');
      })
    }

  createForm() {
    return this.fb.group({
      username: this.fb.control<string>('', [Validators.required, Validators.pattern(/^\S.*\S$/)]),
      email: this.fb.control<string>('', [Validators.required, Validators.email]),
      password: this.fb.control<string>('', Validators.required)
    })
  }
}
