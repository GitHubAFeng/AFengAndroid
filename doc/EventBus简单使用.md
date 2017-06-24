

注意一点是，最好是一个事件定义一个事件模型！

EventBus 的简单使用:

//在Activity中,注册和反注册(通用的写法)
@Override
protected void onCreate(Bundle savedInstanceState) {
   super.onCreate(savedInstanceState);
   setContentView(R.layout.activity_main);
   EventBus.getDefault().register(this); //第1步: 注册
}
@Override
protected void onDestroy() {
   super.onDestroy();
   EventBus.getDefault().unregister(this);//反注册
}

//在注册了的Activity中,声明处理事件的方法
@Subscribe(threadMode = ThreadMode.BackgroundThread) //第2步:注册一个在后台线程执行的方法,用于接收事件
public void onUserEvent(ClassEvent event) {//参数必须是ClassEvent类型, 否则不会调用此方法
}
//----------华丽的分割线---------------

//在任意地方,调用发送事件
EventBus.getDefault().post(new ClassEvent());//第3步: 发送事件

//----------华丽的分割线---------------

//在任意地方,注册事件类
public static class ClassEvent{
} //定义一个事件, 可以不包含成员变量,和成员方法

//当然你也可以定义包含成员变量的事件, 用来传递参数
public class MsgEvent {
   public String jsonData;
   public MsgEvent(String jsonData) {
       this.jsonData = jsonData;
   }
}

2:Sticky事件的使用
之前说的使用方法, 都是需要先注册(register), 再post,才能接受到事件;
如果你使用postSticky发送事件, 那么可以不需要先注册, 也能接受到事件.

首先,你可能需要声明一个方法:

//注意,和之前的方法一样,只是多了一个 sticky = true 的属性.
@Subscribe(threadMode = ThreadMode.MainThread, sticky = true)
public void onEvent(MsgEvent event){

其次, 你可以在没有register的情况下:(发送事件)

EventBus.getDefault().postSticky(new MsgEvent("With Sticky"));
1
之后, 再注册,这样就可以收到刚刚发送的事件了:

EventBus.getDefault().register(this);//注册之后,马上就能收到刚刚postSticky发送的事件
1
3:参数说明:

/**
 * threadMode 表示方法在什么线程执行   (Android更新UI只能在主线程, 所以如果需要操作UI, 需要设置ThreadMode.MainThread)
 * sticky     表示是否是一个粘性事件   (如果你使用postSticky发送一个事件,那么需要设置为true才能接受到事件)
 * priority   优先级                 (如果有多个对象同时订阅了相同的事件, 那么优先级越高,会优先被调用.)
 * */
@Subscribe(threadMode = ThreadMode.MainThread, sticky = true, priority = 100)
public void onEvent(MsgEvent event){
}