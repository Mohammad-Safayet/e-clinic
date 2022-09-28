package self.safayet.e_medical_chamber.ui.fragment.doctor

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import self.safayet.e_medical_chamber.R
import self.safayet.e_medical_chamber.databinding.FragmentDoctorNewPatientPageBinding
import self.safayet.e_medical_chamber.domain.model.Appointment
import self.safayet.e_medical_chamber.domain.model.Doctor
import self.safayet.e_medical_chamber.ui.adapter.AppointmentListAdapter
import self.safayet.e_medical_chamber.ui.dialog.PendingPatientDialog
import self.safayet.e_medical_chamber.ui.viewmodel.UserViewModel

class DoctorNewPatientPage : Fragment(R.layout.fragment_doctor_new_patient_page) {

    private lateinit var mBinding: FragmentDoctorNewPatientPageBinding
    private val mUserViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_doctor_new_patient_page, container, false)

        mBinding = FragmentDoctorNewPatientPageBinding.bind(view)

        mUserViewModel.user.observe(viewLifecycleOwner) { it ->
            val doctor: Doctor = it as Doctor

            val appointments: MutableList<Appointment> = mutableListOf()
            doctor.appointments.forEach { appointment: Appointment ->
                if (appointment.time == "--") {
                    Log.d(TAG, "onCreateView: ${appointment.time}")
                    appointments += appointment
                }
            }

            if (appointments.isEmpty()) {
                mBinding.tvNewPatientText.isVisible = true
                mBinding.rvNewPatientList.isVisible = false
            } else {
                mBinding.tvNewPatientText.isVisible = false
                mBinding.rvNewPatientList.isVisible = true

                mBinding.rvNewPatientList.adapter =
                    AppointmentListAdapter(appointments) { appointment ->
                        PendingPatientDialog(appointment).show(
                            childFragmentManager,
                            "Show Appointment Details"
                        )
                    }
            }
        }

        return view
    }

    companion object {
        const val TAG = "DoctorNewPatientPage"
    }
}