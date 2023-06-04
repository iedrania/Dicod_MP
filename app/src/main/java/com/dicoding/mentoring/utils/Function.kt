package com.dicoding.mentoring.utils

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

private const val FILENAME_FORMAT = "dd-mm-yyyy"

var timeStamp: String = SimpleDateFormat(
    FILENAME_FORMAT,
    Locale.US).format(System.currentTimeMillis())

fun createTemporaryFile(context: Context): File {
    val storageDirectory: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(timeStamp,".jpg", storageDirectory)
}

fun convUriToFile(selectedImage: Uri, context: Context) : File {
    val contentResolver : ContentResolver = context.contentResolver
    val convFile = createTemporaryFile(context)

    val inputStream = contentResolver.openInputStream(selectedImage) as InputStream
    val outputStream : OutputStream = FileOutputStream(convFile)
    var len : Int
    val bytes = ByteArray(1024)

    while (inputStream.read(bytes).also { len = it} > 0) outputStream.write(bytes,0,len)

    inputStream.close()
    outputStream.close()

    return convFile
}

fun reduceFileImage(file: File): File {
    val bitmap = BitmapFactory.decodeFile(file.path)

    var compressQuality = 100
    var streamLength: Int

    do {
        val bmpStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
        val bmpPicByteArray = bmpStream.toByteArray()
        streamLength = bmpPicByteArray.size
        compressQuality -= 5
    } while (streamLength > 1000000)

    bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))

    return file
}
