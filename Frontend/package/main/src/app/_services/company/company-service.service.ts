// company.service.ts
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { ApiService } from '../api/api.service';
import { Company } from 'src/app/_models/company';
import { AuthenticationService } from '../authentication.service';

@Injectable({
  providedIn: 'root',
})
export class CompanyServiceService {
  private activeCompanySetSubject: BehaviorSubject<boolean> =
    new BehaviorSubject<boolean>(false);

  private companiesSubject: BehaviorSubject<Company[]> = new BehaviorSubject<
    Company[]
  >([]);
  private activeCompanySubject: BehaviorSubject<Company | null> =
    new BehaviorSubject<Company | null>(null);

  private companyEndpointSubject: BehaviorSubject<string | null> =
    new BehaviorSubject<string | null>(null);

  constructor(
    private apiService: ApiService,
    private authenticationService: AuthenticationService,
  ) {
    this.authenticationService.isAuthenticated$.subscribe((authenticated) => {
      if (authenticated) {
        // User is authenticated, fetch companies
        this.fetchCompanies();
      }
    });
  }

  // Fetch companies from your API or other sources
  fetchCompanies(): void {
    this.apiService.fetchCompanies().subscribe(
      (response: any) => {
        const companies: Company[] = response.companyList.map(
          (companyData: any) => {
            return {
              name: companyData.companyName,
              id: companyData.id.toString(), // Convert to string
              img: '/assets/images/svgs/icon-office-bag.svg',
              selected: false,
              companyEndpoint: companyData.companyEndpoint,
            };
          },
        );

        // Update the companies in the subject
        this.companiesSubject.next(companies);

        // Check local storage for the last logged-in company
        const lastCompanyId = localStorage.getItem('lastCompanyId');
        const hasActiveCompany = this.activeCompanySubject.value !== null;

        // if (hasActiveCompany) {
        //   console.log(
        //     'Active company already exists:',
        //     this.activeCompanySubject.value
        //   );
        // }

        if (lastCompanyId) {
          const lastCompany = this.companiesSubject.value.find(
            (company) => company.id === lastCompanyId,
          );

          if (lastCompany) {
            // Case 1: When the user logs in and has a lastCompanyId in localStorage
            this.setActiveCompany(lastCompany);

            // console.log(
            //   'Setting active company from localStorage:',
            //   lastCompany
            // );
          }
        } else if (!hasActiveCompany) {
          // Case 2: When the user logs in and has no active company or valid lastCompanyId in localStorage
          // Set the active company to a default company if available
          this.companiesSubject.subscribe((companies) => {
            if (companies.length > 0) {
              const defaultCompany = companies[0];
              this.setActiveCompany(defaultCompany);
              //console.log('Setting active company to default:', defaultCompany);
            }
          });
        }
      },
      (error) => {
        console.error('Error fetching companies:', error);
      },
    );
  }

  // Get the list of companies as an observable
  getCompanies(): Observable<Company[]> {
    return this.companiesSubject.asObservable();
  }

  // Get the active company as an observable
  getActiveCompany(): Observable<Company | null> {
    return this.activeCompanySubject.asObservable();
  }

  // Function to set the active company and update active roles
  setActiveCompany(company: Company): void {
    console.log('Given company: ' + JSON.stringify(company));
    // Update the selected property of the previous active company
    if (this.activeCompanySubject.value) {
      this.activeCompanySubject.value.selected = false;
    }

    // Update the selected property of the new active company
    company.selected = true;

    // Update the active company in the subject
    this.activeCompanySubject.next(company);

    // Log the active company
    console.log('Active Company:', company);

    // Store the last logged-in company in session storage
    localStorage.setItem('lastCompanyId', company.id);

    // Check if the active company endpoint matches the first four characters of the stored roles
    const activeCompanyEndpoint = company.companyEndpoint;

    console.log('Checking active company endpoint:', activeCompanyEndpoint);

    // Notify that the active company has been set
    this.activeCompanySetSubject.next(true);

    // Log the company ID here as well
    console.log('Active Company ID:', company.id);

    sessionStorage.setItem('hasCompany', 'true');
  }

  // Add a method to check if the active company has been set
  activeCompanyIsSet(): Observable<boolean> {
    return this.activeCompanySetSubject.asObservable();
  }

  clearCompanies(): void {
    this.companiesSubject.next([]);
  }
}
