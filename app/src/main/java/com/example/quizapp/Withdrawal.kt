package com.example.quizapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.quizapp.databinding.FragmentWithdrawalBinding
import com.example.quizapp.model.HistoryModelClass
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class Withdrawal : BottomSheetDialogFragment() {
    private lateinit var binding:FragmentWithdrawalBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    var currentCoin = 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWithdrawalBinding.inflate(inflater, container, false)
        Firebase.database.reference.child("playerCoin").
        child(Firebase.auth.currentUser!!.uid).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    currentCoin = snapshot.getValue() as Long
                    binding.totalCoin.text = currentCoin.toString()
                }
                else{
                    var temp = 0
                    binding.totalCoin.text = temp.toString()

                }
            }
            override fun onCancelled(error: DatabaseError) {
                //TODO("Not yet implemented")
            }

        })
        binding.transerfer.setOnClickListener {
            if(binding.amount.text.toString().toDouble() <= currentCoin ){
                Firebase.database.reference.child("playerCoin").
                child(Firebase.auth.currentUser!!.uid)
                    .setValue(currentCoin-binding.amount.text.toString().toDouble())
                var historyModelClass= HistoryModelClass(System.currentTimeMillis().toString(), binding.amount.text.toString().toString(),true)
                Firebase.database.reference.child("playerCoinHistory")
                    .child(Firebase.auth.currentUser!!.uid).push()
                    .setValue(historyModelClass)
            } else {
                Toast.makeText(activity, "Out of naoney", Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }

    companion object {

    }
}