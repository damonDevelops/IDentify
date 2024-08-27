// authorities.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class AuthoritiesService {
  private companyPermissionsSubject: BehaviorSubject<any> =
    new BehaviorSubject<any>(null);
  public companyPermissions$: Observable<any> =
    this.companyPermissionsSubject.asObservable();

  private localStorageKey = 'companyPermissions';

  constructor(private http: HttpClient) {
    // Initialize the service by attempting to load permissions from local storage
    this.loadPermissionsFromLocalStorage();
  }

  // Fetch company permissions based on the active company ID
  fetchCompanyPermissions(companyID: string): void {
    this.http
      .get<string[]>(`${environment.apiUrl}/company/${companyID}/permissions`)
      .subscribe(
        (permissions: string[]) => {
          // Remove the company endpoint prefix and store only the relevant part
          const authoritiesWithoutPrefix = permissions.map((authority) => {
            const parts = authority.split('_');
            if (parts.length > 1) {
              return parts.slice(1).join('_'); // Keep everything after the first underscore
            }
            return authority;
          });

          // Store permissions in local storage
          this.storePermissionsInLocalStorage(authoritiesWithoutPrefix);

          this.companyPermissionsSubject.next(authoritiesWithoutPrefix);

          // Log the updated permissions
          //this.logPermissions(authoritiesWithoutPrefix);
        },
        (error) => {
          console.error('Error fetching company permissions:', error);
        },
      );
  }

  // Log the permissions to the console
  logPermissions(permissions: string[]): void {
    console.log('Stored Permissions:', permissions);
  }

  // Store permissions in local storage
  storePermissionsInLocalStorage(permissions: string[]): void {
    localStorage.setItem(this.localStorageKey, JSON.stringify(permissions));
  }

  // Load permissions from local storage
  loadPermissionsFromLocalStorage(): void {
    const storedPermissions = localStorage.getItem(this.localStorageKey);
    if (storedPermissions) {
      const permissions = JSON.parse(storedPermissions);
      this.companyPermissionsSubject.next(permissions);
    }
  }
}
