Easer [![Build Status](https://travis-ci.org/renyuneyun/Easer.svg?branch=master)](https://travis-ci.org/renyuneyun/Easer)
=======
[<img src="https://f-droid.org/badge/get-it-on-zh-cn.png"
      alt="Get it on F-Droid"
      height="60">](https://f-droid.org/app/ryey.easer)

See [README.en.md](README.en.md) for the English version of README.

簡介
-----
讓你的智能手機更加智能：告訴它該在什麼情況下做什麼事。

Easer是一個事件驅動的Android自動化工具。用戶告訴它在什麼情況下做什麼事（或連結多個事件來自定義事件），此後再也不需要手動進行這些例程，也不必擔心忘記進行。

Easer類似於一個本地版的IFTTT：在不同的狀況（Event）下載入指定的情境（Profile）。每個情境（Profile）由多個動作（Operation）組成，包括但不限於調整手機設置（見下）。

Event可設置依賴（前置條件），使得他們以樹狀鏈接。Easer會以後續遍歷選擇所有符合條件的Event對應Profile載入。

另請參見[wiki](https://github.com/renyuneyun/Easer/wiki)，尤其是[須知](https://github.com/renyuneyun/Easer/wiki/%E9%A0%88%E7%9F%A5)。

例子
-----
* 每天晚上2點關閉WiFi並且靜音
* 工作日內早上8點打開通知音，週末早上10點打開通知音
* 到家附近自動打開WiFi；離開家自動關閉WiFi

已支持功能
----------
Easer支持監聽許多Android事件（如時間、系統狀態、日曆等），所支持動作包括但不限於調整Android設置、發送消息、執行命令等。

當前所支持功能的列表見[這個頁面](https://renyuneyun.github.io/Easer/zh/FEATURES)。

擴展Easer
-----
擴展Easer的功能非常簡單（並且在持續變得更簡單），只需要添加自己的Event或Operation即可。

詳細請參見[這個頁面](https://renyuneyun.github.io/Easer/zh/EXTEND)。


支持Easer
-----
### 提出、評論以及解決issue
如果在使用Easer時發現了什麼問題，你可以[提出一個issue](https://github.com/renyuneyun/Easer/issues/new)。在可行的情況下（當然，這不是強制的），相關信息給得越多越好，以便更早定位問題所在。  
如果你覺得Easer應當添加某些功能，你也可以提出一個issue。

對於現有的issue，無論你有相同的問題（或想法）、你可以提供更多信息、或是你不認同其中的說法，請對其進行評論。十分歡迎對問題進行討論。

在某些情況下（如果你是一個開發者），你也許具有解決某些issue的能力。你可以fork本倉庫，編寫代碼，然後創建pull request。這樣，如果你的代碼的確解決了其問題，則你的代碼會進入主幹，並且你會得到其他人的感謝（而且會被列在*Contributors*列表中）。  
同樣歡迎對非現有issue創建pull request，但建議首先創建一個issue來描述你將要進行的工作（使得其他人意識到此事）。

### 捐助
如果您願意給Easer的開發提供任何額度的捐助，請參看[DONATE.md](https://renyuneyun.github.io/Easer/zh/DONATE)。

版權協議
-----
Copyright (c) 2016 - 2017 Rui Zhao (renyuneyun) <renyuneyun@gmail.com>

以GPLv3+協議分發（參見LICENSE）

### 爲何使用GPL？

Easer的期望功能中包含大量對隱私信息（比如位置信息、日曆信息）的捕捉，而且擁有對網絡的訪問能力。我們永遠不會希望一個本該方便生活的工具變成對自己生活的監視工具，所以必須儘可能保證該腐化不會發生。其唯一方法就是確保Easer的各個部分都允許任何人覈查，即保證Easer（及如果存在的衍生軟件）爲開源軟件。  
而由於設計中Easer的各個功能（在將來）會變爲模塊/插件，因而也需要保證這些模塊/插件不會成爲潛在的黑手，故而希望依靠GPL的特性（鏈接軟件也需要爲GPL）來強制這一點。

事實上，強制各衍生/擴展品爲**GPL**並不必要，因爲只需要它們**開源**即可。但GPL是（我）目前所知的唯一可以保證衍生/擴展品爲開源軟件的協議，所以選擇它。

第三方庫
-----
* [Logger](https://github.com/orhanobut/logger): Apache License v2
* [android-flowlayout](https://github.com/ApmeM/android-flowlayout): Apache License v2
* [Guava](https://github.com/google/guava): Apache License v2
