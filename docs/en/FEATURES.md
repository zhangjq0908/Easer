Supported Features
------
### Event & Condition
* Date
* Time
* WiFi connection
* Cellular Location (based on base station / cellular tower)
* Power status
* Day of week
* Bluetooth device connected
* Connectivity status
* Headset plugged in/out
* Screen on/off
* Calendar event
* (Geo-)Location
* Battery level

### Event
* *Condition* Event
* Any Broadcast/Intent
* Designated SMSs
* Incoming Notifications
* Timer (Delay a while)
* NFC tag scanned
* TCP communication (one round trip)
* Widget (on launcher / desktop / home screen)

### Condition
* Ringer mode

### Operation
* WiFi switch
* Cellular network switch
* Bluetooth switch
* Auto rotation switch
* Sending Broadcast
* Screen brightness
* Ringer mode
* Execute commands
* Control Hotspot switch
* Enable or disable *account synchronization*
* Send network messages
* Send HTTP messages
* Control media player
* Toggle airplane mode
* Send SMS
* Post Notifications
* Set alarm
* Change Event status
* Control volume
* Launch App
* UI mode (e.g. car mode)
* Play media (e.g. music)

### *Dynamics*

*Dynamics* provides a way to use real-time event/condition/environment information in your operation when the profile gets triggered. It makes your profile somewhat similar to templates.

You need to follow the rules below to use it. Use the "chain"/"link" button when editing script to modify Dynamics.

#### Main (functional) info
* In *Operations*, everything like `<<YOUR_DYNAMICS>>` is seen as a *placeholder* which is used to assign *Dynamics*.
* Pleceholders are identified by name, i.e. the string. In a *Profile*, all *placeholders* with the same name are seen as identical, using the same *Dynamics* assignment (if any).
* If a *placeholder* isn't assigned a *Dynamics*, its string literal will be used.
* To assign *Dynamics*, link that through the "chain"/"link" button when editing scripts. You need to select a Profile to use this feature.

#### Temporal info
* Dynamics is introduced to replace the old "formatting expression".
* Initially, *Operations* who supports *Dynamics* was the same as the previous ones supporting "formatting expression". It now expanded a little bit, but is still incomplete. In theory and plan, all *Operation* which allows the user to input data will support *Dynamics*.
* The *Dynamics* are limited to the immediate Script node and environmental information. It is planned to support the predecessors recursively.