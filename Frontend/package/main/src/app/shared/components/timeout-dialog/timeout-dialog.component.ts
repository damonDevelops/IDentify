import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-timeout-dialog',
  templateUrl: './timeout-dialog.component.html',
  styleUrls: ['./timeout-dialog.component.scss'],
})
export class TimeoutDialogComponent {
  constructor(public dialogRef: MatDialogRef<TimeoutDialogComponent>) {}
}
