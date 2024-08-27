import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class CommunicationService {
  private currentImageIndexSource = new Subject<number>();
  currentImageIndex$ = this.currentImageIndexSource.asObservable();

  updateCurrentImageIndex(index: number) {
    this.currentImageIndexSource.next(index);
  }
}
