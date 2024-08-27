import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-consent-dialog',
  templateUrl: './consent-dialog.component.html',
  styleUrls: ['./consent-dialog.component.scss'],
})
export class ConsentDialogComponent {
  consentGiven: boolean = false;

  constructor(public dialogRef: MatDialogRef<ConsentDialogComponent>) {
    dialogRef.disableClose = true;
  }
}
