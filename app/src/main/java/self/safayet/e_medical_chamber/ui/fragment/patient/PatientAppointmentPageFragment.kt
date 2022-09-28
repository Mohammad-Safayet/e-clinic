package self.safayet.e_medical_chamber.ui.fragment.patient

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import self.safayet.e_medical_chamber.ui.fragment.patient.PatientAppointmentPageFragmentArgs
import self.safayet.e_medical_chamber.R
import self.safayet.e_medical_chamber.databinding.FragmentPatientAppointmentPageBinding
import self.safayet.e_medical_chamber.domain.model.Appointment
import self.safayet.e_medical_chamber.domain.model.Doctor
import self.safayet.e_medical_chamber.ui.VideoCallActivity
import self.safayet.e_medical_chamber.ui.adapter.MedicineListAdapter
import self.safayet.e_medical_chamber.ui.adapter.PreviewPrescriptionAdapter
import self.safayet.e_medical_chamber.ui.dialog.DetailsDialog
import self.safayet.e_medical_chamber.ui.dialog.PatientPrescriptionDialog
import self.safayet.e_medical_chamber.ui.dialog.SymptomsDialog
import self.safayet.e_medical_chamber.ui.viewmodel.MeetingViewModel
import self.safayet.e_medical_chamber.ui.viewmodel.PatientViewModel
import self.safayet.e_medical_chamber.ui.viewmodel.UserViewModel
import self.safayet.e_medical_chamber.utils.Utils
import io.agora.agorauikit_android.*
import io.agora.rtc.Constants

class PatientAppointmentPageFragment : Fragment() {

    private val mArgs: PatientAppointmentPageFragmentArgs by navArgs()
    private lateinit var mBinding: FragmentPatientAppointmentPageBinding
    private val mUserViewModel: UserViewModel by activityViewModels()
    private val mPatientViewModel: PatientViewModel by activityViewModels()
    private val mMeetingViewModel: MeetingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_patient_appointment_page, container, false)

        mBinding = FragmentPatientAppointmentPageBinding.bind(view)

        mBinding.rvPreviousPrescription.isVisible = false
        mBinding.rvPresentPrescription.isVisible = false
        mBinding.btnVoiceCall.isVisible = false
        mBinding.btnVideoCall.isVisible = false

        mBinding.imgDoctorAvatar.setImageDrawable(
            context?.let {
                Utils.assetsImage(
                    it,
                    "doctor_placeholder.png"
                )
            }
        )

        mPatientViewModel.getDoctor(mArgs.doctorId)
        mUserViewModel.getAppointmentById(mArgs.patientId, mArgs.doctorId)

        // Setting the doctor name and his appointment date also changing the voice call button to
        // set appointment button.
        mPatientViewModel.doctor.observe(viewLifecycleOwner) { doctor: Doctor ->
            Log.d("TAG", "onCreateView: ${doctor.appointments.size}")
            mBinding.tvDoctorName.isVisible = true
            mBinding.tvDoctorName.text = doctor.fullName
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
                    Log.d("TAG", "onCreateView: ${activeAppointment!!.isActive}")
                    if (activeAppointment?.time == "--")
                        mBinding.tvAppointment.text = "Pending"
                    else {
                        mBinding.tvAppointmentDate.text = activeAppointment?.time
                        mBinding.btnVoiceCall.isVisible = true
                    }

                    mMeetingViewModel.getMeetingId(
                        activeAppointment!!.doctorId,
                        activeAppointment!!.patientId
                    )

                    mMeetingViewModel.meetingId.observe(viewLifecycleOwner) { status ->
                        mBinding.btnVideoCall.isVisible = status != "undefined"

                        mBinding.btnVideoCall.setOnClickListener {
                            val intent =
                                Intent(activity, VideoCallActivity::class.java).also {
                                    it.putExtra("role", Constants.CLIENT_ROLE_BROADCASTER)
                                    it.putExtra("room", status)
                                }
                            startActivity(intent)
                        }
                    }

                    if (activeAppointment?.prescription?.medicine!!.isEmpty())
                        mBinding.rvPresentPrescription.isVisible = false

                    mBinding.btnSymptoms.setOnClickListener {
                        SymptomsDialog(activeAppointment!!.symptoms).show(
                            childFragmentManager, "Show the symptoms"
                        )
                    }

                    mBinding.tvDoctorAppointment.isVisible = true
                    mBinding.tvDoctorAppointment.text = "Appointment: ${activeAppointment!!.time}"
                    mBinding.rvPresentPrescription.isVisible = true
                    mBinding.rvPresentPrescription.adapter =
                        MedicineListAdapter(activeAppointment?.prescription!!.medicine)

                    Log.d(
                        "TAG",
                        "onCreateView: updateAppointment ${mUserViewModel.user.value.toString()}"
                    )

                    mBinding.btnVoiceCall.setOnClickListener {
                        val callIntent = Intent(Intent.ACTION_CALL)
                        callIntent.data = Uri.parse("tel:" + activeAppointment!!.contact)

                        if (ActivityCompat.checkSelfPermission(
                                requireContext(),
                                Manifest.permission.CALL_PHONE
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            return@setOnClickListener
                        }

                        Log.d("CallDoctor", "onCreateDialog: calling")
                        startActivity(callIntent)
                    }

                } else {
                    mBinding.tvAppointmentDate.text = "No appointment is set"
                    mBinding.btnVideoCall.isVisible = false
                    mBinding.btnSymptoms.isVisible = false
                    mBinding.btnVoiceCall.isVisible = true
                    mBinding.btnVoiceCall.text = "Set Appointment"

                    mPatientViewModel.doctor.observe(viewLifecycleOwner) { doctor: Doctor ->
                        Log.d("TAG", "onCreateView: ${doctor.appointments.size}")
                        mBinding.tvDoctorName.isVisible = true
                        mBinding.tvDoctorName.text = doctor.fullName

                        mBinding.btnVoiceCall.setOnClickListener {
                            DetailsDialog.AddSymptomsDialog(
                                Appointment(
                                    mArgs.patientId,
                                    doctor.id,
                                    doctor.fullName,
                                    doctor.mobileNo,
                                    listOf(),
                                    "--",
                                )
                            ){
                                Navigation.findNavController(view).navigate(R.id.nav_to_patient_home_page)
                            }.show(parentFragmentManager, "add symptoms")
                        }
                    }
                }

                // Previous appointments
                if (previousAppointments.isEmpty())
                    mBinding.rvPreviousPrescription.isVisible = false
                else {
                    mBinding.rvPreviousPrescription.isVisible = true
                    mBinding.rvPreviousPrescription.adapter =
                        PreviewPrescriptionAdapter(previousAppointments) { client: Appointment ->
                            PatientPrescriptionDialog(
                                client.time,
                                client.prescription.medicine
                            ).show(
                                parentFragmentManager, "Prescription"
                            )
                        }
                }
            }
        }

        return view
    }

    companion object {

    }
}