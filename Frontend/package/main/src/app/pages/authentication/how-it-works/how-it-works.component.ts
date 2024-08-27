import { Component, OnInit, HostListener } from '@angular/core';
import * as AOS from 'aos';

@Component({
  selector: 'app-how-it-works',
  templateUrl: './how-it-works.component.html',
  styleUrls: ['./how-it-works.component.scss'],
})
export class HowItWorksComponent implements OnInit {
  showTerms = false;
  showFooter = false;

  ngOnInit() {
    AOS.init({
      offset: 225,
      duration: 1800,
    });
  }

  @HostListener('window:scroll', [])
  onScroll() {
    // Calculates the scroll position as a percentage of the document height
    const scrollPercentage =
      (window.scrollY /
        (document.documentElement.scrollHeight - window.innerHeight)) *
      100;

    // Sets the threshold value as a percentage (e.g., 95% from the top)
    const threshold = 95;

    // Checks if the scroll percentage exceeds the threshold
    this.showFooter = scrollPercentage >= threshold;
  }
}
