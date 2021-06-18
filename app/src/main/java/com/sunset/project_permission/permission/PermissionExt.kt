package com.sunset.project_permission.permission

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

/**
 * create by zhuning.su on  2021/6/17
 * @Description:
 */
inline fun AppCompatActivity.requestPermission(
    vararg permissions: String,
    isAllow: Boolean = true,
    requestBlock: PermissionRequest.() -> Unit
) {
    DSLPermissionManager.requestPermissions(this, isAllow, *permissions) {
        this.requestBlock()
    }
}

inline fun Fragment.requestPermission(
    vararg permissions: String,
    isAllow: Boolean = true,
    requestBlock: PermissionRequest.() -> Unit
) {
    DSLPermissionManager.requestPermissions(this, isAllow, *permissions) {
        this.requestBlock()
    }
}