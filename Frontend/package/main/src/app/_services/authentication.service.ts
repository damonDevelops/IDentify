import { Injectable } from '@angular/core';
import { BehaviorSubject, map, Observable, of } from 'rxjs';
import { User } from '../_models/user';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Router } from '@angular/router';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class AuthenticationService {
  private userSubject: BehaviorSubject<User | null>;
  public user: Observable<User | null>;
  public isAuthenticated$: Observable<boolean>;

  constructor(
    private router: Router,
    private http: HttpClient,
  ) {
    this.userSubject = new BehaviorSubject(
      JSON.parse(localStorage.getItem('user')!),
    );
    this.user = this.userSubject.asObservable();

    // Initialize isAuthenticated$ to false
    this.isAuthenticated$ = this.user.pipe(map((user) => user !== null));
  }

  public get userValue() {
    return this.userSubject.value;
  }

  login(username: string, password: string) {
    return this.http
      .post<any>(`${environment.apiUrl}/auth/login`, { username, password })
      .pipe(
        map((user) => {
          //uncomment to view login details
          // console.log("Untouched user roles: ", user.roles)
          // console.log('User JWT AccessToken:', user.accessToken);
          // console.log('User RefreshToken:', user.refreshToken);

          localStorage.setItem('user', JSON.stringify(user));
          //console.log('User saved to local storage');

          this.userSubject.next(user);
          console.log(
            //'UserSubject updated with new user:',
            this.userSubject.value,
          );

          this.isAuthenticated$ = of(true);

          return user;
        }),
      );
  }

  register(username: string, password: string, email: string) {
    console.log('Starting registration...');

    return this.http
      .post<any>(`${environment.apiUrl}/auth/signup`, {
        username,
        password,
        email,
      })
      .pipe(
        map((user) => {
          console.log('Registration response received:', user);

          const authorities = user.roles.map((role: string) => {
            const parts = role.split('_');
            return parts.slice(1).join('_');
          });

          console.log('Extracted authorities:', authorities);

          delete user.roles; // Remove the roles property
          user.authorities = authorities; // Update the user object
          console.log('Updated user:', user);

          localStorage.setItem('user', JSON.stringify(user));
          console.log('User saved to local storage');

          this.userSubject.next(user);
          console.log(
            'UserSubject updated with new user:',
            this.userSubject.value,
          );

          return user;
        }),
      );
  }

  registerV2(fullName: string, email: string): Observable<any> {
    const params = new HttpParams()
      .set('fullName', fullName)
      .set('email', email);

    // Log the URL with query parameters (for debugging purposes)
    const url = `${environment.apiUrl}/auth/signup/v2?${params.toString()}`;
    console.log('API URL (for debugging):', url);

    // Send the POST request to the constructed URL (with query parameters)
    return this.http.post(url, {});
  }

  refresh() {
    console.log('Starting token refresh...');

    return this.http
      .post<any>(`${environment.apiUrl}/auth/refresh`, {
        refreshToken: this.userSubject.value!.refreshToken,
      })
      .pipe(
        map((refresh) => {
          console.log('Refresh response received:', refresh);

          let user: User = JSON.parse(localStorage.getItem('user')!);
          user.refreshToken = refresh.refreshToken;
          user.accessToken = refresh.token;
          localStorage.setItem('user', JSON.stringify(user));
          this.userSubject.next(user);

          console.log('User JWT AccessToken after refresh:', user.accessToken);

          return user;
        }),
      );
  }

  logout() {
    // remove user from local storage to log user out
    localStorage.removeItem('user');
    localStorage.removeItem('');
    sessionStorage.clear();
    this.userSubject.next(null);
    this.router.navigate(['/landingpage']);
    this.isAuthenticated$ = of(false);
  }
}
