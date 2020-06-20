package dika.wardani.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import dika.wardani.R

abstract class BackAbleActivity : AppCompatActivity() {

    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.run {
            setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_24dp)
            setDisplayHomeAsUpEnabled(true)
        }
        dikaOnCreate(savedInstanceState)
    }

    final override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> dikaOnOptionsItemSelected(item)
        }
    }
    
    open fun dikaOnOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }
    
    abstract fun dikaOnCreate(savedInstanceState: Bundle?)
}
