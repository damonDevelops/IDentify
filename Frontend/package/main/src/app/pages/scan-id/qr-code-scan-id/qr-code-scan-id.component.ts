import { Component } from '@angular/core';
import { ApiService } from 'src/app/_services/api/api.service';
import { environment } from 'src/environments/environment';
import { SafeUrl } from '@angular/platform-browser';
import { NoCollectionPointsDialogComponent } from './no-collection-points-dialog/no-collection-points-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { Location } from '@angular/common';
import { CompanyServiceService } from 'src/app/_services/company/company-service.service';
import { Subject, takeUntil } from 'rxjs';
@Component({
  selector: 'desktop-qr-code-scan-id',
  templateUrl: './qr-code-scan-id.component.html',
})
export class DesktopQrCodeScanIdComponent {
  selectedCollectionPoint: string; //stores the selected collection point
  collectionPoints: any[] = []; //stores all collection points
  private ngUnsubscribe = new Subject<void>();

  isQRHidden = true;

  constructor(
    private apiService: ApiService,
    private dialog: MatDialog,
    private router: Router,
    private location: Location,
    private companyService: CompanyServiceService,
  ) {}

  ngOnInit() {
    this.fetchCollectionPoints();
  }

  onGenerateQR() {
    this.isQRHidden = false;
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
              this.collectionPoints = response.filter(
                (pt) => pt.state === 'ACTIVE',
              );
              if (this.collectionPoints.length === 0) {
                this.openNoCollectionPointsDialog();
              }
            },
            (error) => {
              // Handle error if needed
            },
          );
        }
      });
  }

  openNoCollectionPointsDialog(): void {
    const dialogRef = this.dialog.open(NoCollectionPointsDialogComponent, {
      width: '300px', // Set the desired width
    });

    dialogRef.afterClosed().subscribe(() => {
      // Use Location to navigate back to the previous URL
      this.location.back();
    });
  }

  qrCodeUrl: string = `${environment.frontendUrl}/authentication/scan`;
  qrCodeLogo: string = './assets/images/logos/IDentifyLogoWH.png';
  qrCodeImagePath: SafeUrl = '';
  onDownloadUrl(url: SafeUrl) {
    this.qrCodeImagePath = url;
  }

  getEndpointB64(): string {
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
            this.selectedCollectionPoint +
            '/',
        );

        // Do something with endpointB64 if needed
        returnString = endpointB64;
      });

    // Return an initial value or null if the subscription hasn't provided a value yet
    return returnString!;
  }

  onCollectionPointSelected() {
    this.onGenerateQR();
  }

  ngOnDestroy() {
    this.ngUnsubscribe.next();
    this.ngUnsubscribe.complete();
  }
}
