[<img src="https://f-droid.org/badge/get-it-on.png"
      alt="Get it on F-Droid"
      height="80">](https://f-droid.org/app/ryey.easer)<img align="right" src='https://github.com/renyuneyun/Easer/raw/master/app/src/main/ic_launcher-web.png' width='128' height='128'/>

Ease your life by automatically performing routine actions.

Introduction
-----
Make your smart phone smarter: tell it what to do under different situations.

### Smart automation

Easer is an event-driven Android automation tool. It knows various events (see below) and YOU (the user) tell it what to do on what events (you can even combine multiple events). Thus, you no longer need to manually perform routine actions, or worry about forgetting to perform them.

You can think of Easer as a local version of IFTTT: trigger actions or change settings (*Operations*, bundled as *Profiles*) under different situations (*Events*).

### Inter-app coordination

Easer is also a coordinator of inter-app actions (e.g. communications) -- it can send custom `Broadcast`s upon receiving certain `Broadcast`s (designed by YOU).

`Broadcast` (together with `Intent`) is the way Android provides for inter-app communication and signaling.

### Custom Events

You can chain *Script* (which specifies *Events* as well as its *Profile*) as trees (i.e. setting dependencies), by specifying its *Parent*. This mechanism allows Easer to (somewhat) create custom *Events* using Boolean logic (e.g. "and", "or").

Easer is adding the support of *Condition* mechanism, and is transforming relevant *Event* to *Condition*, for better expressivity.

Currently, Easer performs a post-order traversal to load your *Profiles*. In the near future, Easer will have more meticulous, expressive and intuitive categorization of *Events*.

Also, have a look at the [wiki](https://github.com/renyuneyun/Easer/wiki), and especially the [FAQ](https://github.com/renyuneyun/Easer/wiki/FAQ).

Examples
------
* Set your phone to silent mode at 2 a.m.
* Cancel silent mode at 8 a.m. on weekdays, and at 10 a.m. on weekends
* Turn on WiFi when approaching your home; turn off WiFi when leaving your home

Supported functions
--------
Easer supports listening to many Android events (e.g. date/time, system status, calendar). The supported operations include, but are not limited to, changing Android settings, sending messages, and executing commands.

For a list of current features, see [this page](https://renyuneyun.github.io/Easer/en/FEATURES).

Extending Easer
------
Extending the functionality of Easer (by adding more *Events* or *Operations*) is really simple (and is becoming simpler).

Details are described in [this document](EXTEND.md).

Support Easer
------
### Raising issues, commenting on issues and solving issues
If you encounter problems when using Easer, you can submit an issue. The more detail you can provide, the better -- it will let the issue be pinned down faster.
You can also open an issue if you think there are features that Easer should have.

You are also welcome to comment on existing issues. If you believe you have the same problem (or idea), you can provide more information about it. Discussion is always welcome.
Issue expecting ideas are labeled with [RFC](https://github.com/renyuneyun/Easer/issues?q=is%3Aopen+label%3A%22RFC+%2F+Discussion+Wanted%22).

If you are a developer, you may possess the knowledge and time to solve some issues. You can fork the repo, solve the problem, and create a pull request. Then, your code can be merged, and you can be appreciated by others and listed in the *Contributors* list.
Don't know where to start? See [these issues labeled with "help wanted"](https://github.com/renyuneyun/Easer/issues?q=is%3Aopen+label%3A%22help+wanted%22) which usually have clear target and involve few components.
You're also welcome to create pull requests for issues not raised by others, but first, please create an issue describing what you want to do (and that you are going to do it).

### Donation

If you would like to make a donation, please see [DONATE.md](DONATE.md).

Any amount of help is appreciated.

Copyright
------
Copyright (c) 2016 - 2018 Rui Zhao (renyuneyun) <renyuneyun@gmail.com>

Licensed under GPLv3+ (See LICENSE)

### Why GPL?

The expected functions of Easer require access to personal information (e.g. location, calendar) and networking capabilities. We would never want a tool that is expected to better facilitate our lives to spy on us, so we must prevent that from happening as best as we can. The only way to do this is to allow anyone to inspect every part of Easer, which is to say that Easer (and any derived works) must be made open source.
Because of the design of Easer, functionality will eventually become modules / plugins. The GPL requires that derived works also be licensed under the GPL, and thus prevents malicious code from sneaking into these parts.

In fact, ensuring that derived works / plugins are licensed under the **GPL** is unnecessary -- they only need to be **open source**. However, GPL is the only license (that I know of) which requires that derived works / plugins are open sourced, so it's the only choice.

Third-party libraries
-----
* [Logger](https://github.com/orhanobut/logger): Apache License v2
* [android-flowlayout](https://github.com/ApmeM/android-flowlayout): Apache License v2
* [Guava](https://github.com/google/guava): Apache License v2
