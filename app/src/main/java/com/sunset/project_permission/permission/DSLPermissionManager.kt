package com.sunset.project_permission.permission

import android.app.Activity
import android.content.Context
import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

/**
 * create by zhuning.su on  2021/6/17
 * @Description:
 */
class DSLPermissionManager : BasePermissionManager() {

    private var callbackMap = mutableMapOf<Int, PermissionResult.() -> Unit>()

    override fun onPermissionResult(permissionResult: PermissionResult) {
        callbackMap[permissionResult.requestCode]?.let {
            permissionResult.it()
        }
        callbackMap.remove(permissionResult.requestCode)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbackMap.clear()
    }

    companion object {

        private const val TAG = "DSLPermissionManager"

        @JvmStatic
        @MainThread
        inline fun requestPermissions(
            activity: Activity,
            isAllow: Boolean,
            vararg permissions: String,
            requestBlock: PermissionRequest.() -> Unit
        ) {
            val permissionRequest = PermissionRequest().apply(requestBlock)
            requireNotNull(permissionRequest.requestCode){
                "No permission request code specified."
            }
            requireNotNull(permissionRequest.resultCallback){
                "No permission result callback found."
            }
            _requestPermissions(activity, isAllow, permissionRequest.requestCode!!, permissionRequest.resultCallback!!, *permissions)
        }

        @JvmStatic
        @MainThread
        inline fun requestPermissions(
            fragment: Fragment,
            isAllow: Boolean,
            vararg permissions: String,
            requestBlock: PermissionRequest.() -> Unit
        ){
            val permissionRequest = PermissionRequest().apply(requestBlock)
            requireNotNull(permissionRequest.requestCode){
                "No permission request code specified."
            }
            requireNotNull(permissionRequest.resultCallback){
                "No permission result callback found."
            }
            _requestPermissions(fragment, isAllow, permissionRequest.requestCode!!, permissionRequest.resultCallback!!, *permissions)
        }

        fun _requestPermissions(
            activityOrFragment: Any,
            isAllow: Boolean,
            requestCode: Int,
            callback: PermissionResult.() -> Unit,
            vararg permissions: String
        ){
            val fragmentManager = if(activityOrFragment is AppCompatActivity) {
                activityOrFragment.supportFragmentManager
            } else {
                (activityOrFragment as Fragment).childFragmentManager
            }
            if(fragmentManager.findFragmentByTag(TAG) != null){
                (fragmentManager.findFragmentByTag(TAG) as DSLPermissionManager).also {
                    it.callbackMap[requestCode] = callback
                }.requestPermissions(isAllow, requestCode, *permissions)
            } else {
                DSLPermissionManager().also {
                    fragmentManager.beginTransaction().add(it, TAG).commitNow()
                    it.callbackMap[requestCode] = callback
                }.requestPermissions(isAllow, requestCode, *permissions)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        callbackMap.clear()
    }

}