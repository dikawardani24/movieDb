package dika.wardani.activity.movieList

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import dika.wardani.R
import dika.wardani.activity.detailMovie.DetailMovieActivity
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
        viewModel.getMovies().observe(this, Observer {
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

    private fun showFilterDialog() {
        val filterDialog = BottomSheetDialog(this)
        filterDialog.run {
            setContentView(R.layout.dialog_filter_movie)
            setCancelable(true)

            val listener = View.OnClickListener {
                when(it.id) {
                    R.id.popularTv -> viewModel.currentFilterType = MovieListViewModel.FilterType.POPULAR
                    R.id.nowPlayingTv -> viewModel.currentFilterType = MovieListViewModel.FilterType.NOW_PLAYING
                    R.id.topRatedTv -> viewModel.currentFilterType = MovieListViewModel.FilterType.TOP_RATED
                }

                viewModel.resetPage()
                hide()
                loadMovies()
            }

            findViewById<TextView>(R.id.popularTv)?.setOnClickListener(listener)
            findViewById<TextView>(R.id.nowPlayingTv)?.setOnClickListener(listener)
            findViewById<TextView>(R.id.topRatedTv)?.setOnClickListener(listener)
            show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_activity_movie_list, menu)
        return super.onCreateOptionsMenu(menu)
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

        filterBtn.setOnClickListener { showFilterDialog() }
    }

    override fun onSelected(movie: Movie) {
        startActivity(DetailMovieActivity::class) {
            putExtra(DetailMovieActivity.KEY_MOVIE, movie.id)
        }
    }
}
