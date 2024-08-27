import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-failed-jobs-dialog',
  templateUrl: './failed-jobs-dialog.component.html',
  styleUrls: ['./failed-jobs-dialog.component.scss'],
})
export class FailedJobsDialogComponent {
  constructor(public dialogRef: MatDialogRef<FailedJobsDialogComponent>) {}
}
