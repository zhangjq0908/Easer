Currently, all data of Easer are stored as files. JSON is the current recommended version; the old XML format has been removed.

This document describes the format of the file content, to use as a reference.

## Version 11
Introduce *Dynamics*. *Script* has a new entry.

### Script
```JSON
{
	THE SAME AS PREVIOUS
	"dynamics": {
		PLACEHOLDER 1: PROPERTY 1,
		PLACEHOLDER 2: PROPERTY 2,
		...
	}
}
```

## Version 10
Rename *Scenario* to *Event*. The relevant item in *Script* is affected.

### Script with pre-defined Event
```JSON
{
	"name":NAME,
	"version":VERSION OF DATA,
	"active":true or false,
	"profile":PROFILE NAME,
	"after":PARENT EVENT NAME,
	"trigger":{
		"type":"pre_defined",
		"event":SCENARIO NAME
	},
	"reverse":true or false,
	"repeatable":true or false,
	"persistent":true or false
}
```

## Version 9
No format change. Ignored.

## Version 8
Add *Condition* (as the complementary of *Event*). Before this version, the UI component and data directory named "event" has been renamed to "script", but the concept of *Event* doesn't change (see v0.5.8).

### Condition
```JSON
{
	"name":NAME,
	"version":VERSION OF DATA,
	"condition":{
		"spec":ID OF THE CORRESPONDING CONDITION,
		"data":DATA OF THE CORRESPONDING CONDITION
	}
}
```

### Script with Condition
```JSON
{
	"name":NAME,
	"version":VERSION OF DATA,
	"active":true or false,
	"profile":PROFILE NAME,
	"after":PARENT SCRIPT NAME,
	"trigger":{
		"type":"condition",
		"condition":CONDITION NAME
	},
	"reverse":true or false
}
```

## Version 7
Remove `EventType`.
Contents are almost the same as the previous version, with the `trigger/logic` (`EventType`) part removed.

### Event with inline Scenario
```JSON
{
	"name":NAME,
	"version":VERSION OF DATA,
	"active":true or false,
	"profile":PROFILE NAME,
	"after":PARENT EVENT NAME,
	"trigger":{
		"type":"raw_event",
		"situation":{
			"spec":ID OF CORRESPONDING EventPlugin,
			"data":DATA FROM CORRESPONDING EventPlugin
		}
	}
}
```

### Scenario
```JSON
{
	"name":NAME,
	"version":VERSION OF DATA,
	"situation":{
		"spec":ID OF CORRESPONDING EventPlugin,
		"data":DATA FROM CORRESPONDING EventPlugin
	}
}

```

## 版本6
No format change; won't list.

## Version 5
This version slightly modified version 4.

The only affected thing is *Events using inline Scenario*, and we are listing it here only.

### Event with inline Scenario
```JSON
{
	"name":NAME,
	"version":VERSION OF DATA,
	"active":true or false,
	"profile":PROFILE NAME,
	"after":PARENT EVENT NAME,
	"trigger":{
		"type":"raw_event",
		"logic":"after" or "any" or "before" or "is" or "is_not" or "none",
		"situation":{
			"spec":ID OF THE CORRESPONDING EventPlugin,
			"data":DATA FROM THE CORRESPONDING EventPlugin
		}
	}
}
```

## Version 4
This version introduces *Scenario* for the first time.  
The earlier *Event*s are now treated as "Events using inline Scenario".
### Profile
```JSON
{
	"name":NAME,
	"version":VERSION OF DATA,
	"operation":[
		{
			"spec":ID OF THE CORRESPONDING OperationPlugin,
			"data":DATA FROM THE CORRESPONDING OperationPlugin
		},
		...
	]
}
```

### Event
#### Use inline Scenario
```JSON
{
	"name":NAME,
	"version":VERSION OF DATA,
	"active":true or false,
	"profile":PROFILE NAME,
	"after":PARENT EVENT NAME,
	"trigger":{
		"type":"raw_event",
		"logic":"after" or "any" or "before" or "is" or "is_not" or "none",
		"situation":{
			"spec":ID OF THE CORRESPONDING EventPlugin,
			"data":DATA FROM THE CORRESPONDING EventPlugin
		}
	},
	"reverse":true or false,
	"repeatable":true or false,
	"persistent":true or false
}
```
#### Use pre-defined Scenario
```JSON
{
	"name":NAME,
	"version":VERSION OF DATA,
	"active":true or false,
	"profile":PROFILE NAME,
	"after":PARENT EVENT NAME,
	"trigger":{
		"type":"pre_defined",
		"scenario":SCENARIO NAME
	},
	"reverse":true or false,
	"repeatable":true or false,
	"persistent":true or false
}
```
### Scenario
```JSON
{
	"name":NAME,
	"version":VERSION OF DATA,
	"logic":"after" or "any" or "before" or "is" or "is_not" or "none",
	"situation":{
		"spec":ID OF THE CORRESPONDING EventPlugin,
		"data":DATA FROM THE CORRESPONDING EventPlugin
	}
}

```
