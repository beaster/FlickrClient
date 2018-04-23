package co.flickr.client.data.repository.suggestion

import co.flickr.client.util.unsafeLazy
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

class SuggestionDataSource {

    class InMemorySource(private val cache: MutableSet<String>) : DataSource {
        private val subject: Subject<List<String>> = PublishSubject.create()

        override fun addSuggestion(suggestion: String): Completable =
                Completable.fromCallable {
                    cache.add(suggestion)
                    notifyChanges()
                }

        override fun clearAll(): Completable =
                Completable.fromCallable {
                    cache.clear()
                    notifyChanges()
                }

        override fun getSuggestions(): Flowable<List<String>> =
                subject.toFlowable(BackpressureStrategy.LATEST)

        private fun notifyChanges() {
            subject.onNext(cache.toList())
        }

        companion object {
            val instance by unsafeLazy { InMemorySource(mutableSetOf()) }
        }

    }

    interface DataSource {
        fun addSuggestion(suggestion: String): Completable
        fun clearAll(): Completable
        fun getSuggestions(): Flowable<List<String>>
    }

    class Factory(val inMemory: SuggestionDataSource.DataSource)
}