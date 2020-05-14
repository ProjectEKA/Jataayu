package `in`.projecteka.jataayu.consent.listners

import `in`.projecteka.jataayu.consent.callback.PaginationEventCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PaginationScrollListener(private val callback: PaginationEventCallback, private  val totalSize: Int) : RecyclerView.OnScrollListener() {

    private var previousTotal = 0
    private var loading = true
    private var firstVisibleItem = 0
    private var visibleItemCount = 0
    private var totalItemCount = 0

    var visibleThreshold: Int = 0

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        (recyclerView.layoutManager as LinearLayoutManager).let {
            if (dy <= 0) return
            totalItemCount = it.itemCount
            visibleItemCount = it.childCount
            firstVisibleItem = it.findFirstVisibleItemPosition()
            listScrolled(visibleItemCount, firstVisibleItem, totalItemCount)
        }?: kotlin.run {
            TODO("NOT implemented")
        }
    }


    private fun listScrolled(visibleItemCount: Int, firstVisibleItemPosition: Int, totalItemCount: Int) {

        if (totalItemCount == totalSize) return
        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false
                previousTotal = totalItemCount
            }
        }
        val isPageReachingEnd = ((totalItemCount - visibleItemCount) <= (firstVisibleItemPosition + visibleThreshold))
        if (!loading && isPageReachingEnd) {
            callback.loadMoreItems(totalItemCount)
            loading = true
        }
    }
}