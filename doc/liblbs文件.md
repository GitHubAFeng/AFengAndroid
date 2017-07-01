

https://x5.tencent.com/tbs/technical.html#/detail/sdk/1/34cf1488-7dc2-41ca-a77f-0014112bcab7
64位手机无法加载x5(libmttwebview.so is 32-bit instead of 64-bit)
x5内核暂时不提供64位的so文件,在64位手机上需要让AP以32位模式运行。具体操作如下：
1.如果使用是Eclipse则需要将所有的.so文件都放置在so加载目录：lib/armeabi文件夹下(没有该目录则新建一个，AP中没有使用到.so文件则需要拷贝任意一个32位的so文件到该目录下,如果没有合适的so可以到官网http://x5.tencent.com/tbs/sdk.html下载官网“SDK接入示例“,拷贝对应目录下的liblbs.so文件)，lib文件夹下不要有其他以”armeabi“开头的文件夹。
2.如果使用的是 Android studio则需要进行两项配置，
(1)打开对应module中的build.gradle文件,在文件的android{}中的defaultConfig{}里(如果没有defaultConfig{}则手动添加)添加如下配置: ndk{abiFilters "armeabi"}，如果配置后编译报错，那么需要在gradle.properties文件中加上Android.useDeprecatedNdk=true；
(2)找出build.gradle中配置的so加载目录:jniLibs.srcDir:customerDir,如果没有该项配置则so加载目录默认为：src/main/jniLibs，需要将.so文件都放置在so加载目录的armeabi文件夹下(没有该目录则新建一个，AP中没有使用到.so文件则需要拷贝任意一个32位的so文件到该目录下，如果没有合适的so可以到官网http://x5.tencent.com/tbs/sdk.html下载官网“SDK接入示例“,拷贝对应目录下的liblbs.so文件)，so加载目录下不要有其他以”armeabi“开头的文件夹。
可参考论坛回复：http://bbs.mb.qq.com/thread-1473368-1-1.html .如果仍未能解决您的问题，请直接在论坛回复并描述您的问题






此Jar包是腾讯第三方内核浏览器
tbs_sdk_thirdapp_v3.2.0.1104_43200_sharewithdownload_withfilereader_withoutGame_obfs_20170609_115346