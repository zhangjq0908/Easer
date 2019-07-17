[<img src="https://f-droid.org/badge/get-it-on-zh-cn.png"
      alt="在F-Droid下載"
      height="80">](https://f-droid.org/app/ryey.easer)<img align="right" src='https://github.com/renyuneyun/Easer/raw/master/app/src/main/ic_launcher-web.png' width='128' height='128'/>

簡介
-----
讓你的智能手機更加智能：告訴它該在什麼情況下做什麼事。

### 智能，源自用戶定義與顯式自動化

Easer提供顯式的事件驅動的自動化——用戶告訴它在什麼情況下做什麼事（或連結多個事件來自定義事件），此後再也不需要手動進行這些例程，也不必擔心忘記進行。

更妙的是，Easer不僅僅處理*事件*，也可檢查*狀態*，這使得一些不易用事件描述的狀況變得簡單且直覺化。

不僅如此，Easer還支持傳遞事件的指定消息至用戶所設置的動作中。這樣，用戶動作中的文本不再侷限於靜態的內容。

Easer類似於一個本地版的IFTTT：在不同的事件（Event）下執行指定的行爲集（Profile）。每個行爲集（Profile）由多個動作（Operation）組成，包括但不限於調整手機設置（見下）。

### app合作協調

Easer也是app合作的協調者——自定義在收到特定廣播（Broadcast）時，自動發出另外的自定義廣播。

廣播（Brodcast及Intent）是Android系統提供的app間通用的交流、通信機制。Android沒有對互操作進行任何的梳理，那麼Easer就此進行嘗試。

### 自定義事件

Easer可對*用戶腳本*（Script，包含Event或Condition及對應載入的Profile）設置依賴（前置條件），使得他們以樹狀鏈接。這一機制使得Easer的事件初步具備使用布爾邏輯（與、或）相鏈接的能力，以便實現自定義事件。

Easer在逐步支持*狀況*（Condition）機制，且在逐漸將相關*事件*遷移至*狀況*。

當*用戶腳本樹*中的節點狀態產生變化時，Easer會對其採取相應措施（如當某*事件*發生時，Easer會載入相應的*行爲集*）。

### 傳遞事件消息

通過*Dynamics*機制（一種類似於[宏](https://en.wikipedia.org/wiki/Macro_\(computer_science\))的機制），Easer可以將事件中的特定部分傳遞至*Profile*（及其*Operation*）中。這樣，*Profile*中的文本部分不再受限於固定的內容，而可以動態變化。

由於設計上的良好解耦，使用該特性時，要在*Profile*中定義*佔位符*，之後在*用戶腳本*中**鏈接**對應*Dynamics*


另請參見[wiki](https://github.com/renyuneyun/Easer/wiki)，尤其是[須知](https://github.com/renyuneyun/Easer/wiki/%E9%A0%88%E7%9F%A5)。

例子
-----
* 每天晚上2點關閉WiFi並且靜音
* 工作日內早上8點打開通知音，週末早上10點打開通知音
* 到家附近自動打開WiFi；離開家自動關閉WiFi

已支持功能
----------
Easer支持監聽許多Android事件（如時間、系統狀態、日曆等），所支持動作包括但不限於調整Android設置、發送消息、執行命令等。
Easer現已在逐步支持（遠程）插件，不過暫時只有*行爲*。

當前所支持功能的列表見[這個頁面](FEATURES.md)，頁面也包含對於Dynamics的介紹。
[這裏](https://github.com/topics/easer-plugin)是一個（不完整的）（遠程）插件列表。

支持Easer
-----
### 提意見或參與開發
如在使用中發現什麼問題，請[提出一個issue](https://github.com/renyuneyun/Easer/issues/new)。

如果想更多參與，請見[倉庫的README](https://github.com/renyuneyun/Easer/blob/master/README.md)。

### 捐助

如果您願意給Easer的開發提供任何額度的捐助，請參看[DONATE.md](DONATE.md)。

感謝任何額度的支持。

我開源我自豪
-----
Copyright (c) 2016 - 2019 Rui Zhao (renyuneyun) <renyuneyun@gmail.com>

除非額外聲明，Easer以GPLv3+協議分發（參見LICENSE）

### 爲何使用GPL？

Easer的期望功能中包含大量對隱私信息（比如位置信息、日曆信息）的捕捉，而且擁有對網絡的訪問能力。我們永遠不會希望一個本該方便生活的工具變成對自己生活的監視工具，所以必須儘可能保證該腐化不會發生。其唯一方法就是確保Easer的各個部分都允許任何人覈查，即保證Easer（及如果存在的衍生軟件）爲開源軟件。  
而由於設計中Easer的各個功能（在將來）會變爲模塊/插件，因而也需要保證這些模塊/插件不會成爲潛在的黑手，故而希望依靠GPL的特性（鏈接軟件也需要爲GPL）來強制這一點。

事實上，強制各衍生/擴展品爲**GPL**並不必要，因爲只需要它們**開源**即可。但GPL是（我）目前所知的唯一可以保證衍生/擴展品爲開源軟件的協議，所以選擇它。

更多許可證信息
-----
參見[倉庫的README](https://github.com/renyuneyun/Easer/blob/master/README.md)。
