package co.flickr.client.data.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetworkClient {

    private const val BASE_URL = Constants.FLICKR_URL
    private const val REST_API_BASE_URL = "$BASE_URL/services/rest/"

    private val httpClient: OkHttpClient
    private val retrofit by lazy { createRetrofit(REST_API_BASE_URL) }
    private val gson by lazy { GsonFactory.createGsonBuilder().create() }

    init {
        val builder = OkHttpClient.Builder()
        builder.readTimeout(Constants.CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(Constants.READ_TIMEOUT, TimeUnit.SECONDS)
        httpClient = builder.build()
    }

    private fun createRetrofit(baseUrl: String) = createBuilder(baseUrl).client(httpClient).build()
    private fun createBuilder(baseUrl: String) = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))

    fun createFlickrService() = retrofit.create(FlickrService::class.java)

}