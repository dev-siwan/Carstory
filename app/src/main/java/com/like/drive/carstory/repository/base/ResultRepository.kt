package com.like.drive.carstory.repository.base

data class ResultRepository<T>(
    val success: T? = null,
    val fail: Unit ?= null
)
