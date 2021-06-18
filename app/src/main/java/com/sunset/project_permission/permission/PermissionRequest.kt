package com.sunset.project_permission.permission

/**
 * create by zhuning.su on  2021/6/17
 * @Description:
 */
class PermissionRequest (
    var requestCode: Int? = null,
    var resultCallback : (PermissionResult.() -> Unit) ? = null
)