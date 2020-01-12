package kz.iitu.jauap

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {
    private val database by lazy { FirebaseFirestore.getInstance() }
    val storage = FirebaseStorage.getInstance()
    private fun getUserData(onUserDataLoaded: (User) -> Unit) {
        database.collection("users")
            .whereEqualTo("id", FirebaseAuth.getInstance().currentUser?.uid )
            .addSnapshotListener { snapshot, firebaseFirestoreException ->
                onUserDataLoaded(snapshot?.documents?.first()?.toObject(User::class.java)!!)

            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        start_random_quiz_button.setOnClickListener {
            startActivity(Intent(requireContext(),QuestionListActivity::class.java))
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        getUserData {
            surname_home_fragment.text = it.surname
            name_home_fragment.text = it.name
            email_home_fragment.text = it.email
            rank_home_fragment.text = it.rank.toString()
            coins_home_fragment.text = it.coins.toString()
            Log.d("taaaaaaag",it.profilepic)

            //val gsReference = storage.getReferenceFromUrl(it.profilepic)

//            getInstance().reference.child("images/"+it.profilepic).downloadUrl.addOnCompleteListener { it1 ->
                //Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/suraqjauap-c7d4f.appspot.com/o/images%2F18cb3cdf-a37d-4dfe-a3b3-08e769f34b71.png?alt=media&token=77690607-c44d-4873-aa67-9fef03041146").into(profile_pic)
//                Log.d("afa",it1.result.toString())
//            }


        }
        return inflater.inflate(R.layout.fragment_home,container,false)
    }
}