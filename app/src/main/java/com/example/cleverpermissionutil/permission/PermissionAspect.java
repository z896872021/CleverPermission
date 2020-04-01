package com.example.cleverpermissionutil.permission;

import android.app.Service;
import android.content.Context;
import android.util.Log;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Aspect
public class PermissionAspect {
    @Around("execution(@com.example.cleverpermissionutil.permission.Permission * *(..))")
    public void aroundJoinPoint(final ProceedingJoinPoint joinPoint) {
        try {
            // get annotation of method
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            final Method method = methodSignature.getMethod();
            Permission annotation = method.getAnnotation(Permission.class);
            final PermissionCallBackModel annotationCallBackModel = method.getAnnotation(PermissionCallBackModel.class);
            // get annotation value
            final String[] permissions = annotation.permissions();

            Object object = joinPoint.getThis();
            Context context = null;
            if (object instanceof FragmentActivity) {
                context = (FragmentActivity) object;
            } else if (object instanceof Fragment) {
                context = ((Fragment) object).getContext();
            } else if (object instanceof Service) {
                context = (Service) object;
            }

            final CreateObject createObject = new CreateObject();

            RxPermissionUtils.with(context)
                    .request(permissions)
                    .buffer(permissions.length)
                    .subscribe(new Observer<List<PermissionBean>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onNext(List<PermissionBean> permissionBeans) {
                            List<String> failurePermissions = new ArrayList<>();
                            List<String> askNeverAgainPermissions = new ArrayList<>();
                            for (PermissionBean p : permissionBeans) {
                                if (!p.granted) {
                                    if (p.shouldShowRequestPermissionRationale) {
                                        failurePermissions.add(p.name);
                                    } else {
                                        askNeverAgainPermissions.add(p.name);
                                    }
                                }
                            }
                            AtomicReference<PermissionCallback> callback = new AtomicReference<>();
                            if (RxPermissionUtils.getPermissionCallback() == null) {
                                callback.set((PermissionCallback) createObject.obtainInstance(joinPoint.getThis(), (t1, f) -> {
                                    boolean isModel = f.getAnnotation(PermissionCallBackModel.class) != null;
                                    return isModel ? (BasePermissionCallBack) createObject.newClass(f.getType()) : null;
                                }));
                                if (callback.get() == null) {
                                    callback.set(new NormalPermissionCallBack());
                                }
                                try {
                                    ((BasePermissionCallBack)callback.get()).setContext((Context) joinPoint.getThis());
                                }catch (Exception e){
                                    Log.e("onPermission", "@PermissionCallBackModel use object must is extended BasePermissionCallBack");
                                }
                            }else {
                                callback.set(RxPermissionUtils.getPermissionCallback());
                            }

                            if (failurePermissions.size() > 0) {
                                callback.get().onRequestPermissionFailure(failurePermissions);
                            }

                            if (askNeverAgainPermissions.size() > 0) {
                                callback.get().onRequestPermissionFailureWithAskNeverAgain(askNeverAgainPermissions);
                            }
                            if (failurePermissions.size() == 0 && askNeverAgainPermissions.size() == 0) {
                                try {
                                    joinPoint.proceed();
                                } catch (Throwable throwable) {
                                    throwable.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("onPermission", "onError: ", e);
                        }

                        @Override
                        public void onComplete() {
                            Log.e("onPermission", "onComplete: ");
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
