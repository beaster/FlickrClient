package co.flickr.client.data.model.converter

interface SourceConverter<in NW, out D> : BaseConverter {

    fun fromNetwork(source: NW): D
}