package com.example.quizapp.Fragment

import android.os.Bundle
import android.renderscript.ScriptGroup.Binding
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.quizapp.R
import com.example.quizapp.databinding.FragmentProfileBinding
import com.example.quizapp.model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class ProfileFragment : Fragment() {
    val binding by lazy {
        FragmentProfileBinding.inflate(layoutInflater)
    }
    var isExpand = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding.imageButton.setOnClickListener{
            if(isExpand){
                binding.expandableconstraintLayout.visibility=View.VISIBLE
                binding.imageButton.setImageResource(R.drawable.arrowup)
            }else{
                binding.expandableconstraintLayout.visibility=View.GONE
                binding.imageButton.setImageResource(R.drawable.downarrow)

            }
            isExpand = !isExpand

        }
        Firebase.database.reference.child("Users").child(Firebase.auth.currentUser!!.uid).addValueEventListener(object  : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var user = snapshot.getValue(User::class.java)
                binding.nameUp.text=user?.name
                binding.Name.text=user?.name
                binding.Email.text=user?.email
                binding.Age.text=user?.age.toString()
                binding.Password.text=user?.password
//                var snapshot1 = snapshot.children


            }

            override fun onCancelled(error: DatabaseError) {
                // TODO("Not yet implemented")
            }
        })

        return binding.root

    }

    companion object {

    }
}