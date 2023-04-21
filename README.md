# ReitHÍ Android

ReitHÍ for Android is an app that enables students to make better informed decisions about the courses they choose to enroll in. It gives users the opportunity to rate, review and comment on courses that are taught in the University of Iceland.

**A hosted demo of the ReitHÍ website is available on Railway to try out, deployed from the main branch:**

* https://reithi-production.up.railway.app/

## File format:
The file format for the website can roughly be described with the following bullet points:
* `/java`, contains the all of our Java source code, as well as our NetworkManager that executes HTTP requests to our backend.
* `/Activities`, contains all of our activities that control the flow of our application.  
* `/Entities`, contains all of the entities used by the code, for example, Review, User and Course.
* `/Fragments`, contains all of the fragments used in our activities.
* `/Services`, contains all of the services used to call generic functions in NetworkManager.
* `/res`, contains all of our drawables, themes, and layouts.

## Required setup to run the project:
The available version shown above uses a backend with a RESTful API that is hosted on Railway. Note that the backend is only available for ~20 days a month. To set up this project, clone it to your preferred destination, synchronize the necessary dependencies using Gradle, create an emulator with at least API level 26, build the project and run it.

## Additional information:
* General rules for using the ReitHÍ app:
    - Be respectful when leaving comments on courses.
    - Don't use the same passwords for your account you use anywhere else.
    - Don't mention individual teachers when rating a course.
* Project slides: https://docs.google.com/presentation/d/1Js35b0EiUxWmN7be7kWd9Ips4Wl3L4w22DXdbh_I5kU/edit?usp=sharing
