package com.example.ada.data.remote

import android.content.Context
import android.net.Uri
import com.cloudinary.android.MediaManager
import com.example.ada.utils.CloudinaryConfig
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

suspend fun subirIneACloudinary(
    context: Context,
    ineUri: Uri
): String = suspendCancellableCoroutine { continuation ->

    MediaManager.get()
        .upload(ineUri)
        .unsigned(CloudinaryConfig.UPLOAD_PRESET)
        .option("folder", "ada/ines")
        .callback(object : com.cloudinary.android.callback.UploadCallback {
            override fun onStart(requestId: String?) {}

            override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {}

            override fun onSuccess(
                requestId: String?,
                resultData: MutableMap<Any?, Any?>?
            ) {
                val url = resultData?.get("secure_url") as? String

                if (url != null) {
                    continuation.resume(url)
                } else {
                    continuation.resumeWithException(
                        Exception("Cloudinary no devolvió secure_url")
                    )
                }
            }

            override fun onError(
                requestId: String?,
                error: com.cloudinary.android.callback.ErrorInfo?
            ) {
                continuation.resumeWithException(
                    Exception(error?.description ?: "Error al subir imagen")
                )
            }

            override fun onReschedule(
                requestId: String?,
                error: com.cloudinary.android.callback.ErrorInfo?
            ) {}
        })
        .dispatch()
}
