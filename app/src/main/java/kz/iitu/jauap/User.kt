package kz.iitu.jauap

data class User(
    val id : String,
    val email: String,
    val name: String,
    val surname: String,
    val profilepic: String,
    val rank: Long,
    val coins: Long
) {
    constructor(): this("","","","","",0,0)
}