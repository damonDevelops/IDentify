import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-how-does-this-work-dialog',
  templateUrl: './how-does-this-work-dialog.component.html',
  styleUrls: ['./how-does-this-work-dialog.component.scss'],
})
export class HowDoesThisWorkDialogComponent {
  constructor(
    public dialog: MatDialog,
    private formBuilder: FormBuilder,
  ) {}

  openDialog(): void {
    const dialogRef = this.dialog.open(HowDoesThisWorkDialogComponent, {});
  }
}
