package co.flickr.client.data.model.converter

interface BaseConverter {

    fun <T> getOrDie(value: T?, tag: String) =
            value ?: throw Exception("$tag must not be null")

    fun getOrFalse(value: Int?) = value == 1
    fun getOrZero(value: Int?) = value ?: 0

}
