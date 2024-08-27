import { Component, Inject, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { FormControl } from '@angular/forms';
import { MatAutocomplete } from '@angular/material/autocomplete';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import {
  startWith,
  map,
  Observable,
  Subscription,
  BehaviorSubject,
  Subject,
  takeUntil,
} from 'rxjs';
import { ApiService } from 'src/app/_services/api/api.service';
import { CompanyServiceService } from 'src/app/_services/company/company-service.service';

@Component({
  selector: 'app-add-user-dialog',
  templateUrl: './add-user-dialog.component.html',
  styleUrls: ['./add-user-dialog.component.scss'],
})
export class AddUserDialogComponent implements OnInit, OnDestroy {
  private subscriptions: Subscription[] = [];
  myControl = new FormControl('');
  users: any[] = [];
  filteredUsers: any[] = [];
  emailsWithAccess: Set<string> = new Set<string>();

  filteredOptions: Observable<string[]>;
  private filteredOptionsSubject = new BehaviorSubject<string[]>([]);

  @ViewChild('auto') matAutocomplete: MatAutocomplete;
  private ngUnsubscribe = new Subject<void>();

  constructor(
    private apiService: ApiService,
    private companyService: CompanyServiceService,
    private snackBar: MatSnackBar,
    @Inject(MAT_DIALOG_DATA) public data: any,
  ) {}

  ngOnInit() {
    this.subscriptions.push(this.fetchCollectionPointInformation());
    this.subscriptions.push(this.fetchUsers());
    this.filteredOptions = this.filteredOptionsSubject.asObservable();
  }

  private _filter(value: string): string[] {
    const filterValue = value.toLowerCase();
    return this.filteredUsers
      .filter(
        (user) =>
          (user.username || '').toLowerCase().includes(filterValue) ||
          (user.email || '').toLowerCase().includes(filterValue),
      )
      .map((user) => user.email);
  }

  fetchCollectionPointInformation(): Subscription {
    return this.companyService
      .getActiveCompany()
      .pipe(takeUntil(this.ngUnsubscribe))
      .subscribe((activeCompany) => {
        if (activeCompany) {
          this.apiService
            .getCollectionPointInformation(
              this.data.collectionPointId,
              activeCompany.companyEndpoint,
            )
            .subscribe(
              (response) => {
                console.log('Collection Point Information:', response);
                this.emailsWithAccess = new Set(
                  response.userAccessList.map(
                    (user: { email: any }) => user.email,
                  ),
                );
                this.filterUsersWithAccess();
              },
              (error) => {
                console.error(
                  'Error fetching collection point information:',
                  error,
                );
              },
            );
        }
      });
  }

  fetchUsers(): Subscription {
    return this.companyService
      .getActiveCompany()
      .pipe(takeUntil(this.ngUnsubscribe))
      .subscribe((activeCompany) => {
        if (activeCompany) {
          this.apiService.fetchUsers(activeCompany.id).subscribe(
            (response) => {
              console.log('Fetched Users:', response);
              this.users = response.map((user) => ({
                id: user.id,
                email: user.email,
                username: user.username,
              }));
              this.filterUsersWithAccess();
            },
            (error) => {
              console.error('Error fetching users:', error);
            },
          );
        }
      });
  }

  filterUsersWithAccess(): void {
    this.filteredUsers = this.users.filter(
      (user) => !this.emailsWithAccess.has(user.email),
    );
    console.log('Filtered Users:', this.filteredUsers);

    // Update the filteredOptionsSubject when filteredUsers changes
    this.filteredOptionsSubject.next(
      this.filteredUsers.map((user) => user.email),
    );
  }

  openAutocompletePanel(): void {
    this.myControl.setValue('');
    this.matAutocomplete._isOpen = true;
  }

  addUserSubmit(): void {
    const selectedEmail = this.myControl.value;
    this.companyService
      .getActiveCompany()
      .pipe(takeUntil(this.ngUnsubscribe))
      .subscribe((activeCompany) => {
        if (activeCompany) {
          this.apiService
            .addUserToCollectionPoint(
              activeCompany.id,
              this.data.collectionPointId,
              selectedEmail,
            )
            .subscribe(
              (response) => {
                // Successful response
                this.showSnackbar('User added successfully', 'success');
                console.log('Response: ', response);

                // Clear the autocomplete input
                this.myControl.setValue('');

                // Remove the added person from the autocomplete list
                this.removeUserFromAutocomplete(selectedEmail);
              },
              (error) => {
                // Error handling
                this.showSnackbar('Error adding user', 'error');
                console.error('Error adding user:', error);
              },
            );
        }
      });
  }

  private removeUserFromAutocomplete(email: any): void {
    const index = this.users.findIndex((user) => user.email === email);
    if (index !== -1) {
      this.users.splice(index, 1);
      this.filterUsersWithAccess(); // Update the filteredUsers and options
    }
  }

  private showSnackbar(message: string, panelClass: string): void {
    this.snackBar.open(message, 'Close', {
      duration: 3000, // Adjust the duration as needed
      panelClass: panelClass, // Use 'success' or 'error' as classes for styling
    });
  }

  ngOnDestroy() {
    this.subscriptions.forEach((subscription) => subscription.unsubscribe());

    this.ngUnsubscribe.next();
    this.ngUnsubscribe.complete();
  }
}
