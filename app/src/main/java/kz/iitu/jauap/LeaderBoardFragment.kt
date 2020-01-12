package kz.iitu.jauap

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat.setNestedScrollingEnabled
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_question_list.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_leader_board.*

class LeaderBoardFragment : Fragment() {

    private val database by lazy { FirebaseFirestore.getInstance() }

    private fun getUserData(onUserDataLoaded: (User) -> Unit) {
        database.collection("users")
            .whereEqualTo("id", FirebaseAuth.getInstance().currentUser?.uid )
            .addSnapshotListener { snapshot, firebaseFirestoreException ->
                onUserDataLoaded(snapshot?.documents?.first()?.toObject(User::class.java)!!)
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_leader_board,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
                getUserData {
            userName_leader.text = it.surname + " " + it.name
            mail_leader.text = it.email
            coins_leader.text = it.coins.toString()
            rank_leader.text = it.rank.toString()
            //Picasso.get().load(it.profile_pic).into(profile_pic)
        }

        rank_list_view.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        rank_list_view.setNestedScrollingEnabled(false)

        database.collection("users")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                val users = querySnapshot?.documents?.map{
                    it.toObject(User::class.java)
                } ?: emptyList()
                Log.d("taaaaaag",users.toString())
                rank_list_view.adapter = RankUserAdapter(users = users)
            }

    }

}