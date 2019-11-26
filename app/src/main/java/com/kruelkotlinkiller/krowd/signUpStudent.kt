package com.kruelkotlinkiller.krowd

import android.app.AlertDialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.kruelkotlinkiller.krowd.databinding.FragmentSignUpStudentBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [signUpStudent.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [signUpStudent.newInstance] factory method to
 * create an instance of this fragment.
 */



class SignUpStudent : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var binding: FragmentSignUpStudentBinding
    lateinit var submitBtn : Button
    lateinit var fName : EditText
    lateinit var lName : EditText
    lateinit var pWord : EditText
    lateinit var email : EditText
    lateinit var databaseReference: DatabaseReference
    lateinit var database : FirebaseDatabase
    lateinit var mAuth : FirebaseAuth
    lateinit var studentId : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =  DataBindingUtil.inflate(inflater,R.layout.fragment_sign_up_student, container, false)
        submitBtn = binding.button
        fName = binding.simpleEditText
        lName = binding.simpleEditText3
        pWord = binding.simpleEditText6
        email = binding.simpleEditText4
        databaseReference = FirebaseDatabase.getInstance().reference
        mAuth = FirebaseAuth.getInstance()
        submitBtn.setOnClickListener{ view : View ->
            if(fName.text.toString().isNotEmpty()
                && lName.text.toString().isNotEmpty()
                && email.text.toString().isNotEmpty()
                && pWord.text.toString().isNotEmpty()
                && isEmailValid(email.text.toString())) {
                saveStudent()
                view.findNavController().navigate(R.id.action_signUpStudent_to_logIn)
            }else{
                val builder = AlertDialog.Builder(context)
                builder.setTitle("ERROR")
                if(!isEmailValid(email.text.toString())){
                    builder.setMessage("Please enter a valid email address")
                }else {
                    builder.setMessage("Please fill in all the fields!")
                }
                builder.setPositiveButton("Ok"){dialog, which ->

                }
                val alert = builder.create()
                alert.show()

            }
        }

        return binding.root
    }

    private fun saveStudent() {
        val firstName = fName.text.toString().trim()
        val lastName = lName.text.toString().trim()
        val password = pWord.text.toString().trim()
        val emailA = email.text.toString().trim()

        if(isEmailValid(emailA)) {
            val ref = FirebaseDatabase.getInstance().getReference("Student")
            studentId = ref.push().key!!
            val student = Student(studentId,firstName,lastName, emailA, "")
            ref.child(studentId).setValue(student)
            mAuth.createUserWithEmailAndPassword(emailA, password)
                .addOnCompleteListener { task: Task<AuthResult> ->
                    if (task.isSuccessful) {
                        Toast.makeText(context, "register successfully", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(context, "register unsuccessfully", Toast.LENGTH_LONG).show()

                    }
                }
        }
        else{
            val builder = AlertDialog.Builder(context)
            builder.setTitle("ERROR")
            builder.setMessage("Please Enter a valid email!")
            builder.setPositiveButton("Ok"){dialog, which ->

            }
            val alert = builder.create()
            alert.show()

        }

    }
    val EMAIL_REGEX = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
    fun isEmailValid(email: String): Boolean {
        return EMAIL_REGEX.toRegex().matches(email)
    }
    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment signUpStudent.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SignUpStudent().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
