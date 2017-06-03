TODO
=====
* Function / User experience
	* Add more Event (suggestions below)
		* GPS position
		* Receiving specific Broadcast
		* Battery status
		* Calendar events
	* Add more Operation (suggestions below)
		* Location setting
		* Ringtone volume
	* UI
		* Change CheckBox to RadioButton (or swipe-to-change ViewPager) on EditEventDialogFragment
		* Make EditEventDialogFragment more good-looking
		* Dynamically add Operation(s) to EditProfileDialogFragment
		* Make OutlineFragment more good-looking
		* Use a tree view in EventListFragment
		* Add a 'settings'/'preferences' Activity (for managing auto-start or so)
		* Complement 'AboutActivity'
			* Add "Contributors" to the page (or sub-page)
			* If using WebView, remove the "back" FAB; if no longer using WebView, make the "back" FAB go back in the WebView
	* Change the semantics of Event
		* Make one Event receive one (or more) situations (as above) of another Event
* Code / Development
	* Draw / Describe the structure of the app for easier understanding
	* All TODO(s) in the code
	* Test coverage
	* Find a way to make Plugin(s) seperate apks, rather than narrow them together in the mainframe of Easer
* Issues around
	* Better description of Easer

To be considered
=======
* Add a 'smart addition of Event' page so that the user can directly select all the needed Event(s) and the app can analyze and add them to the tree(s)
* Change the relationships between Event(s) from tree to graph
