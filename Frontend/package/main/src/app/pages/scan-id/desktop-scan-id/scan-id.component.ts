import { Component, Inject, HostListener } from '@angular/core';
import { environment } from 'src/environments/environment';
import { Input } from '@angular/core';
import { Subject, Subscription, concat, finalize, interval } from 'rxjs';
import { takeUntil, takeWhile } from 'rxjs/operators';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { ApiService } from 'src/app/_services/api/api.service';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { CommunicationService } from './communication.service';
import { MatSnackBar } from '@angular/material/snack-bar';

import { TimeoutDialogComponent } from 'src/app/shared/components/timeout-dialog/timeout-dialog.component';
import { RedirectDialogComponent } from 'src/app/shared/components/redirect-dialog/redirect-dialog.component';
import { CheckingIpsDialogComponent } from 'src/app/shared/components/checking-ips-dialog/checking-ips-dialog.component';
import { FailedJobsDialogComponent } from '../failed-jobs-dialog/failed-jobs-dialog.component';
import { CompanyServiceService } from 'src/app/_services/company/company-service.service';
@Component({
  selector: 'app-scan-id',
  templateUrl: './scan-id.component.html',
  styleUrls: ['./scan-id.scss'],
})
export class AppScanIdComponent {
  srcResult: any;
  fileName = '';
  uploadProgress: number | null;
  uploadSub: Subscription | null;
  returnVal: any;
  imageSrc = './assets/images/svgs/id-card.svg';
  hasImageBeenUploaded = true;
  file: any;
  isValid = false;
  selectedFiles: File[] = []; //for batch upload
  imagePreviews: string[] = [];
  readonly columnsInGrid = 4; // You can adjust this value as needed
  currentImageIndex: number = -1;
  totalImages: number = 0;

  private ngUnsubscribe = new Subject<void>();

  selectedCollectionPointName: string; // Variable to store the selected collection point name
  selectedCollectionPoint: any; // Variable to store the selected collection point object

  collectionPoints: any[] = []; // Initialize collectionPoints array
  isJobComplete: boolean = false; // Add this property
  isDesktop: boolean = true;
  isMobileScreen: boolean;
  maxFileUploads = 20;

  private failedJobsDialogOpened = false; // Add this flag

  @Input()
  requiredFileType: string;
  successfulImagesCount: number;

  constructor(
    private communicationService: CommunicationService,
    private apiService: ApiService,
    public dialog: MatDialog,
    public redirectDialog: MatDialog,
    public timeOutDialog: MatDialog,
    private companyService: CompanyServiceService,
    private snackBar: MatSnackBar,
  ) {
    this.checkScreenSize();

    this.communicationService.currentImageIndex$.subscribe((imageIndex) => {
      this.currentImageIndex = imageIndex;
    });
  }

  ngOnInit() {
    this.fetchCollectionPoints();
  }

  @HostListener('window:resize', ['$event'])
  onResize(event: any) {
    this.checkScreenSize();
  }

  checkScreenSize() {
    this.isDesktop = window.innerWidth > 768; // Set your desired breakpoint width
    this.isMobileScreen = window.innerWidth <= 768; // Adjust the breakpoint as needed
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
              console.log('New collection points: ' + JSON.stringify(response));
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
    data: any,
  ): void {
    const dialogRef = this.dialog.open(CheckingIpsDialogComponent, {
      width: '250px',
      enterAnimationDuration,
      exitAnimationDuration,
      data: {
        ...data,
        imagePreviews: this.imagePreviews,
      },
    });
  }

  closeDialog(): void {
    this.dialog.closeAll();
  }

  openTimeoutDialog(
    enterAnimationDuration: string,
    exitAnimationDuration: string,
  ): void {
    this.timeOutDialog.open(TimeoutDialogComponent, {
      width: '250px',
      enterAnimationDuration,
      exitAnimationDuration,
    });
  }

  onChangeFile(event: any) {
    if (this.isDesktop) {
      console.log('Desktop');
      if (event.target.files.length > 0) {
        let filesToAdd = Math.min(
          this.maxFileUploads - this.selectedFiles.length,
          event.target.files.length,
        );

        if (filesToAdd <= 0) {
          this.snackBar.open(
            'You have reached the maximum limit of 20 images.',
            'Close',
            {
              duration: 5000, // 5 seconds
            },
          );
        }

        for (let i = 0; i < filesToAdd; i++) {
          const file = event.target.files[i];
          const isFileAlreadySelected = this.selectedFiles.some(
            (selectedFile) => selectedFile.name === file.name,
          );

          if (!isFileAlreadySelected) {
            this.selectedFiles.push(file);

            const reader = new FileReader();
            reader.onload = (event: any) => {
              this.imagePreviews.push(event.target.result);
            };
            reader.readAsDataURL(file);
          } else {
            this.snackBar.open(
              'This file has already been selected.',
              'Close',
              {
                duration: 5000, // 5 seconds
              },
            );
          }
        }
      }
    } else if (this.isMobileScreen) {
      console.log('Mobile');
      if (event.target.files.length > 0) {
        this.fileName = event.target.files[0].name;
        this.file = event.target.files[0];

        const isFileAlreadySelected = this.selectedFiles.some(
          (selectedFile) => selectedFile.name === this.fileName,
        );

        if (!isFileAlreadySelected) {
          var reader = new FileReader();
          reader.readAsDataURL(this.file);
          reader.onload = (event: any) => {
            this.imageSrc = event.target.result;
          };
          this.hasImageBeenUploaded = false;
        } else {
          this.snackBar.open('This file has already been selected.', 'Close', {
            duration: 5000, // 5 seconds
          });
        }
      }
    }
  }

  clearSelectedFiles() {
    this.selectedFiles = [];
    this.imagePreviews = [];
  }

  processImages(event: any) {
    const jobIds: string[] = [];

    this.openDialog('0ms', '0ms', {
      totalImages: this.selectedFiles.length,
      currentImageIndex: 0,
      imagePreviews: this.imagePreviews,
    });

    // Add a 5-second delay before processing images
    setTimeout(() => {
      this.companyService
        .getActiveCompany()
        .pipe(takeUntil(this.ngUnsubscribe))
        .subscribe((activeCompany) => {
          if (!activeCompany) {
            console.error('No active company selected.');
            return;
          }

          for (let i = 0; i < this.selectedFiles.length; i++) {
            const file = this.selectedFiles[i];
            console.log('Submitting image:', file.name);

            const jobIdSubscription = this.apiService
              .submitImage(
                activeCompany.companyEndpoint, // Use activeCompany.companyEndpoint
                this.selectedCollectionPoint.endpoint,
                file,
              )
              .subscribe({
                next: (data: any) => {
                  console.log('Submitted image:', file.name);
                  jobIds.push(data.jobId);
                  jobIdSubscription.unsubscribe();
                },
                error: (error) => {
                  console.error('Error submitting image:', file.name, error);
                  jobIdSubscription.unsubscribe();
                },
              });
          }

          setTimeout(() => {
            this.checkJobStatus(jobIds);
          }, 1000); // Adjust delay as needed
        });
    }, 5000); // 5-second delay before processing images
  }

  private checkJobStatus(jobIds: string[]) {
    const completedJobs: string[] = [];
    const nonCompletedJobs: string[] = [];

    const checkJobState = (jobId: string, imageIndex: number) => {
      this.apiService.getJobState(jobId).subscribe({
        next: (completedData: any) => {
          console.log(
            'Checking state for jobId:',
            jobId,
            'State:',
            completedData.state,
          );

          if (completedData.state === 'COMPLETE') {
            completedJobs.push(jobId);
          } else {
            nonCompletedJobs.push(jobId);
          }

          this.communicationService.updateCurrentImageIndex(imageIndex + 1); // Move to the next image index
        },
        error: (error) => {
          console.error('Error checking job state:', jobId, error);
          nonCompletedJobs.push(jobId);

          this.communicationService.updateCurrentImageIndex(imageIndex + 1); // Move to the next image index
        },
      });
    };

    let currentJobIndex = 0;

    const checkNextJob = () => {
      if (currentJobIndex < jobIds.length) {
        checkJobState(jobIds[currentJobIndex], this.currentImageIndex); // Pass currentImageIndex as the image index
        currentJobIndex++;
      } else {
        // All jobs have been checked, handle final check
        this.handleFinalCheck(completedJobs, jobIds, nonCompletedJobs);
      }
    };

    const recheckNonCompletedJobs = () => {
      const nonCompletedCopy = [...nonCompletedJobs];
      nonCompletedJobs.length = 0; // Clear the array

      nonCompletedCopy.forEach((jobId) => {
        console.log('Rechecking status for jobId:', jobId);
        checkJobState(jobId, this.currentImageIndex); // Pass currentImageIndex as the image index
      });

      // Continue with final check after rechecking non-completed jobs
      setTimeout(() => {
        this.handleFinalCheck(completedJobs, jobIds, nonCompletedJobs);
      }, 2000); // 2 seconds delay
    };

    const checkInterval = interval(500); // Max check duration

    const checkSubscription = checkInterval.subscribe(() => {
      if (currentJobIndex === jobIds.length) {
        // First loop completed, recheck non-completed jobs
        recheckNonCompletedJobs();
        checkSubscription.unsubscribe(); // Stop further checking
      } else {
        // Continue checking the next job
        checkNextJob();
      }
    });

    // Start checking the first job
    checkNextJob();
  }

  private handleFinalCheck(
    completedJobs: string[],
    jobIds: string[],
    nonCompletedJobs: string[],
  ) {
    if (completedJobs.length === 0) {
      if (!this.failedJobsDialogOpened) {
        // Check if the dialog hasn't been opened already
        this.closeDialog();
        this.openFailedJobsDialog();
        this.failedJobsDialogOpened = true; // Set the flag to true
      }
    } else if (
      completedJobs.length + nonCompletedJobs.length ===
      jobIds.length
    ) {
      const successfulImagesCount = completedJobs.length;
      this.closeDialog();
      this.openRedirectDialog(
        '0ms',
        '0ms',
        jobIds.length,
        successfulImagesCount,
      );
      this.clearImages();
    }
  }

  //localhost:8080/submit/RKD3/testPoint2
  removeImage(index: number): void {
    this.imagePreviews.splice(index, 1);
    this.selectedFiles.splice(index, 1);
  }

  clearImages() {
    this.selectedFiles = [];
    this.imagePreviews = [];
  }

  onDragOver(event: DragEvent): void {
    event.preventDefault();
    event.stopPropagation();
    const dataTransfer = event.dataTransfer; // Type assertion
    if (dataTransfer) {
      dataTransfer.dropEffect = 'copy'; // Indicate that copying is allowed
      const dropArea = event.currentTarget as HTMLElement; // Type assertion
      dropArea.classList.add('drag-over'); // Add the drag-over class
    }
  }

  onDragLeave(event: DragEvent): void {
    event.preventDefault();
    event.stopPropagation();
    const dropArea = event.currentTarget as HTMLElement; // Type assertion
    dropArea.classList.remove('drag-over'); // Remove the drag-over class
  }

  onDrop(event: DragEvent): void {
    event.preventDefault();
    event.stopPropagation();

    if (this.selectedFiles.length >= this.maxFileUploads) {
      this.snackBar.open(
        'You have reached the maximum limit of 20 images.',
        'Close',
        {
          duration: 5000, // 5 seconds
        },
      );
      return;
    }

    const dataTransfer = event.dataTransfer;
    if (dataTransfer) {
      const files = dataTransfer.files;

      for (let i = 0; i < files.length; i++) {
        if (this.selectedFiles.length >= this.maxFileUploads) {
          break;
        }

        const file = files[i];
        const isFileAlreadySelected = this.selectedFiles.some(
          (selectedFile) => selectedFile.name === file.name,
        );

        if (!isFileAlreadySelected) {
          this.selectedFiles.push(file);

          const reader = new FileReader();
          reader.onload = (event: any) => {
            this.imagePreviews.push(event.target.result);
          };
          reader.readAsDataURL(file);
        } else {
          this.snackBar.open('This file has already been uploaded.', 'Close', {
            duration: 5000, // 5 seconds
          });
        }
      }

      const dropArea = event.currentTarget as HTMLElement;
      dropArea.classList.remove('drag-over');
    }
  }

  private handleDroppedFiles(files: FileList): void {
    // Check for duplicate files before adding them
    for (let i = 0; i < Math.min(this.maxFileUploads, files.length); i++) {
      const file = files[i];

      // Check if the file with the same name already exists in selectedFiles
      const isFileAlreadySelected = this.selectedFiles.some(
        (selectedFile) => selectedFile.name === file.name,
      );

      if (!isFileAlreadySelected) {
        this.selectedFiles.push(file);

        const reader = new FileReader();
        reader.onload = (event: any) => {
          this.imagePreviews.push(event.target.result);
        };
        reader.readAsDataURL(file);
      }
    }
  }

  openRedirectDialog(
    enterAnimationDuration: string,
    exitAnimationDuration: string,
    totalImages: number,
    successfulImagesCount: number,
  ): void {
    const dialogRef = this.dialog.open(RedirectDialogComponent, {
      width: '250px',
      enterAnimationDuration,
      exitAnimationDuration,
      data: { totalImages, successfulImagesCount }, // Pass the data here
    });

    dialogRef.afterClosed().subscribe(() => {
      // Refresh the page after closing the dialog
      window.location.reload();
    });
  }

  openFailedJobsDialog(): void {
    console.log('Called failed dialog');
    const dialogRef = this.dialog.open(FailedJobsDialogComponent, {
      width: '250px',
    });

    dialogRef.afterClosed().subscribe(() => {
      // Refresh the page after closing the dialog
      window.location.reload();
    });
  }

  displayPointName(point: any): string {
    return point ? point.name : '';
  }

  ngOnDestroy() {
    this.ngUnsubscribe.next();
    this.ngUnsubscribe.complete();
  }
}
