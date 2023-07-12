package com.sven.rainbowbeachlib.tools

import java.io.File

/**
 * @Author:         xwp
 * @CreateDate:     2023/7/12
 * @Version:        1.0
 */
object FileUtils {


    fun createFolder(path: String) {
        //新建一个File，传入文件夹目录
        val file = File(path)
        //判断文件夹是否存在，如果不存在就创建，否则不创建
        if (!file.exists()) {
            //通过file的mkdirs()方法创建目录中包含却不存在的文件夹
            file.mkdirs()
        }
    }
}