HOWTO
======

This document will describe how to add new functionality to Easer, especially how to add new *Event*(s) and/or *Operation*(s). It may also say something about the design of the structure of Easer.  
(**Notice**: *Profile* is merely a collection of *Operation*(s), so if you want to extend the function of *Profile*, you just need to create new *Operation*(s).)

In Easer, all *Event*s and *Operation*s are implemented as plugins, located in the package `ryey.easer.plugins`. More precisely, *Event*s are under `ryey.easer.plugins.event`，*Operation*s are under `ryey.easer.plugins.operation`。

In order to add a new *Event* or *Operation*, there are only two steps:

1. Implement the functions of your new *Event* or *Operation* (by inheriting corresponding interfaces and implementing relevant contents)
2. Register your new plugin in `ryey.easer.plugins.PluginRegistry`

Specifically:

* For new *Event*s, inherit `ryey.easer.commons.plugindef.eventplugin.EventPlugin`
* For new *Operation*s, inherit `ryey.easer.commons.plugindef.operationplugin.OperationPlugin`

To register you new plugin in `PluginRegistry`, you only need to add one line new code in its `init()` method (following existing codes).

I strongly recommend you to follow the way that existing plugins do and put your new plugin into the relevant package.

Both `EventPlugin` and `OperationPlugin` are commented in some detail.

Either an `EventPlugin` or `OperationPlugin` will need to implement it relevant UI, by implementing a subclass of `ryey.easer.commons.plugindef.PluginViewFragment`.

Some common subclasses are already in `ryey.easer.plugins` or its sub-package. You can use them as you wish.
