package kz.iitu.jauap
import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_notification.view.*
import kotlinx.android.synthetic.main.layout_questions.view.*
import kotlinx.android.synthetic.main.layout_rank_list.view.*
import kotlinx.android.synthetic.main.layout_rank_list.view.mail_leader


class RankUserAdapter (
    private val users: List<User?> = listOf()
) : RecyclerView.Adapter<RankUserAdapter.RankViewHolder>(){
     class RankViewHolder(private val view: View): RecyclerView.ViewHolder(view) {


        fun bindUser(user: User?) {
            view.userName_leader.text = user?.name +" "+ user?.surname
            view.mail_leader.text = user?.email
            view.coins_leader.text = user?.coins.toString()
            view.rank_leader.text = "#" + user?.rank.toString()
        }


    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = RankViewHolder(LayoutInflater.from(parent.context)
        .inflate(R.layout.layout_rank_list,parent,false))


    override fun getItemCount() = users.size

    override fun onBindViewHolder(holder: RankViewHolder, position: Int) {
        holder.bindUser(users[position])
    }
}