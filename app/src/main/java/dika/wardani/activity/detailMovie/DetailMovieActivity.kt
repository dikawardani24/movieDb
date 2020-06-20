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
import dika.wardani.domain.Movie
import dika.wardani.domain.Review
import dika.wardani.repository.RepositoryFactory
import dika.wardani.util.DateFormatterHelper
import dika.wardani.util.Result
import dika.wardani.util.showWarning
import kotlinx.android.synthetic.main.activity_detail_movie.*
import kotlinx.android.synthetic.main.activity_detail_movie.noDataContainer
import kotlinx.android.synthetic.main.activity_favourite_movie.*


class DetailMovieActivity : BackAbleActivity(), ReviewItemAdapter.OnOpenReviewPageListener {
    private lateinit var viewModel: DetailMovieViewModel
    private lateinit var adapter: ReviewItemAdapter

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
    }

    private fun loadReviews() {
        viewModel.loadReviews().observe(this, Observer {
            when(it) {
                is Result.Succeed -> {
                    adapter.reviews = it.data
                    adapter.notifyDataSetChanged()
                    showNoData(false)
                }
                is Result.Failed -> {
                    showNoData(true)
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
        reviewsRv.layoutManager = LinearLayoutManager(this)

        favouriteBtn.setOnClickListener { changeFavouriteMovie() }

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
}
