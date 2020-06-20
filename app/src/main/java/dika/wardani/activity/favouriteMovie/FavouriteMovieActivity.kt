package dika.wardani.activity.favouriteMovie

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import dika.wardani.R
import dika.wardani.activity.BackAbleActivity
import dika.wardani.activity.detailMovie.DetailMovieActivity
import dika.wardani.adapter.FavouriteMovieItemAdapter
import dika.wardani.adapter.ScrollListener
import dika.wardani.domain.Movie
import dika.wardani.exception.NotMoreDataException
import dika.wardani.repository.RepositoryFactory
import dika.wardani.util.Result
import dika.wardani.util.showWarning
import dika.wardani.util.startActivity
import kotlinx.android.synthetic.main.activity_favourite_movie.*

class FavouriteMovieActivity : BackAbleActivity(), FavouriteMovieItemAdapter.OnSelectedMovieListener, ScrollListener.OnLoadMoreListener {
    private lateinit var viewModel: FavouriteMoviesViewModel
    private lateinit var adapter: FavouriteMovieItemAdapter
    private lateinit var scrollListener: ScrollListener

    private fun showNoData(show: Boolean) {
        if (show) {
            favMoviesRv.run {
                if (visibility == View.VISIBLE) {
                    visibility = View.GONE
                }
            }
            noDataContainer.run {
                if (visibility == View.GONE) {
                    visibility = View.VISIBLE
                }
            }
        } else {
            favMoviesRv.run {
                if (visibility == View.GONE) {
                    visibility = View.VISIBLE
                }
            }
            noDataContainer.run {
                if (visibility == View.VISIBLE) {
                    visibility = View.GONE
                }
            }
        }
    }

    private fun loadFavouriteMovies() {
        scrollListener.isLoading = true
        viewModel.loadFavouriteViewModel().observe(this, Observer {
            scrollListener.isLoading = false

            Log.d(TAG, "$it")
            when (it) {
                is Result.Succeed -> {
                    adapter.run {
                        movies.addAll(it.data)
                        notifyDataSetChanged()
                    }
                    showNoData(false)
                }
                is Result.Failed -> {
                    if (it.error is NotMoreDataException) {
                        scrollListener.isLastPage = true
                    } else {
                        adapter.run {
                            movies.clear()
                            notifyDataSetChanged()
                        }
                        showNoData(true)
                    }
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.resetPage()
        adapter.movies.clear()
        adapter.notifyDataSetChanged()
        loadFavouriteMovies()
    }

    override fun dikaOnCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_favourite_movie)
        viewModel = FavouriteMoviesViewModel(
            application = application,
            movieRepository = RepositoryFactory.getMovieRepository(this)
        )

        adapter = FavouriteMovieItemAdapter(this)
        adapter.onSelectedMovieListener = this
        favMoviesRv.adapter = adapter

        val layoutManager = LinearLayoutManager(this)
        favMoviesRv.layoutManager = layoutManager
        scrollListener = ScrollListener(
            layoutManager = layoutManager,
            onLoadMoreListener = this
        )
        favMoviesRv.addOnScrollListener(scrollListener)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        viewModel.stop()
    }

    override fun onLoadMoreItems() {
        loadFavouriteMovies()
    }

    override fun onSelected(movie: Movie) {
        startActivity(DetailMovieActivity::class) {
            putExtra(DetailMovieActivity.KEY_MOVIE, movie.id)
        }
    }

    override fun onRemoveAsFavourite(movie: Movie) {
        viewModel.removeFromFavourite(movie).observe(this, Observer {
            when(it) {
                is Result.Succeed -> {
                    adapter.run {
                        movies.remove(movie)
                        notifyDataSetChanged()
                    }
                    showWarning("Movie has been removed from your favourite")
                }
                is Result.Failed -> {
                    showWarning("${it.error}")
                }
            }

            showNoData(adapter.itemCount <= 0)
        })
    }

    companion object {
        private const val TAG = "FavouriteMovieActivity"
    }
}
