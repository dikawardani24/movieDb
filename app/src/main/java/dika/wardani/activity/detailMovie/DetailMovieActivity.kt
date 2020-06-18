package dika.wardani.activity.detailMovie

import android.os.Bundle
import dika.wardani.R
import dika.wardani.activity.BackAbleActivity

class DetailMovieActivity : BackAbleActivity() {

    override fun dikaOnCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_detail_movie)
    }

    companion object {
        const val KEY_MOVIE = "movie"
    }
}
