import { Component, ViewChild, AfterViewInit } from '@angular/core';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { BreakpointObserver } from '@angular/cdk/layout';
import { ApiService } from 'src/app/_services/api/api.service';
import { ExpandedDataModalComponent } from './expand-data-modal/expand-data-modal.component';
import { MatDialog } from '@angular/material/dialog';
import { FormDialogComponent } from './expand-form-dialog/expand-form-dialog.component';
import { CompanyServiceService } from 'src/app/_services/company/company-service.service';
import { Company } from 'src/app/_models/company';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Subject, takeUntil } from 'rxjs';

interface CollectionPoint {
  id: string;
  name: string;
  endpoint: string;
}

@Component({
  selector: 'app-mix-table',
  templateUrl: './mix-table.component.html',
  styleUrls: ['./mix-table.scss'],
})
export class AppMixTableComponent {
  activeCompany: Company;
  individualCardData: any;
  cardProperties: any[] = []; // Array to store processed properties

  displayedColumns = ['cardType', 'name', 'expand', 'useForms'];
  dataSource: MatTableDataSource<any> = new MatTableDataSource(); // Initialize the dataSource
  isLoadingResults = false; // Initialize isLoadingResults

  selectedCollectionPoint: any; // Variable to store the selected collection point object
  collectionPoints: CollectionPoint[] = [];
  @ViewChild(MatPaginator, { static: false }) paginator: MatPaginator =
    Object.create(null);
  @ViewChild(MatSort, { static: false }) sort: MatSort = Object.create(null);

  numForms: number = 0; // Variable to store the number of forms
  isSelectFormButtonDisabled: boolean = false; // Flag to disable the button

  currentPage = 1; // Initialize to the first page
  private ngUnsubscribe = new Subject<void>();

  constructor(
    private breakpointObserver: BreakpointObserver,
    private apiService: ApiService, // Inject your own api service
    private dialog: MatDialog,
    private companyService: CompanyServiceService,
    private snackbar: MatSnackBar,
  ) {
    breakpointObserver.observe(['(max-width: 600px)']).subscribe((result) => {
      this.displayedColumns = result.matches
        ? ['name', 'cardType', 'expand', 'useForms']
        : ['name', 'cardType', 'expand', 'useForms'];
    });

    // Fetch collection points
    //this.fetchCollectionPoints();
  }

  ngOnInit() {
    // Subscribe to the active company observable
    this.companyService
      .getActiveCompany()
      .pipe(takeUntil(this.ngUnsubscribe))
      .subscribe((activeCompany) => {
        if (activeCompany) {
          this.activeCompany = activeCompany;
          // If there's an active company, update the collection points here
          this.apiService.fetchCollectionPointsNew(activeCompany.id).subscribe(
            (response) => {
              this.collectionPoints = response;
            },
            (error) => {
              // Handle error if needed
            },
          );

          // Fetch forms for the active company
          this.apiService.getForms(activeCompany.id).subscribe(
            (forms) => {
              this.numForms = forms.length;
              this.isSelectFormButtonDisabled = this.numForms === 0; // Disable button if no forms
            },
            (error) => {
              console.error("Couldn't fetch forms: " + error);
            },
          );
        }
      });
  }

  /**
   * Set the paginator and sort after the view init since this component will
   * be able to query its view for the initialized paginator and sort.
   */
  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value.toLowerCase();
    this.dataSource.filter = filterValue;
  }

  async loadAllPagesData(endpoint: string) {
    console.log(endpoint);
    this.isLoadingResults = true; // Show spinner
    this.currentPage = 1; // Reset current page to 1

    try {
      // Fetch the first page of data to get the `numPages` value
      const firstPageData = await this.apiService
        .loadTableData(
          endpoint,
          this.activeCompany.companyEndpoint,
          this.currentPage,
        )
        .toPromise();

      if (firstPageData && firstPageData.numPages) {
        const numPages = firstPageData.numPages;

        // Initialize an array to store all the data
        const allData = firstPageData.results;

        // Fetch data for remaining pages sequentially
        for (let page = 2; page <= numPages; page++) {
          const newData = await this.apiService
            .loadTableData(endpoint, this.activeCompany.companyEndpoint, page)
            .toPromise();
          allData.push(...newData.results);
        }

        // Update the dataSource with all the data
        this.dataSource = new MatTableDataSource(allData);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
        this.isLoadingResults = false; // Turn off spinner
      } else {
        // Handle the case where there is no data available (not necessarily an error)
        console.warn('No data available for this endpoint.');
        this.isLoadingResults = false; // Turn off spinner
      }
    } catch (error) {
      // Handle the case where there's a problem with the API connection
      console.error('API connection error: ' + error);
      this.isLoadingResults = false; // Turn off spinner
      this.snackbar.open(
        'Failed to load table data due to an API connection issue. Please try again.',
        'Close',
        {
          duration: 5000,
          panelClass: ['error-snackbar'],
        },
      );
    }
  }

  expandRow(row: any): void {
    this.apiService.loadIndividualResult(row.id).subscribe(
      (result) => {
        this.individualCardData = result;
        this.processCardProperties(); // Preprocess data

        const dialogRef = this.dialog.open(ExpandedDataModalComponent, {
          width: '800px', // Adjust width as needed
          data: {
            individualCardData: result,
            rowId: row.id,
          },
        });
      },
      (error) => {
        console.error("Couldn't fetch individual card result: " + error);
      },
    );
  }

  openFormDialog(row: any): void {
    const dialogRef = this.dialog.open(FormDialogComponent, {
      width: '300px', // Adjust the width as needed
      data: row, // Pass any data you need to the dialog
    });

    dialogRef.afterClosed().subscribe((result) => {
      // Handle any result from the dialog if needed
      console.log('Dialog closed with result:', result);
    });
  }

  processCardProperties(): void {
    this.cardProperties = [];

    for (const [propertyName, propertyValue] of Object.entries(
      this.individualCardData.cardData,
    )) {
      this.cardProperties.push({ name: propertyName, values: propertyValue });
    }
  }

  onEndpointSelected(): void {
    if (this.selectedCollectionPoint) {
      this.isLoadingResults = true; // Show spinner
      this.loadAllPagesData(this.selectedCollectionPoint.endpoint); // Load table data with selected endpoint
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
