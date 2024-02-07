package com.example.quizapp.Fragment

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.quizapp.R
import com.example.quizapp.Withdrawal
import com.example.quizapp.databinding.FragmentSpinBinding
import com.example.quizapp.model.HistoryModelClass
import com.example.quizapp.model.User
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import java.sql.Time
import java.util.concurrent.TimeUnit
import kotlin.random.Random as Random1

class SpinFragment : Fragment() {
    private lateinit var binding: FragmentSpinBinding
    private lateinit var timer: CountDownTimer
    private val itemTitles = arrayOf("100", "Try Again", "500", "Try Again", "200", "Try Again")
    var currentChance=0L
    var currentCoin=0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSpinBinding.inflate(inflater, container, false)
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
        Firebase.database.reference.child("PlayChance").child(Firebase.auth.currentUser!!.uid).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    currentChance = snapshot.getValue() as Long
                    binding.spinChance.text = currentChance.toString()
                }else{
                    var temp = 0
                    binding.spinChance.text = temp.toString()
                    binding.Spin.isEnabled=false
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        Firebase.database.reference.child("playerCoin").child(Firebase.auth.currentUser!!.uid).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    currentCoin = snapshot.getValue() as Long
                    binding.CoinWithdrawal1.text = currentCoin.toString()
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
        return binding.root
    }

    private fun showResult(itemTitle: String,spin:Int) {
        Toast.makeText(requireContext(), itemTitle, Toast.LENGTH_SHORT).show()
        if(spin%2==0){
            var winCoin = itemTitle.toInt()
            currentCoin += winCoin
            Firebase.database.reference.child("playerCoin")
                .child(Firebase.auth.currentUser!!.uid)
                .setValue(currentCoin)
            var historyModelClass=HistoryModelClass(System.currentTimeMillis().toString(), winCoin.toString(),false)
            Firebase.database.reference.child("playerCoinHistory")
                .child(Firebase.auth.currentUser!!.uid).push()
                .setValue(historyModelClass)

            binding.CoinWithdrawal1.text = currentCoin.toString()
        }
        currentChance -= 1
        Firebase.database.reference.child("PlayChance").child(Firebase.auth.currentUser!!.uid).setValue(currentChance)
        binding.Spin.isEnabled = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        binding.Spin.setOnClickListener {
            binding.Spin.isEnabled = false

            if(currentChance>0){
                val spin = Random1.nextInt(6)
                val degrees = 60f * spin
                timer = object : CountDownTimer(5000, 50) {
                    var rotation = 0f

                    override fun onTick(millisUnit: Long) {
                        rotation += 5f
                        if (rotation >= degrees) {
                            rotation = degrees
                            timer.cancel()
                            showResult(itemTitles[spin], spin)
                        }
                        binding.wheel.rotation = rotation
                    }

                    override fun onFinish() {

                    }

                }.start()
            } else {
                Toast.makeText(activity, "Out of spin chance", Toast.LENGTH_SHORT).show()
            }
        }
    }

}