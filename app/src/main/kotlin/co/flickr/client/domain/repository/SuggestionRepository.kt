package co.flickr.client.domain.repository

import io.reactivex.Completable
import io.reactivex.Flowable

interface SuggestionRepository {

    fun addSuggestion(suggestion: String): Completable
    fun clearAll(): Completable
    fun getSuggestions(): Flowable<List<String>>
}