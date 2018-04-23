package co.flickr.client.data.model.network

import com.google.gson.annotations.SerializedName

class NWPhoto {

    @SerializedName("id")
    var id: String? = null

    @SerializedName("owner")
    var owner: String? = null

    @SerializedName("secret")
    var secret: String? = null

    @SerializedName("server")
    var server: String? = null

    @SerializedName("farm")
    var farm: Int? = null

    @SerializedName("title")
    var title: String? = null

    @SerializedName("ispublic")
    var ispublic: Int? = null

    @SerializedName("isfriend")
    var isfriend: Int? = null

    @SerializedName("isfamily")
    var isfamily: Int? = null
}
