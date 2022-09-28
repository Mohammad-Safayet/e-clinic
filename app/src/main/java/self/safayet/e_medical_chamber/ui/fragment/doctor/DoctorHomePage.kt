package self.safayet.e_medical_chamber.ui.fragment.doctor

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import self.safayet.e_medical_chamber.ui.fragment.doctor.DoctorHomePageDirections
import self.safayet.e_medical_chamber.R
import self.safayet.e_medical_chamber.databinding.FragmentDoctorHomePageBinding
import self.safayet.e_medical_chamber.domain.model.Appointment
import self.safayet.e_medical_chamber.domain.model.Doctor
import self.safayet.e_medical_chamber.ui.adapter.AppointmentListAdapter
import self.safayet.e_medical_chamber.ui.viewmodel.UserViewModel

class DoctorHomePage : Fragment() {

    private lateinit var mBinding: FragmentDoctorHomePageBinding
    private val mUserViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_doctor_home_page, container, false)

        mBinding = FragmentDoctorHomePageBinding.bind(view)

        mUserViewModel.user.observe(viewLifecycleOwner) { it ->
            val doctor: Doctor = it as Doctor

            val appointments: MutableList<Appointment> = mutableListOf()
            doctor.appointments.forEach { appointment: Appointment ->
                if (appointment.isActive && appointment.time != "--") {
                    Log.d("TAG", "onCreateView: ${appointment.isActive}")
                    appointments += appointment
                }
            }

            if (appointments.isEmpty()) {
                mBinding.tvDefaultText.isVisible = true
                mBinding.rvActivePatientList.isVisible = false
            } else {
                mBinding.tvDefaultText.isVisible = false
                mBinding.rvActivePatientList.isVisible = true

                mBinding.rvActivePatientList.adapter =
                    AppointmentListAdapter(appointments) { appointment ->
                        val action = DoctorHomePageDirections.navigateToAppointment(
                            appointment.patientId,
                            appointment.doctorId
                        )

                        Navigation.findNavController(view).navigate(action)
                    }
            }
        }

        return view
    }

    companion object {

    }
}