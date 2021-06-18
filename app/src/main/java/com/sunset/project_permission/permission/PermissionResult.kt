package com.sunset.project_permission.permission

/**
 * create by zhuning.su on  2021/6/17
 * @Description:
 * 权限申请状态有：
 *  1. 第一次申请权限
 *  2.
 */
sealed class PermissionResult(val requestCode : Int) {

    /** 权限申请通过 */
    class PermissionGranted(requestCode: Int) : PermissionResult(requestCode)

    /** 权限申请被拒绝 */
    class PermissionDenied(requestCode: Int, val deniedPermissions: List<String>) : PermissionResult(requestCode)

    /** 可以调起系统权限申请弹窗 */
    class ShowRational(requestCode: Int, val permissions: List<String>): PermissionResult(requestCode)

    /** 第一次申请权限提示 */
    class ShowInitial(requestCode: Int, val permissions: List<String>) : PermissionResult(requestCode)

    /** 永久拒绝权限申请 */
    class PermissionDeniedPermanently(requestCode: Int, val permanentlyDeniedPermissions: List<String>) : PermissionResult(requestCode)

}