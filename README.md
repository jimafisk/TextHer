## Overview:
You know you're going to be out on the town but you have plans with someone later on. Maybe you're at a tech meetup, maybe you're staying late at the office to finish a project, but most likely you're getting plastered with your buddies at the bar. Stop making excuses after the damage has been done and start making the before. Ease things over with that special someone when you still have the capacity to do so and let they're know that even when you're making poor decisions, they're still on your mind. Textcuse is a location based app that sends automatic SMS messages to your significant other when there's no way in hell you will be on time for the plans you've made with that person. Conveniently write up an excuse ahead of time or use one we've created for you (not recommended), pop in the location where you need to be, specify the time you need to be there, and let our GIS calculator smooth things over for you.

## Main layout:
The main screen is a form with the following: Location, Time, Excuse, Contact.

### Location:
A Google Maps style text field where you can put in almost any address format and it is able to interpret it and find accurate coordinates. Future iterations will give you the ability to save addresses like "Home".

### Time:
A simple time picker widget that specifies when you are supposed to meet up with said person.

### Excuse:
You can choose "Custom, Saved, Auto". Custom is for people who actually for legitimate excuses for being late (e.g. you know you are meeting with an important client at the end of the day and figure the meeting might go over. You also know you can't pick up the phone and start texting while the client is in the room - so you set a Textcuse). Saved references a database of common excuses that the user has entered knowing they will have to use that excuse again in the future. Auto uses a database of outlandish excuses the Textcuse team has generated for the user (e.g. "Sorry I'm late hun, I got my hand stuck in the salvation army donation can at the mall again"). This is mostly for an audience that wants to use the app just as a novelty.

### Contact:
This is the telephone number and name of the person you are breaking plans with. You should be able to select from auto saved entries like "wife" or "boss".

## Application Logic:
The Excuse will be sent to the Person if the current time + time it takes to navigate to Location from current location is greater than the meet time. Future releases will accommodate follow up messages if the person continues to be late, and multiple Textcuses using a date object, and a "History" layout that aggregates information about Excuses you've sent in the past.
