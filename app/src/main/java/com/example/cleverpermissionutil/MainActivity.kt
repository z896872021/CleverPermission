package com.example.cleverpermissionutil

import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.clever_permission.Permission
import com.example.clever_permission.PermissionCallBackModel
import com.example.cleverpermissionutil.permission.NormalPermissionCallBack
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    @PermissionCallBackModel
    var callBack: NormalPermissionCallBack? =null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestPermission()
    }

    @Permission(permissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION))
    fun requestPermission() {
        tv_text.text = "权限请求成功"
    }
}
