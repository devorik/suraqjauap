package kz.iitu.jauap

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class SignUpActivity : AppCompatActivity() {
    private val auth by lazy { FirebaseAuth.getInstance() }
    private val database by lazy { FirebaseFirestore.getInstance() }
    private val storage: FirebaseStorage by lazy { FirebaseStorage.getInstance() }
    private var imageview: ImageView? = null
    private val GALLERY = 1
    private val CAMERA = 2
    private var global_path = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_sign_up)
        imageview = findViewById<View>(R.id.img_profile) as ImageView
        imageview!!.setOnClickListener { showPictureDialog() }
        database.collection("users")
            .addSnapshotListener {querySnapshot, firebaseFirestoreException ->
                val users = querySnapshot?.documents?.map {
                    it.toObject(User::class.java)
                }
                Log.d("tag",users.toString())
            }
        setUpViews()
    }
    private fun setUpViews() {
        back_arrow_button.setOnClickListener {
            onBackPressed()
        }
        sign_up_button.setOnClickListener {
            if (email_sign_up_edit_text.text.toString().trim().isEmpty() ||
                password_sign_up_edit_text.toString().trim().isEmpty() ||
                name_sign_up_edit_text.toString().trim().isEmpty() ||
                surname_sign_up_edit_text.text.toString().trim().isEmpty()
            ) {
                Toast.makeText(this,"fields cannot be empty",Toast.LENGTH_LONG).show()
            }else {
                saveImage()
                signUp(email_sign_up_edit_text.text.toString().trim(),password_sign_up_edit_text.text.toString().trim())
                onBackPressed()
            }
        }
    }
    private fun saveImage() {
        val capture  = Bitmap.createBitmap(
            imageview!!.width,
            imageview!!.height,
            Bitmap.Config.ARGB_8888
        )
        val captureCanvas = Canvas(capture)
        imageview!!.draw(captureCanvas)
        val outputStream = ByteArrayOutputStream()
        capture.compress(Bitmap.CompressFormat.PNG,100,outputStream)
        val data : ByteArray = outputStream.toByteArray()
        val path = "images/" + UUID.randomUUID() + ".png"
        var images: StorageReference = storage.getReference(path)
        val metadata = StorageMetadata.Builder()
            .setCustomMetadata("user_email",email_sign_up_edit_text.text.toString())
            .build()
        val uploadTask : UploadTask = images.putBytes(data, metadata)
        uploadTask.addOnCompleteListener(this, OnCompleteListener {
            Log.i("taag","Upload task complete")
        })
    }
    private fun getUrl() : String {
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("images/$filename")
        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    global_path = it.toString()
                }
            }
        return global_path
    }
    private fun signUp(
        email: String,
        password: String
    ) {
        showPictureDialog()
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener {task ->
                if (task.isSuccessful) {
                    val id = task.result?.user?.uid
                    val email = email_sign_up_edit_text.text.toString().trim()
                    val name = name_sign_up_edit_text.text.toString().trim()
                    val surname = surname_sign_up_edit_text.text.toString().trim()
                    val profilepic = ""
                    val rank = 0
                    val coins = 0
                    val user = hashMapOf(
                        "id" to id,
                        "email" to email,
                        "name" to name,
                        "surname" to surname,
                        "profilepic" to profilepic,
                        "rank" to rank,
                        "coins" to coins
                    )
                    database.collection("users").add(user)
                }
            }
    }
    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(this)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Select photo from gallery", "Capture photo from camera")
        pictureDialog.setItems(pictureDialogItems
        ) { dialog, which ->
            when (which) {
                0 -> choosePhotoFromGallary()
                1 -> takePhotoFromCamera()
            }
        }
        pictureDialog.show()
    }
    fun choosePhotoFromGallary() {
        val galleryIntent = Intent(Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, GALLERY)
    }
    private fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA)
    }
    var selectedPhotoUri: Uri? = null
    public override fun onActivityResult(requestCode:Int, resultCode:Int, data: Intent?) {
        selectedPhotoUri = data?.data
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY)
        {
            if (data != null)
            {
                val contentURI = data.data
                try
                {
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                    val path = saveImage(bitmap)
                    Toast.makeText(this, "Image Saved!", Toast.LENGTH_SHORT).show()
                    imageview!!.setImageBitmap(bitmap)
                }
                catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show()
                }
            }
        }
        else if (requestCode == CAMERA)
        {
            val thumbnail = data!!.extras!!.get("data") as Bitmap
            imageview!!.setImageBitmap(thumbnail)
            saveImage(thumbnail)
            Toast.makeText(this, "Image Saved!", Toast.LENGTH_SHORT).show()
        }
    }
    fun saveImage(myBitmap: Bitmap):String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val wallpaperDirectory = File(
            (Environment.getExternalStorageDirectory()).toString() + IMAGE_DIRECTORY)
        // have the object build the directory structure, if needed.
        Log.d("fee",wallpaperDirectory.toString())
        if (!wallpaperDirectory.exists())
        {
            wallpaperDirectory.mkdirs()
        }
        try
        {
            Log.d("heel",wallpaperDirectory.toString())
            val f = File(wallpaperDirectory, ((Calendar.getInstance()
                .getTimeInMillis()).toString() + ".jpg"))
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(this,
                arrayOf(f.getPath()),
                arrayOf("image/jpeg"), null)
            fo.close()
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath())
            return f.getAbsolutePath()
        }
        catch (e1: IOException) {
            e1.printStackTrace()
        }
        return ""
    }
    companion object {
        private val IMAGE_DIRECTORY = "/demonuts"
    }
}
