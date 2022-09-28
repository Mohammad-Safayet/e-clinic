package self.safayet.e_medical_chamber.ui.fragment.doctor

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import self.safayet.e_medical_chamber.ui.fragment.doctor.DoctorProfilePageArgs
import self.safayet.e_medical_chamber.R
import self.safayet.e_medical_chamber.databinding.FragmentDoctorCategoryPageBinding
import self.safayet.e_medical_chamber.databinding.FragmentDoctorProfilePageBinding
import self.safayet.e_medical_chamber.domain.model.Doctor
import self.safayet.e_medical_chamber.ui.MainActivity
import self.safayet.e_medical_chamber.ui.viewmodel.UserViewModel

class DoctorProfilePage : Fragment() {

    private val mArgs: DoctorProfilePageArgs by navArgs()
    private lateinit var mBinding: FragmentDoctorProfilePageBinding
    private val mUserViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_doctor_profile_page, container, false)

        mBinding = FragmentDoctorProfilePageBinding.bind(view)
        mBinding.pbDoctorRegister.isVisible = false

        if (mArgs.password) {
            mBinding.inputDoctorEmailLayout.isVisible = false
            mBinding.inputDoctorNameLayout.isVisible = false
            mBinding.inputDoctorNidLayout.isVisible = false
            mBinding.inputDoctorTypeLayout.isVisible = false
            mBinding.inputDoctorIdLayout.isVisible = false
            mBinding.inputDoctorSpecialistLayout.isVisible = false
            mBinding.inputDoctorTitleLayout.isVisible = false
            mBinding.inputDoctorMobileNumberLayout.isVisible = false
            mBinding.inputDoctorGenderLayout.isVisible = false
            mBinding.inputDoctorAboutLayout.isVisible = false
        }

        mUserViewModel.user.observe(viewLifecycleOwner) {
            val user = it as Doctor

            mBinding.inputDoctorEmail.setText(user.email)
            mBinding.inputDoctorName.setText(user.fullName)
            mBinding.inputDoctorType.setText(user.type)
            mBinding.inputDoctorId.setText(user.id)
            mBinding.inputDoctorNid.setText(user.nid)
            mBinding.inputDoctorSpecialist.setText(user.specialist)
            mBinding.inputDoctorTitle.setText(user.title)
            mBinding.inputDoctorMobileNumber.setText(user.mobileNo)
            mBinding.inputDoctorGender.setText(user.gender)
            mBinding.inputDoctorAbout.setText(user.desc)
            mBinding.inputDoctorPassword.setText(user.password)

            mBinding.btnSave.setOnClickListener {
                val inputMethodManager =
                    activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
                mBinding.pbDoctorRegister.isVisible = true
                mBinding.btnSave.text = "Loading..."

                user.email = mBinding.inputDoctorEmail.text.toString()
                user.fullName = mBinding.inputDoctorName.text.toString()
                user.type = mBinding.inputDoctorType.text.toString()
                user.id = mBinding.inputDoctorId.text.toString()
                user.nid = mBinding.inputDoctorNid.text.toString()
                user.specialist = mBinding.inputDoctorSpecialist.text.toString()
                user.title = mBinding.inputDoctorTitle.text.toString()
                user.mobileNo = mBinding.inputDoctorMobileNumber.text.toString()
                user.gender = mBinding.inputDoctorGender.text.toString()
                user.desc = mBinding.inputDoctorAbout.text.toString()
                user.password = mBinding.inputDoctorPassword.text.toString()

                mUserViewModel.updateDoctor(user)
                mUserViewModel.getDoctor(user.email, user.password)

                Toast.makeText(context, "Profile is updated.", Toast.LENGTH_LONG).show()

                mBinding.btnSave.text = "Save"
                mBinding.pbDoctorRegister.isVisible = false
                Navigation.findNavController(view).navigate(R.id.navigate_to_view_page)
            }
        }

        mBinding.btnCanel.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.navigate_to_view_page)
//            activity?.startActivity((Intent(activity,MainActivity::class.java)))
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