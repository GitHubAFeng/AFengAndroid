如何使用:

注：
1.整体实现均使用色值，未使用任何图片资源，核心色彩都已添加自定义属性；
2.整体宽高自行定义，内部元素均根据整体宽高自动缩放适应，但由于整体效果限制，建议宽度不要低于100dp，否则效果不佳；



获取项目资源的两种形式为：
1).直接使用JitPack上的库；
2).拷贝工程的的GADownloadingView及其他资源；

1.1.直接使用JitPack上的库

step 1 :在项目的build.gradle中加入如下代码：

allprojects {
		repositories {
			...
            // add the follow code
			maven { url 'https://jitpack.io' }
		}
	}
step 2 :在相应的模块的build.gradle中加入如下代码：

dependencies {
	compile 'com.github.Ajian-studio:GADownloading:v1.0.2'
}

1.2.拷贝工程的中的GADownloadingView及相应的attr.xml文件

直接复制src/ui/GADownloadingView 及 res/values/attr.xml文件复制到相应的目录下

2.在布局文件中添加GADownloadingView:

<xxx.xxx.xxxx.GADownloadingView
    android:id="@+id/ga_downloading"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:layout_below="@+id/show_failed"
    android:layout_centerHorizontal="true" />
3.在Activity中找到组件：

mGADownloadingView = (GADownloadingView) findViewById(R.id.ga_downloading);
4.核心接口：

4.1.performAnimation()：

启动动画，包括背景和下载箭头抖动部分、背景镂空、圆变换为进度条、进度条抖动、下载箭头变换为承载文字的线框；

4.2.updateProgress(int progress)：

更新进度；

4.3.onFail()：

下载失败、调用则执行失败部分动效；

5.自定义属性：

5.1 可更改属性

    <declare-styleable name="GADownloadingView">

        <attr name="arrow_color" format="color" />
        <attr name="loading_circle_back_color" format="color" />
        <attr name="loading_line_color" format="color" />
        <attr name="progress_line_color" format="color" />
        <attr name="progress_text_color" format="color" />
        <attr name="done_text_color" format="color" />
    </declare-styleable>

5.2.自定义属性使用方式：

添加自定义属性命名空间：

    <RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
         xmlns:gastudio="http://schemas.android.com/apk/res-auto"
         ... ...
    />
添加自定义属性

    <com.gastudio.gadownloading.ui.GADownloadingView
        android:id="@+id/ga_downloading"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        gastudio:arrow_color="@android:color/white"
        gastudio:done_text_color="@android:color/white"
        gastudio:loading_circle_back_color="@android:color/white"
        gastudio:loading_line_color="@android:color/white"
        gastudio:progress_line_color="@android:color/white"
        gastudio:progress_text_color="@android:color/white" />
最后，如果你觉得还不错，欢迎Star！

欢迎加入GAStudio交流qq群: 277582728 ；