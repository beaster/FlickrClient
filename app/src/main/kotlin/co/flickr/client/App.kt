package co.flickr.client

import android.app.Application
import co.flickr.client.data.model.converter.PhotoContainerConverter
import co.flickr.client.data.model.network.NWContainer
import co.flickr.client.data.network.FlickrService
import co.flickr.client.data.network.NetworkClient
import co.flickr.client.data.repository.flickr.FlickrDataRepository
import co.flickr.client.data.repository.flickr.FlickrDataSource
import co.flickr.client.domain.model.PhotoContainer
import co.flickr.client.domain.repository.FlickrRepository
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.*

class App : Application(), KodeinAware {
    override val kodein: Kodein = Kodein.lazy {
        bind<FlickrDataSource.DataSource>(tag = "remote") with provider { FlickrDataSource.RemoteSource(instance(), instance()) }
        bind<FlickrService>() with provider { NetworkClient.createFlickrService() }
        bind<PhotoContainer>() with factory { container: NWContainer -> PhotoContainerConverter.fromNetwork(container) }
        bind<FlickrDataSource.Factory>() with provider { FlickrDataSource.Factory(instance()) }
        bind<FlickrRepository>() with singleton { FlickrDataRepository(instance()) }
//        bind<SuggestionRepository>() with singleton { SuggestionDataRepository() }
    }
}