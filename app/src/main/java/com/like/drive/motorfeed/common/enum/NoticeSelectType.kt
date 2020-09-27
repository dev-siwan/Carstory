package com.like.drive.motorfeed.common.enum

import com.like.drive.motorfeed.R

enum class NoticeSelectType(val resID: Int) {
    NOTIFICATION(R.string.send_notification_text),
    DELETE(R.string.delete_text),
    UPDATE(R.string.update_text);
}