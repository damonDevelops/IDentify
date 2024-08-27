import { Injectable } from '@angular/core';

declare var introJs: any;

@Injectable({
  providedIn: 'root',
})
export class IntroService {
  private intro: any;
  private steps: { step: any; position: number }[] = [];
  private stepsAdded: boolean = false;

  constructor() {
    this.intro = introJs();
  }

  setOptions(options: any) {
    this.intro.setOptions(options);
  }

  addStepWithPosition(step: any, position: number) {
    const existingStep = this.steps.find((s) => s.position === position);

    if (!existingStep) {
      this.steps.push({ step, position });
    }
  }

  setStepsInOrder(steps: { step: any; position: number }[]) {
    this.steps = steps;
  }

  start() {
    if (!this.stepsAdded) {
      // Sort the steps based on their positions before starting the tour.
      this.steps.sort((a, b) => a.position - b.position);

      // Add the sorted steps to the intro.js tour.
      this.steps.forEach((step) => this.intro.addStep(step.step));

      this.stepsAdded = true;
    }

    // Start the intro.js tour.
    this.intro.start();
  }
}
