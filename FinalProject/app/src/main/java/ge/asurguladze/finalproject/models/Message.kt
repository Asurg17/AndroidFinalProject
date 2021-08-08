package ge.asurguladze.finalproject.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Message(var sender: String? = null, var message: String? = null, var time: Long? = null)
