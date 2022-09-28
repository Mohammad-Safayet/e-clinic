package self.safayet.e_medical_chamber.ui.fragment.patient

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import self.safayet.e_medical_chamber.ui.fragment.patient.PatientProfilePageArgs
import self.safayet.e_medical_chamber.R
import self.safayet.e_medical_chamber.databinding.FragmentPatientProfilePageBinding
import self.safayet.e_medical_chamber.domain.model.Patient
import self.safayet.e_medical_chamber.ui.MainActivity
import self.safayet.e_medical_chamber.ui.viewmodel.UserViewModel
import self.safayet.e_medical_chamber.utils.Utils
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker

//TODO: setOnCLickListener to all the input fields
class PatientProfilePage : Fragment() {

    private val mArgs: PatientProfilePageArgs by navArgs()
    private lateinit var mBinding: FragmentPatientProfilePageBinding
    private val mUserViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_patient_profile_page, container, false)

        mBinding = FragmentPatientProfilePageBinding.bind(view)
        var age = 0L

        if (mArgs.password) {
            mBinding.inputPatientGenderLayout.isVisible = false
            mBinding.inputPatientAgeLayout.isVisible = false
            mBinding.inputPatientNameLayout.isVisible = false
            mBinding.inputPatientEmailLayout.isVisible = false
        }

        mBinding.inputPatientAge.setOnClickListener {
            val mCalendarConstraints =
                CalendarConstraints.Builder()
                    .setValidator(DateValidatorPointBackward.now())
                    .setEnd(MaterialDatePicker.todayInUtcMilliseconds())
            val datePicker =
                Utils.showDateTimePicker(mCalendarConstraints) { date: String, dateLong: Long ->
                    mBinding.inputPatientAge.setText("Loading...")
                    val result = Utils.calculateAge(dateLong)

                    age = result
                    Log.d(PatientRegisterPage.TAG, "showDateTimePicker: $age")
                    mBinding.inputPatientAge.setText(date)

                }

            datePicker.show(requireFragmentManager(), "")
        }

        mUserViewModel.user.observe(viewLifecycleOwner) {
            val patient = it as Patient

            mBinding.inputPatientEmail.setText(patient.email)
            mBinding.inputPatientName.setText(patient.fullName)
            mBinding.inputPatientAge.setText(patient.dob)
            mBinding.inputPatientGender.setText(patient.gender)
            mBinding.inputPatientPassword.setText(patient.password)

            mBinding.btnPatientSave.setOnClickListener {
                mBinding.btnPatientSave.text = "Loading..."

                patient.email = mBinding.inputPatientEmail.text.toString()
                patient.fullName = mBinding.inputPatientName.text.toString()
                patient.age = age
                patient.dob = mBinding.inputPatientAge.text.toString()
                patient.password = mBinding.inputPatientPassword.text.toString()
                patient.gender = mBinding.inputPatientGender.text.toString()

                mUserViewModel.updatePatient(patient)

                Toast.makeText(
                    requireContext(),
                    "Profile is updated successfully!",
                    Toast.LENGTH_LONG
                ).show()

                mBinding.btnPatientSave.text = "Save"
                Navigation.findNavController(view).navigate(R.id.navigate_to_profile_view_page)
            }
        }

        mBinding.btnCancel.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.navigate_to_profile_view_page)
//            activity?.startActivity((Intent(activity, MainActivity::class.java)))
        }

        return view
    }

    override fun onResume() {
        super.onResume()

        val modes = resources.getStringArray(R.array.gender)
        mBinding.inputPatientGender.setAdapter(
            ArrayAdapter(requireActivity(), R.layout.dropdown_item, modes)
        )
    }

    companion object {

    }
}