package dika.wardani.activity.movieList

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import dika.wardani.R
import dika.wardani.activity.DetailMovieActivity
import dika.wardani.adapter.MovieItemAdapter
import dika.wardani.domain.Movie
import dika.wardani.repository.RepositoryFactory
import dika.wardani.util.Result
import dika.wardani.util.startActivity
import kotlinx.android.synthetic.main.activity_movie_list.*

class MovieListActivity : AppCompatActivity(), MovieItemAdapter.OnSelectedMovieListener {
    private lateinit var viewModel: MovieListViewModel
    private lateinit var adapter: MovieItemAdapter

    private fun loadMovies() {
        viewModel.loadTopRatedMovie().observe(this, Observer {
            when(it) {
                is Result.Succeed -> {
                    adapter.movies = it.data
                    adapter.notifyDataSetChanged()
                }
                is Result.Failed -> {
                    adapter.movies = emptyList()
                    adapter.notifyDataSetChanged()
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        loadMovies()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_list)

        viewModel = MovieListViewModel(
            application= application,
            movieRepository = RepositoryFactory.getMovieRepository(this)
        )

        adapter = MovieItemAdapter(this)
        adapter.onSelectedMovieListener = this
        moviesRv.adapter = adapter
        moviesRv.layoutManager = LinearLayoutManager(this)
    }

    override fun onSelected(movie: Movie) {
        startActivity(DetailMovieActivity::class) {
            putExtra(DetailMovieActivity.KEY_MOVIE, movie.id)
        }
    }
}
