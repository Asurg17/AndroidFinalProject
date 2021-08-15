package ge.asurguladze.finalproject.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import ge.asurguladze.finalproject.R
import ge.asurguladze.finalproject.models.FullData
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class MainPageListAdapter(private var items: ArrayList<FullData>): RecyclerView.Adapter<CharFriendsListItemHolder>() {

    var mainPageItemClickedListener: MainPageItemClickedListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharFriendsListItemHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.main_page_list_item, parent,false)
        return CharFriendsListItemHolder(view)
    }

    override fun onBindViewHolder(holder: CharFriendsListItemHolder, position: Int) {

        val item = items[position]

        holder.fromUserNickname.text = item.nickname

        if(item.image != null) {
            holder.fromUserImage.setImageBitmap(item.image)
        }else{
            holder.fromUserImage.setImageResource(R.drawable.avatar_image_placeholder)
        }

        holder.lastMessage.text = item.message
        holder.lastMessageSentTime.text = getTime(item.time!!)

        holder.itemView.setOnClickListener {
            mainPageItemClickedListener?.onItemClicked(item)
        }

        val newLayoutParams = ConstraintLayout.LayoutParams(holder.itemView.layoutParams)
        newLayoutParams.topMargin = 20

        if(position == items.size - 1){
            newLayoutParams.bottomMargin = 200
        }

        holder.itemView.layoutParams = newLayoutParams

    }

    override fun getItemCount(): Int {
        return items.size
    }


    private fun getTime(time: Long): String{

        val difference = System.currentTimeMillis() - time
        val minutes = TimeUnit.MILLISECONDS.toMinutes(difference)
        val hours = TimeUnit.MILLISECONDS.toHours(difference)

        return when {
            minutes < 60 -> { // if less than hour
                if(minutes == 0L){
                    "now"
                }else{
                    "$minutes min"
                }
            }
            hours < 24 -> {
                "$hours hour"
            }
            else -> {

                val formatter = SimpleDateFormat("dd MMM", Locale.ENGLISH)
                formatter.format(Date(time))
            }
        }

    }

}

class CharFriendsListItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var fromUserNickname: TextView = itemView.findViewById(R.id.from_user_nickname)
    var fromUserImage: ImageView = itemView.findViewById(R.id.from_user_image)
    var lastMessage: TextView = itemView.findViewById(R.id.last_message)
    var lastMessageSentTime: TextView = itemView.findViewById(R.id.last_message_sent_time)
}

interface MainPageItemClickedListener{
    fun onItemClicked(item: FullData)
}