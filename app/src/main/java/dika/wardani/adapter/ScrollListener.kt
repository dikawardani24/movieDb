package dika.wardani.adapter

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class ScrollListener(
    private val layoutManager: LinearLayoutManager,
    private val onLoadMoreListener: OnLoadMoreListener
): RecyclerView.OnScrollListener() {
    var isLoading = false
    var isLastPage = false

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        if (!isLoading && !isLastPage) {
            if (layoutManager.findLastCompletelyVisibleItemPosition() == layoutManager.itemCount - 1) {
                onLoadMoreListener.onLoadMoreItems()
            }
        }
    }

    interface OnLoadMoreListener {
        fun onLoadMoreItems()
    }
}