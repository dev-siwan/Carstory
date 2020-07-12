package com.like.drive.motorfeed.ui.base
import androidx.lifecycle.ViewModel
import com.like.drive.motorfeed.common.livedata.SingleLiveEvent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseViewModel :ViewModel(){

    private val compositeDisposable = CompositeDisposable()

    fun doClickEvent(liveEvent: SingleLiveEvent<Unit>) = liveEvent.call()

    fun dispose() = compositeDisposable.dispose()
    fun addDisposable(disposables: Disposable) = compositeDisposable.add(disposables)


    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}