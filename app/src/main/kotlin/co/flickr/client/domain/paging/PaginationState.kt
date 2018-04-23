package co.flickr.client.domain.paging

class PaginationState<out Entity, out ReloadCondition>(
        val reloaded: Boolean = false,
        val reloadCondition: ReloadCondition,
        val data: List<Entity> = emptyList(),
        val allLoadedStart: Boolean = false,
        val allLoadedEnd: Boolean = false,
        val hasLoadingErrorStart: Boolean = false,
        val hasLoadingErrorEnd: Boolean = false,
        val errorMessage: String? = null,
        val offset: Int)