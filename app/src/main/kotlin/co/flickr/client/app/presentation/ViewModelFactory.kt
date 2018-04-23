package co.flickr.client.app.presentation

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import co.flickr.client.domain.interactor.PhotoSearchInteractor

object ViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(FlickrViewModel::class.java) ->
            FlickrViewModel(PhotoSearchInteractor(Injection.flickrRepository, Injection.suggestionRepository)) as T
        else -> throw IllegalArgumentException("Unknown ViewModel class")
    }
}