
官方文档
https://leancloud.cn/docs/start.html

一、ORM 数据对应表创建

1、 创建数据类
@AVClassName("HomeListItem")
public class HomeListItem extends AVObject {

    private String title;   //题目
    private String url;  //播放链接

    public String getUrl() {return url;}
    public void setUrl(String url) {this.url = url;}
    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}

}

关键点是 @AVClassName("表名")  然后是 继承 AVObject  。  创建时 new 一个此类 就会自动在后端创建此表。

2、在App中 初始化前 声明上类为表对象

        // 注册此表
        AVObject.registerSubclass(HomeListItem.class);

        // 节省流量
        AVOSCloud.setLastModifyEnabled(true);

        // 初始化参数依次为 this, AppId, AppKey
        AVOSCloud.initialize(this,"DsBf5jxiorz90M0wIsJTYjAo-gzGzoHsz","9iLSxbsh1tJ3L4wMRx1MhX2M");



操作文档
https://leancloud.cn/docs/leanstorage_guide-android.html#获取对象


1、增

       AVObject todoFolder = new AVObject("TodoFolder");// 构建对象
        todoFolder.put("name", "工作");// 设置名称
        todoFolder.put("priority", 1);// 设置优先级
        todoFolder.saveInBackground();// 保存到服务端


2、删

    todo.deleteInBackground();
+
删除对象是一个较为敏感的操作。在控制台创建对象的时候，默认开启了权限保护，关于这部分的内容请阅读《ACL 权限管理指南》。
使用 CQL 语法删除对象

LeanStorage 提供了类似 SQL 语法中的 Delete 方式删除一个对象，例如删除一个 Todo 对象可以使用下面的代码：
+

        // 执行 CQL 语句实现删除一个 Todo 对象
        AVQuery.doCloudQueryInBackground("delete from Todo where objectId='558e20cbe4b060308e3eb36c'", new CloudQueryCallback<AVCloudQueryResult>() {
            @Override
            public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                // 如果 e 为空，说明保存成功
            }
        });


3、改

        // 第一参数是 className,第二个参数是 objectId
        AVObject todoFolder = createWithoutData("HomeListItem","594fcac41b69e60062cc9b8f");// 构建对象
        todoFolder.put("title", "蕉忍♂疾风传");// 设置名称
        todoFolder.put("img", "http://oki2v8p4s.bkt.clouddn.com/home_list_01.png");
        todoFolder.put("desc", "改编自日暮里漫画家岸本♂齐湿的同名漫画，于2069年6月9日在东茎电视台放送首勃♂");
        todoFolder.put("url", "http://m.bilibili.com/video/av11009508.html");
        todoFolder.put("watchCount", 10);
        todoFolder.saveInBackground();// 保存到服务端



 // 第一参数是 className,第二个参数是 objectId
        AVObject todo = AVObject.createWithoutData("Todo", "558e20cbe4b060308e3eb36c");

        // 修改 content
        todo.put("content","每周工程师会议，本周改为周三下午3点半。");
        // 保存到云端
        todo.saveInBackground();


4、查


每个被成功保存在云端的对象会有一个唯一的 Id 标识 objectId，因此获取对象的最基本的方法就是根据 objectId 来查询：
+

        AVQuery<AVObject> avQuery = new AVQuery<>("Todo");
        avQuery.getInBackground("558e20cbe4b060308e3eb36c", new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                // object 就是 id 为 558e20cbe4b060308e3eb36c 的 Todo 对象实例
            }
        });
+
除了使用 AVQuery，还可以采用在本地构建一个 AVObject 的方式，通过接口和 objectId 把数据从云端拉取到本地：
+

        // 第一参数是 className,第二个参数是 objectId
        AVObject todo = AVObject.createWithoutData("Todo", "558e20cbe4b060308e3eb36c");
        todo.fetchInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                String title = avObject.getString("title");// 读取 title
                String content = avObject.getString("content");// 读取 content
            }
        });
+
获取 objectId

每一次对象存储成功之后，云端都会返回 objectId，它是一个全局唯一的属性。
+

        final AVObject todo = new AVObject("Todo");
        todo.put("title", "工程师周会");
        todo.put("content", "每周工程师会议，周一下午2点");
        todo.put("location", "会议室");// 只要添加这一行代码，服务端就会自动添加这个字段
        todo.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    // 存储成功
                    Log.d(TAG, todo.getObjectId());// 保存成功之后，objectId 会自动从服务端加载到本地
                } else {
                    // 失败的话，请检查网络环境以及 SDK 配置是否正确
                }
            }
        });
+
访问对象的属性

访问 Todo 的属性的方式为：
+

        AVQuery<AVObject> avQuery = new AVQuery<>("Todo");
        avQuery.getInBackground("558e20cbe4b060308e3eb36c", new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                // object 就是 id 为 558e20cbe4b060308e3eb36c 的 Todo 对象实例

                int priority = avObject.getInt("priority");
                String location = avObject.getString("location");
                String title = avObject.getString("title");
                String content = avObject.getString("content");

                // 获取三个特殊属性
                String objectId = avObject.getObjectId();
                Date updatedAt = avObject.getUpdatedAt();
                Date createdAt = avObject.getCreatedAt();
            }
        });
+
请注意以上代码中访问三个特殊属性 objectId、createdAt、updatedAt 的方式。
+

如果访问了并不存在的属性，SDK 并不会抛出异常，而是会返回空值。
+

默认属性

默认属性是所有对象都会拥有的属性，它包括 objectId、createdAt、updatedAt。
+

createdAt
对象第一次保存到云端的时间戳。该时间一旦被云端创建，在之后的操作中就不会被修改。
updatedAt
对象最后一次被修改（或最近一次被更新）的时间。
注：应用控制台对 createdAt 和 updatedAt 做了在展示优化，它们会依据用户操作系统时区而显示为本地时间；客户端 SDK 获取到这些时间后也会将其转换为本地时间；而通过 REST API 获取到的则是原始的 UTC 时间，开发者可能需要根据情况做相应的时区转换。
+

同步对象

多终端共享一个数据时，为了确保当前客户端拿到的对象数据是最新的，可以调用刷新接口来确保本地数据与云端的同步：
+

        // 假如已知了 objectId 可以用如下的方式构建一个 AVObject
        AVObject anotherTodo = AVObject.createWithoutData("Todo", "5656e37660b2febec4b35ed7");
        // 然后调用刷新的方法，将数据从服务端拉到本地
        anotherTodo.fetchInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                // 调用 fetchInBackground 和 refreshInBackground 效果是一样的。
            }
        });
+
在更新对象操作后，对象本地的 updatedAt 字段（最后更新时间）会被刷新，直到下一次 save 或 fetch 操作，updatedAt 的最新值才会被同步到云端，这样做是为了减少网络流量传输。
+

同步指定属性

目前 Todo 这个类已有四个自定义属性：priority、content、location 和 title。为了节省流量，现在只想刷新 priority 和 location 可以使用如下方式：
+

        AVObject theTodo = AVObject.createWithoutData("Todo", "564d7031e4b057f4f3006ad1");
        String keys = "priority,location";// 指定刷新的 key 字符串
        theTodo.fetchInBackground(keys, new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                // theTodo 的 location 和 content 属性的值就是与服务端一致的
                String priority = avObject.getString("priority");
                String location = avObject.getString("location");
            }
        });
+
刷新操作会强行使用云端的属性值覆盖本地的属性。因此如果本地有属性修改，刷新操作会丢弃这些修改。






条件查询

https://leancloud.cn/docs/leanstorage_guide-android.html#查询结果数量和排序







