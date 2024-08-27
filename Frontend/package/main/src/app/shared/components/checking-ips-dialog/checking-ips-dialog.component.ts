import { Component, Inject, OnDestroy } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Subscription } from 'rxjs';
import { CommunicationService } from 'src/app/pages/scan-id/desktop-scan-id/communication.service';

@Component({
  selector: 'app-checking-ips-dialog',
  templateUrl: './checking-ips-dialog.component.html',
  styleUrls: ['./checking-ips-dialog.component.scss'],
})
export class CheckingIpsDialogComponent implements OnDestroy {
  currentImageIndex: number = 0;
  subscription: Subscription;
  progress: number = 0; // Add a progress property
  ellipsis: string = '';

  constructor(
    public dialogRef: MatDialogRef<CheckingIpsDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private communicationService: CommunicationService, // Inject the service
  ) {
    this.subscription = this.communicationService.currentImageIndex$.subscribe(
      (index) => {
        this.currentImageIndex = index;

        // Add ellipsis dots based on currentImageIndex
        if (this.currentImageIndex === 0) {
          this.ellipsis = '';
        } else {
          const ellipsisCount = (this.currentImageIndex % 4) + 1; // Change 4 to the desired number of dots
          this.ellipsis = '.'.repeat(ellipsisCount);
        }
      },
    );
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }
}
