package com.example.trainhub.fragments
import android.Manifest
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.trainhub.R
import com.example.trainhub.models.fireBaseModels.UserFireBaseModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class EditProfileFragment : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var set: ImageView
    private lateinit var profilePic: TextView
    private lateinit var editName: TextView
    private lateinit var editPassword: TextView
    private lateinit var pd: ProgressDialog
    private lateinit var storagePermission: Array<String>
    private var userFireBaseModel = UserFireBaseModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_profile, container, false)

        profilePic = view.findViewById(R.id.setting_profile_picture)
        editName = view.findViewById(R.id.etName)
        set = view.findViewById(R.id.setting_profile_image)
        editPassword = view.findViewById(R.id.etPassword)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseUser = firebaseAuth.currentUser!!
        firebaseDatabase = FirebaseDatabase.getInstance()
        storageReference = FirebaseStorage.getInstance().reference
        databaseReference = firebaseDatabase.getReference("Users")

        storagePermission = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)

        pd = ProgressDialog(context)
        pd.setCanceledOnTouchOutside(false)

        val query = databaseReference.orderByChild("email").equalTo(firebaseUser.email)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (dataSnapshot1 in dataSnapshot.children) {
                    val image = "" + dataSnapshot1.child("image").value
                    try {
                        Glide.with(this@EditProfileFragment).load(image).into(set)
                    } catch (_: Exception) {
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        editPassword.setOnClickListener {
            pd.setMessage("Changing Password")
            showPasswordChangeDialog()
        }


        return view
    }

    override fun onPause() {
        super.onPause()
        val query = databaseReference.orderByChild("email").equalTo(firebaseUser.email)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (dataSnapshot1 in dataSnapshot.children) {
                    val image = "" + dataSnapshot1.child("image").value
                    try {
                        Glide.with(this@EditProfileFragment).load(image).into(set)
                    } catch (e: Exception) {
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        editPassword.setOnClickListener {
            pd.setMessage("Changing Password")
            showPasswordChangeDialog()
        }
    }

    override fun onStart() {
        super.onStart()
        val query = databaseReference.orderByChild("email").equalTo(firebaseUser.email)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (dataSnapshot1 in dataSnapshot.children) {
                    val image = "" + dataSnapshot1.child("image").value
                    try {
                        Glide.with(this@EditProfileFragment).load(image).into(set)
                    } catch (e: Exception) {
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        editPassword.setOnClickListener {
            pd.setMessage("Changing Password")
            showPasswordChangeDialog()
        }
    }


    private fun showPasswordChangeDialog() {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_update_password, null)
        val oldPass = view.findViewById<EditText>(R.id.oldpasslog)
        val newPass = view.findViewById<EditText>(R.id.newpasslog)
        val editPass = view.findViewById<Button>(R.id.updatepass)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
        editPass.setOnClickListener {
            val oldP = oldPass.text.toString().trim()
            val newP = newPass.text.toString().trim()
            if (TextUtils.isEmpty(oldP)) {
                Toast.makeText(
                    requireContext(),
                    "Current Password can't be empty",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(newP)) {
                Toast.makeText(requireContext(), "New Password can't be empty", Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }
            if(newP.length < 6){
                Toast.makeText(requireContext(), "Password length must be at least 6 characters", Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }

            dialog.dismiss()
            userFireBaseModel.changePassword(oldP, newP) { success ->
                if (success) {
                    Toast.makeText(requireContext(), "Password Changed", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(requireContext(), "Failed", Toast.LENGTH_LONG).show()
                }
            }

        }
    }



}