package com.like.drive.carstory.common.valid

enum class FieldValidEnum {
    NAME {
        override fun message() = "이름을 2~100자로 입력해주세요."
    },
    BIRTH {
        override fun message() = "생년월일을 숫자8자리 형식으로 작성해주세요.\n(예.19770501)"
    },
    DAY {
        override fun message() = "일을 숫자2자리 형식으로 작성해주세요.\n(예.01~31)"
    },
    PASSWORD {
        override fun message() = "비밀번호는 8자~20자 이내 영문+숫자+특수문자(~`!@#\$%^&*()-)의 조합으로 입력해주세요."
    },
    CALL {
        override fun message() = "전화번호 형식이 다릅니다. 다시 입력해주세요."
    },
    PHONE {
        override fun message() = "휴대폰번호 형식이 다릅니다. 다시 입력해주세요."
    },
    EMAIL {
        override fun message() = "이메일 형식을 확인해주세요."
    },
    PASSWORD_VALID {
        override fun message() = "설정된 비밀번호와 다릅니다. 다시 입력해주세요."
    };



    abstract fun message(): String
}