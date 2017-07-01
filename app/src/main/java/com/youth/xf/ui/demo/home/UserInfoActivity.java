package com.youth.xf.ui.demo.home;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.GetDataCallback;
import com.avos.avoscloud.ProgressCallback;
import com.avos.avoscloud.RequestEmailVerifyCallback;
import com.avos.avoscloud.SaveCallback;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.youth.xf.R;
import com.youth.xf.base.BaseActivity;
import com.youth.xf.ui.constants.Constants;
import com.youth.xf.ui.demo.Login.UserInfoEvent;
import com.youth.xf.ui.demo.Login.UserLoginActivity;
import com.youth.xf.ui.demo.MainActivity;
import com.youth.xf.utils.AFengUtils.AppImageMgr;
import com.youth.xf.utils.AFengUtils.AppValidationMgr;
import com.youth.xf.utils.GlideHelper.GlideCircleTransform;
import com.youth.xf.utils.GlideHelper.ImgLoadUtil;
import com.youth.xf.widget.hipermission.HiPermission;
import com.youth.xf.widget.hipermission.PermissionCallback;
import com.youth.xf.widget.hipermission.PermissionItem;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.CaptureStrategy;
import com.zhihu.matisse.internal.entity.Item;
import com.zhihu.matisse.internal.utils.PhotoMetadataUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import java.util.Observer;
import java.util.Set;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/6/28.
 */

public class UserInfoActivity extends BaseActivity {

    @BindView(R.id.user_info_name)
    TextView mUserName;

    @BindView(R.id.user_info_nickname)
    EditText mNickname;

    @BindView(R.id.user_info_email)
    EditText mEmail;

    @BindView(R.id.user_info_desc)
    EditText mDesc;

    @BindView(R.id.user_info_phone)
    TextView mPhone;

    @BindView(R.id.user_info_avatar)
    ImageView mAvatar;


    UserInfoEvent Eventdata = new UserInfoEvent();


    private static final int REQUEST_CODE_CHOOSE = 23;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_info;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    protected void setListener() {

        mAvatar.setOnClickListener(v -> getImg());
//
//        mEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    // 此处为得到焦点时的处理内容
//                } else {
//                    // 此处为失去焦点时的处理内容
//
//                    String email = mEmail.getText().toString();
//                    if (!TextUtils.isEmpty(email)) {
//                        if (AppValidationMgr.isEmail(email)) {
//                            String user_mail = AVUser.getCurrentUser().getEmail();
//                            if (TextUtils.isEmpty(user_mail)) {
//
//
//                            } else {
//                                if (!email.equals(user_mail)) {
//                                    AVUser.getCurrentUser().setEmail(email);
//                                    AVUser.getCurrentUser().saveInBackground();
//                                }
//                            }
//
//                        }
//
//                    }
//
//                    // 发送验证邮件
//                    AVUser.requestEmailVerifyInBackground(email, new RequestEmailVerifyCallback() {
//                        @Override
//                        public void done(AVException e) {
//                            if (e == null) {
//                                // 求重发验证邮件成功
//                            }
//                        }
//                    });
//
//
//                }
//            }
//        });

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

        initData();

//        initCache();
    }


    @Override
    public void finish() {
        saveData();

        super.finish();
    }

    // 退出时保存
    private void saveData() {

        if (AVUser.getCurrentUser() != null) {

            String email = mEmail.getText().toString();
            String desc = mDesc.getText().toString();
            String nick = mNickname.getText().toString();


            AVUser.getCurrentUser().setEmail(email);
            AVUser.getCurrentUser().saveInBackground();

            // 修改表数据
            // 第一参数是 className,第二个参数是 objectId
            AVObject todo = AVObject.createWithoutData("UserInfo", Constants.USER_INFO_ID);
            // 修改 content
            todo.put("desc", desc);
            todo.put("nickname", nick);
            // 保存到云端
            todo.saveInBackground();


            Eventdata.setDesc(desc);
            Eventdata.setNickname(nick);
            Eventdata.setUserEmail(email);

//            saveCache(Eventdata);

            EventBus.getDefault().post(Eventdata);
        }

    }

    // 进来时先判断有缓存就不要联网了
    private void initCache() {

        Observable.create((ObservableOnSubscribe<UserInfoEvent>) e -> {
            UserInfoEvent data = (UserInfoEvent) mCache.getAsObject(Constants.USER_INFO_KEY);
            if (data != null) {
                e.onNext(data);
            } else {
                e.onError(new Throwable("没有缓存"));
            }

        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<UserInfoEvent>() {

                    Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(UserInfoEvent userInfoEvent) {

                        if (userInfoEvent.getAvatar() != null) {
                            Bitmap img = AppImageMgr.getBitmapByteArray(userInfoEvent.getAvatar(), 512, 512);
                            ImgLoadUtil.displayCircle(mContext, mAvatar, img);

                            mPhone.setText(userInfoEvent.getUserPhone());
                            mEmail.setText(userInfoEvent.getUserEmail());
                            mDesc.setText(userInfoEvent.getDesc());
                            mNickname.setText(userInfoEvent.getNickname());
                        } else {
                            initData();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        initData();
                    }

                    @Override
                    public void onComplete() {
                        if (!disposable.isDisposed())
                            disposable.dispose();
                    }
                });

    }


    private void saveCache(UserInfoEvent data) {

        mCache.put(Constants.USER_INFO_KEY, data);

//        Observable.just(data)
//                .subscribeOn(Schedulers.io())
//                .subscribe(new Consumer<UserInfoEvent>() {
//                    @Override
//                    public void accept(UserInfoEvent userInfoEvent) throws Exception {
//                        mCache.put(Constants.USER_INFO_KEY, userInfoEvent);
//                    }
//                });

    }


    private void initData() {
        if (AVUser.getCurrentUser() != null) {

            String username = AVUser.getCurrentUser().getUsername();
            String email = AVUser.getCurrentUser().getEmail();
            String phone = AVUser.getCurrentUser().getMobilePhoneNumber();

            mPhone.post(() -> mPhone.setText(phone));
            mEmail.post(() -> mEmail.setText(email));

            AVQuery<AVObject> avQuery = new AVQuery<>("UserInfo");
            avQuery.getInBackground(Constants.USER_INFO_ID, new GetCallback<AVObject>() {
                @Override
                public void done(AVObject avObject, AVException e) {

                    String desc = avObject.getString("desc");
                    String nick = avObject.getString("nickname");
                    AVFile avatar = avObject.getAVFile("avatar");

                    String name = TextUtils.isEmpty(nick) ? username : nick;

                    mDesc.setText(desc);
                    mNickname.setText(nick);
                    mUserName.post(() -> mUserName.setText(name));

                    // 把图片下载回来
                    avatar.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] bytes, AVException e) {
                            // bytes 就是文件的数据流
                            if (e == null) {
                                if (bytes != null) {

                                    Bitmap img = AppImageMgr.getBitmapByteArray(bytes, 512, 512);
                                    ImgLoadUtil.displayCircle(mContext, mAvatar, img);

//                                    Eventdata.setAvatar(bytes);
                                    Eventdata.setDesc(desc);
                                    Eventdata.setNickname(nick);
                                    Eventdata.setUserName(name);
                                    Eventdata.setUserEmail(email);
                                    Eventdata.setUserPhone(phone);
                                }
                            }
                        }
                    }, new ProgressCallback() {
                        @Override
                        public void done(Integer integer) {
                            // 下载进度数据，integer 介于 0 和 100。
                        }
                    });

                }
            });


        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            if (data != null) {

                //xLogger("Uris: " + Matisse.obtainResult(data));

                Glide.with(mContext)
                        .load(Matisse.obtainResult(data).get(0))
                        .asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .error(R.drawable.ic_avatar_default)
                        .transform(new GlideCircleTransform(mContext))
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                mAvatar.setImageBitmap(resource);
                                byte[] img = AppImageMgr.getBitmap2Byte(resource);

                                AVFile file = new AVFile("user_avatar.png", img);

                                // 修改表数据
                                // 第一参数是 className,第二个参数是 objectId
                                AVObject info = AVObject.createWithoutData("UserInfo", Constants.USER_INFO_ID);
                                info.put("avatar", file);
                                // 保存到云端
                                info.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(AVException e) {
                                        if (e == null) {
//                                            Eventdata.setAvatar(img);
                                            Eventdata.setAvatarUpdate(true);
                                        }
                                    }
                                });


                                // 单纯上传文件
//                                file.saveInBackground(new SaveCallback() {
//                                    @Override
//                                    public void done(AVException e) {
//                                        // 成功或失败处理...
//                                        if (e == null) {

//                                        }
//                                    }
//                                }, new ProgressCallback() {
//                                    @Override
//                                    public void done(Integer integer) {
//                                        // 上传进度数据，integer 介于 0 和 100。
//                                    }
//                                });


                            }
                        });

            }

        }
    }


    private void getImg() {

        List<PermissionItem> permissionItems = new ArrayList<>();
        permissionItems.add(new PermissionItem(Manifest.permission.READ_EXTERNAL_STORAGE, "相册访问权限", R.drawable.permission_ic_storage));
        permissionItems.add(new PermissionItem(Manifest.permission.WRITE_EXTERNAL_STORAGE, "SD卡访问权限", R.drawable.permission_card1));

        HiPermission.create(UserInfoActivity.this)
                .title("权限申请")
                .permissions(permissionItems)
                .msg("访问相册需要您同意以下权限！")
                .animStyle(R.style.PermissionAnimFade)
                .style(R.style.PermissionDefaultNormalStyle)
                .checkMutiPermission(new PermissionCallback() {
                    @Override
                    public void onClose() {

                    }

                    @Override
                    public void onFinish() {

                        Matisse.from(UserInfoActivity.this)
                                .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))
                                .countable(true)
                                .maxSelectable(1)  //最多选择数量
                                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                                .thumbnailScale(0.85f)
                                .imageEngine(new GlideEngine())
                                .forResult(REQUEST_CODE_CHOOSE);

                    }

                    @Override
                    public void onDeny(String permission, int position) {

                    }

                    @Override
                    public void onGuarantee(String permission, int position) {

                    }
                });


    }


}
