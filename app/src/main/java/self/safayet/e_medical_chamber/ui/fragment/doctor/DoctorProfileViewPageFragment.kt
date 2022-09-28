package self.safayet.e_medical_chamber.ui.fragment.doctor

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import self.safayet.e_medical_chamber.ui.fragment.doctor.DoctorProfileViewPageFragmentDirections
import self.safayet.e_medical_chamber.R
import self.safayet.e_medical_chamber.databinding.FragmentDoctorProfilePageBinding
import self.safayet.e_medical_chamber.databinding.FragmentDoctorProfileViewPageBinding
import self.safayet.e_medical_chamber.databinding.FragmentPatientProfileViewPageBinding
import self.safayet.e_medical_chamber.domain.model.Doctor
import self.safayet.e_medical_chamber.ui.viewmodel.UserViewModel
import self.safayet.e_medical_chamber.utils.Utils

class DoctorProfileViewPageFragment : Fragment() {

    private lateinit var mBinding: FragmentDoctorProfileViewPageBinding
    private val mUserViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_doctor_profile_view_page, container, false)

        mBinding = FragmentDoctorProfileViewPageBinding.bind(view)

        mBinding.btnEdit.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.navigate_to_edit_page)
        }

        mUserViewModel.user.observe(viewLifecycleOwner) {
            val doctor = it as Doctor

            mBinding.tvDoctorNameView.text = doctor.fullName
            mBinding.imageView2.setImageDrawable(this.context?.let { it1 ->
                Utils.assetsImage(
                    it1,
                    "doctor_placeholder.png"
                )
            })

            mBinding.tvCp.setOnClickListener {
                val action = DoctorProfileViewPageFragmentDirections.navigateToEditPage(
                    true
                )

                Navigation.findNavController(view).navigate(action)
            }

        }
        return view
    }

    companion object {

    }
}