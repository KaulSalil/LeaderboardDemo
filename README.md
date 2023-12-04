# WITZEAL Android Assignment Leaderboard Screen
=========================

This is the application for Android Assignment Leaderboard Screen.
First we call the method that fetch the data (first 50 result) from local JSON, As user scroll screen remaining 50 records will be fetched and  so on..

The JSON file Name:
leaderboard.json

Here on each 50 record fetch call I put the 1 second delay so user can see the loader animation.
When we scroll down the data is showing and here my rank is 8th(as per current implementation), when 8th record coming on top that record will sticky
on the top and remaining records/data will be scroll. When we scroll upside then when 7th record will be there the sticky record 
will be removed.


* In this project we are using the following technologies:


Language: Kotlin


Architecture: MVVM (Model View View Model)


Design Pattern: Kodein, Coroutine


Middleware: Repository to fetch the data from JSON(separate business logic from the UI)

For Loader Animation: Lottie

Proguard: Applied(I tested before applying the apk size was 6.75 MB and after applying it was 3 MB something)

Tested on Devices: redmi 10s, redmi note 8, Mi A2, One plus 6T(as I have limited devices)