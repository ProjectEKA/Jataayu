package `in`.projecteka.jataayu.consent.callback

interface PaginationEventCallback {

    fun loadMoreItems(totalFetchedCount: Int)
}