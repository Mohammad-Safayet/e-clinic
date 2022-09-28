package self.safayet.e_medical_chamber.ui.fragment.patient

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import self.safayet.e_medical_chamber.ui.fragment.patient.PatientHomePageDirections
import self.safayet.e_medical_chamber.R
import self.safayet.e_medical_chamber.databinding.FragmentPatientHomePageBinding
import self.safayet.e_medical_chamber.domain.model.Appointment
import self.safayet.e_medical_chamber.domain.model.Patient
import self.safayet.e_medical_chamber.ui.adapter.AppointmentListAdapter
import self.safayet.e_medical_chamber.ui.viewmodel.UserViewModel

class PatientHomePage : Fragment(R.layout.fragment_patient_home_page) {

    private lateinit var mBinding: FragmentPatientHomePageBinding
    private val mPatientViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_patient_home_page, container, false)

        mBinding = FragmentPatientHomePageBinding.bind(view)

        mPatientViewModel.user.observe(viewLifecycleOwner) {
            val patient: Patient = it as Patient

            if (patient.appointment.isEmpty()) {
                mBinding.tvPatientHomeText.isVisible = true
                mBinding.rvActiveDoctorList.isVisible = false
                mBinding.rvNonActiveDoctorList.isVisible = false
            } else {
                mBinding.tvPatientHomeText.isVisible = false
                val activeAppointments = mutableListOf<Appointment>()
                val nonActiveAppointments = mutableListOf<Appointment>()

                patient.appointment.forEach { appointment ->
                    if (appointment.isActive) activeAppointments.add(appointment)
                    else {
                        if (activeAppointments.isNotEmpty()) {
                            activeAppointments.forEach { active ->
                                Log.d("TAG", "onCreateView: activeappointment = ${active.doctorId} ${appointment.doctorId}")
                                if (active.doctorId != appointment.doctorId)
                                    nonActiveAppointments.add(appointment)
                            }
                        } else {
                            nonActiveAppointments.add(appointment)
                        }
                    }
                }

                if (activeAppointments.isEmpty()) {
                    mBinding.tvTextActiveAppointments.isVisible = false
                    mBinding.rvActiveDoctorList.isVisible = false
                } else {
                    mBinding.tvTextActiveAppointments.isVisible = true
                    mBinding.rvActiveDoctorList.isVisible = true

                    mBinding.rvActiveDoctorList.adapter =
                        AppointmentListAdapter(activeAppointments) { appointment ->
                            Log.d("TAG", "onCreateView: $appointment")
                            val action = PatientHomePageDirections.navToAppointmentDetailsPage(
                                appointment.patientId,
                                appointment.doctorId
                            )

                            Navigation.findNavController(view).navigate(action)
                        }
                }

                if (nonActiveAppointments.isEmpty()) {
                    mBinding.tvTextPastAppointments.isVisible = false
                    mBinding.rvNonActiveDoctorList.isVisible = false
                } else {
                    mBinding.tvTextPastAppointments.isVisible = true
                    mBinding.rvNonActiveDoctorList.isVisible = true

                    val temp = removeDuplicateAppointments(nonActiveAppointments)

                    mBinding.rvNonActiveDoctorList.adapter =
                        AppointmentListAdapter(temp) { appointment ->
                            Log.d("TAG", "onCreateView: $appointment")
                            val action = PatientHomePageDirections.navToAppointmentDetailsPage(
                                appointment.patientId,
                                appointment.doctorId
                            )

                            Navigation.findNavController(view).navigate(action)
                        }
                }
            }
        }

        return view
    }

    fun removeDuplicateAppointments(appointments: List<Appointment>): List<Appointment> {
        val tempList = mutableListOf<Appointment>()
        val doctorIdList = mutableListOf<String>()

        appointments.forEach {
            if (doctorIdList.isNotEmpty()) {
                doctorIdList.forEach { id ->
                    if (it.doctorId != id) {
                        doctorIdList.add(it.doctorId)
                        tempList.add(it)
                    }
                }
            } else {
                doctorIdList.add(it.doctorId)
                tempList.add(it)
            }
        }

        return tempList
    }

    companion object {

    }
}