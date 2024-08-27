import { CollectionViewer, DataSource } from '@angular/cdk/collections';
import { BehaviorSubject, catchError, finalize, Observable, of } from 'rxjs';
import { ApiUser } from '../_models/api_user';
import { UserManagementService } from '../_services/user-management.service';

export class ApiUserDataSource implements DataSource<ApiUser> {
  private apiUserSubject = new BehaviorSubject<ApiUser[]>([]);
  private loadingSubject = new BehaviorSubject<boolean>(false);

  public loading$ = this.loadingSubject.asObservable();

  constructor(private userManagementService: UserManagementService) {}

  connect(collectionViewer: CollectionViewer): Observable<ApiUser[]> {
    return this.userManagementService.getAll();
  }

  disconnect(collectionViewer: CollectionViewer): void {
    this.apiUserSubject.complete();
    this.loadingSubject.complete();
  }
}
