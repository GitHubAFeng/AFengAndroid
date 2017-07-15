package com.afeng.xf.utils.AFengUtils;


import com.qiniu.android.storage.UploadManager;
import com.qiniu.util.Auth;


/**
 * Created by Administrator on 2017/7/15.
 */

public class QinIuManager {


    public static String accessKey = "RgaVn1VVQXaQTfpUgFbKzbKiUnsw3waEqWZdRzOe";   //在七牛个人中心》密钥管理 获取
    public static String secretKey = "0eqzcAHvVPxfbpqV5Z6bVtGawnEVXA9iuprVJKfV";   //在七牛个人中心》密钥管理 获取
    public static String bucket = "feiyufuli"; //要上传的空间
    public static String filePrefix = "http://ot2y5fs2o.bkt.clouddn.com/"; //文件URL前缀



//    private static QinIuManager instance;
//
//    public static QinIuManager getInstance() {
//        if (instance == null) {
//            synchronized (QinIuManager.class) {
//                if (instance == null) {
//                    instance = new QinIuManager();
//                }
//            }
//        }
//        return instance;
//    }

    private static UploadManager uploadManager;

    public static UploadManager getUploadManager() {
        if (uploadManager == null) {
            uploadManager = new UploadManager();
        }
        return uploadManager;
    }

    //最简单的上传凭证只需要AccessKey，SecretKey和Bucket就可以。
    public static String getSimpleToKen() {

        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);

        return upToken;

    }


    /**
     * 覆盖上传除了需要简单上传所需要的信息之外，还需要想进行覆盖的文件名称，这个文件名称同时可是客户端上传代码中指定的文件名，两者必须一致。
     *
     * @param key 要覆盖的文件名
     * @return
     */
    public static String getCoverToKen(String key) {

        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket, key);

        return upToken;

    }



//    public void onClick() {
//
//        byte[] data=new byte[]{ 0, 1, 2, 3};
//        //设置上传后文件的key
//        String upkey = "uploadtest.txt";
//        uploadManager.put(data, upkey, uptoken, new UpCompletionHandler() {
//            public void complete(String key, ResponseInfo rinfo, JSONObject response) {
//
//                String s = key + ", " + rinfo + ", " + response;
//
//                //显示上传后文件的url
//                textView.setText(o + s + "\n" + "http://xm540.com1.z0.glb.clouddn.com/" + key);
//            }
//        }, new UploadOptions(null, "test-type", true, null, null));
//
//    }
//}


}
