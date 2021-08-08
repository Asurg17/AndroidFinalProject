package ge.asurguladze.finalproject.models

import android.graphics.Bitmap
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(var nickname: String? = null, var password: String? = null, var profession: String? = null, var image: Bitmap? = null)
