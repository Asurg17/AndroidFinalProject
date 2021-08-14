package ge.asurguladze.finalproject.models

import android.graphics.Bitmap
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class FullData(var nickname: String? = null, var image: Bitmap? = null, var message: String? = null, var time: Long? = null)
