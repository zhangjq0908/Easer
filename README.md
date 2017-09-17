Easer
=======
[<img src="https://f-droid.org/badge/get-it-on.png"
      alt="Get it on F-Droid"
      height="80">](https://f-droid.org/app/ryey.easer)

See [README.en.md](README.en.md) for the English version of README.

簡介
-----
讓你的智能手機更加智能：告訴它該在什麼情況下做什麼事。

Easer類似於一個本地版的IFTTT：在不同的狀況（Event）下載入指定的情境（Profile）。每個情境（Profile）由多個動作（Operation）組成，包括但不限於調整手機設置（見下）。

Event可設置依賴（前置條件），使得他們以樹狀鏈接。Easer會以後續遍歷選擇所有符合條件的Event對應Profile載入。

如有興趣參與開發/增加Event/增加Operation/優化UI，請看[HOWTO](HOWTO)和[TODO](TODO)。

另請參見[wiki](https://github.com/renyuneyun/Easer/wiki)，尤其是[須知](https://github.com/renyuneyun/Easer/wiki/%E9%A0%88%E7%9F%A5)。

例子
-----
* 每天晚上2點關閉WiFi並且靜音
* 工作日內早上8點打開通知音，週末早上10點打開通知音
* 到家附近自動打開WiFi；離開家自動關閉WiFi

已支持功能
----------
### Event
* 日期
* 時間
* WiFi連接名稱
* 位置（基於基站）
* 電池狀態
* 星期幾
* 所連接藍牙設備

### Operation
* WiFi開關
* 數據網絡開關
* 藍牙開關
* 自動旋轉開關
* 發送Broadcast
* 控制亮度
* 控制聲音模式
* 執行命令

版權協議
-----
Copyright (c) 2016 - 2017 Rui Zhao (renyuneyun) <renyuneyun@gmail.com>

以GPLv3+協議分發（參見LICENSE）

###爲何使用GPL？

Easer的期望功能中包含大量對隱私信息（比如位置信息、日曆信息）的捕捉，而且擁有對網絡的訪問能力。我們永遠不會希望一個本該方便生活的工具變成對自己生活的監視工具，所以必須儘可能保證該腐化不會發生。其唯一方法就是確保Easer的各個部分都允許任何人覈查，即保證Easer（及如果存在的衍生軟件）爲開源軟件。  
而由於設計中Easer的各個功能（在將來）會變爲模塊/插件，因而也需要保證這些模塊/插件不會成爲潛在的黑手，故而希望依靠GPL的特性（鏈接軟件也需要爲GPL）來強制這一點。

事實上，強制各衍生/擴展品爲**GPL**並不必要，因爲只需要它們**開源**即可。但GPL是（我）目前所知的唯一可以保證衍生/擴展品爲開源軟件的協議，所以選擇它。

第三方庫
-----
[Logger](https://github.com/orhanobut/logger): Apache License v2
