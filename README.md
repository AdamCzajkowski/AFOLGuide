# AFOL Guide

This is simple app to research LEGO® sets, instuctions and bricks. All information about LEGO® are from rebrickable.com. 

From Now Application is available:

<a href="https://galaxystore.samsung.com/detail/com.afol?fbclid=IwAR1mhCahVVs8dw4kdPjsnlNwXCCEGcgtisBUlGR1d5VhUtocGV3bDTfDiSo&session_id=W_c3a90baa1c64f7c35ef0c8d84c715565"><img src="https://i.ibb.co/QcKbF2R/galaxy-store-available.jpg" alt="galaxy-store-available" width="200" height="70"></a><a href="https://play.google.com/store/apps/details?id=com.afol&hl=en_US"><img src="https://i.ibb.co/yQTT4wk/google-store-available.png" alt="google-store-available" width="200" height="70"></a>

Screens:
---------------------------
<img src="https://i.ibb.co/xCbGsdk/Screenshot-1582282786.png" alt="Screenshot-20200107-152638-AFOL-Guide" width="200"><img src="https://i.ibb.co/27dw7TH/Screenshot-1582283116.png" alt="Screenshot-20200107-152650-AFOL-Guide" width="200"><img src="https://i.ibb.co/kmWGBv9/Screenshot-1582283120.png" alt="Screenshot-20200107-152657-AFOL-Guide" width="200"><img src="https://i.ibb.co/XLp9xMk/Screenshot-1582283125.png" alt="Screenshot-20200107-152729-AFOL-Guide" width="200"><img src="https://i.ibb.co/CPpbJFb/Screenshot-1582283128.png" alt="Screenshot-20200107-152737-AFOL-Guide" width="200"><img src="https://i.ibb.co/vHPMgm2/Screenshot-1582283131.png" alt="Screenshot-20200107-152747-AFOL-Guide" width="200"><img src="https://i.ibb.co/SQfmMJb/Screenshot-1582283143.png" alt="Screenshot-20200107-152753-AFOL-Guide" width="200"><img src="https://i.ibb.co/xfPnTzF/Screenshot-1582283172.png" alt="Screenshot-20200107-152753-AFOL-Guide" width="200">

"Favorite" section:
-----------------------------
In this section, user will be able to see his favorite LEGO® sets. User can add set to "Favorite" by clicking proper button in "LEGO® set's details".
By cliking on LEGO® set user will go to "LEGO® set's details"

"Sets" section:
------------------------------
In this section, user will be able to see his search result LEGO® sets after filling text field with proper tags like set's number or name. 
By cliking on LEGO® set user will go to "LEGO® set's details"

"Bricks" section:
-------------------------------
In this section, user will be able to see his search result LEGO® bricks after filling text field with proper tags like brick's number or name. 
by clicking on image of brick user will see Rebrickable site with information about that brick in web browser.

"LEGO® set's details" screen:
-------------------------------
In this section, user will be able to see LEGO® set's detail. User can add set to "Favorite" by clicking icon of heart and if set is in favorite icon of heart is full cover and text next to will be changed to "Exist in favorites".
By clicking "REBRICKABLE" button, user will see rebrickable site with information about that set in web browser.
By clicking "INSTRUCTION" button, user will see official LEGO® site with instruction in web browser.
By clicking "PARTS LIST" button, user will see screen with list of parts used in that model and by clicking on image of brick user will see Rebrickable site with information about that brick in web browser.
At bottom user will be able to see alternative buildings based on that model which is detail's shown on top and by clicking on it user will see Rebrickable site with information about that MOC in web browser.
User is able to change view between list view and grid view by clicking proper icon in right upper corner

About used technology:
-----------------------------
Main programming language - Kotlin

DI - Kodein

Also:
- Retrofit2,
- MVVM,
- RxJava,
- Picasso,
- Coroutines

Changelog: 
--------------------------
16.09.2019: 
 - Updated UI to Bottom Navigation Bar
 - Removing from 'Favorites' moved to recycler and by swiping to left 
 - Fix bugs 
 - Prepare for receiving instructions for orifinal LEGO sets

17.09.2019:
 - In Details of set now user can go straight to LEGO official site to download instructions
 - Fix bugs
 
 24.09.2019:
 - Removed unused files
 - Polish translation
 
 4.10.2019:
 - Add to "Favorites" from search fragment by swiping to right
 - Fix bugs
 
 7.01.2020:
- Refactored all application and prepare to publish on market
- Removed buggy searching by Theme, add/delete from local db by swiping
- Added icon in list in searched list is set in favortites or not
- Added removing option in list of favorite sets
- Changed design of NavBar and toolbar
- Fix small bugs

21.01.2020 : version 0.91 Beta
- Fix translation bugs submitted by testers 
- Added 'Spare part' label for parts list
- Added information about lack of internet connection
- Added buttons for parts to bricklink page
- Changed layout of single brick in search brick
- Changed minimum android version to 5.0
 
 
 21.02.2020: version 1.2
- Changed logo
- Added switch view in parts list between list view and grid view
- Application sended to Galaxy Store
- Changed main color to blue

By Adam Czajkowski
