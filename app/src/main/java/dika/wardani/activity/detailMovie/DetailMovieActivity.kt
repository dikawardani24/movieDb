package dika.wardani.activity.detailMovie

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import dika.wardani.R
import dika.wardani.activity.BackAbleActivity
import dika.wardani.adapter.ReviewItemAdapter
import dika.wardani.adapter.ScrollListener
import dika.wardani.domain.Movie
import dika.wardani.domain.Review
import dika.wardani.exception.NotMoreDataException
import dika.wardani.repository.RepositoryFactory
import dika.wardani.util.DateFormatterHelper
import dika.wardani.util.Result
import dika.wardani.util.showWarning
import kotlinx.android.synthetic.main.activity_detail_movie.*


class DetailMovieActivity : BackAbleActivity(), ReviewItemAdapter.OnOpenReviewPageListener, ScrollListener.OnLoadMoreListener {
    private lateinit var viewModel: DetailMovieViewModel
    private lateinit var adapter: ReviewItemAdapter
    private lateinit var scrollListener: ScrollListener

    private fun expandReviews(expand: Boolean) {

        if (expand) {
            movieImg.visibility = View.GONE
            movieOverview.visibility = View.GONE
        } else {
            movieImg.visibility = View.VISIBLE
            movieOverview.visibility = View.VISIBLE
        }
    }

    private fun showNoData(show: Boolean) {
        if (show) {
            reviewsRv.run {
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
            reviewsRv.run {
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

    private fun setAsFavourite(isFavourite: Boolean) {
        val icon = if (isFavourite) R.drawable.ic_favorite_red_24dp else R.drawable.ic_favorite_border_black_24dp
        val drawable = ContextCompat.getDrawable(this, icon)
        favouriteBtn.setImageDrawable(drawable)
    }

    private fun determineFavMovie() {
        viewModel.determineFavouriteMovie().observe(this, Observer {
            when(it) {
                is Result.Succeed -> {
                    val isFavourite = it.data
                    setAsFavourite(isFavourite)
                }
            }
        })
    }

    private fun changeFavouriteMovie() {
        viewModel.changeMovieAsFavourite().observe(this, Observer {
            Log.d(TAG, it.toString())

            when(it) {
                is Result.Succeed -> {
                    setAsFavourite(viewModel.isFavourite)
                    showWarning(it.data)
                }
                is Result.Failed -> {
                    val error = it.error
                    showWarning(error.message)
                }
            }
        })
    }

    private fun showDataMovie(movie: Movie) {
        Picasso.get().load(movie.movieImage?.posterPath)
            .fit()
            .centerCrop()
            .noFade()
            .placeholder(R.drawable.progress_animation)
            .into(movieImg)

        movieTitle.text = movie.title
        movieReleaseDate.text = DateFormatterHelper.format(movie.releaseDate)
        movieOverview.text = movie.overview
        voteAverageRb.rating = (movie.vote.average / 2).toFloat()
    }

    private fun loadReviews() {
        scrollListener.isLoading = true
        viewModel.loadReviews().observe(this, Observer {
            scrollListener.isLoading = false
            when(it) {
                is Result.Succeed -> {
                    adapter.reviews.addAll(it.data)
                    adapter.notifyDataSetChanged()
                    showNoData(false)
                }
                is Result.Failed -> {
                    if (it.error is NotMoreDataException) {
                        scrollListener.isLastPage = true
                    } else {
                        adapter.run {
                            reviews.clear()
                            notifyDataSetChanged()
                        }
                        showNoData(true)
                    }
                }
            }
        })
    }

    private fun loadDetailMovie() {
        val receivedId = intent.getIntExtra(KEY_MOVIE, 0)
        viewModel.loadDetailMovie(receivedId).observe(this, Observer {
            when(it) {
                is Result.Succeed -> {
                    val movie = it.data
                    showDataMovie(movie)
                    determineFavMovie()
                    loadReviews()
                }
                is Result.Failed -> {

                }
            }
        })
    }

    override fun dikaOnCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_detail_movie)

        viewModel = DetailMovieViewModel(
            application = application,
            movieRepository = RepositoryFactory.getMovieRepository(this),
            reviewRepository = RepositoryFactory.getReviewRepository(this)
        )

        adapter = ReviewItemAdapter(this)
        adapter.onOpenReviewPageListener = this
        reviewsRv.adapter = adapter
        val layoutManager = LinearLayoutManager(this)
        reviewsRv.layoutManager = layoutManager

        scrollListener = ScrollListener(
            layoutManager = layoutManager,
            onLoadMoreListener = this
        )

        favouriteBtn.setOnClickListener { changeFavouriteMovie() }
        expandReviewsCb.setOnCheckedChangeListener { _, checked -> expandReviews(checked) }

        loadDetailMovie()
    }

    override fun onOpenReviewPage(review: Review) {
        val url = review.url
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }

    companion object {
        private const val TAG = "DetailMovieActivity"
        const val KEY_MOVIE = "movie"
    }

    override fun onLoadMoreItems() {
        loadReviews()
    }
}
