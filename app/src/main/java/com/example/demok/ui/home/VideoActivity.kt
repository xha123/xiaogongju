package com.example.demok.ui.home

import android.app.Activity
import android.content.Intent
import android.net.NetworkInfo
import android.os.Environment
import android.text.TextUtils
import com.example.baselib.utils.LogUtils
import com.example.demok.R
import com.example.demok.base.MyBaseActivity
import com.example.demok.utils.ConstWifi
import com.example.demok.utils.ConstWifi.HTTP_VIDEO
import com.example.demok.utils.WifiUtils
import com.koushikdutta.async.AsyncServer
import com.koushikdutta.async.http.server.AsyncHttpServer
import com.koushikdutta.async.http.server.HttpServerRequestCallback
import kotlinx.android.synthetic.main.activity_video.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.net.URLDecoder


/**
 * @author: xha
 * @date: 2020/9/30 10:19
 * @Description:
 */
class VideoActivity : MyBaseActivity() {
    private val server = AsyncHttpServer()
    private val mAsyncServer = AsyncServer()

    companion object {
        @JvmStatic
        fun start(
            context: Activity
        ) {
            val starter = Intent(context, VideoActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_video
    }

    override fun initOnCreate() {
        setTitleText("视频播放")

        setServer()


        checkWifiState(WifiUtils.getWifiConnectState(this))
    }

    private fun setServer() {
        server.get("/",
            HttpServerRequestCallback { request, response ->
                try {
                    response.send(getIndexContent())
                } catch (e: IOException) {
                    e.printStackTrace()
                    response.code(500).end()
                }
            })

        server.get("/files", HttpServerRequestCallback { request, response ->
            val files = getFileStr(Environment.getExternalStorageDirectory().path)
            response.send(array.toString())
        })

        server.get("/video/jquery-1.7.2.min.js", HttpServerRequestCallback { request, response ->
            try {
                var fullPath = request.path
                fullPath = fullPath.replace("%20", " ")
                var resourceName = fullPath
                if (resourceName.startsWith("/")) {
                    resourceName = resourceName.substring(1)
                }
                if (resourceName.indexOf("?") > 0) {
                    resourceName = resourceName.substring(0, resourceName.indexOf("?"))
                }
                response.setContentType("application/javascript")
                val bInputStream = BufferedInputStream(assets.open(resourceName))
                response.sendStream(bInputStream, bInputStream.available().toLong())
            } catch (e: IOException) {
                e.printStackTrace()
                response.code(404).end()
                return@HttpServerRequestCallback
            }
        })

        server.get("/files/.*", HttpServerRequestCallback { request, response ->
            var path: String? = request.path.replace("/files/", "")
            try {
                path = URLDecoder.decode(path, "utf-8")
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }
            val file = File(path)
            if (file.exists() && file.isFile) {
                try {
                    val fis = FileInputStream(file)
                    response.sendStream(fis, fis.available().toLong())
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                return@HttpServerRequestCallback
            }
            response.code(404).send("Not found!")
        })

        server.listen(mAsyncServer, HTTP_VIDEO)
    }

    val array = JSONArray()

    private fun getFileStr(filepath: String) {
        val dir = File(filepath)
        val fileNames: Array<String> = dir.list()
        LogUtils.i("文件列表", "" + fileNames.size)
        for (fileName in fileNames) {
            val file = File(dir, fileName)
            if (file.exists() && file.isDirectory) {
                getFileStr(file.absolutePath)
            } else if (file.exists() && file.isFile() && file.getName().endsWith(".mp4")) {
                LogUtils.i("MP4文件")
                try {
                    val jsonObject = JSONObject()
                    jsonObject.put("name", fileName)
                    jsonObject.put("path", file.getAbsolutePath())
                    array.put(jsonObject)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }

    }

    private fun checkWifiState(state: NetworkInfo.State) {
        if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
            if (state == NetworkInfo.State.CONNECTED) {
                val ip = WifiUtils.getWifiIp(this)
                if (!TextUtils.isEmpty(ip)) {
                    tvHttpShow.text = kotlin.String.format(
                        getString(R.string.http_address),
                        ip,
                        ConstWifi.HTTP_VIDEO
                    )
                    return
                }
            }
            return
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        server.stop()
        mAsyncServer.stop()
    }

    @Throws(IOException::class)
    private fun getIndexContent(): String? {
        var bInputStream: BufferedInputStream? = null
        return try {
            bInputStream = BufferedInputStream(assets.open("video/index.html"))
            val baos = ByteArrayOutputStream()
            var len = 0
            val tmp = ByteArray(10240)
            while (bInputStream.read(tmp).also { len = it } > 0) {
                baos.write(tmp, 0, len)
            }
            String(baos.toByteArray())
        } catch (e: IOException) {
            e.printStackTrace()
            throw e
        } finally {
            if (bInputStream != null) {
                try {
                    bInputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

}