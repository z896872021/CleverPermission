package com.example.cleverpermissionutil.permission

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject


public class PermissionActivity : AppCompatActivity() {
    companion object {
        private val RC_REQUEST_PERMISSION = 100
        private lateinit var subjectMap: Map<String,Observable<PermissionBean>>
        val KEY_PERMISSIONS = "permissions"
//        fun request(context: Context, permissions: Array<String>) {
//            CALLBACK = callback
//            val intent = Intent(context, PermissionActivity::class.java)
//            intent.putExtra(KEY_PERMISSIONS, permissions)
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            context.startActivity(intent)

//        }

        fun request(context: Context, permissions: Array<String>, map: Map<String,Observable<PermissionBean>>) {
//            CALLBACK = callback
            val intent = Intent(context, PermissionActivity::class.java)
            intent.putExtra(KEY_PERMISSIONS, permissions)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            subjectMap = map;
            context.startActivity(intent)
        }
    }


    @TargetApi(23)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode != RC_REQUEST_PERMISSION) {
            return
        }
        val shouldShowRequestPermissionRationale = BooleanArray(permissions.size)
        for (i in permissions.indices) {
            shouldShowRequestPermissionRationale[i] = shouldShowRequestPermissionRationale(permissions[i])
        }
        this.onRequestPermissionsResult(permissions, grantResults, shouldShowRequestPermissionRationale)
    }

    @TargetApi(23)
    internal fun onRequestPermissionsResult(
        permissions: Array<String>,
        grantResults: IntArray,
        shouldShowRequestPermissionRationale: BooleanArray
    ) {
        val length = permissions.size
        var granted = 0
        for (i in 0 until length) {
            var subject: PublishSubject<PermissionBean> = subjectMap[permissions[i]] as PublishSubject<PermissionBean>;
            subject.onNext(
                PermissionBean(
                    permissions[i],
                    grantResults[i] == PackageManager.PERMISSION_GRANTED,
                    shouldShowRequestPermissionRationale[i]
                )
            )
            subject.onComplete()
        }
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_permission)
        if (!intent.hasExtra(KEY_PERMISSIONS)) {
            return
        }
        val permissions = intent.getStringArrayExtra(KEY_PERMISSIONS)
        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(permissions, RC_REQUEST_PERMISSION)
        }
    }
}
