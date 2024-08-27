import { Component } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { Subject, takeUntil } from 'rxjs';
import { ApiService } from 'src/app/_services/api/api.service';
import { CompanyServiceService } from 'src/app/_services/company/company-service.service';

@Component({
  selector: 'app-invite-users',
  templateUrl: './invite-users.component.html',
  styleUrls: ['./invite-users.component.scss'],
})
export class InviteUsersComponent {
  EMAIL_REGEX = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
  roleControl = new FormControl('EMPLOYEE');

  private ngUnsubscribe = new Subject<void>();
  constructor(
    private companyService: CompanyServiceService,
    private apiService: ApiService,
  ) {}

  emailControl = new FormControl('', [
    Validators.required,
    Validators.pattern(this.EMAIL_REGEX),
  ]);

  inviteUser() {
    const email = this.emailControl.value;
    const role = this.roleControl.value;

    if (!email || !role) {
      console.error('Email or role is empty or null.');
      return;
    }

    this.companyService
      .getActiveCompany()
      .pipe(takeUntil(this.ngUnsubscribe))
      .subscribe((activeCompany) => {
        if (activeCompany) {
          this.apiService.inviteUser(email, role, activeCompany.id).subscribe(
            () => {
              console.log('User invited successfully');
              this.emailControl.reset();
            },
            (error) => {
              console.error('Error inviting user:', error);
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
