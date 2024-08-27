import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse,
} from '@angular/common/http';
import {
  catchError,
  delay,
  Observable,
  pipe,
  retry,
  switchMap,
  throwError,
} from 'rxjs';
import { environment } from 'src/environments/environment';
import { AuthenticationService } from '../_services/authentication.service';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {
  isRefreshing = false;

  constructor(private authenticationService: AuthenticationService) {}

  intercept(
    request: HttpRequest<any>,
    next: HttpHandler,
  ): Observable<HttpEvent<any>> {
    const user = this.authenticationService.userValue;
    const isLoggedIn = user?.accessToken;
    const isApiUrl = request.url.startsWith(environment.apiUrl);
    if (isLoggedIn && isApiUrl) {
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${user.accessToken}`,
        },
      });
    }

    return next.handle(request).pipe(
      catchError((error) => {
        if (
          error instanceof HttpErrorResponse &&
          !request.url.includes('login') &&
          error.status === 401
        ) {
          return this.handle401Error(request, next);
        }
        return throwError(() => error);
      }),
    );
  }

  private handle401Error(request: HttpRequest<any>, next: HttpHandler) {
    if (!this.isRefreshing) {
      this.isRefreshing = true;
      const user = this.authenticationService.userValue;
      const isLoggedIn = user?.accessToken;
      const isApiUrl = request.url.startsWith(environment.apiUrl);
      if (isLoggedIn && isApiUrl) {
        return this.authenticationService.refresh().pipe(
          switchMap(() => {
            this.isRefreshing = false;

            return this.intercept(request, next).pipe(delay(5)).pipe(retry(5));
          }),
          catchError((error) => {
            this.isRefreshing = false;

            if (error.status == '403') {
              console.log('Logged out from the JWT interceptor');
              this.authenticationService.logout();
            }

            return throwError(() => error);
          }),
        );
      }
    }

    return next.handle(request);
  }
}
