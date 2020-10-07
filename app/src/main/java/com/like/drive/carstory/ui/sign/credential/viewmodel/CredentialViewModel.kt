package com.like.drive.carstory.ui.sign.credential.viewmodel

import androidx.databinding.ObservableField
import com.like.drive.carstory.ui.base.BaseViewModel

class CredentialViewModel : BaseViewModel() {

    val password = ObservableField<String>()
    val email = ObservableField<String>()

    fun setCredential() {

    }

}