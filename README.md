Easer [![Build Status](https://travis-ci.org/renyuneyun/Easer.svg?branch=master)](https://travis-ci.org/renyuneyun/Easer) [![weblate](https://hosted.weblate.org/widgets/easer/-/svg-badge.svg)](https://hosted.weblate.org/engage/easer/?utm_source=widget) [![codecov](https://codecov.io/gh/renyuneyun/Easer/branch/master/graph/badge.svg)](https://codecov.io/gh/renyuneyun/Easer) [ ![Download](https://api.bintray.com/packages/renyuneyun/Android/Easer/images/download.svg) ](https://bintray.com/renyuneyun/Android/Easer/_latestVersion)  
[![matrix-chat](https://matrix.to/img/matrix-badge.svg)](https://matrix.to/#/#Easer:matrix.org) [![Backers on Open Collective](https://opencollective.com/Easer/backers/badge.svg)](Contributor.md)
=======
[<img src="https://f-droid.org/badge/get-it-on-zh-cn.png"
      alt="Get it on F-Droid"
      height="60">](https://f-droid.org/app/ryey.easer)
<img align="right" src='./app/src/main/ic_launcher-web.png' width='128' height='128'/>

See [README.en.md](README.en.md) for the English version.

**注意**：由於近期（2020下半年起）時間所限，Easer的維護速度會受到影響。不過由於程序主要框架已經穩定，除了增加對Event和Condition的遠程插件的支持（以及長期計劃中對多機器自動化互聯的支持）外，Easer的核心結構應當不會進行顯著修改（不過擴充功能仍不免需要進行一定修改）。有興趣者可以查看下面的相關章節，理解如何爲Easer添加你需要的功能，或者如何定位錯誤。作者會儘量抽時間看PR，然後先發佈的beta版本測試，隨後發佈正式版本。

簡介
-----
Easer是一个讓智能手機更爲智能更爲自動化的軟件，能夠告訴手機在什麼情況下該做什麼事。Easer不僅可以處理事件，也可以檢查狀態，還能組合事件形成複雜的自定義事件。

如要查看對Easer功能完整的介紹，請參見[網站](https://renyuneyun.github.io/Easer/)。

本文主要描述Easer的開發相關事宜。


擴展Easer
-----
給Easer添加功能主要分爲三類：機制、技能以及遠程技能（插件）。

其中機制是Easer的核心部分，需要對Easer代碼有較好理解；也歡迎對現有代碼進行優化（但不要過度優化）。
對於多數情況下，所要做的是增加新Event、Condition和Operation，這通過增加新的技能或遠程技能（插件）實現。

### 技能

增加技能插件需要添加自己的Event、Condition或Operation至Easer代碼中的`commons`包下。有腳本以簡化該操作，並且現有插件均可作爲示例。

詳細請參見[這個頁面](https://renyuneyun.github.io/Easer/zh/EXTEND)。

### 遠程技能（插件）

遠程技能是v0.7開始新增的功能，旨在通過獨立的軟件擴展Easer的功能。每個遠程技能是一個獨立的app；理論上也可以在一個app中包含多個遠程技能。注意該功能較新，接口暫不穩定（但應該不會大改）。

暫時遠程技能僅有Operation，[該倉庫](https://github.com/renyuneyun/EaserOperationPluginExample)是一個示例。本倉庫中[diagram目錄](diagram/)下有相關文件，比如其[時序圖](diagram/communication.png)。


支持Easer
-----
### 協助翻譯
Easer的翻譯工作託管在[Hosted Weblate](https://hosted.weblate.org/projects/easer/)。它是[weblate](https://weblate.org)（一個FLOSS的在線翻譯貢獻平臺）的官方託管實例。

十分歡迎有志者幫助翻譯工作，聚沙成塔集腋成裘。
如無特別說明，翻譯文本將使用[CC-0](https://creativecommons.org/choose/zero/)協議，這意味着它們會進入[公有領域](https://en.wikipedia.org/wiki/Public_domain)；如果希望使用其他協議，請事先聯繫我們。

另請注意，由於Easer作者的[個人偏好](https://blog.ryey.icu/you-dare-think-traditional-chinese-is-better.html)，中文主體取傳統漢字作爲模字，不取大陸所爲簡化字也不取臺灣所爲正體字。凡涉及翻譯自外文等情況導致地區用語不一致時，模字以大陸版爲主。（計劃後續加入基於[OpenCC](https://github.com/BYVoid/OpenCC)的自動轉換，擬通過CI達成，只是暫時無此精力。歡迎有經驗或有興趣者協助。）

### 提出、評論以及解決issue
如果在使用Easer時發現了什麼問題，你可以[提出一個issue](https://github.com/renyuneyun/Easer/issues/new)。在可行的情況下（當然，這不是強制的），相關信息給得越多越好，以便更早定位問題所在。  
如果你覺得Easer應當添加某些功能，你也可以提出一個issue。

對於現有的issue，無論你有相同的問題（或想法）、你可以提供更多信息、或是你不認同其中的說法，請對其進行評論。十分歡迎對問題進行討論。
其中較需要多方意見的issue被[標爲RFC](https://github.com/renyuneyun/Easer/issues?q=is%3Aopen+label%3A%22RFC+%2F+Discussion+Wanted%22)，歡迎任何人的意見/建議。

如有興趣參與更多但不知道從何下手，可以查看被標爲GFC (Good For Contributors) [L0](https://github.com/renyuneyun/Easer/issues?q=is%3Aissue+is%3Aopen+label%3A%22GFC%3A+L0%22)、[L1](https://github.com/renyuneyun/Easer/issues?q=is%3Aissue+is%3Aopen+label%3A%22GFC%3A+L1%22)、[L2](https://github.com/renyuneyun/Easer/issues?q=is%3Aissue+is%3Aopen+label%3A%22GFC%3A+L2%22) 的issue。一般而言，它們都是目的較爲清晰、涉及組件較少的issue；L0、L1、L2是我個人主觀按難度遞增順序進行的劃分。

在某些情況下（如果你是一個開發者），你也許具有解決某些issue的能力。你可以fork本倉庫，編寫代碼，然後創建pull request。這樣，如果你的代碼的確解決了其問題，則你的代碼會進入主幹，他人均會受益於你的貢獻（並且你會被列在*Contributors*列表中）。  
同樣歡迎對非現有issue創建pull request，但建議首先創建一個issue來描述你將要進行的工作（使得其他人意識到此事）。

### 測試覆蓋
測試實是重要。
而Easer目前遠不及過分測試的程度——事實上，多數代碼並沒有相應的自動測試。

如願意幫助貢獻測試代碼——尤其是對於核心部分（包括UI、數據後端以及服務）的測試——這裏實在感激不盡。

### 捐助

如果您願意給Easer的開發提供任何額度的捐助，請參看[DONATE.md](https://renyuneyun.github.io/Easer/zh/DONATE)。

感謝任何額度的支持。

鳴謝
------

### 協助者

Easer的開發離不開社區的幫助及捐贈。十分感謝各位。

詳細列表請見[Contributor](Contributor.md)文檔。

### 第三方

* [Logger](https://github.com/orhanobut/logger): Apache License v2
* [android-flowlayout](https://github.com/ApmeM/android-flowlayout): Apache License v2
* [Guava](https://github.com/google/guava): Apache License v2
* [StickyListHeaders](https://github.com/emilsjolander/StickyListHeaders): Apache License v2
* [AppIntro](https://github.com/AppIntro/AppIntro): Apache License v2
* [GraphView](https://github.com/Team-Blox/GraphView): Apache License v2
* [locale-helper-android](https://github.com/zeugma-solutions/locale-helper-android): Apache License v2
* [material-about-library](https://github.com/daniel-stoneuk/material-about-library): Apache License v2

* 以`*-fa-*`命名的drawable文件均來自[fontawesome](https://fontawesome.com/): CC-BY 4.0

版權協議
-----
Copyright (c) 2016 - 2019 Rui Zhao (renyuneyun) <renyuneyun@gmail.com>

除非額外說明，程序代碼以GPLv3+協議分發（參見LICENSE）

`utils/`目錄下工具以Apache 2.0協議分發（參見`utils/LICENSE`）

### 爲何使用GPL？

Easer的期望功能中包含大量對隱私信息（比如位置信息、日曆信息）的捕捉，而且擁有對網絡的訪問能力。我們永遠不會希望一個本該方便生活的工具變成對自己生活的監視工具，所以必須儘可能保證該腐化不會發生。其唯一方法就是確保Easer的各個部分都允許任何人覈查，即保證Easer（及如果存在的衍生軟件）爲開源軟件。  
而由於設計中Easer的各個功能（在將來）會變爲模塊/插件，因而也需要保證這些模塊/插件不會成爲潛在的黑手，故而希望依靠GPL的特性（鏈接軟件也需要爲GPL）來強制這一點。

事實上，強制各衍生/擴展品爲**GPL**並不必要，因爲只需要它們**開源**即可。但GPL是（我）目前所知的唯一可以保證衍生/擴展品爲開源軟件的協議，所以選擇它。

