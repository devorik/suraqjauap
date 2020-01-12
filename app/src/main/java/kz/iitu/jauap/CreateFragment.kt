package kz.iitu.jauap

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_create.*

class CreateFragment : Fragment() {

    private val auth by lazy { FirebaseAuth.getInstance() }
    private val database by lazy { FirebaseFirestore.getInstance() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        next.setOnClickListener {
            val quiz = hashMapOf(
                "title" to title.text.toString().trim(),
                "description" to description.text.toString().trim()
            )
            database.collection("quiz").add(quiz)

            startActivity(Intent(requireContext(),PassTestActivity::class.java))
 
        }
    }
}