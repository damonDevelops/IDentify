import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ApiService } from 'src/app/_services/api/api.service';
import { MatSnackBar } from '@angular/material/snack-bar'; // Import MatSnackBar

@Component({
  selector: 'app-expanded-data-modal',
  templateUrl: './expand-data-modal.component.html',
  styleUrls: ['./expand-data-modal.component.scss'],
})
export class ExpandedDataModalComponent {
  individualCardData: any;
  rowId: any;
  cardProperties: any[] = []; // Array to store processed properties

  constructor(
    private apiService: ApiService,
    public dialogRef: MatDialogRef<ExpandedDataModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private snackBar: MatSnackBar,
  ) {
    this.individualCardData = data.individualCardData;
    this.rowId = data.rowId;
  }

  ngOnInit(): void {
    this.processCardProperties();
  }

  processCardProperties(): void {
    this.cardProperties = [];

    for (const [propertyName, propertyValue] of Object.entries(
      this.individualCardData.cardData,
    )) {
      this.cardProperties.push({ name: propertyName, values: propertyValue });
    }
  }

  isFormValid(): boolean {
    // Check if any field is empty
    for (const property of this.cardProperties) {
      for (const value of property.values) {
        if (!value[0]) {
          return false;
        }
      }
    }
    return true;
  }

  saveChanges(): void {
    const updatedData = {
      password: this.apiService.getPassword(),
      cardType: this.individualCardData.cardType,
      cardData: this.individualCardData.cardData,
    };
    console.log('Returning this: ' + JSON.stringify(updatedData));
    // Implement logic to save changes to the data
    // You can access and update this.data to reflect the changes

    this.apiService.editIndividualResult(this.rowId, updatedData, true);

    //figure out a way to refresh page on snackbar close
    this.dialogRef.afterClosed().subscribe(() => {
      window.location.reload();
    });
  }

  deleteRecord(): void {
    this.apiService.deleteIndividualResult(this.rowId).subscribe(
      (result) => {
        // Close the dialog
        this.dialogRef.close();

        // Display a snackbar with success message
        const successSnackBar = this.snackBar.open(
          'Record deleted successfully',
          'Dismiss',
        );

        // Subscribe to the afterDismissed() observable to refresh the page when the success snackbar is closed
        successSnackBar.afterDismissed().subscribe(() => {
          window.location.reload();
        });
      },
      (error) => {
        // Display a snackbar with an error message
        const errorSnackBar = this.snackBar.open(
          'An error occurred while deleting the record',
          'Dismiss',
          {
            panelClass: 'error-snackbar', // Apply a custom CSS class for error snackbar
          },
        );

        // You can add additional handling for the error here if needed
      },
    );
  }
}
