package com.like.drive.carstory.remote.common

import android.net.Uri
import bolts.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.io.File
import java.io.FileInputStream

class FireBaseTask {
    suspend fun <C> getData(doc: DocumentReference, objectClass: Class<C>): Flow<C?> =
        flow {
            val snapShot = doc.get().await()
            if (snapShot.exists())
                emit(snapShot.toObject(objectClass))
            else
                emit(null)
        }

    suspend fun <R, M> getData(ref: R, objectClass: Class<M>): Flow<List<M>> =
        flow {
            when (ref) {
                is CollectionReference -> {
                    val snapShot = ref.get().await()
                    val result = snapShot.toObjects(objectClass)
                    emit(result)
                }

                is Query -> {
                    val snapShot = ref.get().await()
                    val result = snapShot.toObjects(objectClass)
                    emit(result)
                }

                else -> emit(emptyList<M>())
            }
        }

    suspend fun <R> setData(ref: R, data: Any): Flow<Boolean> =
        flow {
            when (ref) {
                is DocumentReference -> {
                    val snapShot = ref.set(data).await()
                    emit(Task.forResult(snapShot).isCompleted)
                }

                is CollectionReference -> {
                    val snapShot = ref.add(data).await()
                    emit(Task.forResult(snapShot).isCompleted)
                }

                else -> emit(false)
            }
        }

    suspend fun updateData(
        ref: DocumentReference,
        map: Map<String, Any>
    ): Flow<Boolean> =
        flow {
            val snapShot = ref.update(map).await()
            emit(Tasks.forResult(snapShot).isComplete)
        }

    suspend fun delete(ref: DocumentReference): Flow<Boolean> =
        flow {
            val snapShot = ref.delete().await()
            emit(Tasks.forResult(snapShot).isComplete)
        }

    suspend fun uploadImage(ref: StorageReference, file: File): Flow<Uri?> =
        flow {
            val uploadTask = ref.putStream(FileInputStream(file)).await()
            if (uploadTask.task.isComplete) {
                emit(uploadTask.storage.downloadUrl.await())
            } else {
                emit(null)
            }
        }

    suspend fun uploadProfileImage(ref: StorageReference, file: File): Flow<Boolean> =
        flow {
            val uploadTask = ref.putStream(FileInputStream(file)).await()
            emit(uploadTask.task.isComplete)
        }

    suspend fun deleteImage(ref: StorageReference): Flow<Boolean> =
        flow {
            val deleteTask = ref.delete().await()
            emit(Tasks.forResult(deleteTask).isComplete)
        }

    suspend fun setFunction(map: Map<String, Any?>, callableName: String): Flow<Any> =
        flow {
            emit(taskFunction(map, callableName).await())
        }

    private fun taskFunction(
        map: Map<String, Any?>,
        callableName: String
    ): com.google.android.gms.tasks.Task<Any> {
        return FirebaseFunctions.getInstance("asia-northeast3")
            .getHttpsCallable(callableName)
            .call(map)
            .continueWith {
                it.result?.data ?: ""
            }
    }

}
