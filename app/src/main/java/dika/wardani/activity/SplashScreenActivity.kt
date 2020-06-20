package dika.wardani.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import dika.wardani.R
import dika.wardani.activity.movieList.MovieListActivity
import dika.wardani.util.startActivity

class SplashScreenActivity : AppCompatActivity() {
    private var currentProgress = 0

    private fun updateProgress() {
        val handler = Handler()

        handler.postDelayed(
            {
                currentProgress += 10

                if (currentProgress < 100) {
                    updateProgress()
                } else {
                    startActivity(MovieListActivity::class)
                }
            },
            300
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        updateProgress()
    }
}
