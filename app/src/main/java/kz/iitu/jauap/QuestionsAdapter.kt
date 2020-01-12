package kz.iitu.jauap

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_questions.view.*



class QuestionsAdapter (
    private val questions: List<Question?> = listOf(),
    private val onQuestionClick: (Question?) -> Unit
) : RecyclerView.Adapter<QuestionsAdapter.QuestionViewHolder>(){
    inner class QuestionViewHolder(private val view: View): RecyclerView.ViewHolder(view) {

        fun bindQuestion(question: Question?) {
            view.option1.setOnClickListener {
                if (view.option1.isClickable)
                view.option1.setBackgroundColor(Color.parseColor("#00ff00"))
            }
            view.question_number.text = "Que "+ question?.index.toString() + "/10"
            view.question_title.text = question?.question
            view.option1.text = question?.option1
            view.option2.text = question?.option2
            view.option3.text = question?.option3
            view.option4.text = question?.option4
            view.setOnClickListener {
                onQuestionClick(question)
            }
        }


    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = QuestionViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_questions,parent,false))


    override fun getItemCount() = questions.size

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        holder.bindQuestion(questions[position])
    }
}