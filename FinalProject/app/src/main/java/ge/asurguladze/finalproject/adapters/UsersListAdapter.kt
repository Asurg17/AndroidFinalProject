package ge.asurguladze.finalproject.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ge.asurguladze.finalproject.R
import ge.asurguladze.finalproject.models.User

class UsersListAdapter(private var list: ArrayList<User>): RecyclerView.Adapter<ItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.users_list_item, parent,false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = list[position]

        holder.userName.text = item.nickname
        holder.userProfession.text = item.profession
    }

    override fun getItemCount(): Int {
        return list.size
    }
}

class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
//    var userImage: ImageView = itemView.findViewById(R.id.user_image)
    var userName: TextView = itemView.findViewById(R.id.user_name)
    var userProfession: TextView = itemView.findViewById(R.id.user_profession)
}