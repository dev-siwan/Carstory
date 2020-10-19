package com.like.drive.carstory.remote.api.admin

import com.google.firebase.firestore.FirebaseFirestore
import com.like.drive.carstory.remote.common.FireBaseTask
import com.like.drive.carstory.remote.reference.CollectionName
import kotlinx.coroutines.flow.Flow

class AdminApiImpl(
    private val fireBaseTask: FireBaseTask,
    private val fireStore: FirebaseFirestore
) : AdminApi {

    private val ref = fireStore.collection(CollectionName.USER)

    override suspend fun sendUserMessage(
        uid: String,
        message: String
    ): Flow<Boolean> {

        val document = ref.document(uid)

        return fireBaseTask.updateData(
            document,
            mapOf(USER_MESSAGE_FIELD to message, USER_MESSAGE_STATUS to true)
        )

    }

    override suspend fun setUserBan(
        uid: String,
        message: String
    ): Flow<Boolean> {
        val document = ref.document(uid)

        return fireBaseTask.updateData(
            document,
            mapOf(USER_MESSAGE_FIELD to message, USER_BAN_FIELD to true)
        )
    }

    companion object {
        const val USER_MESSAGE_FIELD = "userMessage"
        const val USER_BAN_FIELD = "userBan"
        const val USER_MESSAGE_STATUS = "userMessageStatus"
    }

}
