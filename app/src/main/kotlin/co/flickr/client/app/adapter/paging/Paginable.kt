package co.flickr.client.app.adapter.paging

interface Paginable {

    var state: AdapterState
    var hasFooterLoad: Boolean
    var hasHeaderLoad: Boolean
    var isMoreFooterAvailable: Boolean
    var isMoreHeaderAvailable: Boolean

    enum class AdapterState {
        LOAD_FOOTER,
        LOAD_HEADER,
        FOOTER_LOADED,
        HEADER_LOADED,
        UNDEFINED
    }
}