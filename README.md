# Customer and Appointment Manager
 A Java CRUD app connecting to a MySQL database

My second Java app with a JavaFXML interface, utilizing MySQL database. Allows the creation, modification and deletion of various appointment and customer records stored in a database. Makes extensive use of time parsing classes for converting between time zones and checking for appointments. The program is written so as to display all times in the current users locale. Utilizes reading and writing to files, such as a login attempt log.

The database functionality is currently unavailable as the database I used was deleted once the class was complete.

Following is my notes to regarding the stated requirements of this project:

#Requirements

#Create a log-in form that can determine the user’s location and translate log-in and error control messages
#(e.g., “The username and password did not match.”) into two languages.

All error control messages are contained within controllers.LoginMenuController. The program will allow 3 attempts
before terminating the program and writing the timestamp and locale of the failed attempt to the log. The program
will display messages in both English and German, depending on whether the users locale is US or Germany. Code
begins at line 136 and utilizes files.rb_de.properties and files.rb_en.properties.

#Provide the ability to add, update, and delete customer records in the database, including name, address, 
#and phone number.

Too much to cover in a quick synopsis here, but all functionality, with comments explaining the code is located in
controllers.CustomersMenuController as well as MainMenuController.

One note: the customer delete function will only work on newly created customer records. The customer records that
come with the DB are constrained by the foreign key. The only ways I can find to delete these records is to either
turn on cascading delete, or remove the constraints. Both of these would alter the structure of the database, which I
understand we are not supposed to do. So, I must assume these are not meant to be deleted.

#Provide the ability to add, update, and delete appointments, capturing the type of appointment and a link to 
#the specific customer record in the database.

Same as above, functionality contained in controllers.AppointmentsMenuController and MainMenuController.

#Provide the ability to view the calendar by month and by week.

Functionality controlled by radio buttons on the Main Menu screen. Code located in MainMenuController beginning
line 118.

#Provide the ability to automatically adjust appointment times based on user time zones and daylight saving time.

All date, time, and locale functionality is located in utilities.DateTimeLocale. This functionality makes use of
the localToUTC and utcToLocal fucntions.

#Write exception controls to prevent each of the following. You may use the same mechanism of exception control 
#more than once, but you must incorporate at least  two different mechanisms of exception control:

The listed requirements I found were better suited to logical control rather than exception, but I was able to 
make use of multiple exception controls in other areas:

Multi-Catch: AppointmentsMenuController line 175
Try With Resources: ReportsMenuController line 90 and LoginMenuController line 70

#scheduling an appointment outside business hours

Handled by the checkBusinessHours function of DateTimeLocale line 28

#scheduling overlapping appointments

Handled by isOverlapping function of DateTimelocale line 150

#entering nonexistent or invalid customer data

Handled by multiple hard menu controls, narrowing allowable user selections. Saving blank customer data is disallowed
in CustomerMenuController beginning line 102.

#entering an incorrect username and password

Handled by Action Event in LoginMenuController beginning line 67.

#Write two or more lambda expressions to make your program more efficient, justifying the use of each lambda 
#expression with an in-line comment.

MainMenuController comment begins line 387
AppointmentsMenuController comment begins line 248
I think I put one more in there somewhere. Bonus points if you find it!

#Write code to provide an alert if there is an appointment within 15 minutes of the user’s log-in.

Functionality handled in MainMenuController beginning line 345

#Provide the ability to generate each  of the following reports:
#number of appointment types by month
#the schedule for each consultant
#one additional report of your choice

All functionality contained in ReportsMenuController

#Provide the ability to track user activity by recording timestamps for user log-ins in a .txt file. Each new 
#record should be appended to the log file, if the file already exists.

Located in files.logfile.txt. I am including the logs that have been generated during my testing, but
feel free to delete or remove it. The program will create a new one next login.

Functionality contained in LoginMenuController

#Demonstrate professional communication in the content and presentation of your submission.

Hopefully between this and in line comments, the program flow is explained well enough.