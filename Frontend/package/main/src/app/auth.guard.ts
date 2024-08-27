import { Injectable } from '@angular/core';
import {
  CanActivate,
  ActivatedRouteSnapshot,
  RouterStateSnapshot,
  Router,
} from '@angular/router';
import { Observable, map, take } from 'rxjs';
import { AuthenticationService } from './_services/authentication.service';
import { AuthoritiesService } from './_services/authorities/authorities.service';

@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {
  constructor(
    private router: Router,
    private authenticationService: AuthenticationService,
    private authoritiesService: AuthoritiesService,
  ) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    const hasCompany = sessionStorage.getItem('hasCompany') === 'true';
    return this.authoritiesService.companyPermissions$.pipe(
      take(1),
      map((permissions) => {
        const user = this.authenticationService.userValue;
        if (user) {
          // Check if route is restricted by authority
          if (
            route.data['authorities'] &&
            !route.data['authorities'].every((auth: string) =>
              permissions.includes(auth),
            )
          ) {
            // Authority not authorized, so redirect to home page
            this.router.navigate(['/dashboards/dashboard1']);
            return false;
          }

          // If 'hasCompany' is false, restrict access to some routes
          if (!hasCompany) {
            const urlSegments = route.url;
            const routePath = urlSegments.length > 0 ? urlSegments[0].path : '';
            const restrictedRoutes = ['scan-id', 'tables'];
            if (restrictedRoutes.includes(routePath)) {
              this.router.navigate(['/dashboards/dashboard1']);
              return false;
            }
          }

          // JWT is valid and user is authorized, so return true
          return true;
        }

        // Not logged in, so redirect to login page with the return URL
        this.router.navigate(['/authentication/side-login'], {
          queryParams: { returnUrl: state.url },
        });
        return false;
      }),
    );
  }
}
