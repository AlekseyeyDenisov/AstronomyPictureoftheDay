package ru.dw.astronomypictureoftheday.data

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.request.ImageRequest
import ru.dw.astronomypictureoftheday.utils.CONSTANT_IMAGES_DIRECTORY
import ru.dw.astronomypictureoftheday.utils.CONSTANT_IMAGES_DOWNLOAD_ERROR
import ru.dw.astronomypictureoftheday.utils.CONSTANT_IMAGES_FORMAT
import java.io.File
import java.io.FileOutputStream
import java.lang.ref.WeakReference


class FileHelper(private val context: Context)  {
    private val imageLoader = ImageLoader(context)
    private var mContext: WeakReference<Context> = WeakReference(context)

    private var dir = context.getDir(CONSTANT_IMAGES_DIRECTORY, Context.MODE_PRIVATE)

    fun deleteFiles(fileName: String,): Boolean {
        val file = File(dir, fileName)
        return file.delete()

    }

    suspend fun downloadImages(url:String, fileName:String, successImagesName:(String)->Unit){
        val request = ImageRequest.Builder(context)
            .data(url)
            .build()
        val drawable = imageLoader.execute(request).drawable

        val bitmap2:Bitmap? = drawable?.toBitmap()
        bitmap2?.let {bimap->
            saveImages(bimap,fileName){
                successImagesName(it)
            }

        }

    }

    private fun saveImages(bitmap:Bitmap, fileName:String,successImagesName:(String)->Unit){
        mContext.get()?.let {
            val imagesName = fileName(fileName)
            try {
                val file = File(dir, imagesName)
                val out = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
                out.flush()
                out.close()
                successImagesName(imagesName)
            } catch (e: Exception) {
                successImagesName(CONSTANT_IMAGES_DOWNLOAD_ERROR)
            }
        }
    }

    private fun fileName(name:String):String{
        return "${name}.$CONSTANT_IMAGES_FORMAT"
    }

}