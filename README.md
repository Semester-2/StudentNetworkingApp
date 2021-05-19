# CampusFlair 

## Team 5
## Gunjan Srivastava  &  Anupama Kurudi

### Project Idea 
Social networking application where students and faculty members can connect with one another.Users can post,view and enroll to events and can chat, share images and view each other’s profile.

Platform : Android
Language : Kotlin
Integration : Google Firebase

###  App Design 
Application is designed by using Kotlin language and uses Material Design as the app theme. Minimum sdk version supported for the app is 23. Application is designed in such a way that it looks and behaves consistently on different resolution devices. All the layouts have been developed using constraint layouts that adjust the UI based on the device resolutions.
App is designed using jetpack libraries like Data Binding, Live Data, View Model, navigation, etc. that helps in achieving compatibility, performance and security. 
Key Techniques
Android KTX, Android Jetpack, Integration with Firebase Realtime database, Cloud Storage, Cloud Messaging, Integration with social media, Safe Args, Glide library, Data Binding, LiveData, ViewModel, Navigation, Notifications, Fragment, AppCompat, Recycler View, Android Scrolling Activity.

## Features

### Events Section:
#### 1. Welcome Screen 
On application launch, users will be landed on the Welcome screen with two options : Login and Register. Users will be navigated to the respective screens based on what they select.

#### 2. Login with Google
Google Sign-In integration has been done that provides users to authenticate with Firebase using their Google Accounts. After successful login, users' details like displayed name, email address, profile image url will be fetched  and displayed on the application dashboard screen.
        
#### 3. Logout 
Designed and implemented the options-menu with “Logout” functionality. Users can log out of the application and navigate to the Welcome Screen.

#### 4. Display Categories
On successful login, users will be landed on the application main dashboard screen.Here different categories are displayed like Sports, Academics, International Student, Fun Activity, Career and Groups. Users can select any of these categories and navigate to the Category List Screen. Whatever category the user will select, the corresponding events/announcements will be displayed on the list page.
UI : Recycler View is used with GridLayoutManager

#### 5. Navigation Drawer 
Navigation header is displayed with the user profile pic, name and email address fetched from the firebase user. Menu items are linked with the corresponding fragments. Passing arguments using Safe Args between the fragments and the activity.
UI : Navigation drawer , header view
Integration : Firebase User 

#### 6. Display announcements/event list
Once the user selects any one of the 6 categories,he will be navigated to the announcement list activity screen where they can see a list of  the announcements or events posted for that particular category. Information like name of the event, date on which event will be held along with the event image will be displayed. 
UI : Recycler View is used with LinearLayoutManager. Card View has been implemented for the list item.
Safe Args used to pass selected category between Fragment and the Activity Class
Integration and API :
Realtime Database : All the announcement related data has been stored in Firebase realtime database
Cloud Storage : Firebase storage has been used to store all the announcement related images. The image url retrieved from the storage is then stored in the database to fetch the images later.

#### 7. Announcement/Event Detail Page
On this screen, event details will be displayed to the user. Events details like description, location at which event will be held, person name who has posted the event, category to which event belongs to, etc. Users can view and register for the event as per their interests. Also, registered events can be unregistered whenever required.
UI : Android Scrolling Activity has been used with CollapsingtoolbarLayout to display the event details. Glide is used to render images on toolbar
Database Interaction : Respective event details have been fetched from the database table and displayed on screen.

#### 8. Interested/Not Interested for an event
Interested for an Event: Users can register for an event he/she is interested in  from the Event Detail page( Android Scrolling Activity). On click of the “I am Interested” button, the user is registered for an event and the UI gets updated with the “Not Interested” Button.
Not Interested for an Event: User can unregister from two different screens.
Event Detail Page : Once registered for an event, the “Not Interested” button will be displayed on the page. On clicking it, the user gets unregistered for that event.

#### 9. Enrolled Event Page
User registered events will be displayed on this screen. If a user wants to unregister for an event, he can do that by clicking on the “Not Interested” button displayed against each registered event.

UI : Android Scrolling Activity is used to display the event details. 
Database Interaction: Database announcement table and user table is updated with the registration details as per the user interactions.
      
#### 10. Share an event
On the event detail page, users are provided with an option to share the event details with other students/ friends. On click of a fab icon with a shared image, the user can select the medium to send the message like whats app, mail, text message etc.
UI: 
fab icon with share image.
Intent.ACTION_SEND is used to show app chooser to the user and to send the event details.

#### 11. Post announcement/event
Students or faculty have been provided with a feature where they can post new events/announcements. Users have to provide details like event title, description, date of the event, choose category to which event belongs to, location for the event, upload image for the event, etc. 
On submit, the database gets updated with the new event and the image uploads on the cloud storage. The event list will get updated on the application.
UI : 
ScrollView with Constraint Layout is used. 
Calendar view has been integrated to select the event date.
Spinner is used to display all the categories in the drop down list.
Database/Cloud Storage Interaction: On successful post of the event, it will get added in the announcement table and the image will get uploaded on the cloud storage 

#### 12. View enrolled events
Users would be able to view all the events he/she has enrolled for. On Your Enrollments, users can see details of the events and an Unregister button. If the user is not interested or willing to attend the event, he can simply unregister on click of a button. The list will get updated and remove the unregistered events.
UI : 
The Navigation Drawer has been integrated with the “Your Enrollments” menu item. 
Recycler view with LinearlayoutManager to display events in card view
Database Interaction: Registered events are fetched from the user and announcement table.
On click of “Not Interested”, database tables are updated and events get deleted.



### Chat Section:

#### 13. Registration and Login using college user id (email id) 
Users can use their SJSU email id to register themselves to the application. Select a unique username and password and hit register, creating their account. 

Using registered user id and password, they can login. This authentication token is sent to Firebase to be verified. Upon verification, the user logs in and lands on the main   application page.


#### 14. Chats Screen and Display latest message in the Chat conversations
When the user clicks on Chat in the app drawer, they land on the Chats window. This has all the conversations the user has had with different users on the application. This shows the profile picture and username of the users with whom the user has had conversations with, along with the latest message and online/offline status. 
    	
In the Chats list screen with all the conversations between the user and other users, each list item has the profile picture and username, along with the latest message sent. This latest message can be the sent message or the received message based on which is the latest. If the latest message is an image, the latest message reads “Sent an image”.

#### 15. Contact list
This screen shows all the users on the application, which can be filtered using the search feature. When the user clicks on the contact, in a pop-up, two options Send Message and View Profile is seen. If the user selects Send Message, the chat window with that selected user is opened where the user can now send messages and pictures. If the user chooses View Profile, the chosen user’s profile page is displayed.

#### 16.  Search Contacts(as you type characters)
In this tab, the user can search for anybody on the application. This search is based on the username of other users. As the user types the letters, contacts are fetched matching those characters. When this contact list 

#### 17. Send messages  and images to fellow students and faculty 
The user can send and receive messages from other students and faculty members. The user can reach out to anybody on this University network. Messages are sent and received messages can be viewed in the chat window (conversation). After the user types the message in the text box, he hits the send button on the right side, initially in sent status. This generates a message id with receiver and sender details, and the message content, saved in Realtime Database on Firebase.

Users can send each other images. In the chat window, when the user clicks on the attach icon, the user is taken to the image picker of the phone. On choosing a picture, this image is uploaded to the Firebase database and sent to the receiver end and appears on the chat window, initially in sent status.

Sent messages on the right hand side and received messages on the left side.

#### 18. Sent and Seen status
The sent status is the status denoted on the message and image that is sent by a sender and not yet viewed by the receiver on their end. The initial status of images and text messages are set to sent. Once the receiver views it on their end, the status is updated to seen and is updated on the sender chat window to denote the message has been seen by the receiver. This status is saved as a boolean value isseen in the Firebase Database.

#### 19. Online and offline status
This is an icon on the right side of the profile picture of the users in the Chat list. This is grey in color to indicate the user is offline and green in color to indicate online. Online or offline status value is saved in the status variable on the Firebase database. As soon as the user logs in and enters the chat window the status is updated to online and once they leave it is changed to offline.

#### 20. Number of unread messages 
	The number of unread messages are shown in the Chats tab, in brackets. Eg. (3) - means 3 unread messages.

#### 21. Message notifications
The user is notified of the message sent to them by another user as a notification that appears in the notifications drawer on the phone. This is implemented using firebase notifications. This notification contains the sender name and message content. If it is an image, the notification reads “Sent an image”.

#### 22. Delete sent messages
Users can delete the message they have sent by clicking on the message itself in the chat window. This opens up a pop-up with two options: a. Delete message b. Cancel.
When the user clicks on the Delete message, the message is removed from the database chats using the messageid. This is updated on both receiver and sender ends.

#### 23. Delete sent images
Users can delete the message they have sent by clicking on the message itself in the chat window. This opens up a pop-up with three options: a. View image b. Delete image b. Cancel.
When the user clicks on View image, the image is downloaded and opened in a new screen for the user to view it in full size.
When the user clicks on the Delete image, the message is removed from the database chats using the message id. This is updated on both receiver and sender ends.
When the user clicks Cancel, the pop-up disappears.

#### 24. View profile 
Users can view their own and other user’s profiles by clicking on the user and then choosing View Profile option in the pop-up. This takes them to their profile page, where they can see that user’s profile picture, cover picture and social media links.

#### 25. Profile picture, Cover picture and Social media

Seen on the application in the top left corner with the username and while chatting with others in the message chat window.  Users can edit their profile picture in the settings section. When the user clicks on the profile picture, they are redirected to the image picker of the phone and on choosing an image, that image is uploaded and saved in the Relatime Database on Firebase. The next time someone views this user’s profile, this updated profile picture is visible.

Users can edit their cover picture in the settings section. When the user clicks on the cover picture, they are redirected to the image picker of the phone and on choosing an image, that image is uploaded and saved in the Relatime Database on Firebase. The next time someone views this user’s profile, this updated cover picture is visible.

Users can add their social media links under facebook, instagram and linkedIn. When the user clicks on one of the icons, a pop-up appears where the users can type the username/handle and hit save. Eg: anu.kurudi. The next time someone view this user’s profile and clicks on the social media icons, they are taken to that respective pages - https://www.facebook.com/anupamakn , https://www.instagram.com/anu.kurudi and https://www.linkedin.com/in/anupamakn 

#### 26. Logout
User logs out of the application from


###### Task Distribution
###### Gunjan : Designed and implemented Events Section, features 1 to 12 listed above.

###### Anupama : Designed and implemented Chats Section, features 13 to 26 listed above.



