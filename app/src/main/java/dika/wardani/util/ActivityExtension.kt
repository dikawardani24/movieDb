package dika.wardani.util

import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.reflect.KClass


fun AppCompatActivity.startActivity(kClass: KClass<*>, block: Intent.() -> Unit = {}) {
    val intent = Intent(this, kClass.java)
    block(intent)
    startActivity(intent)
}

fun AppCompatActivity.showWarning(message: String?) {
    Toast.makeText(this, message, Toast.LENGTH_LONG)
        .show()
}
