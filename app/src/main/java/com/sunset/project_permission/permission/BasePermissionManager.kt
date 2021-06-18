package com.sunset.project_permission.permission

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

/**
 * create by zhuning.su on  2021/6/17
 * @Description:
 *
 * 第一次请求权限时ActivityCompat.shouldShowRequestPermissionRationale=false;
 * 第一次请求权限被禁止，但未选择【不再提醒】ActivityCompat.shouldShowRequestPermissionRationale=true;
 * 允许某权限后ActivityCompat.shouldShowRequestPermissionRationale=false;
 * 禁止权限，并选中【禁止后不再询问】ActivityCompat.shouldShowRequestPermissionRationale=false；
 */
abstract class BasePermissionManager : Fragment() {

    private val rationalRequest = mutableMapOf<Int, Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }){
            /** 所有权限均已同意 */
            onPermissionResult(PermissionResult.PermissionGranted(requestCode))
        } else if(permissions.any { shouldShowRequestPermissionRationale(it) }){
            /** 有权限未通过，但有可以再次请求的权限 */
            onPermissionResult(
                PermissionResult.PermissionDenied(
                    requestCode,
                    permissions.filterIndexed { index, _ ->
                        grantResults[index] == PackageManager.PERMISSION_DENIED
                    })
            )
        } else {
            /** 有权限未通过，且不允许再次请求 */
            onPermissionResult(
                PermissionResult.PermissionDeniedPermanently(
                    requestCode,
                    permissions.filterIndexed { index, _ ->
                        grantResults[index] == PackageManager.PERMISSION_DENIED
                    })
            )
        }
    }

    protected fun requestPermissions(isAllow: Boolean, requestCode: Int, vararg permissions: String){
        Log.i(">>>>> ", " requestPermissions start <<<")

        val notGranted = permissions.filter {
            ContextCompat.checkSelfPermission(requireActivity(), it) != PackageManager.PERMISSION_GRANTED
        }
        Log.i(">>>>> ", " requestPermissions filter not granted after $notGranted <<<")
        when {
            notGranted.isEmpty() -> {
                Log.i(">>>>> ", " requestPermissions not granted list is empty <<<")
                /** 所有权限都已允许 */
                onPermissionResult(PermissionResult.PermissionGranted(requestCode))
            }
//            !isAllow -> {
//                Log.i(">>>>> ", " requestPermissions custom isAllow is false <<<")
//                /** 第一次需要弹窗提示声明，这个属于业务逻辑 */
//                onPermissionResult(PermissionResult.ShowInitial(requestCode, notGranted))
//            }
            notGranted.any { shouldShowRequestPermissionRationale(it) } -> {
                Log.i(">>>>> ", " requestPermissions not granted list all shouldShowRequestPermissionRationale is true <<<")
                //第一次申请后禁止，再次申请走这里
//                onPermissionResult(PermissionResult.ShowRational(requestCode, notGranted))
                requestPermissions(notGranted.toTypedArray(), requestCode)
            }
            !rationalRequest.containsKey(requestCode) && isAllow -> {
                Log.i(">>>>> ", " requestPermissions show initial <<<")
                rationalRequest[requestCode] = true
                onPermissionResult(PermissionResult.ShowInitial(requestCode, permissions.asList()))
            }
            else -> {
                Log.i(">>>>> ", " requestPermissions else result <<<")

                //第一次申请走else
                requestPermissions(notGranted.toTypedArray(), requestCode)
            }
        }
    }


    protected abstract fun onPermissionResult(permissionResult: PermissionResult)

    protected fun clearRequest(requestCode: Int){
        rationalRequest.remove(requestCode)
    }

    override fun onDestroy() {
        super.onDestroy()
        rationalRequest.clear()
    }
}