package dika.wardani.util

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import kotlin.reflect.KClass


fun AppCompatActivity.startActivity(kClass: KClass<*>, block: Intent.() -> Unit = {}) {
    val intent = Intent(this, kClass.java)
    block(intent)
    startActivity(intent)
}