### 背景
这是一个基于wechaty的微信网页版, 公司限制登陆微信客户端, 手机聊微信有点累, 所以基于wechaty做了一个这个.
### 申明
这是一个mvp产品, 很多细节没处理好
- 代码里面引入了spring-boot-starter-web, 实际上没用到, 主要是收到消息, 需要实时通知到网页, 所以后面重新引入了socket.io
- 没找到好看的聊天模板, 所以找了一个jQuery和html的模板, 地址在 : https://codepen.io/dmjdn/pen/GRJgKGL
- 本地需要启动redis, 存储最近联系人, 最近消息, 没有考虑多账号系统, 虽然加个key就好了.
- 代码里面有一些空指针, 或者代码规范没注意
- 好久没写纯html和jQuery了, 凑活着用吧.
- 未读计数什么的没有做, 太麻烦, 当前页面的话, 收到消息会实时刷新

整体来说, 这个产品自是帮助你在微信客户端封禁的情况下, 聊微信更方便.
如果有很多人有需求的话, 后面会考虑再继续更新维护

预览图大概如下 : 

<img width="779" alt="image" src="https://user-images.githubusercontent.com/1071075/168712349-e5022692-8a95-42e6-a155-7d50d58a2913.png">

wechaty : https://github.com/wechaty/wechaty#readme

token已经过期了, 需要你们自己去获取, 我用的是padLocal : https://wechaty.js.org/docs/puppet-services/padlocal
