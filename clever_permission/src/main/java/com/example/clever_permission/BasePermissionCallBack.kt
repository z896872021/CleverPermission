package com.example.cleverpermissionutil.permission

import android.content.Context

abstract class BasePermissionCallBack : PermissionCallback {
    lateinit var mContext: Context;
    fun setContext(context: Context) {
        this.mContext=context;
    }

    abstract override fun onRequestPermissionFailure(permissions: List<String>)

    abstract override fun onRequestPermissionFailureWithAskNeverAgain(permissions: List<String>)
}