package co.flickr.client.app.presentation

import co.flickr.client.data.repository.flickr.FlickrDataRepository
import co.flickr.client.data.repository.flickr.FlickrDataSource.Factory
import co.flickr.client.data.repository.flickr.FlickrDataSource.RemoteSource
import co.flickr.client.data.repository.suggestion.SuggestionDataRepository
import co.flickr.client.data.repository.suggestion.SuggestionDataSource
import co.flickr.client.data.repository.suggestion.SuggestionDataSource.InMemorySource

object Injection {

    val flickrRepository by lazy { FlickrDataRepository(Factory(RemoteSource.instance)) }
    val suggestionRepository by lazy { SuggestionDataRepository(SuggestionDataSource.Factory(InMemorySource.instance)) }
}