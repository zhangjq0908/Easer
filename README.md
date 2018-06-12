Easer [![Build Status](https://travis-ci.org/renyuneyun/Easer.svg?branch=master)](https://travis-ci.org/renyuneyun/Easer) [![codecov](https://codecov.io/gh/renyuneyun/Easer/branch/master/graph/badge.svg)](https://codecov.io/gh/renyuneyun/Easer) [![matrix-chat](https://matrix.to/img/matrix-badge.svg)](https://matrix.to/#/#Easer:matrix.org)
=======
[<img src="https://f-droid.org/badge/get-it-on-zh-cn.png"
      alt="Get it on F-Droid"
      height="60">](https://f-droid.org/app/ryey.easer)
<img align="right" src='./app/src/main/ic_launcher-web.png' width='128' height='128'/>

See [README.en.md](README.en.md) for the English version.

簡介
-----
本文主要描述Easer的開發相關事宜。

如要查看對Easer功能的介紹，請參見[網站](https://renyuneyun.github.io/Easer/)。

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
其中較需要多方意見的issue被[標爲RFC](https://github.com/renyuneyun/Easer/issues?q=is%3Aopen+label%3A%22RFC+%2F+Discussion+Wanted%22)，歡迎任何人的意見/建議。

如有興趣參與更多但不知道從何下手，可以查看被標爲GFC (Good For Contributors) [L0](https://github.com/renyuneyun/Easer/issues?q=is%3Aissue+is%3Aopen+label%3A%22GFC%3A+L0%22)、[L1](https://github.com/renyuneyun/Easer/issues?q=is%3Aissue+is%3Aopen+label%3A%22GFC%3A+L1%22)、[L2](https://github.com/renyuneyun/Easer/issues?q=is%3Aissue+is%3Aopen+label%3A%22GFC%3A+L2%22) 的issue。一般而言，它們都是目的較爲清晰、涉及組件較少的issue；L0、L1、L2是我個人主觀按難度遞增順序進行的劃分。

在某些情況下（如果你是一個開發者），你也許具有解決某些issue的能力。你可以fork本倉庫，編寫代碼，然後創建pull request。這樣，如果你的代碼的確解決了其問題，則你的代碼會進入主幹，他人均會受益於你的貢獻（並且你會被列在*Contributors*列表中）。  
同樣歡迎對非現有issue創建pull request，但建議首先創建一個issue來描述你將要進行的工作（使得其他人意識到此事）。

### 捐助

如果您願意給Easer的開發提供任何額度的捐助，請參看[DONATE.md](https://renyuneyun.github.io/Easer/zh/DONATE)。

感謝任何額度的支持。

版權協議
-----
Copyright (c) 2016 - 2018 Rui Zhao (renyuneyun) <renyuneyun@gmail.com>

除非額外說明，程序代碼以GPLv3+協議分發（參見LICENSE）

`utils/`目錄下工具以Apache 2.0協議分發（參見`utils/LICENSE`）

### 爲何使用GPL？

Easer的期望功能中包含大量對隱私信息（比如位置信息、日曆信息）的捕捉，而且擁有對網絡的訪問能力。我們永遠不會希望一個本該方便生活的工具變成對自己生活的監視工具，所以必須儘可能保證該腐化不會發生。其唯一方法就是確保Easer的各個部分都允許任何人覈查，即保證Easer（及如果存在的衍生軟件）爲開源軟件。  
而由於設計中Easer的各個功能（在將來）會變爲模塊/插件，因而也需要保證這些模塊/插件不會成爲潛在的黑手，故而希望依靠GPL的特性（鏈接軟件也需要爲GPL）來強制這一點。

事實上，強制各衍生/擴展品爲**GPL**並不必要，因爲只需要它們**開源**即可。但GPL是（我）目前所知的唯一可以保證衍生/擴展品爲開源軟件的協議，所以選擇它。

第三方庫
-----
* [Logger](https://github.com/orhanobut/logger): Apache License v2
* [android-flowlayout](https://github.com/ApmeM/android-flowlayout): Apache License v2
* [Guava](https://github.com/google/guava): Apache License v2
