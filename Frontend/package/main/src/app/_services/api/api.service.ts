import { Injectable } from '@angular/core';
import {
  HttpClient,
  HttpErrorResponse,
  HttpHeaders,
} from '@angular/common/http';
import { Observable, of, throwError } from 'rxjs';
import { catchError, switchMap, tap } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AuthenticationService } from 'src/app/_services/authentication.service';
import { CryptoUtilService } from '../crypto-util/crypto-util.service';
import { AuthoritiesService } from '../authorities/authorities.service';

export interface User {
  id: number;
  username: string;
  email: string;
  creationDate: string;
  lastSeen: string;
  superUser: boolean;
  companyRoles: { [key: string]: string[] };
  systemRoles: string[];
}

@Injectable({
  providedIn: 'root',
})
export class ApiService {
  constructor(
    private http: HttpClient,
    private snackBar: MatSnackBar,
    private authService: AuthenticationService,
    private cryptoUtilService: CryptoUtilService,
    private authoritiesService: AuthoritiesService,
  ) {}

  private getRequestHeaders(): HttpHeaders {
    return new HttpHeaders({
      'Content-Type': 'application/json',
    });
  }

  public getHasCompany(): boolean {
    if (sessionStorage.getItem('hasCompany') === 'true') {
      return true;
    } else {
      return false;
    }
  }

  public getPassword(): string | null {
    return this.cryptoUtilService.retrieveFromSession('password');
  }

  fetchCollectionPointsNew(companyID: string): Observable<any[]> {
    const apiUrl = `${environment.apiUrl}/company/${companyID}/point`;

    return this.http.get<any[]>(apiUrl).pipe(
      catchError((error: HttpErrorResponse) => {
        return throwError(() => new Error('Error fetching collection points'));
      }),
    );
  }

  getCollectionPointInformation(
    collectionPoint: string,
    companyEndpoint: string,
  ): Observable<any> {
    const apiUrl = `${environment.apiUrl}/collection/${companyEndpoint}/${collectionPoint}/`;

    return this.http.get<any>(apiUrl).pipe(
      catchError((error: HttpErrorResponse) => {
        return throwError(
          () => new Error('Error fetching collection point state'),
        );
      }),
    );
  }

  getCollectionPointState(
    collectionPoint: string,
    companyEndpoint: string,
  ): Observable<any> {
    const apiUrl = `${environment.apiUrl}/collection/${companyEndpoint}/${collectionPoint}/state`;

    return this.http.get<any>(apiUrl).pipe(
      catchError((error: HttpErrorResponse) => {
        return throwError(
          () => new Error('Error fetching collection point state'),
        );
      }),
    );
  }

  addUserToCollectionPoint(
    companyID: string,
    collectionPoint: string,
    username: any,
  ): Observable<any> {
    const requestData = {
      idCollectionPoint: { endpoint: collectionPoint }, // Send an object with an 'endpoint' property
      user: { username: username },
      plaintextPassword: this.getPassword(),
    };

    return this.http.post(
      `${environment.apiUrl}/company/${companyID}/point/user`,
      requestData,
    );
  }

  putCollectionPointState(
    collectionPoint: string,
    state: string,
    companyEndpoint: string,
  ): Observable<any> {
    const apiUrl = `${environment.apiUrl}/collection/${companyEndpoint}/${collectionPoint}/state`;

    return this.http.put(apiUrl + '?state=' + state, '').pipe(
      catchError((error: HttpErrorResponse) => {
        return throwError(
          () => new Error('Error setting collection point state'),
        );
      }),
    );
  }

  deleteCollectionPoint(
    collectionPoint: string,
    companyEndpoint: string,
  ): Observable<any> {
    const apiUrl = `${environment.apiUrl}/collection/${companyEndpoint}/${collectionPoint}/delete`;

    return this.http.delete(apiUrl).pipe(
      catchError((error: HttpErrorResponse) => {
        return throwError(() => new Error('Error deleting collection point'));
      }),
    );
  }

  //Gets the companies a user is a part of
  fetchCompanies(): Observable<any> {
    const apiUrl = `${environment.apiUrl}/user/`;
    return this.http.get(apiUrl).pipe(
      catchError((error: HttpErrorResponse) => {
        console.error('Error fetching companies:', error);
        return throwError(() => new Error('Error fetching companies'));
      }),
    );
  }

  //Gets the currently logged in user
  fetchUser(): Observable<any> {
    const apiUrl = `${environment.apiUrl}/user/`;
    return this.http.get(apiUrl).pipe(
      catchError((error: HttpErrorResponse) => {
        console.error('Error fetching user data:', error);
        return throwError(() => new Error('Error fetching user data'));
      }),
    );
  }

  createCompany(companyName: string): Observable<any> {
    const requestData = {
      name: companyName,
    };

    return this.http.post(`${environment.apiUrl}/company/create`, requestData);
  }

  //gets all the users for a company
  fetchUsers(companyID: string): Observable<User[]> {
    // Check if the user has the 'EDIT_COMPANY' permission
    return this.authoritiesService.companyPermissions$.pipe(
      catchError(() => {
        return of(null); // Handle errors from authoritiesService
      }),
      switchMap((permissions) => {
        if (!permissions.includes('EDIT_COMPANY')) {
          return throwError('User does not have permission to access API');
        }
        const apiUrl = `${environment.apiUrl}/company/${companyID}/users/`;
        return this.http.get<User[]>(apiUrl).pipe(
          catchError((error: HttpErrorResponse) => {
            console.error('Error fetching users:', error);
            return throwError('Error fetching users');
          }),
        );
      }),
    );
  }

  //updates the users email
  updateUserEmail(newEmail: string): Observable<any> {
    const apiUrl = `${environment.apiUrl}/user/email`;

    const requestBody = {
      newEmail: newEmail,
    };

    return this.http.post(apiUrl, requestBody);
  }

  //updates the users password
  updateUserPassword(
    oldPassword: string,
    newPassword: string,
  ): Observable<any> {
    const apiUrl = `${environment.apiUrl}/user/password`;

    const requestBody = {
      oldPassword: oldPassword,
      newPassword: newPassword,
    };

    return this.http.post(apiUrl, requestBody);
  }

  //Creates a POST request to create a new collection point for the company
  createNewCollectionPoint(
    collectionPointName: string,
    collectionPoint: string,
    companyID: string,
  ): Observable<any> {
    const apiUrl = `${environment.apiUrl}/company/${companyID}/point`;

    const requestData = {
      endpoint: collectionPoint,
      name: collectionPointName,
    };

    return this.http.post(apiUrl, requestData).pipe(
      catchError((error: HttpErrorResponse) => {
        console.error('Error creating collection point:', error);
        this.snackBar.open('Error creating collection point', 'Close', {
          duration: 5000,
          panelClass: ['snackbar-error'],
        });

        return throwError(() => new Error('Error creating collection point'));
      }),
      tap(() => {
        this.snackBar.open('Collection point created successfully', 'Close', {
          duration: 5000,
          panelClass: ['snackbar-success'],
        });
      }),
    );
  }

  //A POST request for inviting a user to the company currently being viewed
  inviteUser(email: string, role: string, companyID: string): Observable<any> {
    const apiUrl = `${environment.apiUrl}/company/${companyID}/users/invite`;

    const requestBody = {
      email: email,
      role: role,
    };

    return this.http.post(apiUrl, requestBody).pipe(
      catchError((error: HttpErrorResponse) => {
        console.error('Error inviting user:', error);
        let errorMessage = 'Error inviting user';

        if (error.status === 404 || error.status === 409) {
          errorMessage = error.error.message;
        }

        this.snackBar.open(errorMessage, 'Dismiss', {
          duration: 5000,
          panelClass: ['mat-snack-bar-error'],
        });

        return throwError(() => new Error(errorMessage));
      }),
      tap(() => {
        this.snackBar.open('User invited successfully', 'Dismiss', {
          duration: 3000,
          panelClass: ['mat-snack-bar-success'],
        });
      }),
    );
    return this.http.post(apiUrl, requestBody);
  }

  //for loading card data into the table
  loadTableData(
    endpoint: string,
    companyEndpoint: string,
    pageNum: number,
  ): Observable<any> {
    const apiUrl = `${environment.apiUrl}/collection/${companyEndpoint}/${endpoint}/all`;

    const requestBody = {
      password: this.getPassword(),
      pageNumber: pageNum,
    };

    return this.http.post(apiUrl, requestBody);
  }

  loadIndividualResult(cardId: string): Observable<any> {
    const apiUrl = `${environment.apiUrl}/card/${cardId}/info`;
    const requestBody = {
      password: this.getPassword(),
    };

    return this.http.post(apiUrl, requestBody);
  }

  editIndividualResult(
    cardId: string,
    requestBody: any,
    openSnackbar: boolean,
  ): void {
    const apiUrl = `${environment.apiUrl}/card/${cardId}/update`;

    this.http.put(apiUrl, requestBody).subscribe((data) => {
      console.log(data);
      if (openSnackbar === true) {
        this.snackBar.open(
          'Card information successfully updated!',
          'Reload Page',
          {
            duration: 5000,
            panelClass: ['snackbar-success'],
          },
        );
      }
    });
  }

  deleteIndividualResult(cardId: string): Observable<any> {
    const apiUrl = `${environment.apiUrl}/card/${cardId}/delete`;

    return this.http.delete(apiUrl);
  }

  submitImage(
    companyEndpoint: string,
    collectionPoint: string,
    file: File,
  ): Observable<any> {
    return this.authoritiesService.companyPermissions$.pipe(
      catchError(() => {
        return of(null); // Handle errors from authoritiesService
      }),
      switchMap((permissions) => {
        if (!permissions.includes('VIEW_COMPANY')) {
          return throwError('User does not have permission to access API');
        }
        const formData = new FormData();
        formData.append('image', file);

        return this.http.post<any>(
          `${environment.apiUrl}/submit/${companyEndpoint}/${collectionPoint}`,
          formData,
        );
      }),
    );
  }

  submitImageCustomer(
    companyEndpoint: string,
    collectionPoint: string,
    file: File,
  ): Observable<any> {
    const formData = new FormData();
    formData.append('image', file);

    return this.http.post<any>(
      `${environment.apiUrl}/submit/${companyEndpoint}/${collectionPoint}/customer`,
      formData,
    );
  }

  getJobState(jobId: string): Observable<any> {
    return this.http.get<any>(`${environment.apiUrl}/submit/${jobId}/state`);
  }
  // Add more API functions as needed

  //File Management API Calls

  //allows the user to upload files to the backend
  uploadForm(formData: FormData, companyID: string): Observable<any> {
    return this.http.post(
      `${environment.apiUrl}/files/${companyID}/upload`,
      formData,
    );
  }

  editForm(
    fileId: any,
    formData: FormData,
    companyID: string,
  ): Observable<any> {
    return this.http.put(
      `${environment.apiUrl}/files/${companyID}/update/${fileId}`,
      formData,
    );
  }

  //allows the user to get a list of the forms available
  getForms(companyID: string): Observable<any> {
    return this.http.get(`${environment.apiUrl}/files/${companyID}`);
  }

  //allows the user to get back a specific file
  getIndividualFile(fileId: any, companyID: string): Observable<Blob> {
    return this.http.get(
      `${environment.apiUrl}/files/${companyID}/download/${fileId}`,
      {
        responseType: 'blob',
      },
    );
  }

  //allows users to delete files
  deleteFile(fileId: any, companyID: string): Observable<any> {
    return this.http.delete(
      `${environment.apiUrl}/files/${companyID}/delete/${fileId}`,
    );
  }

  async convertWordToPDF(file: any) {
    const url =
      'https://petadata-document-conversion-suite.p.rapidapi.com/ConvertToPDF';
    const data = new FormData();
    data.append('file', file);

    const options = {
      method: 'POST',
      headers: {
        'X-RapidAPI-Key': 'e346f1249amshd202ace106ca993p1d70f7jsn8ea6707212bb',
        'X-RapidAPI-Host': 'petadata-document-conversion-suite.p.rapidapi.com',
      },
      body: data,
    };

    try {
      const response = await fetch(url, options);
      if (!response.ok) {
        throw new Error(`Failed to convert Word to PDF: ${response.status}`);
      }

      const blobData = await response.blob();

      // Create a Blob URL for the PDF data
      const blobUrl = URL.createObjectURL(blobData);

      return blobUrl; // Resolve the Blob URL
    } catch (error) {
      console.error(error);
      throw error; // Rethrow the error to handle it further up the chain
    }
  }
}
