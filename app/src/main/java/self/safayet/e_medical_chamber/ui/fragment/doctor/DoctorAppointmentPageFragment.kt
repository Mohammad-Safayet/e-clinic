package self.safayet.e_medical_chamber.ui.fragment.doctor

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import self.safayet.e_medical_chamber.ui.fragment.doctor.DoctorAppointmentPageFragmentArgs
import self.safayet.e_medical_chamber.R
import self.safayet.e_medical_chamber.ui.VideoCallActivity
import self.safayet.e_medical_chamber.databinding.FragmentDoctorAppointmentPageBinding
import self.safayet.e_medical_chamber.domain.model.Appointment
import self.safayet.e_medical_chamber.domain.model.Medicine
import self.safayet.e_medical_chamber.domain.model.Meeting
import self.safayet.e_medical_chamber.ui.adapter.MedicineListAdapter
import self.safayet.e_medical_chamber.ui.adapter.PreviewPrescriptionAdapter
import self.safayet.e_medical_chamber.ui.dialog.AddMedicineDialog
import self.safayet.e_medical_chamber.ui.dialog.PatientPrescriptionDialog
import self.safayet.e_medical_chamber.ui.dialog.SymptomsDialog
import self.safayet.e_medical_chamber.ui.viewmodel.MeetingViewModel
import self.safayet.e_medical_chamber.ui.viewmodel.UserViewModel
import io.agora.agorauikit_android.AgoraVideoViewer
import io.agora.rtc.Constants


class DoctorAppointmentPageFragment : Fragment() {

    private lateinit var mBinding: FragmentDoctorAppointmentPageBinding
    private val mUserViewModel: UserViewModel by activityViewModels()
    private val mMeetingViewModel: MeetingViewModel by activityViewModels()
    private val mArgs: DoctorAppointmentPageFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_doctor_appointment_page, container, false)

        mBinding = FragmentDoctorAppointmentPageBinding.bind(view)

        mBinding.pbProgress.isVisible = false
        mBinding.btnPatientSymptoms.isVisible = false
        mBinding.btnDone.isVisible = false
        mBinding.btnCreateRoom.isVisible = false

        mUserViewModel.getAppointmentById(mArgs.patientId, mArgs.doctorId)
        val patient =
            mUserViewModel.getUserById(mArgs.patientId, true)
        mMeetingViewModel.getMeetingId(mArgs.doctorId, mArgs.patientId)

        var meds = mutableListOf<Medicine>()
        val medsLiveData = MutableLiveData(meds)

        patient.observe(viewLifecycleOwner) {
            if (it == null) {
                mBinding.pbProgress.isVisible = true
                mBinding.btnCreateRoom.isVisible = false
                mBinding.btnDone.isVisible = false
                mBinding.btnPatientSymptoms.isVisible = false
            } else {
                mBinding.pbProgress.isVisible = false
                mBinding.btnCreateRoom.isVisible = true
                mBinding.btnDone.isVisible = true
                mBinding.btnPatientSymptoms.isVisible = true

                mBinding.tvPatientName.text = it.fullName
                mBinding.tvPatientAge.text = "Age: " + it.age
                mBinding.tvPatientEmail.text = "E-mail: " + it.email
                mBinding.tvPatientGender.text = "Gender: " + it.gender
            }
        }

        mUserViewModel.appointments.observe(viewLifecycleOwner) { it ->
            if (it != null) {
                var activeAppointment: Appointment? = null
                val previousAppointments: MutableList<Appointment> = mutableListOf()

                // Divide the active appointment from the previous appointment
                it.forEach { value ->
                    if (value.isActive) {
                        activeAppointment = value
                    } else {
                        previousAppointments.add(value)
                    }
                }

                // Active appointment
                if (activeAppointment != null) {
                    meds = activeAppointment?.prescription?.medicine!!.toMutableList()
                    medsLiveData.value = meds
                    mBinding.rvCurrentPrescription.isVisible = true

                    mBinding.btnPatientSymptoms.setOnClickListener {
                        SymptomsDialog(activeAppointment!!.symptoms).show(
                            childFragmentManager, "Show the symptoms"
                        )
                    }

                    medsLiveData.observe(viewLifecycleOwner) { medicines ->
                        Log.d(
                            "",
                            "onCreateView: medicines = ${medsLiveData.value} "
                        )
                        if (medicines != null) {
                            mBinding.rvCurrentPrescription.adapter = MedicineListAdapter(medicines)
                        }
                    }

                    mBinding.tvPatientAppointment.text = "Appointment: " + activeAppointment!!.time

                    Log.d(
                        "TAG",
                        "onCreateView: updateAppointment ${mUserViewModel.user.value.toString()}"
                    )

                    mBinding.btnDone.setOnClickListener {
                        activeAppointment?.isActive = false

                        mUserViewModel.updateAppointment(activeAppointment!!)

                        Navigation.findNavController(view)
                            .navigate(R.id.navigate_back_to_home)
                    }

//                    mBinding.rvCurrentPrescription.adapter =
//                        MedicineListAdapter(activeAppointment!!.prescription.medicine)

                    mBinding.fabAddMedicine.setOnClickListener {
                        AddMedicineDialog() { med: Medicine ->
                            meds.add(med)
                            medsLiveData.value = meds
                            Log.d(
                                "",
                                "onCreateView: medicines = $meds ===== ${medsLiveData.value} "
                            )

                            activeAppointment!!.prescription.medicine = meds
                            mUserViewModel.updateAppointment(activeAppointment!!)
                        }.show(
                            childFragmentManager, "Add Prescription"
                        )
                    }

                    mMeetingViewModel.meetingId.observe(viewLifecycleOwner) { status ->
                        if (status != "undefined") mBinding.btnCreateRoom.text = "join"

                        mBinding.btnCreateRoom.setOnClickListener {
                            Log.d(
                                "TAG",
                                "onCreateView: meeting id: $status ${mBinding.btnCreateRoom.text}"
                            )
                            if (mBinding.btnCreateRoom.text.toString() == "join") {
                                val intent =
                                    Intent(activity, VideoCallActivity::class.java).also {
                                        it.putExtra("role", Constants.CLIENT_ROLE_BROADCASTER)
                                        it.putExtra("room", status)
                                    }

                                startActivity(intent)
                            } else {
                                mBinding.btnCreateRoom.text = "Creating"

                                val meeting = Meeting(
                                    "",
                                    activeAppointment!!.patientId,
                                    activeAppointment!!.doctorId,
                                    "Room: " + activeAppointment!!.doctorId + "-" + activeAppointment!!.doctorId
                                )

                                mMeetingViewModel.setMeeting(meeting)
                                mMeetingViewModel.getMeetingId(
                                    activeAppointment!!.doctorId,
                                    activeAppointment!!.patientId
                                )
                                mBinding.btnCreateRoom.text = "Join"
                            }
                        }
                    }
                } else {
                    mBinding.tvPatientAppointment.text = "No appointment is set."
                    mBinding.btnPatientSymptoms.isVisible = false
                    mBinding.btnDone.isVisible = false
                    mBinding.btnCreateRoom.isVisible = false
                    mBinding.fabAddMedicine.isVisible = false
                }

                // Previous appointments
                if (previousAppointments.isEmpty())
                    mBinding.rvPreviousPrescription.isVisible = false
                else {
                    mBinding.rvPreviousPrescription.adapter =
                        PreviewPrescriptionAdapter(previousAppointments) { client: Appointment ->
                            PatientPrescriptionDialog(
                                client.time,
                                client.prescription.medicine
                            ).show(
                                parentFragmentManager, "Prescription"
                            )

                            Navigation.findNavController(view).navigate(R.id.navigate_back_to_home)
                        }
                }
            }
        }

        return view
    }

    companion object {

    }
}