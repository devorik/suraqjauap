package kz.iitu.jauap

import android.graphics.Color
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main_page.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.header.*
import kotlinx.android.synthetic.main.header.profile_pic

class MainPageActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener{
    private val auth by lazy { FirebaseAuth.getInstance() }
    private val database by lazy { FirebaseFirestore.getInstance() }

    var drawerLayout: DrawerLayout? = null
    var toolbar: androidx.appcompat.widget.Toolbar? = null
    var navigationView: NavigationView? = null
    var toggle: ActionBarDrawerToggle? = null
    private val storage: FirebaseStorage by lazy { FirebaseStorage.getInstance() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)


        drawerLayout = findViewById(R.id.drawer)
        toolbar = findViewById(R.id.toolbar)
        navigationView = findViewById(R.id.navigationView)
        setSupportActionBar(toolbar)
        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar!!.setTitle("Suraq Jauap")


        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment()).commit()
            navigationView!!.setCheckedItem(R.id.home)
        }

        toggle = ActionBarDrawerToggle(this, drawerLayout,toolbar,R.string.drawerOpen, R.string.drawerClose )

        drawerLayout?.run { addDrawerListener(toggle!!) }
        toggle!!.syncState()
        navigationView?.setNavigationItemSelectedListener(this)
        navigationView!!.setItemIconTintList(null)
        //navigationView!!.menu.findItem(R.id.play).icon.colorFilter(Color.parseColor("#b69260"), PorterDuff.Mode.SRC_ATOP)

        getUserData {
            name_text_view.text = it.name
            surname_text_view.text = it.surname
            email_text_view.text  = it.email
        }


    }

    private fun getUserData(onUserDataLoaded: (User) -> Unit) {
        database.collection("users")
            .whereEqualTo("id", FirebaseAuth.getInstance().currentUser?.uid )
            .addSnapshotListener { snapshot, firebaseFirestoreException ->
                onUserDataLoaded(snapshot?.documents?.first()?.toObject(User::class.java)!!)
            }
    }




    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        when (p0.itemId) {
            R.id.home -> supportFragmentManager.beginTransaction().replace(R.id.fragment_container, HomeFragment()).commit()
            R.id.create -> supportFragmentManager.beginTransaction().replace(R.id.fragment_container, CreateFragment()).commit()
            R.id.leader_board -> supportFragmentManager.beginTransaction().replace(R.id.fragment_container, LeaderBoardFragment()).commit()
            R.id.notifications -> supportFragmentManager.beginTransaction().replace(R.id.fragment_container, NotificationsFragment()).commit()
            R.id.edit_profile -> supportFragmentManager.beginTransaction().replace(R.id.fragment_container, EditProfileFragment()).commit()
        }
        drawerLayout?.closeDrawer(GravityCompat.START)
        return true
    }




}
