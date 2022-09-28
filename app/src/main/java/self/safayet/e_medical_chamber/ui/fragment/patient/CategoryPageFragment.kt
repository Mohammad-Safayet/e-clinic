package self.safayet.e_medical_chamber.ui.fragment.patient

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import self.safayet.e_medical_chamber.ui.fragment.patient.CategoryPageFragmentArgs
import self.safayet.e_medical_chamber.R
import self.safayet.e_medical_chamber.databinding.FragmentCategoryPageBinding
import self.safayet.e_medical_chamber.domain.model.Doctor
import self.safayet.e_medical_chamber.domain.model.Patient
import self.safayet.e_medical_chamber.ui.adapter.DoctorListAdapter
import self.safayet.e_medical_chamber.ui.dialog.DetailsDialog
import self.safayet.e_medical_chamber.ui.viewmodel.PatientViewModel
import self.safayet.e_medical_chamber.ui.viewmodel.UserViewModel


class CategoryPageFragment : Fragment() {

    private val mArgs: CategoryPageFragmentArgs by navArgs()
    private lateinit var mBinding: FragmentCategoryPageBinding
    private val mPatientViewModel: PatientViewModel by activityViewModels()
    private val mUserViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_category_page, container, false)

        mBinding = FragmentCategoryPageBinding.bind(view)

        mPatientViewModel.getAvailableDoctors()
        mPatientViewModel.availableDoctors.observe(viewLifecycleOwner) {
            if (it != null) {
                mBinding.textView8.isVisible = false

                val list = it.filter { doctor ->
                    doctor.specialist == mArgs.category
                }

                if (list.isEmpty()) {
                    mBinding.rvCategoriesDoctor.isVisible = false
                    mBinding.textView8.isVisible = true
                } else {
                    mBinding.rvCategoriesDoctor.isVisible = true
                    mBinding.rvCategoriesDoctor.adapter = DoctorListAdapter(
                        requireContext(),
                        list
                    ) { doctor: Doctor ->
                        val patient = mUserViewModel.user.value as Patient

                        DetailsDialog(patient.id, doctor).show(
                            childFragmentManager,
                            "Show Doctor Details"
                        )
                    }
                }

                Log.d("TAG", "onCreateView: ${list}")
            } else {
                mBinding.rvCategoriesDoctor.isVisible = false
            }
        }

        return view
    }

    companion object {

    }
}