import { Component, ElementRef, ViewChild } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Input } from '@angular/core';
import { Subject, Subscription, finalize, interval } from 'rxjs';
import { takeUntil, takeWhile } from 'rxjs/operators';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';

import { ApiService } from 'src/app/_services/api/api.service';
import { TimeoutDialogComponent } from 'src/app/shared/components/timeout-dialog/timeout-dialog.component';
import { CheckingIpsDialogComponent } from 'src/app/shared/components/checking-ips-dialog/checking-ips-dialog.component';
import { RedirectDialogComponent } from 'src/app/shared/components/redirect-dialog/redirect-dialog.component';
import { ExpandDataModalMobileComponent } from 'src/app/shared/expand-data-modal-mobile/expand-data-modal-mobile.component';
import { CompanyServiceService } from 'src/app/_services/company/company-service.service';
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
  isValid = false;
  selectedCollectionPoint: any; // Add this variable to store the selected collection point
  collectionPoints: any[] = []; // Initialize collectionPoints array
  imagePreviews: string[] = [];
  @ViewChild('uploadedImage') uploadedImage: ElementRef;

  isUploadDisabled = false;
  isSubmitDisabled = true;
  isRemoveDisabled = true;

  private ngUnsubscribe = new Subject<void>();

  @Input()
  requiredFileType: string;

  individualCardData: any;
  cardProperties: any[] = []; // Array to store processed properties

  constructor(
    private apiService: ApiService,
    private http: HttpClient,
    public dialog: MatDialog,
    public redirectDialog: MatDialog,
    public timeOutDialog: MatDialog,
    private companyService: CompanyServiceService,
  ) {}

  ngOnInit() {
    console.log("You're viewing the mobile version");
    this.fetchCollectionPoints();
  }

  fetchCollectionPoints(): void {
    this.companyService
      .getActiveCompany()
      .pipe(takeUntil(this.ngUnsubscribe))
      .subscribe((activeCompany) => {
        if (activeCompany) {
          // If there's an active company, update the collection points here
          this.apiService.fetchCollectionPointsNew(activeCompany.id).subscribe(
            (response) => {
              this.collectionPoints = response;
            },
            (error) => {
              // Handle error if needed
            },
          );
        }
      });
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
        this.imagePreviews.push(event.target.result);
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
    console.log(this.selectedCollectionPoint);
    this.openDialog('0ms', '0ms');
    const formData = new FormData();
    formData.append('image', this.file);

    this.companyService
      .getActiveCompany()
      .pipe(takeUntil(this.ngUnsubscribe))
      .subscribe((activeCompany) => {
        if (activeCompany) {
          this.apiService
            .submitImage(
              activeCompany.companyEndpoint,
              this.selectedCollectionPoint.endpoint,
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
                          //change this logic slightly to enable the user to edit the card information

                          this.apiService
                            .loadIndividualResult(data.jobId)
                            .subscribe(
                              (result) => {
                                this.individualCardData = result;
                                this.processCardProperties(); // Preprocess data

                                const dialogRef = this.dialog.open(
                                  ExpandDataModalMobileComponent,
                                  {
                                    width: '400px', // Adjust width as needed
                                    data: {
                                      individualCardData: result,
                                      rowId: data.jobId,
                                    },
                                  },
                                );

                                dialogRef.afterClosed().subscribe(() => {
                                  // This code will be executed once the dialog is closed
                                  this.closeDialog();
                                  this.openRedirectDialog('0ms', '0ms', 1, 1);
                                  unsubscribeCalled = true;
                                  subscription.unsubscribe();
                                });
                              },
                              (error) => {
                                console.error(
                                  "Couldn't fetch individual card result: " +
                                    error,
                                );
                              },
                            );
                          // Unsubscribe to break out of the loop completely
                          unsubscribeCalled = true;
                          subscription.unsubscribe();
                        }
                      },
                      error: (error) => {
                        console.error('Error while checking state: ', error);
                        // Optionally handle error if needed
                      },
                    });
                  });

                // Optionally, you can show an initial message for debugging purposes
                // alert("Image Processing started!\nJob ID: " + data.jobId + "\nWaiting for completion...");
                // this.removeImage();

                //add an open dialog for the one saying whether or not the processing was successful
              },
              error: (error) => {
                alert(
                  'An error occurred, your image could not be processed at this time',
                );
                console.error('Error: ', error);
              },
            });
        }
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

  displayPointName(point: any): string {
    return point ? point.name : '';
  }

  ngOnDestroy() {
    this.ngUnsubscribe.next();
    this.ngUnsubscribe.complete();
  }
}

interface ApiResponse {
  jobId: string; // Replace 'string' with the actual type of 'state'
  // Add other properties if the API response contains more data.
}

interface NewApiResponse {
  state: string;
}
