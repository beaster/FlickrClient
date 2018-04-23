package co.flickr.client.data.repository.suggestion

import co.flickr.client.domain.repository.SuggestionRepository

class SuggestionDataRepository(factory: SuggestionDataSource.Factory) : SuggestionRepository {

    //Only in-memory
    private val inMemorySource = factory.inMemory

    override fun addSuggestion(suggestion: String) = inMemorySource.addSuggestion(suggestion)
    override fun clearAll() = inMemorySource.clearAll()
    override fun getSuggestions() = inMemorySource.getSuggestions()

}