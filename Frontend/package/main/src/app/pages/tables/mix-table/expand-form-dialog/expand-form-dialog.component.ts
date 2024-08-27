import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ApiService } from 'src/app/_services/api/api.service';
import { MatRadioModule } from '@angular/material/radio';

import { FormGroup } from '@angular/forms';
import { saveAs } from 'file-saver';
import Docxtemplater from 'docxtemplater';
import PizZip from 'pizzip';
import PizZipUtils from 'pizzip/utils/index.js';
import * as pdfMake from 'pdfmake/build/pdfmake';
import * as pdfFonts from 'pdfmake/build/vfs_fonts';
import { CompanyServiceService } from 'src/app/_services/company/company-service.service';
import { Subject, takeUntil } from 'rxjs';

function loadBlobContent(blob: Blob): Promise<Uint8Array> {
  return new Promise<Uint8Array>((resolve, reject) => {
    const reader = new FileReader();
    reader.onload = (event) => {
      const result = event.target?.result as ArrayBuffer;
      if (result) {
        resolve(new Uint8Array(result));
      } else {
        reject(new Error('Failed to load the blob content'));
      }
    };
    reader.onerror = () => {
      reject(new Error('Failed to read the blob'));
    };
    reader.readAsArrayBuffer(blob);
  });
}

function loadFile(
  urlOrFile: string | Blob, // Modify the function to accept a Blob
  callback: (error: any, content: PizZip.LoadData) => void,
) {
  if (typeof urlOrFile === 'string') {
    // If the argument is a string, assume it's a URL and load the content
    PizZipUtils.getBinaryContent(urlOrFile, (error, content) => {
      if (error) {
        callback(error, null as unknown as PizZip.LoadData); // Handle the error and provide a compatible value
      } else {
        callback(null, content);
      }
    });
  } else if (urlOrFile instanceof Blob) {
    // If the argument is a Blob object, load its content using the new function
    loadBlobContent(urlOrFile)
      .then((content) => {
        callback(null, content as PizZip.LoadData);
      })
      .catch((error) => {
        callback(error, null as unknown as PizZip.LoadData); // Handle the error
      });
  } else {
    callback(
      new Error('Invalid argument type'),
      null as unknown as PizZip.LoadData,
    ); // Handle the error
  }
}

@Component({
  selector: 'app-expanded-form-dialog',
  templateUrl: './expand-form-dialog.component.html',
  styleUrls: ['./expand-form-dialog.component.scss'],
})
export class FormDialogComponent implements OnInit {
  selectedForm: string;
  selectedFormat: string = 'word';
  forms: any[] = []; // Initialize forms as an empty array
  formOptions: any[] = []; // Define formOptions to hold the form data
  chosenForm: any;

  cardProperties: any[] = [];

  formatOptions: string[] = ['pdf', 'word'];
  savingFile: boolean = false; // Add a property to track file saving progress

  private ngUnsubscribe = new Subject<void>();

  constructor(
    public dialogRef: MatDialogRef<FormDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private apiService: ApiService,
    private companyService: CompanyServiceService,
  ) {}

  ngOnInit(): void {
    this.selectedFormat = 'word';

    this.companyService
      .getActiveCompany()
      .pipe(takeUntil(this.ngUnsubscribe))
      .subscribe((activeCompany) => {
        if (activeCompany) {
          this.apiService.getForms(activeCompany.id).subscribe(
            (forms) => {
              this.forms = forms;
              this.formOptions = forms.map(
                (form: { id: any; fileName: any }) => {
                  return { id: form.id, fileName: form.fileName };
                },
              );
              console.log(this.forms);
              this.processCardProperties();
            },
            (error) => {
              // Handle the error here if needed
            },
          );
        }
      });
  }

  processCardProperties(): void {
    this.cardProperties = [];

    for (const [propertyName, propertyValue] of Object.entries(
      this.data.cardData,
    )) {
      this.cardProperties.push({ name: propertyName, values: propertyValue });
    }
  }

  onFormSelectionChange(selectedFormId: string): void {
    console.log('Selected Form ID:', selectedFormId);
  }

  onSaveClick(): void {
    if (this.selectedForm) {
      this.companyService
        .getActiveCompany()
        .pipe(takeUntil(this.ngUnsubscribe))
        .subscribe((activeCompany) => {
          if (activeCompany) {
            this.apiService
              .getIndividualFile(this.selectedForm, activeCompany.id)
              .subscribe(
                (fileData) => {
                  // Save the file data to a component variable
                  this.chosenForm = fileData;
                  console.log('File data retrieved:', this.chosenForm);

                  // Now that you have the file, call the generateDocument function
                  this.generateDocument();
                },
                (error) => {
                  console.error('Error retrieving file data:', error);
                },
              );
          }
        });
    } else {
      console.log('No form selected.');
    }
  }

  generateDocument() {
    if (this.chosenForm) {
      this.savingFile = true; // Add a property to track file saving progress
      // Check if the chosenForm is available
      // Load the template document
      loadFile(this.chosenForm, (error: any, content: PizZip.LoadData) => {
        if (error) {
          console.error('Error loading document:', error);
          return;
        }

        const zip = new PizZip(content);
        const doc = new Docxtemplater(zip, {
          paragraphLoop: true,
          linebreaks: true,
        });

        // Prepare data for replacement in the document
        const data: any = {}; // Initialize an empty object for data

        // Iterate through cardProperties and add each property to data
        for (const property of this.cardProperties) {
          if (property.values && property.values.length > 0) {
            if (property.values.length === 1) {
              // If there is only one value, use it as is
              data[property.name] = property.values[0][0];
            } else {
              // If there are multiple values, join them with spaces
              data[property.name] = property.values
                .map((item: any[]) => item[0])
                .join(' ');
            }
          } else {
            // Handle the case where property.values is empty or undefined
            data[property.name] = ''; // or any other default value
          }
        }
        // You can also add additional properties to data if needed
        // data.additionalProperty = 'Additional Value';

        doc.setData(data);

        try {
          // Render the document (replace all occurrences of placeholders)
          doc.render();
        } catch (error) {
          // Handle any rendering errors
          console.error('Error rendering document:', error);
          return;
        }

        const editedContent = doc.getZip().generate({
          type: 'blob',
          mimeType:
            'application/vnd.openxmlformats-officedocument.wordprocessingml.document',
        });

        // Output the edited document using Data-URI
        const editedFile = new File([editedContent], 'edited_document.docx');

        console.log('EDITED CONTENT:', editedContent);

        if (this.selectedFormat === 'pdf') {
          // Convert the edited file to PDF and download it
          this.apiService.convertWordToPDF(editedFile).then((pdfBlobUrl) => {
            // Download the PDF Blob as a file
            fetch(pdfBlobUrl)
              .then((response) => response.blob())
              .then((pdfBlob) => {
                const pdfFile = new File([pdfBlob], 'converted_document.pdf');
                saveAs(pdfFile);
                window.location.reload();
              })
              .catch((error) => {
                console.error('Error downloading PDF:', error);
              });
          });
        } else {
          // Download the edited file
          saveAs(editedFile);
        }
      });
    }
  }

  onCancelClick(): void {
    // Log cardProperties
    console.log('Card Properties:', this.cardProperties);
    this.dialogRef.close(); // Close the dialog without a result
  }

  ngOnDestroy() {
    this.ngUnsubscribe.next();
    this.ngUnsubscribe.complete();
  }
}
