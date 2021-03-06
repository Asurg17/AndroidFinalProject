package ge.asurguladze.finalproject.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ge.asurguladze.finalproject.R
import ge.asurguladze.finalproject.models.User

class UsersListAdapter(private var list: ArrayList<User>): RecyclerView.Adapter<ItemViewHolder>() {

    var userClickListener: UserClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.users_list_item, parent,false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = list[position]

        holder.userName.text = item.nickname
        holder.userProfession.text = item.profession

        if(item.image != null) {
            holder.userImage.setImageBitmap(item.image)
        }else{
            holder.userImage.setImageResource(R.drawable.avatar_image_placeholder)
        }

        holder.itemView.setOnClickListener {
            userClickListener?.onUserClicked(item)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }
}

class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    var userImage: ImageView = itemView.findViewById(R.id.user_image)
    var userName: TextView = itemView.findViewById(R.id.user_name)
    var userProfession: TextView = itemView.findViewById(R.id.user_profession)
}

interface UserClickListener{
    fun onUserClicked(user: User)
}