import { Component } from '@angular/core';
import { UserManagementService } from '../_services/user-management.service';
import { ApiUser } from '../_models/api_user';
import { ApiUserDataSource } from './ApiUserDataSource';
import { formatDate } from '@angular/common';
import { Observable } from 'rxjs';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { NewUserDialogComponent } from '../dialogs/newuser/newuser.component';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css'],
})
export class UsersComponent {
  // @ts-ignore
  dataSource: ApiUserDataSource;
  userSelected = false;
  loading = false;
  selectedUser!: ApiUser;
  columnsToDisplay = ['id', 'username', 'email', 'lastSeen'];
  roleList: Observable<String[]>;
  roleFc!: FormControl;
  editUserForm!: FormGroup;

  constructor(
    private userManagementService: UserManagementService,
    private formBuilder: FormBuilder,
    public dialog: MatDialog,
  ) {
    this.roleList = userManagementService.getRoleEnum();
  }
  ngOnInit() {
    this.dataSource = new ApiUserDataSource(this.userManagementService);
    this.editUserForm = this.formBuilder.group({
      roles: [[''], Validators.required],
      password: [''],
    });
  }

  get form() {
    return this.editUserForm.controls;
  }

  protected readonly formatDate = formatDate;

  selectUser(row: ApiUser) {
    this.selectedUser = row;
    this.userSelected = true;
    this.form['roles'].setValue(this.selectedUser.systemRoles);
  }

  onEditSubmission() {
    this.loading = true;
    console.log(this.selectedUser.systemRoles != this.form['roles'].value);
    if (this.selectedUser.id != undefined) {
      if (this.form['password'].value != '') {
        this.userManagementService
          .setUserPassword(this.selectedUser.id, this.form['password'].value)
          .subscribe((val) => console.log(val));
      }
      if (this.selectedUser.systemRoles != this.form['roles'].value) {
        this.userManagementService
          .setUserRoles(this.selectedUser.id, this.form['roles'].value)
          .subscribe((val) => console.log(val));
      }
    }
    this.loading = false;
    this.refresh();
  }

  refresh() {
    this.dataSource = new ApiUserDataSource(this.userManagementService);
  }

  openNewUser(): void {
    const dialogRef = this.dialog.open(NewUserDialogComponent, {
      width: '500px',
      data: { roleList: this.roleList },
    });
    dialogRef.afterClosed().subscribe((result) => {
      this.refresh();
    });
  }
}
