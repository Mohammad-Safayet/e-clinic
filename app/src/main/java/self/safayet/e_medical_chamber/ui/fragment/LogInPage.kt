package self.safayet.e_medical_chamber.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import self.safayet.e_medical_chamber.ui.fragment.LogInPageArgs
import self.safayet.e_medical_chamber.R
import self.safayet.e_medical_chamber.databinding.FragmentLogInPageBinding
import self.safayet.e_medical_chamber.ui.viewmodel.UserViewModel
import com.google.android.material.progressindicator.CircularProgressIndicatorSpec
import com.google.android.material.progressindicator.IndeterminateDrawable

class LogInPage : Fragment(R.layout.fragment_log_in_page) {

    private lateinit var mBinding: FragmentLogInPageBinding
    private val mArgs: LogInPageArgs by navArgs()
    private val mUserViewModel: UserViewModel by activityViewModels()

    private val gotUser = MutableLiveData<Boolean>(false)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_log_in_page, container, false)

        mBinding = FragmentLogInPageBinding.bind(view)

        val isPatient = mArgs.isPatient
        val email = mArgs.email
        val password = mArgs.password

        if (email != null && password != null) {
            mBinding.inputEmail.setText(email)
            mBinding.inputPassword.setText(password)
        }
        mBinding.pbLoginProgress.isVisible = false


        mBinding.btnLogin.setOnClickListener {
            mBinding.pbLoginProgress.isVisible = true
            mBinding.btnLogin.text = "Loading..."
            val userEmail = mBinding.inputEmail.text.toString()
            val userPassword = mBinding.inputPassword.text.toString()

            if (userEmail.isEmpty() && userPassword.isEmpty()) {
                Toast
                    .makeText(context, "Fill the credentials to login.", Toast.LENGTH_SHORT)
                    .show()
            } else {
                //TODO("Get rid of the repetitive code")
                if (isPatient) {
                    getUser(userEmail, userPassword)

                    gotUser.observe(viewLifecycleOwner) {
                        if (it == true)
                            Navigation.findNavController(view)
                                .navigate(R.id.navigate_to_patient_home_pg)
                    }
                } else {
                    getUser(userEmail, userPassword)

                    gotUser.observe(viewLifecycleOwner) {
                        if (it == true)
                            Navigation.findNavController(view)
                                .navigate(R.id.navigate_to_doctor_home_pg)
                    }
                }
            }
        }

        mBinding.btnRegister.setOnClickListener {
            if (isPatient) {
                Navigation.findNavController(view).navigate(R.id.navigate_to_patient_register_pg)
            } else {
                Navigation.findNavController(view).navigate(R.id.navigate_to_doctor_register_pg)
            }
        }

        return view
    }

    private fun getUser(email: String, password: String) {
        if (mArgs.isPatient) {
            mUserViewModel.getPatient(email, password)
        } else {
            mUserViewModel.getDoctor(email, password)
        }

        mUserViewModel.user.observe(viewLifecycleOwner) {
            if (it != null) {
                gotUser.value = true
                Log.d(TAG, "gotUser: $it")
            } else {
                gotUser.value = false
            }
        }
    }

    companion object {
        val TAG = LogInPage::class.java.toString()
    }
}