package com.example.cleverpermissionutil.permission

import android.content.Context

abstract class BasePermissionCallBack(context: Context) : PermissionCallback {
    var mContext: Context = context

    abstract override fun onRequestPermissionFailure(permissions: List<String>)

    abstract override fun onRequestPermissionFailureWithAskNeverAgain(permissions: List<String>)
}