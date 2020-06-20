package dika.wardani.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import dika.wardani.R
import dika.wardani.databinding.ItemMovieFavBinding
import dika.wardani.domain.Movie
import dika.wardani.util.DateFormatterHelper
import kotlinx.android.synthetic.main.item_movie.view.*
import kotlinx.android.synthetic.main.item_movie.view.container
import kotlinx.android.synthetic.main.item_movie_fav.view.*

class FavouriteMovieItemAdapter(
    private val context: Context
): RecyclerView.Adapter<FavouriteMovieItemAdapter.ViewHolder>() {
    var movies: ArrayList<Movie> = ArrayList()
    var onSelectedMovieListener: OnSelectedMovieListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding = ItemMovieFavBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)

        onSelectedMovieListener?.run {
            holder.itemView.container.setOnClickListener {
                onSelected(movie)
            }

            holder.itemView.favouriteBtn.setOnClickListener {
                onRemoveAsFavourite(movie)
            }
        }
    }

    class ViewHolder(
        private val binding: ItemMovieFavBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            binding.run {
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
        }
    }

    interface OnSelectedMovieListener {
        fun onSelected(movie: Movie)
        fun onRemoveAsFavourite(movie: Movie)
    }
}