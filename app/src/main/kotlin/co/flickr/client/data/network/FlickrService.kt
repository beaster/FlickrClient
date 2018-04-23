package co.flickr.client.data.network

import co.flickr.client.data.model.network.NWContainer
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrService {

    @GET("?method=flickr.photos.search&format=json&nojsoncallback=1")
    fun searchPhotoByText(@Query("api_key") apiKey: String,
                          @Query("text") query: String,
                          @Query("page") page: Int,
                          @Query("per_page") perPage: Int): Single<NWContainer>
}