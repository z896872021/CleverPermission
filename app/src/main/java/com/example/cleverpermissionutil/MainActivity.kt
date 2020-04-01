package com.example.cleverpermissionutil

import android.Manifest
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.cleverpermissionutil.permission.NormalPermissionCallBack
import com.example.cleverpermissionutil.permission.Permission
import com.example.cleverpermissionutil.permission.PermissionCallBackModel
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
    }

    @Permission(permissions = arrayOf(Manifest.permission.CAMERA))
    fun requestPermission() {
        tv_text.text = "权限请求成功"
    }

    @Permission(permissions = arrayOf(Manifest.permission.RECORD_AUDIO))
    fun requestPermission1() {
        tv_text1.text = "权限1请求成功"
    }
}
