HOWTO
======

This document will describe how to add new functionality to Easer, especially how to add new *Event*(s) (and Condition(s)) and/or *Operation*(s). It may also say something about the design of the structure of Easer.  
(**Notice**: *Profile* is merely a collection of *Operation*(s), so if you want to extend the function of *Profile*, you just need to create new *Operation*(s).)

Add new Event/Operation
------

In Easer, all *Event*s and *Operation*s are abstracted as skills, located in the package `skills`. More precisely, *Event*s are under `skills.event`，*Operation*s are under `skills.operation`.

You can use the provided scripts in the "utils" directory to automatically generate the skeleton of a skill; or creating everything manually.

### Use the script
Under "utils" directory, two scripts are provided (`new_event.py` and `new_operation.py`) to automatically generate the skeleton of a new Event/Operation skill, as well as other related resources (e.g. tests).
After executing the script, you only need to fill in each of the "TODO"s in the directory for the new skill. Then, after registering in `LocalSkillRegistry` (see below), the new skill will appear in Easer.

Detailed explaination of each component are presented below.

### Detailed steps

In order to add a new *Event* or *Operation*, there are two main steps:

1. Implement the functions of your new *Event* or *Operation* (by inheriting corresponding interfaces and implementing relevant contents)
2. Register your new skill in `skills.LocalSkillRegistry`

Specifically:

* For new *Event*s, inherit `local_skill.eventskill.EventSkill`
* For new *Operation*s, inherit `local_skill.operationskill.OperationSkill`

To register you new skill in `LocalSkillRegistry`, you only need to add one line new code in its `init()` method (following existing codes).

Strongly recommend to follow the way that existing skills do and put your new skill into the relevant package.

Both `EventSkill` and `OperationSkill` are commented in some detail.

Either an `EventSkill` or `OperationSkill` will need to implement it relevant UI, by implementing a subclass of `skills.SkillViewFragment`.

Some common subclasses are already in `skills` or its sub-package. You can use them as you wish.

#### Usage of related interfaces / abstract classes

`Skill`, `StorageData` and `DataFactory` are the general interfaces, and only their sub-interfaces (e.g. `EventSkill`) will be used. Because each component of a skill will use its data, the data class (i.e. its subclass of [the sub-interface of] `StorageData`) will be used as the generics parameter.

* `Skill`/`EventSkill`/`OperationSkill`
	* Entrance of a skill. The use of a skill starts with the calling of methods in the entrance.
* `StorageData`/`EventData`/`OperationData`
	* Data of a skill. Every instance is "fixed" after creation; modification of data creates new instances.
	* Generally, the internal value (e.g. fields of the class) will only be used inside the skill. Therefore, there is no access interfaces to the outside.
	* The data will be persisted (e.g. saving to a file), so there is `serialize()` interface defined; see `DataFactory` for the `parse()` interface.
	* Data will be passed across Android components, so `Parcelable` interface is implemented.
* `DataFactory`/`EventDataFactory`/`OperationDataFactory`
	* Data during runtime is created from here.
	* The `parse()` interface is used to create instance of corresponding `StorageData` from previous persisted data.
* `SkillViewFragment`
	* The UI part when editing the skill, used to interact with the user.
	* When "loading", if there is initial data, UI will be initialized with this data.
	* When "saving", corresponding `StorageData` will be created for future process (e.g. persist).
* `Slot`/`AbstractSlot`
	* Used in `EventSkill`, for monitoring (listenting to) events and notify state change.
	* Generally, monitoring is done by registering `BroadcastListener`.
	* When satisfied, call `changeSatisfiedState()` method to notify (relevant Easer components) about the state change.
	* When Easer is running, every Event in the EventTree will has it own `Slot` created (this behaviour is to be optimized).
* `OperationLoader`
	* The place where an Operation is carried out / loaded.

