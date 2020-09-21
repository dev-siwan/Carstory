package com.like.drive.motorfeed.common.define

object GitDefine {
    const val GIT_BASE_URL =
        "https://raw.githubusercontent.com/DeveloperKimsiwan/taso-docs/master/"
    const val HEADER_AUTH = "Authorization"
    const val GIT_TOKEN = "token 70fa1707e6e6af9a11bcc60e464261a006309787"

    private const val GIT_PATH_DOCUMENT="document/"
    private const val GIT_PATH_NOTICE = "notice/"

    const val TERMS_USE_URL = "${GIT_BASE_URL}${GIT_PATH_DOCUMENT}terms_user.md"
    const val PRIVACY_URL="${GIT_BASE_URL}${GIT_PATH_DOCUMENT}terms_privacy.md"

    fun getNoticeFile(fileName: String) = "${GIT_BASE_URL}${GIT_PATH_NOTICE}$fileName"

}
