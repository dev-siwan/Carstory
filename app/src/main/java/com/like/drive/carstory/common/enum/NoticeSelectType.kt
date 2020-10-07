package com.like.drive.carstory.common.enum

import com.like.drive.carstory.R

enum class NoticeSelectType(val resID: Int) {
    NOTIFICATION(R.string.send_notification_text),
    DELETE(R.string.delete_text),
    UPDATE(R.string.update_text);
}