[<img src="https://f-droid.org/badge/get-it-on.png"
      alt="Get it on F-Droid"
      height="80">](https://f-droid.org/app/ryey.easer)<img align="right" src='https://github.com/renyuneyun/Easer/raw/master/app/src/main/ic_launcher-web.png' width='128' height='128'/>

Ease your life by automatically performing routine actions.

Introduction
-----
Make your smart phone smarter: tell it what to do under different situations.

### Smart by user-defined explicit automation

Easer provides your with *explicit automation*. It knows various events (see below) and YOU (the user) tell it what to do on what events (you can even combine multiple events). Thus, you no longer need to manually perform routine actions, or worry about forgetting to perform them.

More than that, Easer can not only handle *Events*, but also check for *Conditions*. This makes things simpler when they can't be seens as Events, and makes it more intuitive.

It doesn't stop here: in Easer, you can pass message from an Event to a Profile. This allow dynamic content to be generated in Profiles.

You can think of Easer as a local version of IFTTT: trigger actions or change settings (*Operations*, bundled as *Profiles*) under different situations (*Events*).

### Inter-app coordination

Easer is also a coordinator of inter-app actions (e.g. communications) -- it can send custom `Broadcast`s upon receiving certain `Broadcast`s (designed by YOU).

`Broadcast` (together with `Intent`) is the way Android provides for inter-app communication and signaling.

### Custom Events

You can combine Scripts (which specifies *Events* or *Condition* as well as its *Profile*) using arbitrary logic operators by placing them in a logic graph. This mechanism allows Easer to create custom *Events* using Boolean logic (e.g. "and", "or").

When a node in the *Script* graph changes its state, Easer will take corresponding actions (e.g. when an Event happens, Easer will load its corresponding *Profile* and listen to children nodes).

### Passing content from Event

By the *Dynamics* mechanism (a mechanism like [macro](https://en.wikipedia.org/wiki/Macro_\(computer_science\))), Easer can pass specific parts (contents) of Events to Profiles (and their corresponding Operations). This makes the text content in a *Profile* not limited to static contents, but can change dynamically.

Thanks to the good decoupling, when using this feature, user need to specify *placeholder* in *Profile*, and link to *Dynamics* in the *Script*.


Also, have a look at the [wiki](https://github.com/renyuneyun/Easer/wiki), and especially the [FAQ](https://github.com/renyuneyun/Easer/wiki/FAQ).


Examples
------
* Set your phone to silent mode at 2 a.m.
* Cancel silent mode at 8 a.m. on weekdays, and at 10 a.m. on weekends
* Turn on WiFi when approaching your home; turn off WiFi when leaving your home

Supported functions
--------
Easer supports listening to many Android events (e.g. date/time, system status, calendar). The supported operations include, but are not limited to, changing Android settings, sending messages, and executing commands.
Easer is gradually supporting *Remote Skill* (*Plugin*)*; currently only *Remote Operation Plugin*.

For a list of current features, see [this page](https://renyuneyun.github.io/Easer/en/FEATURES).
For an (incomplete) list of *Plugins*, see [here](https://github.com/topics/easer-plugin).

Support Easer
------
### Raising issues or participating discussion / development
If you encounter any problem when using Easer, please [raise an issue](https://github.com/renyuneyun/Easer/issues/new).

If you'd like to engage more (e.g. join discussion, extend Easer, join development), you can find more information on the [README in the repo](https://github.com/renyuneyun/Easer/blob/master/README.en.md).

### Donation

If you would like to make a donation, please see [DONATE.md](DONATE.md).

Any amount of help is appreciated.

Proud to be open-source
------
Copyright (c) 2016 - 2019 Rui Zhao (renyuneyun) <renyuneyun@gmail.com>

Unless otherwise stated, Easer is licensed under GPLv3+ (See LICENSE)

### Why GPL?

The expected functions of Easer require access to personal information (e.g. location, calendar) and networking capabilities. We would never want a tool that is expected to better facilitate our lives to spy on us, so we must prevent that from happening as best as we can. The only way to do this is to allow anyone to inspect every part of Easer, which is to say that Easer (and any derived works) must be made open source.
Because of the design of Easer, functionality will eventually become modules / plugins. The GPL requires that derived works also be licensed under the GPL, and thus prevents malicious code from sneaking into these parts.

In fact, ensuring that derived works / plugins are licensed under the **GPL** is unnecessary -- they only need to be **open source**. However, GPL is the only license (that I know of) which requires that derived works / plugins are open sourced, so it's the only choice.

More information on license
-----
See [README in the repo](https://github.com/renyuneyun/Easer/blob/master/README.en.md).
