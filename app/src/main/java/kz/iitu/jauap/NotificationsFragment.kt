package kz.iitu.jauap

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_leader_board.*
import kotlinx.android.synthetic.main.fragment_notifications.*

class NotificationsFragment : Fragment() {
    private val database by lazy { FirebaseFirestore.getInstance() }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notifications,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        notifications_view.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,false)
        notifications_view.setNestedScrollingEnabled(false)

        database.collection("users")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                val users = querySnapshot?.documents?.map{
                    it.toObject(User::class.java)
                } ?: emptyList()
                Log.d("taaaaaag",users.toString())
                notifications_view.adapter = RankUserAdapter(users = users)
            }
    }
}