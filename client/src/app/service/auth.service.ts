import { HttpClient } from '@angular/common/http';
import { EventEmitter, Injectable, Output } from '@angular/core';
import { LoginRequestPayload, LoginResponse, SignupRequestPayload } from '../model/models';
import { Observable, firstValueFrom, map, tap, throwError } from 'rxjs';
import { LocalStorageService } from 'ngx-webstorage';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  @Output() loggedIn: EventEmitter<boolean> = new EventEmitter();
  @Output() username: EventEmitter<string> = new EventEmitter();

  refreshTokenPayload = {
    refreshToken: this.getRefreshToken(),
    username: this.getUsername()
  }

  constructor(private httpClient: HttpClient, private localStorage: LocalStorageService,
    private router: Router) { }

  //signup 
  signup(signupRequestPayload: SignupRequestPayload): Observable<any> {
    return this.httpClient.post<LoginResponse>('/api/auth/signup', signupRequestPayload)
  }

  login(loginRequestPayload: LoginRequestPayload): Observable<boolean> {
    return this.httpClient.post<LoginResponse>('/api/auth/login', loginRequestPayload)
          .pipe(map(data=>{
            this.localStorage.store('authenticationToken', data.authenticationToken)
            this.localStorage.store('username', data.username)
            this.localStorage.store('refreshToken', data.refreshToken)
            this.localStorage.store('expiresAt', data.expiresAt)
    this.loggedIn.emit(true);
    this.username.emit(data.username);
    return true;
    }))

  }

  logout() {
    this.httpClient.post('/api/auth/logout', this.refreshTokenPayload,
      { responseType: 'text' })
      .subscribe(data => {
        console.log(data);
      }, error => {
        throwError(error);
      })
    this.localStorage.clear('authenticationToken');
    this.localStorage.clear('username');
    this.localStorage.clear('refreshToken');
    this.localStorage.clear('expiresAt');
  }


  refreshToken() {
    const refreshTokenPayload = {
      refreshToken: this.getRefreshToken(),
      username: this.getUsername()
    }
    return this.httpClient.post<LoginResponse>('/api/auth/refresh/token', refreshTokenPayload)
        .pipe(tap(response => {
          this.localStorage.store('authenticationToken', 
            response.authenticationToken)
          this.localStorage.store('expiresAt', response.expiresAt)
        }))
  }

  getUsername() {
    return this.localStorage.retrieve('username')
  }

  getRefreshToken() {
    return this.localStorage.retrieve('refreshToken')
  }

  isLoggedIn(): boolean {
    return this.getJwtToken() != null
  }

  getJwtToken() {
    return this.localStorage.retrieve('authenticationToken')
  }

   
}
