package co.flickr.client.domain.interactor

import co.flickr.client.domain.model.PhotoContainer
import co.flickr.client.domain.repository.FlickrRepository
import com.nhaarman.mockito_kotlin.mock
import io.reactivex.Single
import junit.framework.Assert
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.Before
import org.junit.Test
import org.mockito.Matchers
import java.util.concurrent.TimeUnit

class PhotoFlickrPaginatorTest {

    private lateinit var defaultPaginator: PhotoFlickrPaginator

    @Before
    fun setup() {
        defaultPaginator = buildPaginator(PhotoContainer(1, 2, emptyList()).apply { isSuccess = true })
    }

    @Throws(Exception::class)
    @Test
    fun reloadTest() {
        val testSubscriber = defaultPaginator.observable().test()

        defaultPaginator.reload("text")

        testSubscriber.await(100, TimeUnit.MILLISECONDS)
        testSubscriber.assertNoErrors()
        val state = testSubscriber.values().first()

        assertNull(state.errorMessage)
        Assert.assertFalse(state.allLoadedEnd)
        Assert.assertTrue(state.reloaded)
        assertThat(state.offset, equalTo(1))
        assertThat(state.reloadCondition, equalTo("text"))
    }

    @Throws(Exception::class)
    @Test
    fun loadNextPageTest() {
        val testSubscriber = defaultPaginator.observable().test()
        defaultPaginator.reload("text")
        testSubscriber.await(100, TimeUnit.MILLISECONDS)

        defaultPaginator.loadNextPage()
        testSubscriber.await(100, TimeUnit.MILLISECONDS)

        testSubscriber.assertNoErrors()
        val state = testSubscriber.values()[1]

        assertNull(state.errorMessage)

        //check end of pages
        Assert.assertTrue(state.allLoadedEnd)
        assertThat(state.offset, equalTo(2))

        Assert.assertFalse(state.reloaded)
        assertThat(state.reloadCondition, equalTo("text"))
    }

    @Throws(Exception::class)
    @Test
    fun errorTest() {
        val errorPaginator = buildPaginator(PhotoContainer(0, 2, emptyList()).apply {
            errorMessage = "Error"
            isSuccess = false
        })

        val testSubscriber = errorPaginator.observable().test()
        errorPaginator.reload("text")
        testSubscriber.await(100, TimeUnit.MILLISECONDS)

        testSubscriber.assertNoErrors()
        val state = testSubscriber.values().first()

        assertNotNull(state.errorMessage)
        Assert.assertFalse(state.allLoadedEnd)
        Assert.assertTrue(state.hasLoadingErrorEnd)
    }

    private fun buildPaginator(container: PhotoContainer): PhotoFlickrPaginator {
        val repository: FlickrRepository = mock {
            on { searchPhotosByText(Matchers.anyString(), Matchers.anyInt()) }
                    .thenReturn(Single.fromCallable { container })
        }
        return PhotoFlickrPaginator(repository)
    }
}