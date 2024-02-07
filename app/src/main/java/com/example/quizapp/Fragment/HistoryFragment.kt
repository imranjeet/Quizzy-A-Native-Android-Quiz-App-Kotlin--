package com.example.quizapp.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentContainer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizapp.R
import com.example.quizapp.Withdrawal
import com.example.quizapp.adaptor.HistoryAdaptor
import com.example.quizapp.databinding.FragmentHistoryBinding
import com.example.quizapp.model.HistoryModelClass
import com.example.quizapp.model.User
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import java.util.Collections

class HistoryFragment : Fragment() {
    val binding by lazy {
        FragmentHistoryBinding.inflate(layoutInflater)
    }

    lateinit var adaptor:HistoryAdaptor

    private var ListHistory = ArrayList<HistoryModelClass>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Firebase.database.reference.child("playerCoinHistory")
            .child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    ListHistory.clear()
                    var ListHistory1 = ArrayList<HistoryModelClass>()
                    for (dataSnapshot in snapshot.children){
                        var data = dataSnapshot.getValue(HistoryModelClass::class.java)
                        ListHistory1.add(data!!)
                    }
                    Collections.reverse(ListHistory1)
                    ListHistory.addAll(ListHistory1)
                    adaptor.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
//                    TODO("Not yet implemented")
                }

            })
//        ListHistory.add(HistoryModelClass("12:03", "200", false))
//        ListHistory.add(HistoryModelClass("05:03", "500", false))
//        ListHistory.add(HistoryModelClass("11:03", "1000", false))
//        ListHistory.add(HistoryModelClass("09:03", "800", false))

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Firebase.database.reference.child("playerCoin").child(Firebase.auth.currentUser!!.uid).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    binding.CoinWithdrawal1.text = (snapshot.getValue() as Long).toString()
                }
                else{
                    var temp = 0
                    binding.CoinWithdrawal1.text = temp.toString()

                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        binding.CoinWithdrawal.setOnClickListener {
            val bottomSheetDialog: BottomSheetDialogFragment = Withdrawal()
            bottomSheetDialog.show(requireActivity().supportFragmentManager, "Test")
            bottomSheetDialog.enterTransition

        }
        binding.CoinWithdrawal1.setOnClickListener {
            val bottomSheetDialog: BottomSheetDialogFragment = Withdrawal()
            bottomSheetDialog.show(requireActivity().supportFragmentManager, "Test")
            bottomSheetDialog.enterTransition

        }
        binding.HistoryRecycleView.layoutManager= LinearLayoutManager(requireContext())
        adaptor = HistoryAdaptor(ListHistory)
        binding.HistoryRecycleView.adapter = adaptor
        binding.HistoryRecycleView.setHasFixedSize(true)

        Firebase.database.reference.child("Users").child(Firebase.auth.currentUser!!.uid).addValueEventListener(object  :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var user = snapshot.getValue(User::class.java)
                binding.Name.text=user?.name
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