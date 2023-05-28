import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/service/auth.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {

  isLoggedIn: boolean = false;
  username: string = ''

  constructor(private authService: AuthService, private router: Router) {}
  
  ngOnInit(): void {
      this.authService.loggedIn.subscribe((data: boolean) => {
        this.isLoggedIn = data
        // console.log("is logged in?" , this.isLoggedIn)
      })
      this.authService.username.subscribe((data: string) => {
        this.username = data
      })
      this.isLoggedIn = this.authService.isLoggedIn();
      this.username = this.authService.getUsername();
  
  }

  logout(): void{
    this.authService.logout();
    this.isLoggedIn = false;
    this.router.navigate(['/']);
  }
}
