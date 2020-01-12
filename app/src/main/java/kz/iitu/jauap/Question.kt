package kz.iitu.jauap

data class Question(
    val id:String,
    val question: String,
    val index: Int,
    val option1: String,
    val option2: String,
    val option3 : String,
    val option4 : String,
    val answer: String
) {
    constructor(): this("","",0,"","","","","")
}