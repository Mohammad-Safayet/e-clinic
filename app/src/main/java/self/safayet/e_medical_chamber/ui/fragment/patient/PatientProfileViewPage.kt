package self.safayet.e_medical_chamber.ui.fragment.patient

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import self.safayet.e_medical_chamber.ui.fragment.patient.PatientProfileViewPageDirections
import self.safayet.e_medical_chamber.R
import self.safayet.e_medical_chamber.databinding.FragmentPatientProfileViewPageBinding
import self.safayet.e_medical_chamber.domain.model.Patient
import self.safayet.e_medical_chamber.ui.viewmodel.UserViewModel
import self.safayet.e_medical_chamber.utils.Utils

class PatientProfileViewPage : Fragment() {

    private lateinit var mBinding: FragmentPatientProfileViewPageBinding
    private val mUserViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_patient_profile_view_page, container, false)

        mBinding = FragmentPatientProfileViewPageBinding.bind(view)

        mBinding.btnEdit.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.navigate_to_profile_edit_page)
        }

        mUserViewModel.user.observe(viewLifecycleOwner) {
            val patient = it as Patient

            mBinding.tvPatientNameView.text = it.fullName
            mBinding.imageView2.setImageDrawable(this.context?.let { it1 ->
                Utils.assetsImage(
                    it1,
                    "profile.png"
                )
            })

            mBinding.tvCp.setOnClickListener {
                val action = PatientProfileViewPageDirections.navigateToProfileEditPage(
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