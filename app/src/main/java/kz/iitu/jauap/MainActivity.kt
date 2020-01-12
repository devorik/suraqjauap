package kz.iitu.jauap

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_sign_up.*

class MainActivity : AppCompatActivity() {

    private val auth by lazy { FirebaseAuth.getInstance() }
    private val database by lazy { FirebaseFirestore.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_main)
        setUpViews()




        database.collection("users")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                val users = querySnapshot?.documents?.map {
                    it.toObject(User::class.java)
                }
                Log.d("taag", users.toString())
            }
    }


    private fun setUpViews() {
        new_user_button.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
        }
        sign_in_button.setOnClickListener {
            if (email_sign_in_edit_text.text.toString().trim().isEmpty() ||
                password_sign_in_edit_text.toString().trim().isEmpty()){
                Toast.makeText(this,"fields cannot be empty!",Toast.LENGTH_LONG).show()
            }else {
                SignIn(
                    email_sign_in_edit_text.text.toString().trim(),
                    password_sign_in_edit_text.text.toString().trim()
                )
            }
        }
    }

    private fun SignIn(
        email: String,
        password: String
    ) {
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener {task ->
                if(task.isSuccessful) {
                    startActivity(Intent(this,MainPageActivity::class.java))
                    return@addOnCompleteListener
                }

                Toast.makeText(this, task.exception?.message,Toast.LENGTH_LONG).show()}
    }





}
