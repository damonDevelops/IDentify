import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CoreService } from 'src/app/_services/core.service';

@Component({
  selector: 'app-branding',
  standalone: true,
  imports: [RouterModule],
  template: `
    <div class="branding d-none d-lg-flex align-items-center">
      <img
        src="./assets/images/logos/IDentifyLogo.svg"
        class="align-middle m-2"
        alt="logo"
        width="175"
        height="115"
      />
    </div>
  `,
  styles: [
    `
      .branding {
        width: 100%; /* Ensure the container takes up the full width of the sidebar */
        height: auto; /* Let the height adjust to maintain aspect ratio */
      }

      /* Optional: Adjust margins and padding as needed */
      .branding img {
        margin: 0; /* Remove any default margins */
        padding: 10px; /* Add padding as needed for spacing */
      }
    `,
  ],
})
export class BrandingComponent {
  options = this.settings.getOptions();

  constructor(private settings: CoreService) {}
}
