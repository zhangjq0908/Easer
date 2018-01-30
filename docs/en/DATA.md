Currently, all data of Easer are stored as files. JSON is the current recommended version; the old XML format will be removed in near future.

This document describes the format of the file content, to use as a reference.

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
