HOWTO
======

This document will describe how to add new functionality to Easer, especially how to add new *Event*(s) and/or *Operation*(s). It may also say something about the design of the structure of Easer.  
(**Notice**: *Profile* is merely a collection of *Operation*(s), so if you want to extend the function of *Profile*, you just need to create new *Operation*(s).)

Add new Event/Operation
------

In Easer, all *Event*s and *Operation*s are implemented as plugins, located in the package `ryey.easer.plugins`. More precisely, *Event*s are under `ryey.easer.plugins.event`，*Operation*s are under `ryey.easer.plugins.operation`.

You can use the provided scripts in the "utils" directory to automatically generate the skeleton of plugin; or creating everything manually.

### Use the script
Under "utils" directory, two scripts are provided (`new_event.py` and `new_operation.py`) to automatically generate the skeleton of a new Event/Operation plugin, as well as other related resources (e.g. tests).
After executing the script, you only need to fill in each of the "TODO"s in the directory for the new plugin. Then, after registering in `PluginRegistry` (see below), the new plugin will appear in Easer.

Detailed explaination of each component are presented below.

### Detailed steps

In order to add a new *Event* or *Operation*, there are two main steps:

1. Implement the functions of your new *Event* or *Operation* (by inheriting corresponding interfaces and implementing relevant contents)
2. Register your new plugin in `ryey.easer.plugins.LocalPluginRegistry`

Specifically:

* For new *Event*s, inherit `ryey.easer.commons.local_plugin.eventplugin.EventPlugin`
* For new *Operation*s, inherit `ryey.easer.commons.local_plugin.operationplugin.OperationPlugin`

To register you new plugin in `PluginRegistry`, you only need to add one line new code in its `init()` method (following existing codes).

I strongly recommend you to follow the way that existing plugins do and put your new plugin into the relevant package.

Both `EventPlugin` and `OperationPlugin` are commented in some detail.

Either an `EventPlugin` or `OperationPlugin` will need to implement it relevant UI, by implementing a subclass of `ryey.easer.plugins.PluginViewFragment`.

Some common subclasses are already in `ryey.easer.plugins` or its sub-package. You can use them as you wish.

#### Usage of related interfaces / abstract classes

`PluginDef`, `StorageData` and `DataFactory` are the general interfaces, and only their sub-interfaces (e.g. `EventPlugin`) will be used. Because each component of a plugin will use its data, the data class (i.e. its subclass of [the sub-interface of] `StorageData`) will be used as the generics parameter.

* `PluginDef`/`EventPlugin`/`OperationPlugin`
	* Entrance of a plugin. The use of a plugin starts with the calling of methods in the entrance.
* `StorageData`/`EventData`/`OperationData`
	* Data of a plugin. Every instance is "fixed" after creation; modification of data creates new instances.
	* Generally, the internal value (e.g. fields of the class) will only be used inside the plugin. Therefore, there is no access interfaces to the outside.
	* The data will be persisted (e.g. saving to a file), so there is `serialize()` interface defined; see `DataFactory` for the `parse()` interface.
	* Data will be passed across Android components, so `Parcelable` interface is implemented.
* `DataFactory`/`EventDataFactory`/`OperationDataFactory`
	* Data during runtime is created from here.
	* The `parse()` interface is used to create instance of corresponding `StorageData` from previous persisted data.
* `PluginViewFragment`
	* The UI part of the plugin, used to interact with the user.
	* When "loading", if there is initial data, UI will be initialized with this data.
	* When "saving", corresponding `StorageData` will be created for future process (e.g. persist).
* `Slot`/`AbstractSlot`
	* Used in `EventPlugin`, for monitoring (listenting to) events and notify state change.
	* Generally, monitoring is done by registering `BroadcastListener`.
	* When satisfied, call `changeSatisfiedState()` method to notify (relevant Easer components) about the state change.
	* When Easer is running, every Event in the EventTree will has it own `Slot` created (this behaviour is to be optimized).
* `OperationLoader`
	* The place where an Operation is carried out / loaded.

