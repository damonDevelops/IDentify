import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-no-collection-points-dialog',
  templateUrl: './no-collection-points-dialog.component.html',
  styleUrls: ['./no-collection-points-dialog.component.scss'],
})
export class NoCollectionPointsDialogComponent {
  constructor(
    public dialogRef: MatDialogRef<NoCollectionPointsDialogComponent>,
  ) {}

  closeDialog(): void {
    this.dialogRef.close();
  }
}
