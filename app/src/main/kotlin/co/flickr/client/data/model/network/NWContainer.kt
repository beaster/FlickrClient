package co.flickr.client.data.model.network

import com.google.gson.annotations.SerializedName

class NWContainer {

    @SerializedName("photos")
    var photoContainer: NWPhotoContainer? = null

    @SerializedName("stat")
    var stat: String? = null

    @SerializedName("code")
    var code: Int? = null

    @SerializedName("message")
    var message: String? = null
}