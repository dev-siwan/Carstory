package com.like.drive.motorfeed.remote.common

import android.net.Uri
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.StorageReference
import com.like.drive.motorfeed.common.ResultState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream


class FireBaseTask {
    suspend fun <T> getData(doc: DocumentReference, objectClass: Class<T>): ResultState<T> =
        withContext(Dispatchers.IO) {
            try {
                val snapShot = doc.get().await()

                if (snapShot.exists()) {
                    val result = snapShot.toObject(objectClass)
                    return@withContext ResultState.Success(result!!)
                } else {
                    return@withContext ResultState.Error(emptyDocument = true)
                }
            } catch (e: FirebaseFirestoreException) {
                return@withContext ResultState.Error(e)
            }
        }

    suspend fun <R, M> getData(ref: R, objectClass: Class<M>): ResultState<List<M>> =
        withContext(Dispatchers.IO) {
            when (ref) {
                is CollectionReference -> {
                    try {
                        val snapShot = ref.get().await()
                        val result = snapShot.toObjects(objectClass)
                        return@withContext ResultState.Success(result)
                    } catch (e: FirebaseFirestoreException) {
                        return@withContext ResultState.Error(e)
                    }
                }

                is Query -> {
                    try {
                        val snapShot = ref.get().await()
                        val result = snapShot.toObjects(objectClass)
                        return@withContext ResultState.Success(result)
                    } catch (e: FirebaseFirestoreException) {
                        return@withContext ResultState.Error(e)
                    }
                }

                else -> ResultState.Error(Exception("Not found Reference"))
            }
        }

    suspend fun <R> setData(ref: R, data: Any): ResultState<Boolean> = withContext(Dispatchers.IO) {
        when (ref) {
            is DocumentReference -> {
                try {
                    val snapShot = ref.set(data).await()
                    return@withContext ResultState.Success(Tasks.forResult(snapShot).isSuccessful)
                } catch (e: FirebaseFirestoreException) {
                    return@withContext ResultState.Error(e)
                }
            }

            is CollectionReference -> {
                try {
                    val snapShot = ref.add(data).await()
                    return@withContext ResultState.Success(Tasks.forResult(snapShot).isSuccessful)
                } catch (e: FirebaseFirestoreException) {
                    return@withContext ResultState.Error(e)
                }
            }

            else -> ResultState.Error(Exception("Not found Reference"))
        }
    }

    suspend fun updateData(
        ref: DocumentReference,
        date: Any,
        fieldName: String
    ): ResultState<Boolean> =
        withContext(Dispatchers.IO) {
            try {
                val snapShot = ref.update(fieldName, date).await()
                return@withContext ResultState.Success(Tasks.forResult(snapShot).isSuccessful)
            } catch (e: FirebaseFirestoreException) {
                return@withContext ResultState.Error(e)
            }
        }

    suspend fun delete(ref: DocumentReference): ResultState<Boolean> =
        withContext(Dispatchers.IO) {
            try {
                val snapShot = ref.delete().await()
                return@withContext ResultState.Success(Tasks.forResult(snapShot).isSuccessful)
            } catch (e: FirebaseFirestoreException) {
                return@withContext ResultState.Error(e)
            }
        }


    suspend fun setStorageImage(ref: StorageReference, file: File): ResultState<Uri> =
        withContext(Dispatchers.IO) {
            try {
                val uploadTask = ref.putStream(FileInputStream(file)).await()
                if (uploadTask.task.isSuccessful) {
                    return@withContext ResultState.Success(uploadTask.storage.downloadUrl.await())
                } else {
                    return@withContext ResultState.Error(Exception(""))
                }
            } catch (e: StorageException) {
                return@withContext ResultState.Error(e)
            }
        }

}