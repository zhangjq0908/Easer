指南
======

本指南將描述如何爲Easer添加新功能，如增加新的Event或Operation，同時也會簡單涉及Easer的設計結構。
（注意：Profile是一組Operation的集合，所以要擴展Profile只要創建相應的新Operation即可。）

在Easer中，所有的Event和Operation都被視爲插件(plugin)，均放置於`ryey.easer.plugins`包下——更具體地，Event在`ryey.easer.plugins.event`包下，Operation在`ryey.easer.plugins.operation`包下。

要增加新的Event或Operation，只要做兩件事：

1. 實現新Event或Operation的功能（通過擴展/繼承相應接口，並實現相應內容）
2. 在`ryey.easer.plugins.PluginRegistry`中註冊該新插件

其中：

* 添加新Event需要繼承`ryey.easer.commons.plugindef.eventplugin.EventPlugin`
* 添加新Operation需要繼承`ryey.easer.commons.plugindef.operationplugin.OperationPlugin`

而在`PluginRegistry`中註冊只需要在其`init()`方法中仿照已有條目，爲新插件寫一行代碼。

強烈建議仿照已有插件的做法，將新的插件放於相應的包/目錄內。

`EventPlugin`接口和`OperationPlugin`接口均有詳細註釋。

無論是`EventPlugin`還是`OperationPlugin`均需實現UI部分，均是通過實現一個`ryey.easer.commons.plugindef.ContentLayout`的子類來完成。一些常用的子類已在`ryey.easer.plugins`或子包中寫好，方便使用。