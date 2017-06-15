AgentWeb 功能

支持进度条以及自定义进度条
支持文件下载
支持文件下载断点续传
支持下载通知形式提示进度
简化 Javascript 通信
支持 Android 4.4 Kitkat 以及其他版本文件上传
支持注入 Cookies
加强 Web 安全
支持全屏播放视频
兼容低版本 Js 安全通信
更省电 。
支持调起微信支付
支持调起支付宝（请参照sample）
默认支持定位
为什么要使用 AgentWeb ？

Web	文件下载	文件上传	Js 通信	断点续传	使用简易度	进度条	线程安全	全屏视频
WebView	不支持	不支持	支持	不支持	麻烦	没有	不安全	不支持
AgentWeb	支持	支持	更简洁	支持	简洁	有	安全	支持
引入

Gradle

compile 'com.just.agentweb:agentweb:1.2.1'
Maven

 <dependency>
   <groupId>com.just.agentweb</groupId>
   <artifactId>agentweb</artifactId>
   <version>1.2.1</version>
   <type>pom</type>
 </dependency>

使用

为什么说它简洁易用吗 ？ 下面京东效果图 ， 只需一句话 ！

mAgentWeb = AgentWeb.with(this)//传入Activity
                .setAgentWebParent(mLinearLayout, new LinearLayout.LayoutParams(-1, -1))//传入AgentWeb 的父控件 ，如果父控件为 RelativeLayout ， 那么第二参数需要传入 RelativeLayout.LayoutParams
                .useDefaultIndicator()// 使用默认进度条
                .defaultProgressBarColor() // 使用默认进度条颜色
                .setReceivedTitleCallback(mCallback) //设置 Web 页面的 title 回调
                .createAgentWeb()//
                .ready()
                .go("http://www.jd.com");

里面没有一句 Setting ， 甚至连 WebChromeClient 都不用配置就有进度条 。

效果图





Javascript 通信拼接太麻烦 ？ 请看 。

//Javascript 方法
function callByAndroid(){
      console.log("callByAndroid")
  }
//Android 端
mAgentWeb.getJsEntraceAccess().quickCallJs("callByAndroid");
//结果
consoleMessage:callByAndroid  lineNumber:27
事件处理

@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (mAgentWeb.handleKeyEvent(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
跟随 Activity Or Fragment 生命周期 ， 释放 CPU 更省电 。

	@Override
    protected void onPause() {
        mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();

    }

    @Override
    protected void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }
文件上传处理

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mAgentWeb.uploadFileResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
全屏视频播放

<!--如果你的应用需要用到视频 ， 那么请你在使用 AgentWeb 的 Activity 对应的清单文件里加入如下配置-->
android:hardwareAccelerated="true"
android:configChanges="orientation|screenSize"
定位

	<!--AgentWeb 是默认启动定位的 ， 请在你的 AndroidManifest 文件里面加入如下权限 。-->
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
混淆

如果你的项目需要加入混淆 ， 请加入如下配置

-keep class com.just.library.** {
    *;
}
-dontwarn com.just.library.**

Java 注入类不要混淆 ， 例如 App 里面的 AndroidInterface 类 ， 需要 Keep 。

-keepclassmembers class com.just.library.agentweb.AndroidInterface{ *; }
更新日志

v_1.2.1 支持调起支付宝 ， 微信支付 。
v_1.2.0 全面支持全屏视频
v_1.1.2 完善功能