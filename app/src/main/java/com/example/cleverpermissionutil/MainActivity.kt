package com.example.cleverpermissionutil

import android.Manifest
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.clever_permission.Permission
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    fun onClick(view: View) {
        when (view.id) {
            R.id.tv_text -> requestPermission()
            R.id.tv_text1 -> requestPermission1()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        RxPermissionUtils.setPermissionCallback(PermissionSkipFailureCallBack(this))
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    @Permission(permissions = [Manifest.permission.CAMERA])
    fun requestPermission() {
        tv_text.text = "权限请求成功"
    }

    @Permission(permissions = [Manifest.permission.RECORD_AUDIO])
    fun requestPermission1() {
        tv_text1.text = "权限1请求成功"
    }
}
