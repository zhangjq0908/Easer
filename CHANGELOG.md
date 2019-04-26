CHANGELOG
======

* v0.7.4.1: Crash fix && i18n
	* Fix crash when trying to pick CellLocation
	* Slightly improved English
	* i18n updates:
		* New: Italian
		* Updated: German, Japanese, Norwegian Bokmål

* v0.7.4: New Pivot graph && Remove deprecated functions && i18n update
	* API 16+ is now required (because of GraphView)
	* New Pivot graph for displaying the structure of Scripts
		* Expansion is expected in future versions
	* Remove "passive mode"
	* Remove old formatting replacer
	* i18n update:
		* new languages: Korean & Italian
		* updated translation: German

* v0.7.3.1: Small bug fixes && i18n update
	* Fix app_name being incorrect in several languages
	* Treat invalid CellLocation as invalid
	* Fix "launch Activity" not checked by default
	* i18n update:
		* new language: Swedish
		* updated translation: Danish, Japanese

* v0.7.3: Better welcome page && PlayMediaOperationPlugin && Other changes && i18n
	* A series of dedicated welcome pages are now used
	* New operation: PlayMediaOperationPlugin
	* Allow to specify class in LaunchAppOperationPlugin
	* i18n update: Danish & German & Japanese

* v0.7.2.4.1: Fix wrong user notification of passive-mode
	* Passive mode should be enabled

* v0.7.2.4: Bug fixes & Slight changes & i18n update
	* Fix StateControlOperationPlugin causes freeze
	* Fix command not executed correctly
	* Alert for near future change (if necessary)
	* Notification slight change
	* i18n: Chinese & Danish & Japanese & Norwegian Bokmål

* v0.7.2.3: Record CellLocation for a timespan && fix && i18n
	* Now records CellLocation for a timespan, instead of one-shot
	* Fix renaming Condition not correctly changing inline ConditionEvent
	* i18n:
		* Chinese
		* Norwegian Bokmål

* v0.7.2.2: More Dynamics
	* Add more Dynamics
	* Update German Translation

* v0.7.2.1: Bug fixes
	* Fix NetworkTransmission not working
	* Fix DayOfWeek not correctly scheduled
	* Fix SendBroadcast with extras not working
	* Translation update

* v0.7.2: Tree List support && Translation update
	* Add tree list support for Script
	* Translations update
	* New translations:
		* Dutch
		* Russian
	* Localized Chinese for Taiwan variant through Weblate
		* Note: as described in the README.md, OpenCC will be used for localized Chinese variants. Changes made on Weblate may be lost later.
	* Small code refinement

* v0.7.1.1: Bug fixes && Translation update
	* Fix AirplaneMode not correctly working
	* Fix permission check incorrect for some NotificationListener (e.g. ringer mode)
	* Trnalstion update
		* Danish
		* Norwegian Bokmål

* v0.7.1: New Conditions && More detailed ActivityLog && TimerEvent supports seconds && Fix && Code clean up && Translation Update
	* New Conditions
		* WifiEnabledCondition
		* BluetoothEnabledCondition
	* More detailed ActivityLog (now contains detailed Profile status)
	* Add short delay to TimerEvent to support seconds of delay
	* Fix *repeated* and *persistent* fields ignored
	* Code clean up
		* Create different instances for DataStorage, instead of singleton
		* Rename PluginRegistry to LocalPluginRegistry
		* Method signature annotation update
		* Update utils/templates a little more (C.Format ==> PluginDataFormat)
	* Translation update
		* Galician (new)
		* Danish

* v0.7.0.5: Bug fixes && Translation update
	* Fix not working after Easer put to background
		* By fixing Services not correctly in foreground
		* Code refactor
	* Fix Dynamics not working correctly
	* Translation update
		* Norwegian Bokmål
		* German
		* Danish

* v0.7.0.4: Better Notification && Bug fix && Translation update
	* Add settings for Notification
	* Lower Notification priority if possible
	* Fix crash when adding cell location
	* Minor Translation update
		* French
		* Norwegian Bokmål

* v0.7.0.3: Run service in foreground && Bug fixes && Translation update
	* Run service in foreground, for Android 8 and memory management
	* Fix Send Broadcast not working
	* Catch warm-reboot broadcast
	* Update Danish translation

* v0.7.0.2: Fix crash for Remote Plugin && Update translation
	* Fix crash when reading data from Remote Plugin
	* Translation update
		* French
		* Norwegian Bokmål

* v0.7.0.1: Code cleanup && More robust Remote Plugin && Slight UI modification && Bug fixes
	* Rename `remote_plugin` to `plugin`
	* Allow to explicitly specify the Activity to edit data in Remote Plugin
	* Add menu to ActivityHistory to clear history
	* Respect 12/24 hour clock
	* Translation update
		* Japanese
		* Norwegian Bokmål
		* Danish
	* Fix incorrect reference in some translated strings
	* Dev changes

* v0.7: Introduce Remote Plugins && Translation updates && More Dynamics
	* Introduce Remote Plugins to Easer
		* Remote Plugins are separate apps which can act as plugins of Easer to support more functions (Event/Condition/Operation)
		* Currently only Operation Remote Plugin is introduced
		* See [EaserOperationPluginExample](https://github.com/renyuneyun/EaserOperationPluginExample) for an example
	* More translations from weblate
		* Danish
		* Japanese
		* French
		* Norwegian Bokmål
		* Germany
	* Add Dynamics for Sms Event (for #164)

* v0.6.9.1: Allow multiple Ringer Modes && Update translations && Bug fix
	* Allow to set mutliple Runger Modes in one Profile (close #155)
	* Update translations
		* Update Danish translation
		* Update Chinese translation
	* Fix incorrect status of WiFi Event/Condition when connecting (fix #149)

* v0.6.9: Better Activity Log && Widget && More Dynamics && Better UX && i18n && dev change
	* Allow to log more type of items in Activity Log
		* Script status
		* Service on / off
		* Profile triggering
	* Add (launcher) widget (for service status)
	* Add ScriptName & ProfileName Dynamics
	* Allow to select destination for exporting on Kitkat+ (fix #146)
	* Fix crash when trying to edit Dynamics with no Event selected (fix #147)
	* Update Danish translation (#148)
	* Dev change:
		* Use Kotlin for new code
		* Use DataBindingUtil

* v0.6.8: New logo && Show help text when list empty && Add category for Operation && Other UI fine-tune
	* New logo (mainly from #138)
	* Show help text when Script/Event/Condition/Profile list is empty (#96)
	* Add category for OperationPlugin and categorize them when adding to Profile
	* Add "root-feature" indicator to plugin enabling page
	* Use white "Add" for FAB

* v0.6.7.2: Allow to manually trigger Profiles && Bug fixes
	* Add a function to manually trigger any Profile (in the Context menu) (#136)
	* Fix not working Operation after v0.6.7 (#143, #141)
		* Previously incorrectly returned `null` in `OperationData.applyDynamics()`
		* Now return `this` instead
	* Fix crash when opening Dynamics Link on some Profiles (#142)

* v0.6.7.1: Localized names for Dynamics && Make events "repeatable" by default && Bug fixes && Danish translation && Dev changes
	* Use localized names for Dynamics
	* Make Events "repeatable" by default when creating an Event
	* Bug fix:
		* WifiEventPlugin not correctly handling "connecting" (#133)
		* Cooldown time unit was milliseconds, but should be seconds
	* Update Danish translation (#139)
	* Dev change:
		* Rename "Property" to "Dynamics" (class, and some methods)
		* Return an array of Dynamics, instead of Set (though the semantics is still a set)

* v0.6.7: Add *Dynamics* && Support *Dynamics* in `NotificationEventPlugin`
	* *Dynamics* is the universal way of passing data from *Event* to *Profile*
		* It replaces the previous formatting expressions
		* Relevant UI is also added
	* `NotificationEventPlugin` supports *Dynamics* now (#113)

* v0.6.6.1: Bug fixes && Update Danish translation
	* Fix crash when opening some existing Broadcast Operation (#129)
	* Fix incorrect handle of "reversed" of Condition
	* Fix WifiTracker's initial state (#128)
	* Update Danish translation (#126)

* v0.6.6: Rename "Scenario" to "Event" && Colorize lists && Various fixes
	* Rename "Scenario" to "Event" in code & storage & UI
	* Show different color in lists
		* Red for invalid
		* Grey for inactive (Script)
	* Several Fixes
		* Fix incorrect handling of renaming (not linking update)
		* Fix crash after renaming Condition (by reloading Condition)

* v0.6.5.1: Various fixes
	* Fixed checkbox in TimerCondition
	* Fix CellLocationCondition UI not correctly add location
	* Fix "reverse" not working in TimeCondition

* v0.6.5: Add `TimeConditionPlugin` & `WifiConditionPlugin` & `HeadsetConditionPlugin` && Back to "Outline" && Fix BroadcastEventPlugin
	* Copied `TimeConditionPlugin`
		* With a few changes to suit semantics
	* Copied `WifiConditionPlugin`
	* Copied `HeadsetConditionPlugin`
		* Also check state initially
	* "Back" now goes back to "Outline" (rather than stacked)
	* Fix `BroadcastEventPlugin` won't save
		* Wrong condition check for `isValid()`

* v0.6.4: Copied `DateConditionPlugin` & `DayOfWeekConditionPlugin` & `ScreenConditionPlugin` && Crash fix
	* Copied `DateConditionPlugin` (from `DateEventPlugin`)
		* With a few changes to suit semantics of Condition
	* Copied `DayOfWeekConditionPlugin` (from `DayOfWeekEventPlugin`)
		* With a few changes for better performance
	* Copied `ScreenConditionPlugin` (from `ScreenEventPlugin`)
	* Fix crash when deleting Scenario
	* Fix crash when using ConditionEvent

* v0.6.3: Copied `BTDeviceConditionPlugin` & `ConnectivityConditionPlugin` && Scroll up/down when editing Scenario or Condition
	* Copied `BTDeviceConditionPlugin` (from `BTDeviceEventPlugin`)
	* Copied `ConnectivityConditionPlugin` (from `ConnectivityEventPlugin`)
	* Allow to scroll up and down when editing Scenario or Condition
	* Update da_DK translation
	* Rename package `ryey.easer.core.ui.edit` to `ryey.easer.core.ui.data`

* v0.6.2.1: Less crash
	* Fix crash when renaming Scenario
	* Leave broken data in place, without actually using it in Service
	* Dev change
		* Refactor helper functions

* v0.6.2: Add `CellLocationConditionPlugin` && Use cards on Outline && Add screen unlock event && Crash removal
	* Add `CellLocationConditionPlugin` (mirror from `CellLocationEventPlugin`)
		* Also complement `SkeletonTracker`
	* Use CardView on Outline
		* Each section is one card
		* Respond to click / long-click
	* Prevent a potential crash
		* Prevent from removing a Script referenced in StateControlOperation
	* Add screen unlock event (thanks to @DeathTickle [#105](https://github.com/renyuneyun/Easer/pull/105))
	* Update da_DK translation (thanks to @twikedk [#109](https://github.com/renyuneyun/Easer/pull/109))
	* The previous version should really be v0.6.0.1, and this should be v0.6.1 if that didn't happen

* v0.6.1: Bug fixes && prompt improvement
	* Fix crash when renaming Condition
	* Fix crash when deleting Condition in use
	* Disallow adding new Condition / Scenario / Profile when no relevant plugin is enabled
	* Prompt for failure to delete

* v0.6: Add ConditionEvent (enter/leave) && Go back when pressing "back" && Do not clear log when reloading service && Various fixes && Dev changes
	* Add ConditionEvent - Events for Condition state
		* Enter Condition and Leave condition
	* Go to previous page / fragment when pressing "back" button
	* Do not clear log when reloading EHService
		* The log is kept to at most 1000 entries
		* The log will be lost if EHService is freed
	* Fixes
		* Fix title and navigation drawer behavior of Activity Log
		* Fix unexpected reloading of scripts when switching pages
		* Fit spinners when editing Script into screen
		* Make "reverse" work for Condition
		* Fix `BatteryTracker.state()`
	* Dev changes
		* Move `ConditionHolder` to a separate `Service`

* v0.5.9.1: Allow to restart after update && Fix Events not triggered sometimes && Do not trigger `TimeEventPlugin` for the past time of the current day
	* Allow to restart service after updating app (changeable in settings)
	* Fix "reversed" Events not triggered at the first time
	* Do not trigger TimeEvent if the current time is later than the designated time
		* Skip the trigger for that day (i.e. move to the next day)
	* Add tests

* v0.5.9: Add *Condition* mechanism && Add BatteryConditionPlugin && Fix importing / exporting due to storage change
	* Add *Condition* mechanism, as a complementary of Event
		* *Condition* represents for "state", and *Event* will be changed to represent real one-shot events
	* Add BatteryConditionPlugin
	* Fix importing / exporting, which was broken because of naming change (event -> script)

* v0.5.8: Rename the UI component "Event" to "Script" && Activity Log && New plugins (`UiModeOperationPlugin` & `ScreenEventPlugin`) && Allow to assign delay to set bluetooth volume && Little UI update
	* Rename the UI component "Event" to "Script" to avoid confusion to "scenario" (and also for future needs)
	* New page (Activity Log) for Easer's activity log
	* New plugin `UiModeOperationPlugin` for Android UI mode (e.g. car mode and normal mode)
	* New plugin `ScreenEventPlugin` for screen on / off
	* Allow to assign delay when setting bluetooth volume
	* Add divider when selecting Operation

* v0.5.7.1: Fix bluetooth volume not changing && Fix crash when rotating && Code clean up
	* Fix `VolumeOperationPlugin` changing bluetooth volume
	* Fix crash when rotating device on `Edit{Event,Profile}Activity`
	* Code clean up
		* Remove unneeded and deprecated API a little bit

* v0.5.7: Remove deprecated classes && Add `LaunchAppOperationPlugin` && Add Bluetooth to `VolumeOperationPlugin` && Dev changes
	* Remove deprecated interfaces / classes
		* Remove `XmlDataStorageBackend`
		* Remove `EventType`
	* New Operation: Launch App (`LaunchAppOperationPlugin`)
	* Allow to adjust Bluetooth volume in `VolumeOperationPlugin`
	* Dev changes:
		* New script to automatically create templates for new `OperationPlugin` (!!)
		* Remove `StorageData.parse()`
			* StorageData can not have their fields declared `final` in principle (!)
		* Remove `DataFactory.emptyData()` && clean up empty constructors

* v0.5.6.1: Switch between 12-hour and 24-hour clocks && Better MediaControlOperationPlugin for Lollipop+ && Update Danish translation && Minor changes
	* Allow to change between 12-hour and 24-hour clocks in Settings
	* Use `MediaSessionManager` for Lollipop+ (API 21+) for better media control
	* Update Danish translation (thanks to twikedk)
	* Remove static variable `running` in some `NotificationListenerService`s in favor of PackageManager detection

* v0.5.6: Add the ability to control volume && Allow to customize Do Not Disturb mode (Ringer Mode) && Drop unneeded menus
	* Add `VolumeOperationPlugin` to control volume
	* Allow to customize Do Not Disturb mode in `RingerModeOperationPlugin`
	* Drop unneeded menus / menu items (because their usage is already in somewhere else)
		* Drop the menu (which contains only "Add") in the list of Profile / Event / Scenario
		* Drop menu item "About" in Outline
	* Dev changes
		* Add forgotten tests
		* Move a class to the correct package

* v0.5.5.4: Use the newest WiFi scan result && Validate data before importing && Check permission before importing and exporting && Danish translation && Minor changes
	* Request to scan for WiFi APs when obtaining WiFi list
	* Validate data before actually importing
		* Prevents from importing if the data is invalid
	* Request for relevant permissions before importing and exporting data
	* Add Danish translation (thanks twikedk)

* v0.5.5.3: Widen "cooldown" && Alert for the change in v0.5.7 && Developer changes
	* Widen the range of "cooldown": when setting "satisfied" and "unsatisfied", they both check cooldown
	* Alert for the drop of Event Type in v0.5.7
		* Also pin the removal of old (XML) data format in v0.5.7
	* Developer changes:
		* Remove several no longer needed / unneeded methods
		* Better advanced Scenario condition checks
			* Check "satisfied" when checking "persistent" in slots

* v0.5.5.2: Fix "reversed" & "passive mode" && Better handling of `WifiConnSlot`
	* Fix "reversed" not working
	* Fix reversed "passive mode" (correct the semantics of "passive mode")
	* Do not check unneeded conditions for `WifiConnSlot`

* v0.5.5.1: Better looking when editing Events with larger fonts && Correct `NotificationEventPlugin`'s compatibility check && Check all plugins' permissions when logging is enabled && Developer/Debug changes
	* Use `GridLayout` for the top few elements on `EditEventActivity`
		* Let some UI elements adjust themselves on `EditEventActivity`
		* Looks better on larger fonts
	* Check all plugins' permissions when logging enabled
		* This will log all (enabled) plugins with insufficient permission
		* When logging is disabled, the performance is not affected
	* Correct `NotificationEventPlugin`'s compatibility check
		* It's only valid on KitKat+
		* It could support JellyBean in principle, and this may be implemented in the future
			* Or, it may also be implemented together with the support of older devices
	* Dev/Debug changes:
		* Simplify `NotificationEventPlugin`'s definition
		* Move `SettingsHelper` to outer package && Move one function to it
		* Better debugging messages

* v0.5.5: Add `EventControlOperationPlugin` && Introduce format expression && Add 'passive mode' setting && Fix UI problem when requesting permission && Dev changes
	* Add `EventControlOperationPlugin` to be able to change Events' status
	* Introduce format expression for many user-input fields
		* Current supported expressions are:
			* `%DATE%` for current date (`yyyy-MM-DD`)
			* `%TIME%` for current time (`HH-mm-ss`)
	* Add 'passive mode' setting
		* When setting to passive mode, Easer won't check the initial status, but would only listen to new events thereafter
	* Fix unable to set enabled/disabled state for plugin views after requesting permission
	* Dev change: simplify several plugins

* v0.5.4: Restore the old definitions for inline Scenarios && Add `TcpTripEventPlugin` && Various changes
	* Restore the old definitions of "repeatable" and "persistent" when using inline Scenarios
		* They were overridden previously when introducing Scenario
		* The ability to do advanced customization (e.g. "repeatable" and "persistent") is now only available to explicit Scenarios
	* Add `TcpTripEventPlugin` to perform TCP communication and check its success and reply data
		* When not checking reply data, if the packet is successfully sent, it is considered as "true"
		* When checking reply data, only when the actual reply data "startswith" the designated data, it is considered as "true"
	* Make some methods in `AbstractSlot` (e.g. `listen()`) run in separate threads
		* This shall make `EHService` run slightly faster
		* Does not affect the implementation of subclasses of `AbstractSlot`
	* Remove useless section in the data of Events with inline Scenario
	* Developer changes
		* See git's log

* v0.5.3.2: Non-crucial bug fixes
	* Fix "import" using wrong MIME type
		* This bug caused the default file picker not able to pick the backup file
	* Fix wrong check when deleting a Scenario
		* Prevents an "in-use" Scenario from being deleted

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
	* Rename `ryey.easer.core.ui.edit.PluginViewFragment` to `ryey.easer.core.ui.edit.PluginViewContainerFragment` to avoid name duplication with `ryey.easer.plugins.PluginViewFragment` (which was introduced in v0.4)
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
