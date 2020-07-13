package com.like.drive.motorfeed.repository.base

open class ResultRepository(
    val  success : ()->Unit,
    val  error : ()->Unit
)
