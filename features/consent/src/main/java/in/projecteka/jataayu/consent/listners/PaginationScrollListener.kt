package `in`.projecteka.jataayu.consent.listners

import `in`.projecteka.jataayu.consent.callback.PaginationEventCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber

class PaginationScrollListener(private val callback: PaginationEventCallback) : RecyclerView.OnScrollListener() {

    private var visibleThreshold: Int = 0
    private var totalSize: Int = 0


    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        (recyclerView.layoutManager as LinearLayoutManager).let {
            if (dy <= 0) return
            val totalItemCount = it.itemCount
            val visibleItemCount = it.childCount
            val lastVisibleItem = it.findFirstVisibleItemPosition()
            listScrolled(visibleItemCount, lastVisibleItem, totalItemCount)
        }?: kotlin.run {
            TODO("NOT implemented")
        }
    }

    fun updateTotalSize(totalSize: Int) {
        this.totalSize = totalSize
    }

    private fun listScrolled(visibleItemCount: Int, firstVisibleItemPosition: Int, totalItemCount: Int) {

        if (totalItemCount >= totalSize) return
        val isPageReachingEnd = ((totalItemCount - visibleItemCount) <= (firstVisibleItemPosition + visibleThreshold))
        if (isPageReachingEnd) {
            Timber.d("total size is $totalSize, $totalItemCount")
            callback.loadMoreItems(totalItemCount)
        }
    }

}