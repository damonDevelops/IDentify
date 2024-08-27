import { Component } from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  ValidationErrors,
  Validators,
} from '@angular/forms';
import { CompanyServiceService } from 'src/app/_services/company/company-service.service';
import { AbnSearchService } from 'src/app/_services/api/abn.search.service';
import { ApiService } from 'src/app/_services/api/api.service';
import { switchMap, of, catchError } from 'rxjs';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-create-company',
  templateUrl: './create-company.component.html',
  styleUrls: ['./create-company.component.scss'],
})
export class CreateCompanyComponent {
  companyNameForm: FormGroup;
  isAbnValid: boolean = false; // Initialize isAbnValid property
  companyName: string = ''; // Property to store the company name
  companyEndpoint: string = '';

  constructor(
    private fb: FormBuilder,
    private companyService: CompanyServiceService,
    private abnService: AbnSearchService,
    private apiService: ApiService,
    private snackBar: MatSnackBar,
  ) {
    this.companyNameForm = this.fb.group({
      companyName: [
        '',
        [Validators.required, Validators.pattern(/^[A-Za-z0-9_\s-]+$/)],
      ],
      abn: [
        '',
        [
          Validators.required,
          (control: AbstractControl) => this.validateAbn(control.value),
        ],
      ],
    });
  }

  ngOnInit() {
    // Subscribe to ABN form control's valueChanges
    const abnControl = this.companyNameForm.get('abn');
    if (abnControl) {
      abnControl.valueChanges.subscribe((abnValue) => {
        // Ensure abnValue is a string and not null or undefined
        if (typeof abnValue === 'string') {
          // Call the validateAbn function and update isAbnValid
          this.isAbnValid = this.validateAbn(abnValue) === null;
          console.log(abnValue + ' Validation Result:', this.isAbnValid);
        }
      });
    }
  }

  createCompany() {
    const guid = 'b15308b8-e593-4bd4-a794-1ea411bf836c';
    const abn = this.companyNameForm.value.abn;

    this.abnService
      .searchByABN(guid, abn, true)
      .pipe(
        switchMap((result) => {
          // Check the ABN status
          if (result.entityStatus.entityStatusCode === 'Active') {
            // ABN is active, proceed with creating the company
            return this.apiService.createCompany(
              this.companyNameForm.value.companyName,
            );
          } else {
            // ABN is not active, show a snackbar
            this.snackBar.open(
              'ABN is Cancelled, please provide a valid active ABN',
              'Close',
              {
                duration: 3000,
              },
            );
            return of(null); // Return an empty observable
          }
        }),
        catchError((error) => {
          console.error(
            'Error occurred during ABN search or company creation:',
            error,
          );
          // Show an error snackbar
          this.snackBar.open('An error occurred', 'Close', {
            duration: 3000,
          });
          return of(null); // Return an empty observable
        }),
      )
      .subscribe((result) => {
        if (result) {
          const snackBarRef = this.snackBar.open(
            'Company created successfully',
            'Close',
            {
              duration: 3000,
            },
          );

          snackBarRef.afterDismissed().subscribe(() => {
            // Now that the company is created, refresh the companies list
            // and set the new company as the active company
            this.companyService.fetchCompanies();
            this.companyService.setActiveCompany(result);

            window.location.reload();
          });
        }
      });
  }

  validateAbn(abn: string): ValidationErrors | null {
    if (typeof abn !== 'string') {
      return null; // Return null for valid value
    }

    // Remove spaces and check for 11 digits
    abn = abn.replace(/\s/g, '');

    if (abn.length !== 11 || !/^\d+$/.test(abn)) {
      return { invalidABN: true }; // Return a custom error
    }

    // Convert ABN to an array of numbers
    const abnArray = abn.split('').map(Number);

    // Subtract 1 from the first (left-most) digit
    abnArray[0] = abnArray[0] - 1;

    // Define the weighting factors for each position
    const weightingFactors = [10, 1, 3, 5, 7, 9, 11, 13, 15, 17, 19];

    // Calculate the sum of products
    let sum = 0;
    for (let i = 0; i < 11; i++) {
      sum += abnArray[i] * weightingFactors[i];
    }

    // Check if the remainder after dividing by 89 is zero
    if (sum % 89 === 0) {
      return null; // Return null for a valid ABN
    } else {
      return { invalidABN: true }; // Return a custom error for an invalid ABN
    }
  }
}
