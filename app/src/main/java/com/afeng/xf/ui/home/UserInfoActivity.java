package com.afeng.xf.ui.home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.afeng.xf.utils.AFengUtils.PerfectClickListener;
import com.afeng.xf.widget.button.CountDownTimerButton;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.GetDataCallback;
import com.avos.avoscloud.ProgressCallback;
import com.avos.avoscloud.RequestEmailVerifyCallback;
import com.avos.avoscloud.SaveCallback;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.afeng.xf.R;
import com.afeng.xf.base.BaseActivity;
import com.afeng.xf.ui.constants.Constants;
import com.afeng.xf.ui.Login.UserInfoEvent;
import com.afeng.xf.utils.AFengUtils.AppImageMgr;
import com.afeng.xf.utils.GlideHelper.GlideCircleTransform;
import com.afeng.xf.utils.GlideHelper.ImgLoadUtil;
import com.afeng.xf.widget.hipermission.HiPermission;
import com.afeng.xf.widget.hipermission.PermissionCallback;
import com.afeng.xf.widget.hipermission.PermissionItem;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
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

    @BindView(R.id.user_info_email_verified)
    CountDownTimerButton mEmailButton;


    UserInfoEvent Eventdata = new UserInfoEvent();


    private static final int REQUEST_CODE_CHOOSE = 23;
    public static final int RESULT_CODE = 101;
    public static final String RESULT_KEY = "101";


    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_info;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

        mEmailButton.setClickAfter("秒后可点击");
        mEmailButton.setClickBeffor("验证邮箱");

    }

    @Override
    protected void setListener() {


        mEmailButton.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                VerifyEmail();
            }
        });


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


    //复写onBackPressed()的时候，把自己的代码放在super.onBackPressed()上面。 否则 onActivityResult 无法接受到resultCode
    @Override
    public void onBackPressed() {
        saveData();

        super.onBackPressed();
    }


    private void VerifyEmail() {

        String temp = mEmail.getText().toString().trim();

        // 查询邮箱是否已经被注册
        AVQuery<AVObject> query = new AVQuery<>("_User");
        query.whereNotEqualTo("objectId", AVUser.getCurrentUser().getObjectId());
        query.whereEqualTo("email", temp);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    String email;
                    boolean isequals = false;

                    if (list != null) {
                        for (AVObject avObject : list) {
                            email = avObject.getString("email");

                            if (temp.equals(email)) {
                                isequals = true;
                                break;
                            }
                        }
                    } else {
                        xLogger("email列表为空");
                    }

                    if (isequals) {
                        xToastShow("此邮箱地址已经被注册");
                    } else {

                        //开始倒计
                        mEmailButton.startTimer();
                        xToastShow("正在发送邮件");

                        AVUser.requestEmailVerifyInBackground(temp, new RequestEmailVerifyCallback() {
                            @Override
                            public void done(AVException e) {
                                if (e == null) {
                                    // 求重发验证邮件成功
                                    xToastShow("邮件发送成功");
                                }
                            }
                        });
                    }
                } else {
                    xToastShow("" + e.getCode());
                    if (e.getCode() == 403) {
                        xToastShow("此邮箱地址已经被注册");
                    }
                }


            }
        });

    }


    // 退出时保存
    private void saveData() {

        if (AVUser.getCurrentUser() != null) {

            String email = mEmail.getText().toString();
            String desc = mDesc.getText().toString();
            String nick = mNickname.getText().toString();


            Eventdata.setDesc(desc);
            Eventdata.setNickname(nick);
            Eventdata.setUserEmail(email);

            Intent intent = getIntentBySerializData(RESULT_KEY, Eventdata);

            setResult(RESULT_CODE, intent);

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

    }


    private void initData() {
        if (AVUser.getCurrentUser() != null) {

            String username = AVUser.getCurrentUser().getUsername();
            String phone = AVUser.getCurrentUser().getMobilePhoneNumber();

            mPhone.post(() -> mPhone.setText(phone));


            // 查询用户邮箱与手机是否验证
            AVQuery<AVObject> userQuery = new AVQuery<>("_User");
            userQuery.getInBackground(AVUser.getCurrentUser().getObjectId(), new GetCallback<AVObject>() {
                @Override
                public void done(AVObject avObject, AVException e) {
                    if (avObject != null) {

                        boolean isemailVerified = avObject.getBoolean("emailVerified");
                        boolean isphoneVerified = avObject.getBoolean("mobilePhoneVerified");
                        Eventdata.setEmailVerified(isemailVerified);
                        Eventdata.setPhoneVerified(isphoneVerified);

                        String email = avObject.getString("email");
                        mEmail.post(() -> mEmail.setText(email));
                        Eventdata.setUserEmail(email);


                        if (isemailVerified) {
                            mEmailButton.setVisibility(View.GONE);
                        } else {
                            mEmailButton.setVisibility(View.VISIBLE);
                        }

                    }

                }
            });


            AVQuery<AVObject> avQuery = new AVQuery<>("UserInfo");
            avQuery.getInBackground(Constants.USER_INFO_ID, new GetCallback<AVObject>() {
                @Override
                public void done(AVObject avObject, AVException e) {

                    if (avObject != null) {

                        String desc = avObject.getString("desc");
                        String nick = avObject.getString("nickname");
                        AVFile avatar = avObject.getAVFile("avatar");

                        String name = TextUtils.isEmpty(nick) ? username : nick;

                        mDesc.setText(desc);
                        mNickname.setText(nick);
                        mUserName.post(() -> mUserName.setText(name));

                        if (avatar != null) {

                            // 把图片下载回来
                            avatar.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] bytes, AVException e) {
                                    // bytes 就是文件的数据流
                                    if (e == null) {
                                        if (bytes != null) {

                                            Bitmap img = AppImageMgr.getBitmapByteArray(bytes, 512, 512);
                                            ImgLoadUtil.displayCircle(mContext, mAvatar, img);

                                            Eventdata.setDesc(desc);
                                            Eventdata.setNickname(nick);
                                            Eventdata.setUserName(name);
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
                    }
                }
            });

        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            if (data != null) {

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
