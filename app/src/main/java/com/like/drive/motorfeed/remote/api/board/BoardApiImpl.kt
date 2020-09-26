package com.like.drive.motorfeed.remote.api.board

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.like.drive.motorfeed.data.board.CommentData
import com.like.drive.motorfeed.data.board.BoardData
import com.like.drive.motorfeed.data.board.ReCommentData
import com.like.drive.motorfeed.data.motor.MotorTypeData
import com.like.drive.motorfeed.remote.common.FireBaseTask
import com.like.drive.motorfeed.remote.reference.CollectionName
import com.like.drive.motorfeed.remote.reference.CollectionName.BOARD_COMMENT
import com.like.drive.motorfeed.remote.reference.CollectionName.BOARD_RE_COMMENT
import com.like.drive.motorfeed.ui.board.data.LikeCountEnum
import com.like.drive.motorfeed.ui.board.category.data.CategoryData
import kotlinx.coroutines.flow.Flow
import java.util.*

class BoardApiImpl(
    private val fireBaseTask: FireBaseTask,
    private val fireStore: FirebaseFirestore
) : BoardApi {
    override suspend fun setFeed(boardData: BoardData): Flow<Boolean> {
        return fireBaseTask.setData(
            fireStore.collection(CollectionName.BOARD).document(boardData.bid ?: ""), boardData
        )
    }

    override suspend fun setUserFeed(uid: String, boardData: BoardData): Flow<Boolean> {
        return fireBaseTask.setData(
            fireStore.collection(CollectionName.USER).document(uid).collection(CollectionName.BOARD)
                .document(boardData.bid ?: ""), boardData
        )
    }

    override suspend fun removeFeed(boardData: BoardData): Flow<Boolean> {
        return fireBaseTask.delete(
            fireStore.collection(CollectionName.BOARD).document(boardData.bid ?: "")
        )
    }

    override suspend fun removeUserFeed(boardData: BoardData): Flow<Boolean> {
        return fireBaseTask.delete(
            fireStore.collection(CollectionName.USER).document(boardData.userInfo?.uid ?: "")
                .collection(CollectionName.BOARD)
                .document(boardData.bid ?: "")
        )
    }

    override suspend fun getComment(bid: String): Flow<List<CommentData>> {
        val collection =
            fireStore.collection(CollectionName.BOARD).document(bid).collection(BOARD_COMMENT)
        return fireBaseTask.getData(collection, CommentData::class.java)
    }

    override suspend fun getReComment(bid: String): Flow<List<ReCommentData>> {
        val collection =
            fireStore.collection(CollectionName.BOARD).document(bid).collection(BOARD_RE_COMMENT)
        return fireBaseTask.getData(collection, ReCommentData::class.java)
    }

    override suspend fun getFeed(bid: String): Flow<BoardData?> {
        val document = fireStore.collection(CollectionName.BOARD).document(bid)
        return fireBaseTask.getData(document, BoardData::class.java)
    }

    override suspend fun getFeedList(
        date: Date,
        motorTypeData: MotorTypeData?,
        cetegoryData: CategoryData?,
        tagStr: String?
    ): Flow<List<BoardData>> {
        val feedCollection = fireStore.collection(CollectionName.BOARD)

        val query = when {
            motorTypeData != null && cetegoryData == null -> {
                if (motorTypeData.modelCode == 0) {
                    feedCollection.whereEqualTo(BRAND_CODE_FIELD, motorTypeData.brandCode)
                } else {
                    feedCollection.whereEqualTo(BRAND_CODE_FIELD, motorTypeData.brandCode)
                        .whereEqualTo(
                            MODE_CODE_FIELD, motorTypeData.modelCode
                        )
                }
            }

            cetegoryData != null && motorTypeData == null -> {
                feedCollection.whereEqualTo(CATEGORY_CODE_FIELD, cetegoryData.typeCode)
            }

            motorTypeData != null && cetegoryData != null -> {
                if (motorTypeData.modelCode == 0) {
                    feedCollection.whereEqualTo(BRAND_CODE_FIELD, motorTypeData.brandCode)
                        .whereEqualTo(CATEGORY_CODE_FIELD, cetegoryData.typeCode)
                } else {
                    feedCollection.whereEqualTo(BRAND_CODE_FIELD, motorTypeData.brandCode)
                        .whereEqualTo(
                            MODE_CODE_FIELD, motorTypeData.modelCode
                        ).whereEqualTo(CATEGORY_CODE_FIELD, cetegoryData.typeCode)
                }
            }
            else -> {
                feedCollection
            }
        }

        val tagQuery = tagStr?.let {
            query
                .whereArrayContains(BOARD_TAG_LIST, tagStr)
        } ?: query

        return fireBaseTask.getData(
            tagQuery.whereLessThan(CREATE_DATE_FIELD, date)
                .orderBy(CREATE_DATE_FIELD, Query.Direction.DESCENDING).limit(INIT_SIZE.toLong()),
            BoardData::class.java
        )
    }

    override suspend fun getUserFeedList(date: Date, uid: String): Flow<List<BoardData>> {
        val feedCollection =
            fireStore.collection(CollectionName.USER).document(uid).collection(CollectionName.BOARD)

        return fireBaseTask.getData(
            feedCollection.whereLessThan(CREATE_DATE_FIELD, date)
                .orderBy(CREATE_DATE_FIELD, Query.Direction.DESCENDING).limit(INIT_SIZE.toLong()),
            BoardData::class.java
        )
    }

    override suspend fun addComment(commentData: CommentData): Flow<Boolean> {
        val commentFeedCollection =
            fireStore.collection(CollectionName.BOARD).document(commentData.bid ?: "")
                .collection(BOARD_COMMENT)

        val cid = commentFeedCollection.document().id

        commentData.cid = cid

        return fireBaseTask.setData(commentFeedCollection.document(cid), commentData)
    }

    override suspend fun updateCount(bid: String, flag: LikeCountEnum) {
        val document = fireStore.collection(CollectionName.BOARD).document(bid)
        when (flag) {
            LikeCountEnum.LIKE -> {
                document.update(LIKE_COUNT_FIELD, FieldValue.increment(1))
            }
            LikeCountEnum.UNLIKE -> {
                document.update(LIKE_COUNT_FIELD, FieldValue.increment(-1))
            }
            else -> Unit
        }

    }

    override suspend fun addReComment(reCommentData: ReCommentData): Flow<Boolean> {
        val reCommentFeedCollection =
            fireStore.collection(CollectionName.BOARD).document(reCommentData.bid ?: "")
                .collection(BOARD_RE_COMMENT)

        val rcId = reCommentFeedCollection.document().id
        reCommentData.rcId = rcId

        return fireBaseTask.setData(reCommentFeedCollection.document(rcId), reCommentData)
    }

    override suspend fun updateComment(commentData: CommentData): Flow<Boolean> {
        val document =
            fireStore.collection(CollectionName.BOARD).document(commentData.bid ?: "")
                .collection(BOARD_COMMENT)
                .document(commentData.cid ?: "")
        return fireBaseTask.setData(document, commentData)
    }

    override suspend fun updateReComment(
        reCommentData: ReCommentData
    ): Flow<Boolean> {
        val document =
            fireStore.collection(CollectionName.BOARD).document(reCommentData.bid ?: "")
                .collection(BOARD_RE_COMMENT)
                .document(reCommentData.cid ?: "")
        return fireBaseTask.setData(document, reCommentData)
    }

    override suspend fun removeComment(commentData: CommentData): Flow<Boolean> {
        val document = fireStore.collection(CollectionName.BOARD).document(commentData.bid ?: "")
            .collection(BOARD_COMMENT).document(commentData.cid ?: "")

        return fireBaseTask.delete(document)
    }

    override suspend fun removeReComment(reCommentData: ReCommentData): Flow<Boolean> {
        val document =
            fireStore.collection(CollectionName.BOARD).document(reCommentData.bid ?: "").collection(
                BOARD_RE_COMMENT
            ).document(reCommentData.rcId ?: "")
        return fireBaseTask.delete(document)
    }

    companion object {
        const val LIKE_COUNT_FIELD = "likeCount"
        const val BRAND_CODE_FIELD = "brandCode"
        const val MODE_CODE_FIELD = "modelCode"
        const val CATEGORY_CODE_FIELD = "categoryCode"
        const val CREATE_DATE_FIELD = "createDate"
        const val BOARD_TAG_LIST = "tagList"
        const val INIT_SIZE = 5
    }

}