package co.flickr.client.app.adapter.paging

internal class PaginableImpl : Paginable {

    var delegate: ChangeStateDelegate? = null

    override var state = Paginable.AdapterState.UNDEFINED
        set(value) {
            if (value != state) {
                field = value
                delegate?.onChangeState(value)
            }
        }

    override var hasFooterLoad: Boolean = false
    override var hasHeaderLoad: Boolean = false

    override var isMoreFooterAvailable: Boolean = false
    override var isMoreHeaderAvailable: Boolean = false

    interface ChangeStateDelegate {
        fun onChangeState(newState: Paginable.AdapterState)
    }

}