package com.example.cleverpermissionutil;

import android.Manifest;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.clever_permission.Permission;
import com.example.clever_permission.PermissionCallBackModel;
import com.example.clever_permission.RxPermissionUtils;
import com.example.cleverpermissionutil.permission.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Main2Activity extends AppCompatActivity {
    @PermissionCallBackModel
    private NormalPermissionCallBack permissionCallBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        RxPermissionUtils.setPermissionCallback(new PermissionCallback() {
            @Override
            public void onRequestPermissionFailure(@NotNull List<String> permissions) {
                Toast.makeText(Main2Activity.this, "你拒绝了这个权限请求", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onRequestPermissionFailureWithAskNeverAgain(@NotNull List<String> permissions) {

            }
        });
        initView();
    }

    @Permission(permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA})
    private void initView() {
        Toast.makeText(this, "OK,权限请求成功", Toast.LENGTH_LONG).show();
    }
}
