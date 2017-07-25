Easer
=======
[<img src="https://f-droid.org/badge/get-it-on.png"
      alt="Get it on F-Droid"
      height="80">](https://f-droid.org/app/ryey.easer)

Ease your life by automatically performing routine actions.

Introduction
-----
Make your smart phone smarter: tell it what to do under different situations.

You can think Easer as a local version of IFTTT: Trigger actions or change settings (*Operation*s, bundled as *Profile*) in different situations (*Event*).

You can chain *Event*s (set dependencies) as trees. Easer will perform Post-Order Traversal to load you Profile(s).

Interested in joining development / add more *Event*s or *Operation*s / optimising UI? Have a look at [HOWTO](HOWTO.en.md) and [TODO](TODO.en.md).

Also have a look at [wiki](https://github.com/renyuneyun/Easer/wiki), especially [FAQ](https://github.com/renyuneyun/Easer/wiki/FAQ).

Examples
------
* Turn your phone into Silent mode at 2 a.m.
* Cancel Silent mode at 8 a.m. on weekdays, and at 10 a.m. on weekends
* Turn WiFi on when approaching your home; turn WiFi off when leaving your home

Functions already supported
--------
### Event
* Date
* Time
* WiFi connection name (SSID)
* Location (based on basestations)
* Battery status
* Day of week
* Bluetooth device connected

### Operation
* WiFi switch
* Cellular network switch
* Bluetooth switch
* Auto rotation switch
* Sending Broadcast
* Screen brightness
* Ringer mode
* Run commands

Copyright
------
Copyright (c) 2016 - 2017 Rui Zhao (renyuneyun) <renyuneyun@gmail.com>

Licensed under GPLv3+ (See LICENSE)

### Why GPL?

The expected functions of Easer contain lots of tracks / 
captures of privacy (e.g. location, calendar), and would be able to access the Internet. We would never want a tool which is expected to better facilitate our lives to become a spying tool, so we must prevent that from happening as we can. The only way is to allow anyone to censor every part of Easer, which means to ensure Easer (and its derivated work, if any) to be opensource.  
Because as of the design of Easer, each functionality will (in the furutre) become modules / plugins, maicious codes should also be prohibited from these parts, so we hope to rely on the feature of GPL (that derivated work should also be licensed under GPL) to enforce this.

In fact, ensuring derivated work / plugins to be **GPL** is unneeded, because we only need them to be **opensource**. However, GPL is the only license (known by me) which can enforce deriavted work / plugins to be opensource, so it's the only choice.
