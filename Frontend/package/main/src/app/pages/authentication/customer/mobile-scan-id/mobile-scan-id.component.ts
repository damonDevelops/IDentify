import { Component, ElementRef, ViewChild } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Input } from '@angular/core';
import { Subscription, finalize, interval } from 'rxjs';
import { takeWhile } from 'rxjs/operators';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';

import { ApiService } from 'src/app/_services/api/api.service';
import { ActivatedRoute } from '@angular/router';

//dialogs for image processing
import { CheckingIpsDialogComponent } from 'src/app/shared/components/checking-ips-dialog/checking-ips-dialog.component';
import { ConsentDialogComponent } from 'src/app/shared/components/consent-dialog/consent-dialog.component';
import { RedirectDialogComponent } from 'src/app/shared/components/redirect-dialog/redirect-dialog.component';
import { TimeoutDialogComponent } from 'src/app/shared/components/timeout-dialog/timeout-dialog.component';
import { DeactivatedCollectionPointDialogComponent } from './deactivated-collection-point-dialog/deactivated-collection-point-dialog.component';
import { ExpandDataModalMobileComponent } from 'src/app/shared/expand-data-modal-mobile/expand-data-modal-mobile.component';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-mobile-scan-id',
  templateUrl: './mobile-scan-id.component.html',
  styleUrls: ['./mobile-scan-id.component.scss'],
})
export class MobileScanIdComponent {
  srcResult: any;
  fileName = '';
  uploadProgress: number | null;
  uploadSub: Subscription | null;
  returnVal: any;
  imageSrc = './assets/images/svgs/id-card.svg';
  file: any;
  @ViewChild('uploadedImage') uploadedImage: ElementRef;

  companyEndpoint = '';
  collectionPoint = '';
  selectedCollectionPoint: string; // Add this variable to store the selected collection point

  individualCardData: any;
  cardProperties: any[] = []; // Array to store processed properties

  isUploadDisabled = false;
  isSubmitDisabled = true;
  isRemoveDisabled = true;

  @Input()
  requiredFileType: string;

  constructor(
    private apiService: ApiService,
    private http: HttpClient,
    public dialog: MatDialog,
    public redirectDialog: MatDialog,
    public timeOutDialog: MatDialog,
    private route: ActivatedRoute,
    private snackBar: MatSnackBar, // Inject MatSnackBar
  ) {}

  ngOnInit() {
    // Extract the endpoint parameter from the URL
    this.route.queryParams.subscribe((params) => {
      if (params['endpoint']) {
        const decodedEndpoint = atob(params['endpoint']); // Decode the parameter
        const endpointParts = decodedEndpoint.split('/'); // Split into parts

        if (endpointParts.length >= 2) {
          this.companyEndpoint = endpointParts[0];
          this.collectionPoint = endpointParts[1];
          console.log('Decoded Company Endpoint:', this.companyEndpoint);
          console.log('Decoded Collection Point:', this.collectionPoint);
        }
      }
    });

    //Code below only works for logged in user, uncomment if backend allows it to be used by any user
    // this.apiService.getCollectionPointState(this.companyEndpoint, this.collectionPoint).subscribe(
    //   (response) => {
    //     console.log(response)
    //   },
    //   (error) => {
    //     console.error('Error fetching collection point state:', error);
    //   }
    // );

    this.openConsentDialog();
  }

  extractCompanyEndpoint(url: string): string {
    // Assuming the URL structure is like "https://identify.rodeo/authentication/scan/<companyEndpoint>"
    const parts = url.split('/');
    return parts[parts.length - 1];
  }

  openDialog(
    enterAnimationDuration: string,
    exitAnimationDuration: string,
  ): void {
    this.dialog.open(CheckingIpsDialogComponent, {
      width: '250px',
      enterAnimationDuration,
      exitAnimationDuration,
    });
  }

  openDeactivatedDialog(
    enterAnimationDuration: string,
    exitAnimationDuration: string,
  ): void {
    this.dialog.open(DeactivatedCollectionPointDialogComponent, {
      width: '250px',
      enterAnimationDuration,
      exitAnimationDuration,
    });
  }

  openConsentDialog(): void {
    const dialogRef = this.dialog.open(ConsentDialogComponent, {
      width: '300px', // Adjust the width as needed
    });
  }

  closeDialog(): void {
    this.dialog.closeAll();
  }

  openRedirectDialog(
    enterAnimationDuration: string,
    exitAnimationDuration: string,
    totalImages: number,
    successfulImagesCount: number,
  ): void {
    this.dialog
      .open(RedirectDialogComponent, {
        width: '250px',
        enterAnimationDuration,
        exitAnimationDuration,
        data: { totalImages, successfulImagesCount }, // Pass the data here
      })
      .afterClosed()
      .subscribe(() => {
        // This code will be executed once the dialog is closed
        this.closeDialog();
        // Reload the page
        this.reloadPage();
      });
  }

  openTimeoutDialog(
    enterAnimationDuration: string,
    exitAnimationDuration: string,
  ): void {
    this.timeOutDialog
      .open(TimeoutDialogComponent, {
        width: '250px',
        enterAnimationDuration,
        exitAnimationDuration,
      })
      .afterClosed()
      .subscribe(() => {
        // This code will be executed once the dialog is closed
        this.closeDialog();
        // Reload the page
        this.reloadPage();
      });
  }

  reloadPage() {
    window.location.reload();
  }

  onChangeFile(event: any) {
    if (event.target.files.length > 0) {
      this.fileName = event.target.files[0].name;
      this.file = event.target.files[0];

      var reader = new FileReader();
      reader.readAsDataURL(this.file);
      reader.onload = (event: any) => {
        this.imageSrc = event.target.result;
        if (this.uploadedImage) {
          this.uploadedImage.nativeElement.width = 200;
          this.uploadedImage.nativeElement.height = 200;
        }
        this.isUploadDisabled = true;
        this.isSubmitDisabled = false;
        this.isRemoveDisabled = false;
      };
    }
  }

  //this method needs to make a post request to the backend and then open up a dialog with a spinning wheel.
  //every so often it will check the state of that job and if it is completed, the user is told, and given the option to go view results
  processImage(event: any) {
    this.openDialog('0ms', '0ms');

    this.apiService
      .submitImageCustomer(
        this.companyEndpoint,
        this.collectionPoint,
        this.file,
      )
      .subscribe({
        next: (data: ApiResponse) => {
          // Make GET requests every second for 20 seconds
          const interval$ = interval(1000);
          let subscription: Subscription; // Create a Subscription object
          let unsubscribeCalled = false; // Flag to track if unsubscribe is called

          subscription = interval$
            .pipe(
              takeWhile((_, index) => index < 20),
              finalize(() => {
                // This code block will be executed after the 20 seconds are up (observable completes).
                // Check if unsubscribe is not called, then execute the additional code.
                if (!unsubscribeCalled) {
                  this.closeDialog();
                  this.openTimeoutDialog('0ms', '0ms');
                }
              }),
            )
            .subscribe(() => {
              console.log(data.jobId);
              this.apiService.getJobState(data.jobId).subscribe({
                next: (completedData: NewApiResponse) => {
                  if (completedData.state === 'COMPLETE') {
                    // This code will be executed once the dialog is closed
                    this.closeDialog();
                    this.openRedirectDialog('0ms', '0ms', 1, 1);
                    unsubscribeCalled = true;
                    subscription.unsubscribe();
                  }
                },
                error: (error) => {
                  // failed to get the job state, likely because the collection point is deactivated
                  console.error('Failed to check job state:', error);

                  if (error.status === 401) {
                    // Throw a dialog to let them know the collection point is deactivated
                    console.warn(
                      'Job failed, the collection point is deactivated',
                    );
                  } else {
                    // An error that we aren't aware of
                    // Throw a dialog that tells them we don't know what happened
                    console.warn('Jab failed, we dont know why');
                  }

                  // This code will be executed when the request fails
                  this.closeDialog();
                  unsubscribeCalled = true;
                  subscription.unsubscribe();
                },
              });
            });

          // Optionally, you can show an initial message for debugging purposes
          // alert("Image Processing started!\nJob ID: " + data.jobId + "\nWaiting for completion...");
          // this.removeImage();

          // Add an open dialog for the one saying whether or not the processing was successful
        },
        error: (error) => {
          // This code will be executed when the request fails
          this.closeDialog();

          console.error('Error processing image:', error);
          if (error.status === 401) {
            // Throw a dialog to let them know the collection point is deactivated
            console.warn('Job failed, the collection point is deactivated');
            this.openDeactivatedDialog('0ms', '0ms');
          } else {
            const snackBarRef = this.snackBar.open(
              'Sorry we could not process your image. Please consult the business',
              'OK',
              {
                duration: 3000, // Adjust the duration as needed
              },
            );

            snackBarRef.afterDismissed().subscribe(() => {
              // Reload the page when the snackbar is dismissed
              window.location.reload();
            });
          }
        },
      });
  }

  //localhost:8080/submit/RKD3/testPoint2
  removeImage() {
    this.imageSrc = './assets/images/svgs/id-card.svg';
    this.isUploadDisabled = false;
    this.isSubmitDisabled = true;
    this.isRemoveDisabled = true;
  }

  processCardProperties(): void {
    this.cardProperties = [];

    for (const [propertyName, propertyValue] of Object.entries(
      this.individualCardData.cardData,
    )) {
      this.cardProperties.push({ name: propertyName, values: propertyValue });
    }
  }
}

interface ApiResponse {
  jobId: string; // Replace 'string' with the actual type of 'state'
  // Add other properties if the API response contains more data.
}

interface NewApiResponse {
  state: string;
}
