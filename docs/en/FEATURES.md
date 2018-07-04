Supported Features
------
### Event
* *Condition* Event
* Date
* Time
* WiFi connection name (SSID)
* Location (based on base station / cellular tower)
* Battery status
* Day of week
* Bluetooth device connected
* Connectivity status
* Calendar Event
* Any Broadcast/Intent
* Designated SMSs
* Incoming Notifications
* Timer (Delay a while)
* NFC tag scanned
* Headset plug in/out
* TCP communication (one round trip)
* Screen on/off

### Condition
* Battery status
* Location (based on base station / cellular tower)
* Bluetooth device connected
* Connectivity status
* Date
* Day of week
* Headset state
* Screen state (on/off/unlocked)
* Time
* WiFi connection status

### Operation
* WiFi switch
* Cellular network switch
* Bluetooth switch
* Auto rotation switch
* Sending Broadcast
* Screen brightness
* Ringer mode
* Execute commands
* Enable or disable *account synchronization*
* Send network messages
* Control media player
* Toogle airplane mode
* Send SMS
* Post Notifications
* Set alarm
* Change Event status
* Control volume
* Launch App
* UI mode (e.g. car mode)

### *Dynamics*
* Dynamics is introduced to replace the old "formatting expression"
* In *Operations*, everything like `<<YOUR_DYNAMICS>>` is seen as the *placeholder* which is used to assign *Dynamics*
* In a *Profile*, all *placeholders* are seen as identical, using the same *Dynamics* assignment (if any)
* If a *placeholder* isn't assigned a *Dynamics*, its string literal will be used
* Currently, *Operations* who supports *Dynamics* is the same as the previous ones supporting "formatting expression". However, in theory and plan, all *Operation* which allows the user to input data will support *Dynamics*
