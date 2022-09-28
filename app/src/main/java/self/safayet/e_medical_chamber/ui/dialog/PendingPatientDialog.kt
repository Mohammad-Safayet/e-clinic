package self.safayet.e_medical_chamber.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import self.safayet.e_medical_chamber.R
import self.safayet.e_medical_chamber.databinding.DialogPandingPatientBinding
import self.safayet.e_medical_chamber.databinding.DialogSetAppointmentBinding
import self.safayet.e_medical_chamber.domain.model.Appointment
import self.safayet.e_medical_chamber.ui.adapter.SymptomsListAdapter
import self.safayet.e_medical_chamber.ui.viewmodel.UserViewModel
import self.safayet.e_medical_chamber.utils.Utils
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*

class PendingPatientDialog(private val appointment: Appointment) : DialogFragment() {

    private lateinit var mBinding: DialogPandingPatientBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = MaterialAlertDialogBuilder(it)

            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_panding_patient, null)
            mBinding = DialogPandingPatientBinding.bind(view)

            Log.d("PendingPatientDialog", "onCreateDialog: ${appointment.symptoms.size}")
            mBinding.rvPatientSymptoms.adapter = SymptomsListAdapter(appointment.symptoms)

            builder.setView(view)
                .setPositiveButton("Set Appointment") { _, _ ->
                    SetAppointmentDialog(appointment).show(parentFragmentManager, "Set appointment")
                }.setNegativeButton("Cancel") { _, _ ->
                    dialog?.cancel()
                }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    class SetAppointmentDialog(private val appointment: Appointment) : DialogFragment() {

        private lateinit var mBinding: DialogSetAppointmentBinding
        private val mUserViewModel: UserViewModel by activityViewModels()

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return activity?.let {
                val builder = MaterialAlertDialogBuilder(it)

                val inflater = requireActivity().layoutInflater
                val view = inflater.inflate(R.layout.dialog_set_appointment, null)
                mBinding = DialogSetAppointmentBinding.bind(view)
                var appointmentDate: String = ""

                mBinding.inputAppointmentDate.setOnClickListener {
                    val mCalendarConstraints = CalendarConstraints.Builder()
                        .setValidator(DateValidatorPointForward.now())
                    Utils.showDateTimePicker(mCalendarConstraints) { date, _ ->
                        mBinding.inputAppointmentDate.setText(date)
                        appointmentDate += date
                    }.show(requireFragmentManager(), "")
                }

                mBinding.inputAppointmentTime.setOnClickListener {
                    Utils.openTimePicker(
                        requireContext(),
                        "Select Appointment Time"
                    ) { time, _, _ ->
                        mBinding.inputAppointmentTime.setText(time)
                        appointmentDate += " $time"
                    }.show(childFragmentManager, "Appointment Time Picker")
                }

                builder.setView(view)
                    .setPositiveButton("Set Appointment") { _, _ ->
                        if (appointmentDate == "") {
                            dialog?.cancel()
                        }
                        appointment.time = appointmentDate

                        mUserViewModel.updateAppointment(appointment)
                    }.setNegativeButton("Cancel") { _, _ ->
                        dialog?.cancel()
                    }

                builder.create()
            } ?: throw IllegalStateException("Activity cannot be null")
        }
    }
}