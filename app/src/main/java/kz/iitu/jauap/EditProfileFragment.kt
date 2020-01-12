package kz.iitu.jauap

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_edit_profile.*

class EditProfileFragment : Fragment() {

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
        getUserData {
            name_sign_up_edit_text.setText(it.name)
            surname_sign_up_edit_text.setText(it.surname)
            email_sign_up_edit_text.setText(it.email)
        }

        return inflater.inflate(R.layout.fragment_edit_profile,container,false)
    }
}