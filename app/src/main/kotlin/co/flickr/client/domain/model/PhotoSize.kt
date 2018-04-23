package co.flickr.client.domain.model

enum class PhotoSize(val suffix: String) {
    SMALL("m"),
    MEDIUM(""),
    LARGE("b"),
    SMALL_SQUARE("s"),
    LARGE_SQUARE("q"),
    THUMBNAIL("t")
}