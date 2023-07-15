package com.sven.rainbowbeachlib.service

import android.content.Context
import android.os.Environment
import com.koushikdutta.async.AsyncServer
import com.koushikdutta.async.http.body.MultipartFormDataBody
import com.koushikdutta.async.http.server.AsyncHttpServer
import com.koushikdutta.async.http.server.AsyncHttpServerRequest
import com.koushikdutta.async.http.server.AsyncHttpServerResponse
import com.sven.rainbowbeachlib.tools.CommonUtils
import com.sven.rainbowbeachlib.tools.Constants
import com.sven.rainbowbeachlib.tools.RbbLogUtils
import com.sven.rainbowbeachlib.tools.RbbUtils
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.net.URLDecoder
import java.nio.charset.Charset


/**
 * @Author:         xwp
 * @CreateDate:     2023/7/13
 * @Version:        1.0
 */
class LocalFileServer {

    companion object {
        const val FILE_TYPE_IMG = 1
        const val FILE_TYPE_VIDEO = 2
    }

    private val server = AsyncHttpServer()
    private val mAsyncServer = AsyncServer()
    private lateinit var mContext: Context
    private var isRecivedFileData = false

    fun start(context: Context) {
        mContext = context
        server.get(
            "/"
        ) { request, response ->
            request ?: return@get
            response ?: return@get
            try {
                response.send(getIndexContent(context));
            } catch (e: IOException) {
                e.printStackTrace();
                response.code(500)?.end();
            }
        };

        server.get("/jquery-1.7.2.min.js") { request, response ->
            try {
                request ?: return@get
                response ?: return@get
                var fullPath = request.path
                fullPath = fullPath.replace("%20", " ");
                var resourceName = fullPath
                if (resourceName.startsWith("/")) {
                    resourceName = resourceName.substring(1)
                }
                if (resourceName.indexOf("?") > 0) {
                    resourceName = resourceName.substring(0, resourceName.indexOf("?"))
                }
                response.setContentType("application/javascript");
                val bInputStream = BufferedInputStream(
                    mContext.assets.open(
                        resourceName
                    )
                )
                response.sendStream(bInputStream, bInputStream.available().toLong())
            } catch (e: Exception) {
                e.printStackTrace();
                response.code(404).end();
            }
        }


        server.post("/upload-ajax") { request, response ->
            request ?: return@post
            response ?: return@post
            val name = request.query["name"]?.get(0)
            RbbLogUtils.logInfo("upload file name = $name")
            val uploadFilePath =
                mContext.externalCacheDir?.absolutePath + Constants.UPLOAD_PATH + name
            val outputFile =
                File(uploadFilePath)

            if (outputFile.parentFile?.exists() != true) {
                //父目录不存在 创建父目录
                outputFile.parentFile?.mkdirs() != true
            }
            if (!outputFile.exists()) {
                outputFile.createNewFile()
            }

            handleFileUpload(request, response, outputFile)
        }


        // 查看图片和视频的接口
        server.get("/files/.*") { request, response ->
            request ?: return@get
            response ?: return@get
            var path = request.path.replace("/files/", "")
            try {
                path = URLDecoder.decode(path, "utf-8")
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }
            val file = File(path);
            if (file.exists() && file.isFile) {
                try {
                    val fis = FileInputStream(file)
                    response.sendStream(fis, fis.available().toLong())
                } catch (e: Exception) {
                    e.printStackTrace();
                }
                return@get;
            }
            response.code(404).send("Not found!")
        }


        server.get("/files") { request, response ->
            request ?: return@get
            response ?: return@get
            val path = request.query["path"]?.get(0)
            var queryFilePath = Environment.getExternalStorageDirectory().path

            if (path == "images") {
                getAllFilesByType(response, FILE_TYPE_IMG);
                return@get
            } else if (path == "videos") {
                getAllFilesByType(response, FILE_TYPE_VIDEO);
                return@get
            } else if (path == "appData") {
                queryFilePath = mContext.externalCacheDir?.parentFile?.absolutePath
                    ?: Environment.getExternalStorageDirectory().path
            } else if (path == "appPrivateData") {
                queryFilePath = mContext.applicationInfo.dataDir
            } else {
                if (path != null && path.isNotEmpty() && path.startsWith("/")) {
                    queryFilePath = path
                }
            }

            var queryFile = File(queryFilePath)

            val array = JSONArray()
            val fileList = queryFile.listFiles()

            fileList?.forEach { file ->
                if (file.exists()) {
                    try {
                        val jsonObject = JSONObject()
                        jsonObject.put("name", file.name)
                        jsonObject.put("path", file.absolutePath)
                        if (file.isDirectory) {
                            jsonObject.put("type", 3)
                        } else if (file.path.endsWith(".jpg")
                            || file.path.endsWith(".jpeg")
                            || file.path.endsWith(".png")
                            || file.path.endsWith(".gif")
                        ) {
                            jsonObject.put("type", 1)
                        } else if (file.path.endsWith(".mp4")) {
                            jsonObject.put("type", 2)
                        } else {
                            jsonObject.put("type", 0)
                        }
                        array.put(jsonObject)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }
            response.send(array.toString())
        }

        server.listen(mAsyncServer, 9090)

        server.setErrorCallback {

        }

        RbbLogUtils.logInfo("文件服务器已启动：http://${CommonUtils.getHostIP()}:9090")
        RbbUtils.showToast(mContext, "文件服务器已启动：http://${CommonUtils.getHostIP()}:9090")
    }

    private fun getAllFilesByType(response: AsyncHttpServerResponse, fileType: Int) {
        val array = JSONArray()
        val findFileList = mutableListOf<File>()
        val rootFileList = mutableListOf(
            File(Environment.getExternalStorageDirectory().path + "/DCIM"),
            File(Environment.getExternalStorageDirectory().path + "/Documents"),
            File(Environment.getExternalStorageDirectory().path + "/Download"),
        )
        getFileByType(rootFileList, findFileList, fileType)
        findFileList.sortByDescending {
            it.lastModified()
        }
        findFileList.forEach { file ->
            val jsonObject = JSONObject()
            jsonObject.put("name", file.name)
            jsonObject.put("path", file.absolutePath)
            if (fileType == FILE_TYPE_IMG) {
                jsonObject.put("type", 1)
            } else if (fileType == FILE_TYPE_VIDEO) {
                jsonObject.put("type", 2)
            }
            array.put(jsonObject)
        }
        response.send(array.toString())
    }

    private fun getFileByType(
        dirFileList: MutableList<File>,
        findFileList: MutableList<File>,
        fileType: Int
    ) {
        if (dirFileList.isNullOrEmpty()) {
            return
        }
        val dirFileListTemp = mutableListOf<File>()
        dirFileList.forEach { dirFile ->
            dirFile.listFiles()?.forEach { file ->
                if (file.exists()) {
                    if (file.isFile) {
                        if (fileType == FILE_TYPE_IMG
                            && (file.path.endsWith(".jpg")
                                    || file.path.endsWith(".jpeg")
                                    || file.path.endsWith(".png")
                                    || file.path.endsWith(".gif")
                                    )
                        ) {
                            findFileList.add(file)
                        } else if (fileType == FILE_TYPE_VIDEO && file.path.endsWith(".mp4")) {
                            findFileList.add(file)
                        }
                    } else {
                        dirFileListTemp.add(file)
                    }
                }
            }
        }

        if (dirFileListTemp.isNotEmpty()) {
            getFileByType(dirFileListTemp, findFileList, fileType)
        }
    }

    fun destroy() {
        server.stop();
        mAsyncServer.stop();
    }


    @Throws(IOException::class)
    private fun getIndexContent(context: Context): String? {
        var bInputStream: BufferedInputStream? = null
        return try {
            bInputStream = BufferedInputStream(context.assets.open("index.html"))
            val baos = ByteArrayOutputStream()
            var len = 0
            val tmp = ByteArray(10240)
            while (bInputStream.read(tmp).also { len = it } > 0) {
                baos.write(tmp, 0, len)
            }
            String(baos.toByteArray(), Charset.forName("UTF-8"))
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


    private fun handleFileUpload(
        request: AsyncHttpServerRequest,
        response: AsyncHttpServerResponse,
        targetFile: File
    ) {
        val outputStream = FileOutputStream(targetFile)
        val multipartFormDataBody = request.getBody() as MultipartFormDataBody
        multipartFormDataBody.setMultipartCallback {
            if (it.isFile) {
                //  文件
                multipartFormDataBody.setDataCallback { emitter, bb ->
                    val bytes = ByteArray(bb.remaining())
                    RbbLogUtils.logInfo("handleFileUpload len = ${bytes.size}")
                    bb.get(bytes)
                    outputStream.write(bytes, 0, bytes.size)
                    bb.recycle()
                }
            } else {
                // 非文件
                if (multipartFormDataBody.dataCallback == null) {
                    multipartFormDataBody.setDataCallback { emitter, bb ->
                        bb.recycle()
                    }
                }
            }
        }

        request.setEndCallback {
            RbbLogUtils.logInfo("handleFileUpload end")
            response.send("文件上传完毕")
            response.end()
        }
    }
}