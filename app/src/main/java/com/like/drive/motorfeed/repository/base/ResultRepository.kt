package com.like.drive.motorfeed.repository.base

data class ResultRepository<T>(
    val success: T? = null,
    val fail: Unit ?= null
)
