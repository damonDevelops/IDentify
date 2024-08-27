import { Component } from '@angular/core';
import { Subject, takeUntil } from 'rxjs';
import { ApiService } from 'src/app/_services/api/api.service';
import { CompanyServiceService } from 'src/app/_services/company/company-service.service';
@Component({
  selector: 'app-view-users',
  templateUrl: './view-users.component.html',
  styleUrls: ['./view-users.component.scss'],
})
export class ViewUsersComponent {
  users: any[] = [];
  expandedUserIds: number[] = [];
  private ngUnsubscribe = new Subject<void>();

  constructor(
    private apiService: ApiService,
    private companyService: CompanyServiceService,
  ) {}

  ngOnInit() {
    this.fetchUsers();
  }

  // Function to set the expanded user
  setExpandedUser(userId: number): void {
    const index = this.expandedUserIds.indexOf(userId);
    if (index === -1) {
      this.expandedUserIds.push(userId);
    } else {
      this.expandedUserIds.splice(index, 1);
    }
  }

  fetchUsers(): void {
    this.companyService
      .getActiveCompany()
      .pipe(takeUntil(this.ngUnsubscribe))
      .subscribe((activeCompany) => {
        if (activeCompany) {
          this.apiService.fetchUsers(activeCompany.id).subscribe(
            (response) => {
              // Handle response data
              console.log('Fetched users:', response);
              this.users = response; // Update the users property
            },
            (error) => {
              console.error('Error fetching users:', error);
            },
          );
        }
      });
  }

  ngOnDestroy() {
    this.ngUnsubscribe.next();
    this.ngUnsubscribe.complete();
  }
}
