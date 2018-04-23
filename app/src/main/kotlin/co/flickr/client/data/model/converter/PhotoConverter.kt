package co.flickr.client.data.model.converter

import co.flickr.client.data.model.network.NWPhoto
import co.flickr.client.domain.model.FlickrPhoto

object PhotoConverter : SourceConverter<NWPhoto, FlickrPhoto> {

    override fun fromNetwork(source: NWPhoto) =
            FlickrPhoto(
                    getOrDie(source.id, "id"),
                    getOrDie(source.owner, "owner"),
                    getOrDie(source.secret, "secret"),
                    getOrDie(source.server, "server"),
                    getOrZero(source.farm),
                    source.title,
                    getOrFalse(source.ispublic),
                    getOrFalse(source.isfriend),
                    getOrFalse(source.isfamily)
            )

}