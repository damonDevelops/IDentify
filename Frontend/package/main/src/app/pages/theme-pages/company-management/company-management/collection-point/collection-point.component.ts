import { Component } from '@angular/core';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { SafeUrl } from '@angular/platform-browser';
import { ApiService } from 'src/app/_services/api/api.service';
import { CompanyServiceService } from 'src/app/_services/company/company-service.service';
import { environment } from 'src/environments/environment';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatButtonModule } from '@angular/material/button';
import { AuthoritiesService } from 'src/app/_services/authorities/authorities.service';
import { AddUserDialogComponent } from './add-user-dialog/add-user-dialog/add-user-dialog.component';
import { Subject, takeUntil } from 'rxjs';

interface CollectionPoint {
  id: string;
  endpoint: string;
  name: string;
  accessList: AccessListUser[];
  companyName: string;
  state: string;
}

interface AccessListUser {
  id: number;
  username: string;
  email: string;
}

enum State {
  DEACTIVATED = 'DEACTIVATED',
  ACTIVE = 'ACTIVE',
}

@Component({
  selector: 'app-collection-point',
  templateUrl: './collection-point.component.html',
  styleUrls: ['./collection-point.component.scss'],
})
export class CollectionPointComponent {
  collectionPoints: CollectionPoint[] = [];
  expandedPointIds: string[] = [];
  collectionPointForm: FormGroup;
  collectionPointName: string = ''; // Store the collection point name

  userHasEditCompanyPermission: boolean = false; // Add this property

  private ngUnsubscribe = new Subject<void>();

  constructor(
    private apiService: ApiService,
    private companyService: CompanyServiceService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar,
    private fb: FormBuilder,
    private authoritiesService: AuthoritiesService,
  ) {
    this.collectionPointForm = this.fb.group({
      collectionPointName: ['', Validators.required],
      collectionPoint: ['', Validators.required],
    });

    // Subscribe to the authorities to check for 'EDIT_COMPANY' permission
    this.authoritiesService.companyPermissions$.subscribe((permissions) => {
      this.userHasEditCompanyPermission = permissions.includes('EDIT_COMPANY');
    });
  }

  ngOnInit(): void {
    if (this.apiService.getHasCompany()) {
      this.fetchCollectionPoints();
    }
  }

  //qr code data varaible store
  qrCodeUrl: string = `${environment.frontendUrl}/authentication/scan`;
  qrCodeLogo: string = './assets/images/logos/IDentifyLogoWH.png';
  qrCodeImagePath: SafeUrl = '';

  onDownloadUrl(url: SafeUrl) {
    this.qrCodeImagePath = url;
  }
  getEndpointB64(pointIndex: number): string {
    let returnString: string | null = null;

    // Subscribe to the getActiveCompany() observable
    this.companyService
      .getActiveCompany()
      .pipe(takeUntil(this.ngUnsubscribe))
      .subscribe((activeCompany) => {
        // Now that you have the companyEndpoint, you can use it
        const endpointB64 = btoa(
          activeCompany!.companyEndpoint +
            '/' +
            this.collectionPoints[pointIndex].endpoint +
            '/',
        );

        // Do something with endpointB64 if needed
        returnString = endpointB64;
      });

    // Return an initial value or null if the subscription hasn't provided a value yet
    return returnString!;
  }

  fetchCollectionPoints() {
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

  getState(pointIndex: number): boolean {
    return this.collectionPoints[pointIndex].state === State.ACTIVE;
  }

  getToggledState(pointIndex: number): State {
    return this.collectionPoints[pointIndex].state === State.DEACTIVATED
      ? State.ACTIVE
      : State.DEACTIVATED;
  }

  onStatusToggle(pointIndex: number) {
    this.companyService
      .getActiveCompany()
      .pipe(takeUntil(this.ngUnsubscribe))
      .subscribe((activeCompany) => {
        if (activeCompany) {
          // If there's an active company, update the collection points here
          this.apiService
            .putCollectionPointState(
              this.collectionPoints[pointIndex].endpoint,
              this.getToggledState(pointIndex),
              activeCompany.companyEndpoint,
            )
            .subscribe((response) => {
              console.log('State updated successful');
              this.collectionPoints[pointIndex].state =
                this.getToggledState(pointIndex);
            });
        }
      });
  }

  openDeletePointDialog(pointIndex: number) {
    const dialogRef = this.dialog.open(CollectionPointDeleteDialog);

    dialogRef.afterClosed().subscribe((result) => {
      if (result === true) {
        this.companyService
          .getActiveCompany()
          .pipe(takeUntil(this.ngUnsubscribe))
          .subscribe((activeCompany) => {
            if (activeCompany) {
              this.apiService
                .deleteCollectionPoint(
                  this.collectionPoints[pointIndex].endpoint,
                  activeCompany.companyEndpoint,
                )
                .subscribe((response) => {
                  console.log('Deleted point successful');
                  window.location.reload();
                });
            }
          });
      }
    });
  }

  addUserAccess(pointIndex: number): void {
    console.log('Point index: ' + this.collectionPoints[pointIndex].endpoint);
    //make a call to the api to add a user
    const dialogRef = this.dialog.open(AddUserDialogComponent, {
      data: { collectionPointId: this.collectionPoints[pointIndex].endpoint },
      width: '350px',
    });
  }

  setExpandedPoint(pointId: string): void {
    const index = this.expandedPointIds.indexOf(pointId);
    if (index === -1) {
      this.expandedPointIds.push(pointId);
    } else {
      this.expandedPointIds.splice(index, 1);
    }
  }

  createNewCollectionPoint() {
    // Check if the user has 'EDIT_COMPANY' permission before creating a new collection point
    if (!this.userHasEditCompanyPermission) {
      // You can show a message or handle it in any way you prefer
      console.log(
        'User does not have permission to create a new collection point',
      );
      return;
    }

    const collectionPointName =
      this.collectionPointForm.value.collectionPointName;
    const collectionPoint = this.collectionPointForm.value.collectionPoint;

    this.companyService
      .getActiveCompany()
      .pipe(takeUntil(this.ngUnsubscribe))
      .subscribe((activeCompany) => {
        if (activeCompany) {
          this.apiService
            .createNewCollectionPoint(
              collectionPointName,
              collectionPoint,
              activeCompany.id,
            )
            .subscribe(
              () => {
                const snackBarRef = this.snackBar.open(
                  'Collection point created successfully',
                  'Close',
                  {
                    duration: 3000,
                  },
                );

                snackBarRef.afterDismissed().subscribe(() => {
                  // This code will be executed after the snackbar is closed
                  window.location.reload();
                });
              },
              (error) => {
                console.error('Error creating collection point:', error);
              },
            );
        }
      });
  }

  ngOnDestroy() {
    this.ngUnsubscribe.next();
    this.ngUnsubscribe.complete();
  }
}

@Component({
  selector: 'delete-point-dialog',
  templateUrl: 'company-collection-point-dialog.html',
  standalone: true,
  imports: [MatDialogModule, MatButtonModule],
})
export class CollectionPointDeleteDialog {}
