package com.like.drive.carstory.util.photo

import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.exifinterface.media.ExifInterface
import timber.log.Timber
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

object ImageResizeUtil {

    fun resizeFile(file: File, newFile: File, newWidth: Int) {

        var originalBm: Bitmap? = null
        var resizedBitmap: Bitmap? = null

        try {

            val options = BitmapFactory.Options()
            options.inBitmap
            options.inMutable = true


            originalBm = BitmapFactory.decodeFile(file.absolutePath, options)

            // 카메라인 경우 이미지를 상황에 맞게 회전시킨다
            try {

                val exif = ExifInterface(file.absolutePath)
                val exifOrientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL
                )
                val exifDegree = exifOrientationToDegrees(exifOrientation)
                Timber.d("exifDegree : $exifDegree")

                originalBm = rotate(originalBm, exifDegree)

            } catch (e: IOException) {
                e.printStackTrace()
            }


            if (originalBm == null) {
                //Timber.e("파일 에러")
                return
            }

            val width = originalBm.width
            val height = originalBm.height

            val aspect: Float
            val scaleWidth: Float
            val scaleHeight: Float
            if (width > height) {
                if (width <= newWidth) return

                aspect = width.toFloat() / height

                scaleWidth = newWidth.toFloat()
                scaleHeight = scaleWidth / aspect

            } else {

                if (height <= newWidth) return

                aspect = height.toFloat() / width

                scaleHeight = newWidth.toFloat()
                scaleWidth = scaleHeight / aspect

            }

            // create a matrix for the manipulation
            val matrix = Matrix()

            // resize the bitmap
            matrix.postScale(scaleWidth / width, scaleHeight / height)

            // recreate the new Bitmap
            resizedBitmap = Bitmap.createBitmap(originalBm, 0, 0, width, height, matrix, true)

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {

                resizedBitmap!!.compress(CompressFormat.JPEG, 80, FileOutputStream(newFile))

            } else {

                resizedBitmap!!.compress(CompressFormat.PNG, 80, FileOutputStream(newFile))

            }

        } catch (e: FileNotFoundException) {

            e.printStackTrace()

        } finally {

            originalBm?.recycle()

            resizedBitmap?.recycle()
        }

    }

    /**
     * EXIF 정보를 회전각도로 변환하는 메서드
     *
     * @param exifOrientation EXIF 회전각
     * @return 실제 각도
     */
    private fun exifOrientationToDegrees(exifOrientation: Int): Int {
        return when (exifOrientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }
    }

    /**
     * 이미지를 회전시킵니다.
     *
     * @param bitmap 비트맵 이미지
     * @param degrees 회전 각도
     * @return 회전된 이미지
     */
    private fun rotate(bitmap: Bitmap?, degrees: Int): Bitmap? {
        var rotationBitmap: Bitmap? = bitmap

        rotationBitmap?.let {
            if (degrees != 0) {
                val m = Matrix()
                m.setRotate(
                    degrees.toFloat(), it.width.toFloat() / 2,
                    it.height.toFloat() / 2
                )

                try {
                    val converted = Bitmap.createBitmap(
                        it, 0, 0,
                        it.width, it.height, m, true
                    )
                    if (bitmap != converted) {
                        it.recycle()
                        rotationBitmap = converted
                    }
                } catch (ex: OutOfMemoryError) {
                    // 메모리가 부족하여 회전을 시키지 못할 경우 그냥 원본을 반환합니다.
                }

            }

        }
        return rotationBitmap
    }
}