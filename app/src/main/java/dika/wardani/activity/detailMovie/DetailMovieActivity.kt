package dika.wardani.activity.detailMovie

import android.os.Bundle
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

        loadDetailMovie()
    }

    override fun onOpenReviewPage(review: Review) {

    }

    companion object {
        private const val TAG = "DetailMovieActivity"
        const val KEY_MOVIE = "movie"
    }
}
