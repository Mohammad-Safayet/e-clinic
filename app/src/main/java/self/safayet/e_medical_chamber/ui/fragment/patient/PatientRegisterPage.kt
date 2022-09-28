package self.safayet.e_medical_chamber.ui.fragment.patient

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import self.safayet.e_medical_chamber.ui.fragment.patient.PatientRegisterPageDirections
import self.safayet.e_medical_chamber.R
import self.safayet.e_medical_chamber.databinding.FragmentPatientRegisterPageBinding
import self.safayet.e_medical_chamber.domain.model.Patient
import self.safayet.e_medical_chamber.ui.viewmodel.UserViewModel
import self.safayet.e_medical_chamber.utils.Utils
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.flow.collectLatest
import kotlin.properties.Delegates

class PatientRegisterPage : Fragment(R.layout.fragment_patient_register_page) {

    private lateinit var mBinding: FragmentPatientRegisterPageBinding
    private val mPatientViewModel: UserViewModel by activityViewModels()
    private var age by Delegates.notNull<Long>()

    override fun onResume() {
        super.onResume()

        val modes = resources.getStringArray(R.array.gender)
        mBinding.inputPatientGender.setAdapter(
            ArrayAdapter(requireActivity(), R.layout.dropdown_item, modes)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_patient_register_page, container, false)

        mBinding = FragmentPatientRegisterPageBinding.bind(view)

        mBinding.pbPatientRegister.isVisible = false
        mBinding.inputPatientAge.setOnClickListener {
            val mCalendarConstraints =
                CalendarConstraints.Builder()
                    .setValidator(DateValidatorPointBackward.now())
                    .setEnd(MaterialDatePicker.todayInUtcMilliseconds())
            val datePicker = Utils.showDateTimePicker(mCalendarConstraints, this::setDate)

            datePicker.show(requireFragmentManager(), "")
        }

        mBinding.btnRegister.setOnClickListener {
            mBinding.pbPatientRegister.isVisible = true
            mBinding.btnRegister.text = "Loading..."
            val email = mBinding.inputPatientEmail.text.toString()
            val password = mBinding.inputPatientPassword.text.toString()
            val fullName = mBinding.inputPatientName.text.toString()
            val gender = mBinding.inputPatientGender.text.toString()
            val dob = mBinding.inputPatientAge.text.toString()

            val profile = Patient(
                "",
                email,
                fullName,
                age,
                dob,
                password,
                gender
            )

            mPatientViewModel.registerPatient(profile)
            lifecycleScope.launchWhenStarted {
                mPatientViewModel.regSuccess.collectLatest {
//                    if (it) {
                    val action = PatientRegisterPageDirections.navigateBackToLoginPg1(
                        email,
                        password,
                        true
                    )
                    Navigation.findNavController(view).navigate(action)
//                    } else {
//                        TODO("Implement loading screen")
//                    }
                }
            }
        }

        return view
    }

    private fun setDate(date: String, dateLong: Long) {
        val result = Utils.calculateAge(dateLong)

        age = result
        Log.d(TAG, "showDateTimePicker: $age")
        mBinding.inputPatientAge.setText(date)
    }

    companion object {
        val TAG = PatientRegisterPage::class.java.toString()
    }
}