CHANGELOG
======

* v0.3.7.2: Bug fix && Use fragment for plugin views
	* Fix a crash when trying to open the edit screen of existing events (caused by unintentionally recursion)
	* Use fragment for the display of plugin views (for better expressivity)

* v0.3.7.1: Allow to control external logging
	* Disable external logging by default
	* User can control whether to enable external logging (setting will take effect the next time Easer starts)
	* Show app version in Settings

* v0.3.7: Better debug (logging)
	* Use [Logger](https://github.com/orhanobut/logger) for better logging
		* Better output
		* Log to file (/sdcard/logger/) for better debug
	* Add more logging points
	* Check some argument types
	* Minor changes

* v0.3.6.1: Fix autostart on boot

* v0.3.6: Add HotspotOperationPlugin && Change event check behaviour
	* Add a new OperationPlugin to control hotspot settings (use reflection, may not work on all devices)
		* Code inspired by https://stackoverflow.com/questions/25766425/android-programmatically-turn-on-wifi-hotspot
	* Do not check event status when reloading (e.g. after any event is changed or the reload button is pressed)

* v0.3.5.2: Fix crash on API lower than 21 during startup
	* Caused by using vector drawable (which was introduced in API 21, and is supported by support library on lower API systems) and the support library doesn't work fully **automagically**.
		* Two were fixed to correctly use the support library
		* One (MainActivity) is fixed to handle a place where support library doesn't work **automagically**.

* v0.3.5.1: No auto-starting of the EHService during application startup
	* Mainly used for debugging purpose

* v0.3.5: Add BluetoothDeviceEventPlugin && Add import/export && fixes
	* Add a new event to listen to the bluetooth device connected
	* Add the ability to import and export events and profiles
	* Fix the name of CommandOperationPlugin

* v0.3.4: Add CommandOperationPlugin && More XMLs && Code rearrangements
	* Add CommandOperationPlugin: run any commands
	* Add more XML layout usages
	* Rearrange some code

* v0.3.3.2: Bug fixes && More XMLs
	* Fix WifiEventPlugin's compariasion of SSIDs
	* Fix BatteryEventPlugin's monitoring
	* Use XML in WifiEventPlugin
	* Slight code changes (with no changing of functions)

* v0.3.3.1: Use layout xmls instead of Java codes for some plugin layouts

* v0.3.3: Add DayOfWeekEventPlugin && (re)add enabling state of events
	* Add new event: DayOfWeekEventPlugin
	* Restore the ability to set an event enabled or disabled
	* Slight UI changes (better appearance)

* v0.3.2: Make broadcast fully customizable && Add battery event plugin && Fix
	* Make user able to customize all fields of intent (broadcast)
	* Add BatteryEventPlugin to listen to battery status
	* Fix permission issue of bluetooth
	* Add MODIFY_AUDIO_SETTINGS in case of permission issue

* v0.3.1: Add Settings activity and Welcome dialog
	* Add SettingsActivity
	* Show Welcome dialog when first start the app
	* Fix resetting to Outline when rotating screen (thanks to trikaphundo)
	* Fix deletiong of events and profiles
	* Fix renaming profiles (also change them in events)
	* Minor changes of source code:
		* Remove useless strings
		* Rename some strings

* v0.3.0: Support event types for all existing events
	* Add more types
	* Add more types for all current EventPlugin(s)
	* Change Chinese UI name for 'Profile'
	* Update documentation/comments in code
	* Rename some methods/functions/fields

* v0.2.9
	* Add "is" type for TimeEventPlugin
	* Also notify *unsatisfied* in SelfNotifiableSlot
	* Fix editing of non-leaf events

* v0.2.8
	* Move global initialization to Application level
	* Change the way to search for which event is satisfied
		* Promote subtree to child slots
			* add a function: Abstract.canPromoteSub()
		* Report also unsatisfying

* v0.2.7
	* Add EventType
	* Assigned a default type to each event (so old data won't be broken)
	* Will bump to v0.3 after finishing the trigger (Lotus and EHService) part

* v0.2.6
	* Add operation: ringer mode (support to change silent/vibrate/normal)

* v0.2.5.3
	* Add Japanese translation (thanks to naofum)
	* Add Chinese UI (not really a "translation" because I'm a native Chinese speaker)

* v0.2.5.2
	* Remove broken Events / Profiles when adding a new one with the same name

* v0.2.5.1
	* Check data validity before saving (both for Event and Profile)

* v0.2.5
	* Add "last profile" section in outline

* v0.2.4
	* Complement "About" page (thanks alaskalinuxuser)

* v0.2.3
	* Add operation: brightness control

* v0.2.2
	* No longer set the description of the layouts (of plugins) in themselves
	* Remove built-in Label (in favour of the ability to add the label in SwitchItemLayout) in the layout of plugins

* v0.2.1
	* Make OutlineFragment look a bit better
	* Add Start and Stop to the menu of OutlineFragment

* v0.2
	* Redesign the bachground mechanism of Easer
