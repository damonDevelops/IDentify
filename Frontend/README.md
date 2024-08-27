# IDentify Frontend README

Welcome to the IDentify Frontend, this readme will give you an overview of how to navigate our site, important details and information on building the project as well as how to build the documentation package.

## How to Run

1. Ensure your computer meets the software requirements:

   - Angular CLI: 16.0.2
   - Node: 18.16.0
   - Package Manager: npm 9.6.2

2. Clone the frontend git repository from [Git Repository](https://website-name.com "Link title")

   - We suggest using VsCode to do this as it was the IDE which the system was developed in

3. Running the Frontend
   - Now that you have the frontend open in your IDE of choice, navigate to package/main
   - In VsCode, you can right click on the package/main folder in finder and select 'Open in Integrated Terminal', otherwise, open a terminal and use `cd .\package\main\`
   - To install the node modules, use the command `npm i`. This will install all dependencies of the system
   - Once this is complete, if you are running the backend locally on your PC (see backend README), use the command `ng serve -c development`.
   - The system will take some time to load if it is the first time running, once it is successful, you will see the following messages:
     - `** Angular Live Development Server is listening on localhost:4200, open your browser on http://localhost:4200/ ** `
     - `√ Compiled successfully.`
     - `✔ Browser application bundle generation complete.`
   - Either CTRL + click on the link or navigate to [http://localhost:4200/](http://localhost:4200/)

## Notes about the backend

If you are running the backend and frontend locally, you will need to use a no CORS browser to log in and use the system. The system WILL NOT WORK unless you do so

- To see how to set up a no CORS browser, visit [Run Chrome without CORS](https://alfilatov.com/posts/run-chrome-without-cors/)

# Running Documentation

- The angular project has automatic documentation which is powered by [CompoDoc](https://compodoc.app/)
- To view the documentation, navigate to package/main in the same way as discussed previously
- Enter the command `compodoc -p tsconfig.doc.json -s -r 9000`
- You will then see a message like this:
  - `Serving documentation from ./documentation/ at http://127.0.0.1:9000`
  - Navigate to the address given and you can explore the documentation

# Using the system

- To learn how to use the system, check the [User Manual]()

# Additional notes:

The system has a default user, to log in to it use the following credentials:

- Email: demo
- Password: demo
