## 使用姿势
在debug模式下通过以下方式启用调试界面

	1.支持摇一摇进入Debug页面
	2.支持前后台切换出现Debug悬浮按钮，点击悬浮按钮跳转到Debug页面

---
引用了[Facebook stetho](https://github.com/facebook/stetho)来查看本地的数据库和SP内容

1.运行App, 打开Chrome输入chrome://inspect/#devices
	（别忘了用数据线把手机和电脑连起来哦）

![inspect](https://git.oschina.net/im_cc/im_cc_android/raw/feature/zxm-debug-mode/libdebugger/screenshot/stetho%20inspect.png)
	
2.如上图，chrome会检测到我们的app，点击inspect进入查看页面

![inspect](https://git.oschina.net/im_cc/im_cc_android/raw/feature/zxm-debug-mode/libdebugger/screenshot/inspect.png)

### 碰到的问题：
>如果inspect的页面打开是空白页面
>解决方案：
>打开的时候使用VPN连接国外，重新键入chrome://inspect就正常了

### 参考资料
[http://www.jianshu.com/p/03da9f91f41f](http://www.jianshu.com/p/03da9f91f41f)