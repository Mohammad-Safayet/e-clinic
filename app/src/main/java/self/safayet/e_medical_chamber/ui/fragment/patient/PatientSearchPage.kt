package self.safayet.e_medical_chamber.ui.fragment.patient

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import self.safayet.e_medical_chamber.R
import self.safayet.e_medical_chamber.databinding.FragmentPatientSearchPageBinding
import self.safayet.e_medical_chamber.domain.model.Doctor
import self.safayet.e_medical_chamber.domain.model.Patient
import self.safayet.e_medical_chamber.ui.adapter.DoctorListAdapter
import self.safayet.e_medical_chamber.ui.dialog.DetailsDialog
import self.safayet.e_medical_chamber.ui.viewmodel.PatientViewModel
import self.safayet.e_medical_chamber.ui.viewmodel.UserViewModel

class PatientSearchPage : Fragment(R.layout.fragment_patient_search_page) {

    private lateinit var mBinding: FragmentPatientSearchPageBinding
    private val mPatientViewModel: PatientViewModel by activityViewModels()
    private val mUserViewModel: UserViewModel by activityViewModels()
    private val TAG = "PatientSearchPage"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_patient_search_page, container, false)

        mBinding = FragmentPatientSearchPageBinding.bind(view)
        mPatientViewModel.getAvailableDoctors()

        mPatientViewModel.availableDoctors.observe(viewLifecycleOwner) { doctors ->
            if (doctors.isNotEmpty()) {
                mBinding.tvSearchPageDesc.isVisible = false
                mBinding.rvSearchList.isVisible = true

                mBinding.svDoctorSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        mPatientViewModel.filterDoctor(newText!!)
                        return true
                    }

                })

                val patient = mUserViewModel.user.value as Patient
                mBinding.rvSearchList.adapter = DoctorListAdapter(
                    requireContext(),
                    doctors
                ) { doctor: Doctor ->
                    DetailsDialog(patient.id, doctor).show(
                        childFragmentManager,
                        "Show Doctor Details"
                    )
                }

                Log.d(TAG, "onCreateView: $doctors")
            } else {
                mBinding.tvSearchPageDesc.isVisible = true
                mBinding.rvSearchList.isVisible = false
            }
        }

        return view
    }

}