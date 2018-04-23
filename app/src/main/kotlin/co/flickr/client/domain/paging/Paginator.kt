package co.flickr.client.domain.paging

import io.reactivex.Observable

interface Paginator<Entity, ReloadCondition> {

    fun loadNextPage()
    fun loadPrevPage()
    fun reload(condition: ReloadCondition)
    fun observable(): Observable<PaginationState<Entity, ReloadCondition>>
    fun finish()
}