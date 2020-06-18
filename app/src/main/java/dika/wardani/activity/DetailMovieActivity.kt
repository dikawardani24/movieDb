package dika.wardani.activity

import android.os.Bundle
import dika.wardani.R

class DetailMovieActivity : BackAbleActivity() {

    override fun dikaOnCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_detail_movie)
    }

    companion object {
        const val KEY_MOVIE = "movie"
    }
}
