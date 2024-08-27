import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ApiService } from 'src/app/_services/api/api.service';

@Component({
  selector: 'app-expanded-data-modal',
  templateUrl: './expand-data-modal-mobile.component.html',
  styleUrls: ['./expand-data-modal-mobile.component.scss'],
})
export class ExpandDataModalMobileComponent {
  individualCardData: any;
  rowId: any;
  cardProperties: any[] = []; // Array to store processed properties

  constructor(
    private apiService: ApiService,
    public dialogRef: MatDialogRef<ExpandDataModalMobileComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
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

    this.apiService.editIndividualResult(this.rowId, updatedData, false);

    //figure out a way to refresh page on snackbar close
    this.dialogRef.close();
  }

  deleteRecord(): void {
    // Implement logic to delete the record
    // You might want to show a confirmation dialog
    this.dialogRef.close();
  }
}
