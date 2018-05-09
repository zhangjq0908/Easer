[<img src="https://f-droid.org/badge/get-it-on-zh-cn.png"
      alt="在F-Droid下載"
      height="80">](https://f-droid.org/app/ryey.easer)<img align="right" src='https://github.com/renyuneyun/Easer/raw/master/app/src/main/ic_launcher-web.png' width='128' height='128'/>

簡介
-----
讓你的智能手機更加智能：告訴它該在什麼情況下做什麼事。

### 智能自動化
Easer是一個事件驅動的Android自動化工具——用戶告訴它在什麼情況下做什麼事（或連結多個事件來自定義事件），此後再也不需要手動進行這些例程，也不必擔心忘記進行。

Easer類似於一個本地版的IFTTT：在不同的事件（Event）下執行指定的行爲集（Profile）。每個行爲集（Profile）由多個動作（Operation）組成，包括但不限於調整手機設置（見下）。

### app合作協調

Easer也是app合作的協調者——自定義在收到特定廣播（Broadcast）時，自動發出另外的自定義廣播。

廣播（Brodcast及Intent）是Android系統提供的app間通用的交流、通信機制。Android沒有對互操作進行任何的梳理，那麼Easer就此進行嘗試。

### 自定義事件

Easer可對事件腳本（Script，包含Event及對應載入的Profile）設置依賴（前置條件），使得他們以樹狀鏈接。這一機制使得Easer的事件初步具備使用布爾邏輯（與、或）相鏈接的能力，以便實現自定義事件。

Easer在逐步支持*狀況*（Condition）機制，且在逐漸將相關*事件*遷移至*狀況*。

當前，Easer會以後續遍歷選擇所有符合條件的Script對應Profile載入。但在不久的將來，Easer將會擁有更細緻且更直觀的對事件狀況的分類，使得用戶擁有對事件更佳的把握。

另請參見[wiki](https://github.com/renyuneyun/Easer/wiki)，尤其是[須知](https://github.com/renyuneyun/Easer/wiki/%E9%A0%88%E7%9F%A5)。

例子
-----
* 每天晚上2點關閉WiFi並且靜音
* 工作日內早上8點打開通知音，週末早上10點打開通知音
* 到家附近自動打開WiFi；離開家自動關閉WiFi

已支持功能
----------
Easer支持監聽許多Android事件（如時間、系統狀態、日曆等），所支持動作包括但不限於調整Android設置、發送消息、執行命令等。

當前所支持功能的列表見[這個頁面](FEATURES.md)。

擴展Easer
-----
擴展Easer的功能非常簡單（並且在持續變得更簡單），只需要添加自己的Event或Operation即可。

詳細請參見[這個頁面](EXTEND.md)。

支持Easer
-----
### 提出、評論以及解決issue
如果在使用Easer時發現了什麼問題，你可以[提出一個issue](https://github.com/renyuneyun/Easer/issues/new)。在可行的情況下（當然，這不是強制的），相關信息給得越多越好，以便更早定位問題所在。  
如果你覺得Easer應當添加某些功能，你也可以提出一個issue。

對於現有的issue，無論你有相同的問題（或想法）、你可以提供更多信息、或是你不認同其中的說法，請對其進行評論。十分歡迎對問題進行討論。
其中較需要多方意見的issue被[標爲RFC](https://github.com/renyuneyun/Easer/issues?q=is%3Aopen+label%3A%22RFC+%2F+Discussion+Wanted%22)，歡迎任何人的意見/建議。

在某些情況下（如果你是一個開發者），你也許具有解決某些issue的能力。你可以fork本倉庫，編寫代碼，然後創建pull request。這樣，如果你的代碼的確解決了其問題，則你的代碼會進入主幹，並且你會得到其他人的感謝（而且會被列在*Contributors*列表中）。  
如果有興趣參與但不知道從何下手，可以查看這些被[標爲help wanted的issue](https://github.com/renyuneyun/Easer/issues?q=is%3Aopen+label%3A%22help+wanted%22)——一般而言，它們都是目的較爲清晰、涉及組件較少的issue。
同樣歡迎對非現有issue創建pull request，但建議首先創建一個issue來描述你將要進行的工作（使得其他人意識到此事）。

### 捐助

如果您願意給Easer的開發提供任何額度的捐助，請參看[DONATE.md](DONATE.md)。

感謝任何額度的支持。

版權協議
-----
Copyright (c) 2016 - 2018 Rui Zhao (renyuneyun) <renyuneyun@gmail.com>

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
