import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-deactivated-collection-point-dialog',
  templateUrl: './deactivated-collection-point-dialog.component.html',
  styleUrls: ['./deactivated-collection-point-dialog.component.scss'],
})
export class DeactivatedCollectionPointDialogComponent {
  constructor(
    public dialogRef: MatDialogRef<DeactivatedCollectionPointDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any, // Inject the data here
  ) {}
}
