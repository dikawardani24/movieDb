package dika.wardani.activity.detailMovie

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
import kotlinx.android.synthetic.main.activity_detail_movie.*


class DetailMovieActivity : BackAbleActivity(), ReviewItemAdapter.OnOpenReviewPageListener {
    private lateinit var viewModel: DetailMovieViewModel
    private lateinit var adapter: ReviewItemAdapter

    private fun saveFavouriteMovie() {
        viewModel.saveMoveAsFavourite().observe(this, Observer {
            Log.d(TAG, it.toString())

            when(it) {
                is Result.Succeed -> {
                    favouriteBtn.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_favorite_black_24dp))
                    Toast.makeText(this, "Saved successfully", Toast.LENGTH_LONG)
                        .show()
                }
                is Result.Failed -> {
                    Toast.makeText(this, "${it.error.message}, cause : ${it.error.cause?.message}", Toast.LENGTH_LONG)
                        .show()
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

    private fun loadReviews(movie: Movie) {
        viewModel.loadReviews(movie).observe(this, Observer {
            when(it) {
                is Result.Succeed -> {
                    adapter.reviews = it.data
                    adapter.notifyDataSetChanged()
                }
                is Result.Failed -> {

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
                    loadReviews(movie)
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

        favouriteBtn.setOnClickListener { saveFavouriteMovie() }

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
