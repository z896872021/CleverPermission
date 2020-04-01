package com.example.cleverpermissionutil.permission

import android.app.Activity
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment

public class NormalPermissionCallBack : BasePermissionCallBack() {
    //    AlertDialog.Builder alertDialog
    lateinit var alertDialog: AlertDialog.Builder
    override fun onRequestPermissionFailure(permissions: List<String>) {
        if (mContext is Activity){
            (mContext as Activity).finish()
        }else if (mContext is Fragment){
            (mContext as Fragment).activity?.let {
                it.finish()
            }
        }
    }

    override fun onRequestPermissionFailureWithAskNeverAgain(permissions: List<String>) {
        onCreateDialog()
        alertDialog.setTitle("提示")
        alertDialog.setMessage("权限缺失，点击确定后前往设置页面设置权限")
        alertDialog.setPositiveButton("确定", { dialogInterface: DialogInterface, i: Int ->
            RxPermissionUtils.startSettingsActivity(mContext)
        }).show()
    }

    private fun onCreateDialog(){
        if (!::alertDialog.isInitialized)
            alertDialog=AlertDialog.Builder(mContext)
    }
    val tag: String = javaClass.simpleName

}