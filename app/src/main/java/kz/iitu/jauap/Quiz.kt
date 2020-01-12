package kz.iitu.jauap

data class Quiz(
    val title : String,
    val description: String
) {
    constructor(): this("","")
}