CHANGELOG
======

* v0.4.2: Add `CalendarEventPlugin` && UI optimization && small changes
	* Add a new event (`CalendarEventPlugin`) to listen to calendar events
	* Add FloatingActionButton (as "add") to the pages listing events or profiles
	* Use [android-flowlayout](https://github.com/ApmeM/android-flowlayout) for day-of-week
	* Minor changes
		* Make `AbstractSlot.changeSatisfiedState` `synchronized` in case of concurrent calls
		* Fix a debug message

* v0.4.1: Add `ConnectivityEventPlugin` && Refactor `OutlineFragment`
	* Add `ConnectivityEventPlugin` for different connectivity status
	* Refactor `OutlineFragment`
		* Split the history part to `LoadedHistoryFragment`
		* Use `ConstraintLayout`
		* Dynamically load fragments instead of specifying in layout xml file

* v0.4.0.1: Use generic on `PluginRegistry` && Remove unneeded codes && class name changes
	* `PluginRegistry` change
		* Make the component in `PluginRegistry` generic to ease maintainance
		* Use query on `PluginRegistry` to get information
		* Replace all manual check of plugins with methods in `PluginRegistry.Registry`
			* Remove the need of `EventData.pluginClass()`
		* Use singleton of `PluginRegistry` instead of manual synchronization
	* Remove unneeded codes
		* remove the need of `EventData.pluginClass()`
		* remove the need of static method `OerationPlugin.pname()` and `EventPlugin.pname()` in the implementation of event plugins (not in the interface)
	* Rename `ryey.easer.core.ui.edit.PluginViewFragment` to `ryey.easer.core.ui.edit.PluginViewContainerFragment` to avoid name duplication with `ryey.easer.commons.plugindef.PluginViewFragment` (which was introduced in v0.4)
	* More tests

* v0.4: Change `ContentLayout` to `PluginViewFragment` && add permission notification on Outline && other change
	* Change `ContentLayout` (subclass of `LinearLayout`) to `PluginViewFragment` (subclass of `Fragment`) for better expressivity
		* Change classes related to it
		* Handle `EventType` in `core.ui`
		* Migrate all existing plugins to `PluginViewFragment`
		* Handle the unregistration of receiver (of `BluetoothOperationPlugin`'s view) in Fragment's lifecycle
	* Add a section to show permission issues of Easer to the Outline page
		* Display only if there isn't enough permission	
	* plugin's view's changes
		* Add picker to WifiContentLayout
		* Do not perform spellcheck for Wifi and Bluetooth
		* Capitalize Bluetooth

* v0.3.9: Change cell location data && use multi for data && other change
	* Add `XmlHelper.EventHelper.readMultipleSituation()` and `XmlHelper.EventHelper.writeMultipleSituation()` to handle events whose data could be multiple section (instead of handling it on each plugin)
	* Add versioning to storage data (for compatibility between versions)
	* Change `CellLocationEventData`
		* Separate with new lines to display
		* Save with multi situation
	* Make `BluetoothEventPlugin` and `WifiEventPlugin` multi selectional
	* Check for permission before using location picker and bluetooth selector (Service side is not handled yet)

* v0.3.8: Use `ViewPager` for views of event plugins
	* Use `ViewPager` to display the fragment of the view of each event plugin (one at a time)
		* Add `ScrollView`
		* Clean up
	* Split PluginViewFragment to two classes for Event and Profile
	* Clean up receiver for `BTDeviceContentLayout` (there was a receiver leak previously)
	* Fix the crash of `BatteryContentLayout` when calling `getData()` with empty selection

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
