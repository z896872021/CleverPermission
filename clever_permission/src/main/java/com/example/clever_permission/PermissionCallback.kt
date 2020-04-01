package com.example.cleverpermissionutil.permission

import android.content.Context

interface PermissionCallback {

    abstract fun onRequestPermissionFailure(permissions: List<String>)

    abstract fun onRequestPermissionFailureWithAskNeverAgain(permissions: List<String>)
}