package com.example.cleverpermissionutil.permission;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.subjects.PublishSubject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RxPermissionUtils {
    private Context context;
    static final Object TRIGGER = new Object();
    static final String TAG = RxPermissionUtils.class.getSimpleName();
    private static PermissionCallback permissionCallback;

    public static void setPermissionCallback(PermissionCallback permissionCallback) {
        RxPermissionUtils.permissionCallback = permissionCallback;
    }

    public static PermissionCallback getPermissionCallback() {
        return permissionCallback;
    }

    public RxPermissionUtils(Context context) {
        this.context = context;
    }

    public static RxPermissionUtils with(Context context) {
        RxPermissionUtils permisson = new RxPermissionUtils(context);
        return permisson;
    }

    public Observable<PermissionBean> request(String[] permissions) {
        if (permissions == null || permissions.length <= 0) {
            return Observable.empty();
        }
//        PermissionActivity.Companion.request(context, permissions, callback);
        return Observable.just(TRIGGER).compose(ensureEach(permissions));
    }

    public <T> ObservableTransformer<T, PermissionBean> ensureEach(final String... permissions) {
        return new ObservableTransformer<T, PermissionBean>() {
            public ObservableSource<PermissionBean> apply(Observable<T> o) {
                return requestImplementation(permissions);
            }
        };
    }

    @TargetApi(23)
    private Observable<PermissionBean> requestImplementation(String... permissions) {
        Log.e(TAG, "requestPermissionsFromFragment " + TextUtils.join(", ", permissions));
        List<Observable<PermissionBean>> list = new ArrayList<>(permissions.length);
        Map<String, Observable<PermissionBean>> map = new HashMap<>();
        for (String permission : permissions) {
            PublishSubject<PermissionBean> subject = PublishSubject.create();
            map.put(permission, subject);
            list.add(subject);
        }
        PermissionActivity.Companion.request(context, permissions, map);
        return Observable.concat(Observable.fromIterable(list));
    }

    private Observable<?> oneOf(Observable<?> trigger, Observable<?> pending) {
        return trigger == null ? Observable.just(TRIGGER) : Observable.merge(trigger, pending);
    }

    boolean isMarshmallow() {
        return Build.VERSION.SDK_INT >= 23;
    }


    /**
     * Jump to Settings page of your application
     *
     * @param context
     */
    public static void startSettingsActivity(Context context) {
        Uri packageURI = Uri.parse("package:" + context.getPackageName());
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
