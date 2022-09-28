package self.safayet.e_medical_chamber.ui.fragment.doctor

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import self.safayet.e_medical_chamber.ui.fragment.doctor.DoctorRegisterPageDirections
import self.safayet.e_medical_chamber.R
import self.safayet.e_medical_chamber.databinding.FragmentDoctorRegisterPageBinding
import self.safayet.e_medical_chamber.domain.model.Doctor
import self.safayet.e_medical_chamber.ui.viewmodel.UserViewModel
import kotlinx.coroutines.flow.collectLatest

class DoctorRegisterPage : Fragment(R.layout.fragment_doctor_register_page) {

    private lateinit var mBinding: FragmentDoctorRegisterPageBinding
    private val mUserViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_doctor_register_page, container, false)

        mBinding = FragmentDoctorRegisterPageBinding.bind(view)
        mBinding.pbDoctorRegister.isVisible = false

        mBinding.btnRegister.setOnClickListener {
            mBinding.pbDoctorRegister.isVisible = true
            mBinding.btnRegister.text = "Loading..."
            val email = mBinding.inputDoctorEmail.text.toString()
            val password = mBinding.inputDoctorPassword.text.toString()
            val fullName = mBinding.inputDoctorName.text.toString()
            val gender = mBinding.inputDoctorGender.text.toString()
            val nid = mBinding.inputDoctorNid.text.toString()
            val type = mBinding.inputDoctorType.text.toString()
            val regId = mBinding.inputDoctorId.text.toString()
            val title = mBinding.inputDoctorTitle.text.toString()
            val mobileNo = mBinding.inputDoctorMobileNumber.text.toString()
            val specialist = mBinding.inputDoctorSpecialist.text.toString()
            val desc = mBinding.inputDoctorAbout.text.toString()

            val doctor = Doctor(
                "",
                email,
                fullName,
                type,
                regId,
                password,
                nid,
                specialist,
                title,
                mobileNo,
                gender,
                desc
            )

            mUserViewModel.registerDoctor(doctor)
            lifecycleScope.launchWhenStarted {
                mUserViewModel.regSuccess.collectLatest {
                    if (it) {
                        val action = DoctorRegisterPageDirections.navigateBackToLoginPg2(
                            email,
                            password,
                            false
                        )
                        Navigation.findNavController(view).navigate(action)
                    }
                }
            }
        }

        return view
    }

    override fun onResume() {
        super.onResume()

        val types = resources.getStringArray(R.array.doctor_types)
        mBinding.inputDoctorType.setAdapter(
            ArrayAdapter(requireActivity(), R.layout.dropdown_item, types)
        )

        val gender = resources.getStringArray(R.array.gender)
        mBinding.inputDoctorGender.setAdapter(
            ArrayAdapter(requireActivity(), R.layout.dropdown_item, gender)
        )

        val categories = resources.getStringArray(R.array.doctor_categories)
        mBinding.inputDoctorSpecialist.setAdapter(
            ArrayAdapter(requireActivity(), R.layout.dropdown_item, categories)
        )

        val titles = resources.getStringArray(R.array.doctor_titles)
        mBinding.inputDoctorTitle.setAdapter(
            ArrayAdapter(requireActivity(), R.layout.dropdown_item, titles)
        )
    }

    companion object {

    }
}