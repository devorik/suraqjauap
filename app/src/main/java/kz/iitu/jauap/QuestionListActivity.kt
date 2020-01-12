package kz.iitu.jauap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.WindowManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_question_list.*
import kotlinx.android.synthetic.main.layout_questions.*

class QuestionListActivity : AppCompatActivity() {

    private val database by lazy { FirebaseFirestore.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_list)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setUpViews()
    }

    private fun getQuestionData(onQuestionLoaded: (Question) -> Unit) {
        database.collection("questions")
            .whereEqualTo("option1",  option1.text )
            .addSnapshotListener { snapshot, firebaseFirestoreException ->
                onQuestionLoaded(snapshot?.documents?.first()?.toObject(Question::class.java)!!)
            }
    }
    private fun setUpViews() {
//        option1.setOnClickListener {
//            option1.setBackgroundColor(resources.getColor(R.color.colorAccent))
//        }

        finish.setOnClickListener{
            list_view.smoothScrollToPosition(1)
        }
        back_random.setOnClickListener {
            finish()
            onBackPressed()
        }

        list_view.layoutManager = LinearLayoutManager(this)


        database.collection("questions")
            .whereEqualTo("id", "quiz1")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->


                val questions = querySnapshot?.documents?.map{
                    it.toObject(Question::class.java)
                } ?: emptyList()
                list_view.adapter = QuestionsAdapter(questions = questions,
                    onQuestionClick = {

                    })
            }
    }
}
