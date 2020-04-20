package com.example.cleverpermissionutil.permission

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import com.example.clever_permission.RxPermissionUtils

public abstract class PermissionSkipFailureCallBack(context: Context) : BasePermissionCallBack(context) {
    //    AlertDialog.Builder alertDialog
    lateinit var alertDialog: AlertDialog.Builder

    override fun onRequestPermissionFailureWithAskNeverAgain(permissions: List<String>) {
        onCreateDialog()
        alertDialog.setTitle("提示")
        alertDialog.setMessage("权限缺失，点击确定后前往设置页面设置权限")
        alertDialog.setPositiveButton("确定") { dialogInterface: DialogInterface, i: Int ->
            RxPermissionUtils.startSettingsActivity(mContext)
        }.show()
    }

    private fun onCreateDialog() {
        if (!::alertDialog.isInitialized)
            alertDialog = AlertDialog.Builder(mContext)
    }

    val tag: String = javaClass.simpleName

}