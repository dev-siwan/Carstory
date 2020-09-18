package com.like.drive.motorfeed.ui.sign.credential.viewmodel

import androidx.databinding.ObservableField
import com.like.drive.motorfeed.ui.base.BaseViewModel

class CredentialViewModel : BaseViewModel() {

    val password = ObservableField<String>()
    val email = ObservableField<String>()

    fun setCredential() {

    }

}