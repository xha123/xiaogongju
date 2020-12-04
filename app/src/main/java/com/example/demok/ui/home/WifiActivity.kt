package com.example.demok.ui.home

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Resources.NotFoundException
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.baselib.utils.setOnClickListenerWithTrigger
import com.example.baselib.utils.showToast
import com.example.demok.R
import com.example.demok.base.MyBaseActivity
import com.example.demok.net.data.InfoModel
import com.example.demok.ui.adapter.WifiAdapter
import com.example.demok.ui.dialog.PopupMenuDialog
import com.example.demok.utils.ConstWifi
import com.example.demok.utils.WebService
import com.hwangjr.rxbus.RxBus
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.annotation.Tag
import com.hwangjr.rxbus.thread.EventThread
import kotlinx.android.synthetic.main.activity_wifi.*
import kotlinx.android.synthetic.main.content_main.*
import timber.log.Timber
import java.io.File
import java.text.DecimalFormat

/**
 * @author: xha
 * @date: 2020/9/29 16:37
 * @Description:apk快传
 */
class WifiActivity : MyBaseActivity() {

    private val wifiAdapter by lazy {
        WifiAdapter()
    }

    companion object {
        @JvmStatic
        fun start(
            context: Activity
        ) {
            val starter = Intent(context, WifiActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_wifi
    }

    override fun initOnCreate() {
        setTitleText("apk 快传")
        setRightText("清空")
        RxBus.get().register(this)
        setAdapter()
        RxBus.get().post(ConstWifi.RxBusEventType.LOAD_BOOK_LIST, 0)
    }

    override fun initOnClickerListener() {
        super.initOnClickerListener()
        fab.setOnClickListenerWithTrigger {
            if (requestPermission()) {
                WebService.start(this)
                PopupMenuDialog(this).builder().setCancelable(false)
                    .setCanceledOnTouchOutside(false).show()
            }
        }
        setRightButton(View.OnClickListener {
            val mApps = wifiAdapter.data
            if (mApps.isNullOrEmpty()) {
                showToast("暂无内容")
            } else {
                showHintDialog()
            }
        })
    }

    //显示确认对话框
    private fun showHintDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("温馨提示:")
        builder.setMessage("确定全部删除吗？")
        builder.setPositiveButton(
            "确定"
        ) { dialog, _ ->
            dialog.dismiss()
            deleteAll()
        }
        builder.setNegativeButton(
            "取消"
        ) { dialog, _ -> dialog.dismiss() }
        builder.show()
    }

    @Subscribe(tags = [Tag(ConstWifi.RxBusEventType.POPUP_MENU_DIALOG_SHOW_DISMISS)])
    fun onPopupMenuDialogDismiss(type: Int) {
//        if (type === Const.MSG_DIALOG_DISMISS) {
//            WebService.stop(this)
//        }
    }


    override fun onDestroy() {
        super.onDestroy()
        WebService.stop(this)
        RxBus.get().unregister(this)
    }


    var allpermissions = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    private fun requestPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= 23) {
            var needapply = false
            for (i in allpermissions.indices) {
                val chechpermission = ContextCompat.checkSelfPermission(
                    applicationContext,
                    allpermissions.get(i)
                )
                if (chechpermission != PackageManager.PERMISSION_GRANTED) {
                    needapply = true
                }
            }
            if (needapply) {
                ActivityCompat.requestPermissions(this, allpermissions, 1)
            }
            return !needapply
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for (i in grantResults.indices) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, permissions[i] + "已授权", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, permissions[i] + "授权通过方能使用", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun setAdapter() {
        mRecyclerviewMain.layoutManager = LinearLayoutManager(this)
        mRecyclerviewMain.adapter = wifiAdapter

        val viewEmpty = View.inflate(this, R.layout.empty_view, null)
        wifiAdapter.setEmptyView(viewEmpty)

        mSwipeRefreshLayoutMain.setOnRefreshListener {
            RxBus.get().post(ConstWifi.RxBusEventType.LOAD_BOOK_LIST, 0)
        }

        wifiAdapter.addChildClickViewIds(R.id.tv_install, R.id.tv_delete)
        wifiAdapter.setOnItemChildClickListener { adapter, view, position ->
            val infoModel = adapter.getItem(position) as InfoModel
            when (view.id) {
                R.id.tv_install -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        val installAllowed = packageManager.canRequestPackageInstalls()
                        if (installAllowed) {
                            installApkFile(
                                this,
                                File(infoModel.path)
                            )
                        } else {
                            val intent = Intent(
                                Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, Uri.parse(
                                    "package:$packageName"
                                )
                            )
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            installApkFile(
                                this,
                                File(infoModel.path)
                            )
                        }
                    } else {
                        installApkFile(this, File(infoModel.path))
                    }
                }
                R.id.tv_delete -> {
                    delete(this, infoModel.packageName)
                }
            }
        }
    }

    //安装
    fun installApkFile(context: Context, file: File?) {
        val intent = Intent(Intent.ACTION_VIEW)
        //兼容7.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            val contentUri: Uri =
                FileProvider.getUriForFile(context, context.packageName + ".fileprovider", file!!)
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive")
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        if (context.packageManager.queryIntentActivities(intent, 0).size > 0) {
            context.startActivity(intent)
        }
    }

    //卸载
    fun delete(context: Context, packageName: String?) {
        val uri = Uri.fromParts("package", packageName, null)
        val intent = Intent(Intent.ACTION_DELETE, uri)
        context.startActivity(intent)
    }

    //删除所有文件
    private fun deleteAll() {
        val dir = ConstWifi.DIR
        if (dir.exists() && dir.isDirectory) {
            val fileNames = dir.listFiles()
            if (fileNames != null) {
                for (fileName in fileNames) {
                    fileName.delete()
                }
            }
        }
        RxBus.get().post(ConstWifi.RxBusEventType.LOAD_BOOK_LIST, 0)
    }

    var mApps: ArrayList<InfoModel> = ArrayList()

    @Subscribe(thread = EventThread.IO, tags = [Tag(ConstWifi.RxBusEventType.LOAD_BOOK_LIST)])
    fun loadAppList(type: Int?) {
        Timber.d("loadAppList:" + Thread.currentThread().name)
        val listArr: MutableList<InfoModel> = arrayListOf()
        val dir = ConstWifi.DIR
        if (dir.exists() && dir.isDirectory) {
            val fileNames = dir.listFiles()
            if (fileNames != null) {
                for (fileName in fileNames) {
                    handleApk(fileName.absolutePath, fileName.length(), listArr)
                }
            }
        }
        runOnUiThread {
            mSwipeRefreshLayoutMain.isRefreshing = false
            mApps.clear()
            mApps.addAll(listArr)
            wifiAdapter.setList(mApps)
        }
    }

    //获取apk信息
    private fun handleApk(path: String, length: Long, list: MutableList<InfoModel>?) {
        val infoModel =
            InfoModel(
                "", "", "", "", "",
                false, ContextCompat.getDrawable(this, R.drawable.back)
            )
        var archiveFilePath = ""
        archiveFilePath = path
        val pm = packageManager
        val info = pm.getPackageArchiveInfo(archiveFilePath, 0)
        if (info != null) {
            val appInfo = info.applicationInfo
            appInfo.sourceDir = archiveFilePath
            appInfo.publicSourceDir = archiveFilePath
            val packageName = appInfo.packageName //得到安装包名称
            val version = info.versionName //得到版本信息
            var icon = pm.getApplicationIcon(appInfo)
            var appName: String? = pm.getApplicationLabel(appInfo).toString()
            if (TextUtils.isEmpty(appName)) {
                appName = getApplicationName(packageName)
            }
            if (icon == null) {
                icon = getIconFromPackageName(packageName, this) // 获得应用程序图标
            }
            infoModel.name = appName!!
            infoModel.packageName = (packageName)
            infoModel.path = (path)
            infoModel.size = (getFileSize(length)!!)
            infoModel.version = (version)
            infoModel.icon = (icon)
            infoModel.installed = (isAvilible(this, packageName))
            list?.add(infoModel) ?: mApps.add(infoModel)
        }
    }

    @Synchronized
    fun getIconFromPackageName(packageName: String?, context: Context): Drawable? {
        val pm = context.packageManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            try {
                val pi = pm.getPackageInfo(packageName, 0)
                val otherAppCtx = context.createPackageContext(packageName, CONTEXT_IGNORE_SECURITY)
                val displayMetrics = intArrayOf(
                    DisplayMetrics.DENSITY_XXXHIGH,
                    DisplayMetrics.DENSITY_XXHIGH,
                    DisplayMetrics.DENSITY_XHIGH,
                    DisplayMetrics.DENSITY_HIGH,
                    DisplayMetrics.DENSITY_TV
                )
                for (displayMetric in displayMetrics) {
                    try {
                        val d = otherAppCtx.resources.getDrawableForDensity(
                            pi.applicationInfo.icon,
                            displayMetric
                        )
                        if (d != null) {
                            return d
                        }
                    } catch (e: NotFoundException) {
                        continue
                    }
                }
            } catch (e: Exception) {
                // Handle Error here
            }
        }
        var appInfo: ApplicationInfo? = null
        appInfo = try {
            pm.getApplicationInfo(packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            return null
        }
        return appInfo?.loadIcon(pm)
    }

    /**
     * 判断相对应的APP是否存在
     *
     * @param context
     * @param packageName(包名)(若想判断QQ，则改为com.tencent.mobileqq，若想判断微信，则改为com.tencent.mm)
     * @return
     */
    fun isAvilible(context: Context, packageName: String?): Boolean {
        val packageManager = context.packageManager

        //获取手机系统的所有APP包名，然后进行一一比较
        val pinfo = packageManager.getInstalledPackages(0)
        for (i in pinfo.indices) {
            if ((pinfo[i] as PackageInfo).packageName
                    .equals(packageName, ignoreCase = true)
            ) return true
        }
        return false
    }

    private fun getFileSize(length: Long): String? {
        val df = DecimalFormat("######0.00")
        val d1 = 3.23456
        val d2 = 0.0
        val d3 = 2.0
        df.format(d1)
        df.format(d2)
        df.format(d3)
        val l = length / 1000 //KB
        if (l < 1024) {
            return df.format(l) + "KB"
        } else if (l < 1024 * 1024f) {
            return df.format((l / 1024f).toDouble()) + "MB"
        }
        return df.format(l / 1024f / 1024f.toDouble()) + "GB"
    }

    fun getApplicationName(packageName: String?): String? {
        var packageManager: PackageManager? = null
        var applicationInfo: ApplicationInfo? = null
        try {
            packageManager = applicationContext.packageManager
            applicationInfo = packageManager.getApplicationInfo(packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            applicationInfo = null
        }
        return if (packageManager != null && applicationInfo != null) {
            packageManager.getApplicationLabel(applicationInfo) as String
        } else packageName
    }


}