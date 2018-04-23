package co.flickr.client.data.repository.suggestion

import org.junit.Before
import org.junit.Test

class SuggestionDataSourceTest {

    private lateinit var source: SuggestionDataSource.DataSource

    @Before
    fun setup() {
        source = SuggestionDataSource.InMemorySource(mutableSetOf())
    }

    @Throws(Exception::class)
    @Test
    fun checkAddSuggestion() {
        val testFlowable = source.getSuggestions().test()
        source.addSuggestion("first").subscribe()
        source.addSuggestion("second").subscribe()

        testFlowable.assertNoErrors()
        testFlowable.assertValueAt(0, listOf("first"))
        testFlowable.assertValueAt(1, listOf("first", "second"))
    }

    @Throws(Exception::class)
    @Test
    fun checkClearSuggestion() {
        val testFlowable = source.getSuggestions().test()
        source.addSuggestion("first").subscribe()
        source.addSuggestion("second").subscribe()
        source.clearAll().subscribe()

        testFlowable.assertNoErrors()
        testFlowable.assertValueAt(2, listOf())
    }
}