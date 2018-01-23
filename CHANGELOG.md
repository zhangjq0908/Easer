CHANGELOG
======

* v0.5.3.1: Various fixes to v0.5.3
	* Fix "import" not working correctly
		* due to the wrong handling of directory entry in zip)
	* Fix crash when trying to open an existing Profile
		* due to the wrong design of `DelayedJob`
	* Fix incorrect convertion of data formats
		* Inline `Scenario`s should be kept when editing an `EventStructure`

* v0.5.3: Add `Scenario` to allow reuse of event data && Add more configurations to events && Fix import not working correctly && Code refactor
	* Add `Scenario` as an abstraction of EventData and allow EventStructure to link to it
	* Add *reverse* to directly reverse the Scenario
	* Add *repeatable*, *persistent* as more granulated configuration to events
	* Fix importing not working correctly due to filesystem handling
	* Code factor
		* Extract more abstract classes and generics

* v0.5.2: Add "cool down" (seconds) && Allow to match BSSID for WiFi Event && Check permissions before enabling plugins && fixes & improvements
	* Add "cool down" time (in seconds) for the re-activation of the same event
	* `WifiEventPlugin` can now handle BSSIDs
		* If you need to match BSSID and ESSID, you will need two chained events
	* Check (and require) permissions before enabling plugins in Settings
	* Fix `NfcEventPlugin` (which wasn't really working previously)
	* Code improvements
		* See 00fa3a6

* v0.5.1: Change a bit of plugins' definition (mainly generify and remove unneeded methods) && Add `HeadsetEventPlugin` && Code clean up
	* Add `DataFactory` (and subclasses) to be used as a wrapper of `StorageData` (and subclasses)
	* Generify `PluginDef`, `PluginViewFragment`, `DataFactory` (and subclasses) to use their related `StorageData` as the type parameter (so that there is no need to do lots of manual type casting and checking)
		* Fix classes using them to satisfy generics
	* Add `HeadsetEventPlugin` to listen to headset plug in and plug out
	* Remove redundant wrapper (`OperationLoader.load()`)
	* Add `ValidData` annotation to describe that the data is "valid"

* v0.5: Add Timer and NFC Event & Set alarm Operation && Fix leaked BroadcastListener && Fix "unsatisfied" not triggered && Better UI for settings page
	* Add `TimerEventPlugin` to set a timer which allows delaying for a few time
	* Add `NfcTagEventPlugin` to listen to NFC tag being scanned
	* Add `AlarmOperationPlugin` to set alarm
	* Fix leaked BroadcastListener in two classes (should benefit battery consumption)
	* Fix "unsatisfied" not trigger (fix part of the desired function)
	* Better UI for settings page
		* Add spaces between each categories
		* Fix translation
		* Add "back" to the UI
	* Fix title for "settings" and "about"

* v0.4.9: Add Event for listening to notifications and Operation for sending notifications && Add a setting entry for enabling / disabling plugins && Add compatibility checking for plugins && Inform future changes && Bug fixes
	* Add `NotificationEventPlugin` to listen to notifications and `SendNotificationOperationPlugin` to post notifications
	* Add a setting entry to enable or disable plugins
	* Add `PluginDef.isCompatible()` to check for the compatibility of plugins before using them
	* Add a prompt screen to inform the user about possible future changes and actions to take
		* Currently, v0.5 (or later) will drop the support for old data formats, so users should convert the old formats to new formats (by using the conversion provided in the setting screen).
	* (Lollipop+ / SDK 21+) Fix the function of `RingerModeOperationPlugin` to set to silent mode
	* Fix the problem of re-requesting permissions
	* (*dev*) Explicitly fix `0` as `infinity` for `OperationPlugin.maxExistence()`
	* (*dev*) Remove the ordering of plugins from `PluginRegistry` (moved that to where it is needed)

* v0.4.8: Run multiple commands in one process && multiple developer changes
	* `CommandOperationPlugin` now runs multiple commands in one process, which allows inputting data into an interactive shell
	* Developer changes
		* Make `StorageData` `Parcelable`
			*  Implement that in all subclasses
			*  Add tests for them
		*  Change `Map` to `Set` in `CalendarEventData` (for the condition) (as well as in the test)
		*  Add many *nullality* annotations to plugins (including data and views)
		*  Use exception instead of `null` for data retrival (this seems to be more natural in Java)
		*  Resolve some lint warnings
			*  Make the `Handler` in `ryey.easer.plugins.operation.brightness.DumbSettingBrightnessActivity` `static`
			*  Add some `final`s
			*  Use `StringBuilder` in some places (instead of raw `String` manipulation)
			*  Fix a few javaDoc
			*  Use explicit locales
			*  Use new API instead of old API (`Fragment.onAttach()`)
			*  Remove casts of `findViewById()` (because from API 26 that cast is no longer required)
			*  Better access modifiers
			*  Minor fixes and clean-ups

* v0.4.7: Require permissions during runtime && fix several bugs
	* Add permission checking and requesting codes for each plugin
	* When loading the plugin's view, its permission is checked and requested (if necessary)
	* Request root permission when enabling "root features"
	* Fix incorrect root usages for `{AirplaneMode,Cellular,Command}Operationplugin`
	* Fix "root preference" of `CommandOperationPlugin`

* v0.4.6.1: Fix start-on-boot && remove an unneeded class

* v0.4.6: Use *selection* instead of *directly listing* for *Operation*s && Add operations: airplane mode & send sms && Add event: receive sms && several bug fixes
	* When editing a *Profile*, *Operation*s are not listed directly but should be **added** from a list
		* One *Operation* could be chosen multiple times (if it allows)
		* Introduce dependency of Guava
	* Add `AirplaneModeOperationPlugin` to toggle the airplane mode (root only)
	* Add `SendSmsOperationPlugin` to send SMS
	* Add `SmsEventPlugin` to listen to incoming SMSs
	* Bug fix:
		* legal broadcast may be treated as illegal
		* change to silent mode not work on Android 5+ ([#32](https://github.com/renyuneyun/Easer/issues/32))
		* change brightness not functioning ([#32](https://github.com/renyuneyun/Easer/issues/32))
		* translation fix

* v0.4.5: Add `BroadcastEventPlugin` and `MediaControlOperationPlugin` && Fix and update `BroadcastOperationPlugin` && Fix the selector title of `WifiOperationPlugin`'s view'
	* Add `BroadcastEventPlugin` to listen to system broadcasts
	* Add `MediaControlOperationPlugin` to control media player behavior ([#31](https://github.com/renyuneyun/Easer/issues/31))
	* Fix a data bug in `BroadcastOperationPlugin` and allow to add extras (see [#31](https://github.com/renyuneyun/Easer/issues/31))
	* Fix the selector title of `WifiOperationPlugin` (thanks to Sohalt [#41](https://github.com/renyuneyun/Easer/pull/41))

* v0.4.4: Introduce `SynchronizationOperationPlugin` and `NetworkTransmissionOperationPlugin` && Add *root features* && Bug fixes && Deveopment changes
	* Introduce `SynchronizationOperationPlugin` to control the *account synchronization* setting
	* Introduce `NetworkTransmissionOperationPlugin` to send network packets
	* Introduce the ability to use rooted-only features
		* Some functions don't have exposed APIs so using reflections is the way to implement them previously. However such reflection is not guarenteed to work on all devices so these functions are not reliable. Using root permission could help to solve (at least part of) this problem by using root-only actions (e.g. executing special commands).
		* Add a section in the settings page to control the enabling state
		* Introduce a way to mark each plugin's needs of using root permission (to use in the future)
		* Adopt rooted features to `CellularOperationPlugin` (to turn mobile data on and off)
	* Fix the `equals()` method in some data (this bug has no affection of daily usage)
	* More tests
	* Remove the need of manually injecting `PluginViewFragment.expectedDataClass` in favor of `PluginRegistry`'s ability to lookup (this introduces slight performance degrading which will be optimized later)

* v0.4.3: Introduce new interface for data serializing and parsing && Faster loading speed for data && Add JSON backend for data storage && Refactoring
	* Introduce new serializing and parsing interface for `StorageData` (which affects all plugins)
		* The new interface now handles versioning and different backend types (which can be safely ignored if intended)
		* Serializing always yields the newest version of data format
		* The old interface is deprecated and will be completely removed in the future
	* Add JSON as a backend (co-exists with XML)
		* JSON is now prefered and used by default
		* Compatibility to old data (XML format) is kept
			* When saving (editing) from a old data, it will be stored to JSON format
		* The old XML backend exists and there isn't a current plan to remove it
	* Handling (saving, editing, listing) of events and profiles should be faster now
	* Large refactoring (e.g. generify lots of classes) related to the data storage backend

* v0.4.2.1: UI improvements
	* Better UI of the page for editing events
		* Text size/spacing (especially for Chinese)
		* Use `ConstraintLayout`
	* Show the "when..." text for `CalendarEventPlugin`
	* Use `Cursor` and `CursorAdapter` (implies asynchronous) for the picker list of calendars (for `CalendarEventPlugin`)

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
