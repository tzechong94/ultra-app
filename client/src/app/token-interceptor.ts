import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { BehaviorSubject, Observable, catchError, filter, switchMap, take, throwError } from "rxjs";
import { AuthService } from "./service/auth.service";
import { LoginResponse } from "./model/models";

@Injectable({
    providedIn: 'root'
})
export class TokenInterceptor implements HttpInterceptor {
    
    isTokenRefreshing = false;
    refreshTokenSubject: BehaviorSubject<any> = new BehaviorSubject(null)

    constructor(public authService: AuthService) {}

    intercept(req: HttpRequest<any>, next: HttpHandler):
    Observable<HttpEvent<any>> {
        // console.log(req.url,"REQURL")
        if (req.url.indexOf('refresh') !== -1 || req.url.indexOf('login') !== -1) {
                // console.log('test req url ', req.url)
                return next.handle(req);    
        }
        const jwtToken = this.authService.getJwtToken();
        // console.log("JWT TOKEN " + jwtToken)
        if (req.url.includes('/manifest.json') || req.url.includes('/assets/images/icons')) {
            // Skip authentication for manifest icons requests
            return next.handle(req);
        }
        
        if (jwtToken) {
            return next.handle(this.addToken(req, jwtToken)).pipe(catchError(error => {
                if (error instanceof HttpErrorResponse
                    && error.status === 401) {
                    return this.handleAuthErrors(req, next);
                } else {
                    return throwError(() => new Error(error));
                }
            }));
        }
        return next.handle(req);

    }   

    handleAuthErrors(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        if (!this.isTokenRefreshing) {
            this.isTokenRefreshing = true
            this.refreshTokenSubject.next(null)

            return this.authService.refreshToken().pipe(
                switchMap((refreshTokenResponse: LoginResponse) => {
                    this.isTokenRefreshing = false
                    this.refreshTokenSubject
                        .next(refreshTokenResponse.authenticationToken)
                    return next.handle(this.addToken(req, refreshTokenResponse.authenticationToken))
                })
            )
        } else {
            return this.refreshTokenSubject.pipe(
                filter(result => result !== null),
                take(1),
                switchMap((res) => {
                    return next.handle(this.addToken(req,
                        this.authService.getJwtToken()))
                })
            );
        }
    }

    addToken(req: HttpRequest<any>, jwtToken: any) {
        return req.clone({
            headers: req.headers.set('Authorization', 
                'Bearer ' + jwtToken)
        })
    }

}