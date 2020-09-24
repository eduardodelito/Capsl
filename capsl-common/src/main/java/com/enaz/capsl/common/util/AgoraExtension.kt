package com.enaz.capsl.common.util

import android.content.Context
import android.os.Build
import android.os.Environment
import java.io.File

/**
 * Created by eduardo.delito on 9/24/20.
 */

private val LOG_FOLDER_NAME: String? = "log"
private const val LOG_FILE_NAME = "agora-rtc.log"

fun initializeLogFile(context: Context): String? {
    var folder: File?
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        folder = File(
            context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
            LOG_FOLDER_NAME
        )
    } else {
        val path = Environment.getExternalStorageDirectory()
            .absolutePath + File.separator +
                context.packageName + File.separator +
                LOG_FOLDER_NAME
        folder = File(path)
        if (!folder.exists() && !folder.mkdir()) folder = null
    }
    return if (folder != null && !folder.exists() && !folder.mkdir()) "" else File(
        folder,
        LOG_FILE_NAME
    ).absolutePath
}
