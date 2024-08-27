import { Component, Output, EventEmitter, Input } from '@angular/core';
import { CoreService } from 'src/app/_services/core.service';
import { ViewportScroller } from '@angular/common';

interface apps {
  id: number;
  img: string;
  title: string;
  subtitle: string;
  link: string;
}

interface quicklinks {
  id: number;
  title: string;
  link: string;
}

interface demos {
  id: number;
  name: string;
  url: string;
  imgSrc: string;
}

interface testimonials {
  id: number;
  name: string;
  subtext: string;
  imgSrc: string;
}

interface features {
  id: number;
  icon: string;
  title: string;
  subtext: string;
}

@Component({
  selector: 'app-landingpage',
  templateUrl: './landingpage.component.html',
})
export class AppLandingpageComponent {
  @Input() showToggle = true;
  @Output() toggleMobileNav = new EventEmitter<void>();
  @Output() toggleMobileFilterNav = new EventEmitter<void>();
  @Output() toggleCollapsed = new EventEmitter<void>();

  options = this.settings.getOptions();

  constructor(
    private settings: CoreService,
    private scroller: ViewportScroller,
  ) {}

  // scroll to demos
  gotoDemos() {
    this.scroller.scrollToAnchor('demos');
  }

  apps: apps[] = [
    {
      id: 1,
      img: '/assets/images/svgs/icon-dd-chat.svg',
      title: 'Chat Application',
      subtitle: 'Messages & Emails',
      link: '/apps/chat',
    },
    {
      id: 2,
      img: '/assets/images/svgs/icon-dd-cart.svg',
      title: 'Blog App',
      subtitle: 'Get a Blog',
      link: '/apps/blog/post',
    },
    {
      id: 3,
      img: '/assets/images/svgs/icon-dd-invoice.svg',
      title: 'Invoice App',
      subtitle: 'Get latest invoice',
      link: '/apps/invoice',
    },
    {
      id: 4,
      img: '/assets/images/svgs/icon-dd-date.svg',
      title: 'Calendar App',
      subtitle: 'Get Dates',
      link: '/apps/calendar',
    },
    {
      id: 5,
      img: '/assets/images/svgs/icon-dd-mobile.svg',
      title: 'Contact Application',
      subtitle: '2 Unsaved Contacts',
      link: '/apps/contacts',
    },
    {
      id: 6,
      img: '/assets/images/svgs/icon-dd-lifebuoy.svg',
      title: 'Tickets App',
      subtitle: 'Create new ticket',
      link: '/apps/tickets',
    },
    {
      id: 7,
      img: '/assets/images/svgs/icon-dd-message-box.svg',
      title: 'Email App',
      subtitle: 'Get new emails',
      link: '/apps/email/inbox',
    },
    {
      id: 8,
      img: '/assets/images/svgs/icon-dd-application.svg',
      title: 'Courses',
      subtitle: 'Create new course',
      link: '/apps/courses',
    },
  ];

  demos: demos[] = [
    {
      id: 1,
      imgSrc: '/assets/images/landingpage/demos/demo-main.jpg',
      name: 'Main',
      url: 'https://materialpro-angular-main.netlify.app/dashboards/dashboard1',
    },
    {
      id: 2,
      imgSrc: '/assets/images/landingpage/demos/demo-dark.jpg',
      name: 'Dark',
      url: 'https://materialpro-angular-dark.netlify.app/dashboards/dashboard2',
    },
    {
      id: 3,
      imgSrc: '/assets/images/landingpage/demos/demo-firebase.jpg',
      name: 'Authguard',
      url: 'https://materialpro-angular-authguard.netlify.app/authentication-side-login',
    },
    {
      id: 4,
      imgSrc: '/assets/images/landingpage/demos/demo-rtl.jpg',
      name: 'RTL',
      url: 'https://materialpro-angular-rtl.netlify.app/dashboards/dashboard1',
    },
    {
      id: 5,
      imgSrc: '/assets/images/landingpage/demos/demo-minisidebar.jpg',
      name: 'Minisidebar',
      url: 'https://materialpro-angular-minisidebar.netlify.app/dashboards/dashboard1',
    },
    {
      id: 6,
      imgSrc: '/assets/images/landingpage/demos/demo-horizontal.jpg',
      name: 'Horizontal',
      url: 'https://materialpro-angular-horizontal.netlify.app/dashboards/dashboard2',
    },
  ];

  appdemos: demos[] = [
    {
      id: 1,
      imgSrc: '/assets/images/landingpage/apps/app-calendar.jpg',
      name: 'Calendar',
      url: 'https://materialpro-angular-main.netlify.app/apps/calendar',
    },
    {
      id: 2,
      imgSrc: '/assets/images/landingpage/apps/app-chat.jpg',
      name: 'Chat',
      url: 'https://materialpro-angular-main.netlify.app/apps/chat',
    },
    {
      id: 3,
      imgSrc: '/assets/images/landingpage/apps/app-contact.jpg',
      name: 'Contact',
      url: 'https://materialpro-angular-main.netlify.app/apps/contacts',
    },
    {
      id: 4,
      imgSrc: '/assets/images/landingpage/apps/app-email.jpg',
      name: 'Email',
      url: 'https://materialpro-angular-main.netlify.app/apps/email/inbox',
    },
    {
      id: 5,
      imgSrc: '/assets/images/landingpage/apps/app-courses.jpg',
      name: 'Courses',
      url: 'https://materialpro-angular-main.netlify.app/apps/courses',
    },
    {
      id: 6,
      imgSrc: '/assets/images/landingpage/apps/app-employee.jpg',
      name: 'Employee',
      url: 'https://materialpro-angular-main.netlify.app/apps/employee',
    },
    {
      id: 7,
      imgSrc: '/assets/images/landingpage/apps/app-note.jpg',
      name: 'Notes',
      url: 'https://materialpro-angular-main.netlify.app/apps/notes',
    },
    {
      id: 8,
      imgSrc: '/assets/images/landingpage/apps/app-ticket.jpg',
      name: 'Tickets',
      url: 'https://materialpro-angular-main.netlify.app/apps/tickets',
    },
    {
      id: 9,
      imgSrc: '/assets/images/landingpage/apps/app-invoice.jpg',
      name: 'Invoice',
      url: 'https://materialpro-angular-main.netlify.app/apps/invoice',
    },
    {
      id: 10,
      imgSrc: '/assets/images/landingpage/apps/app-todo.jpg',
      name: 'Todo',
      url: 'https://materialpro-angular-main.netlify.app/apps/todo',
    },
    {
      id: 11,
      imgSrc: '/assets/images/landingpage/apps/app-taskboard.jpg',
      name: 'Taskboard',
      url: 'https://materialpro-angular-main.netlify.app/apps/taskboard',
    },
    {
      id: 12,
      imgSrc: '/assets/images/landingpage/apps/app-blog.jpg',
      name: 'Blog List',
      url: 'https://materialpro-angular-main.netlify.app/apps/blog/post',
    },
  ];

  testimonials: testimonials[] = [
    {
      id: 1,
      imgSrc: '/assets/images/profile/user-1.jpg',
      name: 'Ajit Singh',
      subtext:
        'The theme is very flexible with most of the content already available. And also the support team is very active. :)',
    },
    {
      id: 2,
      imgSrc: '/assets/images/profile/user-2.jpg',
      name: 'Kévin PEREZ',
      subtext:
        'This design is really good. The code is good. Its easy to start with it when we are new with Angular. Like this theme because its a material theme !',
    },
    {
      id: 3,
      imgSrc: '/assets/images/profile/user-3.jpg',
      name: 'Jikes Sam',
      subtext:
        'I am using the MaterialPro angular admin, and its feature rich, easy to use and saves a ton of time. The Support Team is great.',
    },
  ];

  //Used for Software used feature section
  features: features[] = [
    {
      id: 1,
      icon: 'brand-angular',
      title: 'Angular',
      subtext:
        'IDentify is built with Angular, allowing for a clean and dynamic design.',
    },
    {
      id: 2,
      icon: 'shield-lock', //brand-nodejs should work but appears invisible
      title: 'Node',
      subtext: 'Node enables scalable and efficient web applications',
    },
    {
      id: 3,
      icon: 'matchstick',
      title: 'PyTorch',
      subtext:
        'PyTorch is the main deep learning framework used for building IDentify.',
    },
    {
      id: 4,
      icon: 'adjustments',
      title: 'OpenCV',
      subtext: 'OpenCV for image and video processing tasks',
    },
    {
      id: 5,
      icon: 'id',
      title: 'YOLACT ',
      subtext:
        'Real-time object detection and segmentation model to Identify ID cards.',
    },
    {
      id: 6,
      icon: 'box-model',
      title: 'CLIP',
      subtext:
        'A deep learning model for understanding images and text together',
    },
    {
      id: 7,
      icon: 'brand-python',
      title: 'NumPy',
      subtext: 'Python library for numerical computing and data manipulation',
    },
    {
      id: 8,
      icon: 'brand-python',
      title: 'EasyOCR',
      subtext: 'Python library for optical character recognition (OCR).',
    },
    // {
    //   id: 9,
    //   icon: 'chart-pie',
    //   title: 'Lots of Chart Options',
    //   subtext: 'You name it and we have it, Yes lots of variations for Charts.',
    // },
    // {
    //   id: 10,
    //   icon: 'layers-intersect',
    //   title: 'Lots of Table Examples',
    //   subtext: 'Data Tables are initial requirement and we added them.',
    // },
    // {
    //   id: 11,
    //   icon: 'refresh',
    //   title: 'Regular Updates',
    //   subtext: 'We are constantly updating our pack with new features..',
    // },
    // {
    //   id: 12,
    //   icon: 'book',
    //   title: 'Detailed Documentation',
    //   subtext: 'We have made detailed documentation, so it will easy to use.',
    // },
    // {
    //   id: 13,
    //   icon: 'calendar',
    //   title: 'Calendar Design',
    //   subtext: 'Calendar is available with our package & in nice design.',
    // },
    // {
    //   id: 14,
    //   icon: 'messages',
    //   title: 'Dedicated Support',
    //   subtext: 'We believe in supreme support is key and we offer that.',
    // },
  ];

  //Used for What We Do feature section
  features2: features[] = [
    {
      id: 2,
      icon: 'qrcode',
      title: 'Signing up to a business',
      subtext:
        'When joining a business, you can simply scan the provided QR code instead of filling out traditional forms.',
    },
    {
      id: 2,
      icon: 'upload',
      title: 'Uploading an Image',
      subtext:
        'Simply select the upload button and choose an image from whatever operating system IDentify is being used on.',
    },
    {
      id: 3,
      icon: 'loader-2',
      title: 'Processing an Image',
      subtext:
        'Once uploaded, select "Submit for Processing", IDentify will then analyse the card and retrieve the required information.',
    },
    {
      id: 4,
      icon: 'edit-circle',
      title: 'Confirming Accurate Data is Recorded',
      subtext:
        'A table of collected information can be edited by hitting the "Expand" button to ensure the captured information is correct.',
    },
    {
      id: 5,
      icon: '',
      title: '',
      subtext: '',
    },

    {
      id: 6,
      icon: 'database',
      title: 'How Stored Data is Used ',
      subtext:
        'Real-time object detection and segmentation model to Identify ID cards, this is kept in the assigned company.',
    },
    {
      id: 7,
      icon: 'photo-check',
      title: 'Why IDentify ',
      subtext:
        'Now your scanned information can be used to auto-fill and sign into multiple venues. Saving you time and effort.',
    },
    // {
    //   id: 6,
    //   icon: 'diamond',
    //   title: 'CLIP',
    //   subtext:
    //     'A deep learning model for understanding images and text together',
    // },
    // {
    //   id: 7,
    //   icon: 'language-katakana',
    //   title: 'NumPy',
    //   subtext: 'Python library for numerical computing and data manipulation',
    // },
    // {
    //   id: 8,
    //   icon: 'arrows-shuffle',
    //   title: 'EasyOCR',
    //   subtext: 'Python library for optical character recognition (OCR).',
    // },
    // {
    //   id: 9,
    //   icon: 'chart-pie',
    //   title: 'Lots of Chart Options',
    //   subtext: 'You name it and we have it, Yes lots of variations for Charts.',
    // },
    // {
    //   id: 10,
    //   icon: 'layers-intersect',
    //   title: 'Lots of Table Examples',
    //   subtext: 'Data Tables are initial requirement and we added them.',
    // },
    // {
    //   id: 11,
    //   icon: 'refresh',
    //   title: 'Regular Updates',
    //   subtext: 'We are constantly updating our pack with new features..',
    // },
    // {
    //   id: 12,
    //   icon: 'book',
    //   title: 'Detailed Documentation',
    //   subtext: 'We have made detailed documentation, so it will easy to use.',
    // },
    // {
    //   id: 13,
    //   icon: 'calendar',
    //   title: 'Calendar Design',
    //   subtext: 'Calendar is available with our package & in nice design.',
    // },
    // {
    //   id: 14,
    //   icon: 'messages',
    //   title: 'Dedicated Support',
    //   subtext: 'We believe in supreme support is key and we offer that.',
    // },
  ];

  quicklinks: quicklinks[] = [
    {
      id: 1,
      title: 'Pricing Page',
      link: '/theme-pages/pricing',
    },
    {
      id: 2,
      title: 'Authentication Design',
      link: '/authentication/side-login',
    },
    {
      id: 3,
      title: 'Register Now',
      link: '/authentication/side-register',
    },
    {
      id: 4,
      title: '404 Error Page',
      link: '/authentication/error',
    },
    {
      id: 5,
      title: 'Notes App',
      link: '/apps/notes',
    },
    {
      id: 6,
      title: 'Employee App',
      link: '/apps/employee',
    },
    {
      id: 7,
      title: 'Todo Application',
      link: '/apps/todo',
    },
    {
      id: 8,
      title: 'Treeview',
      link: '/theme-pages/treeview',
    },
  ];
}
