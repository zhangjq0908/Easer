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
