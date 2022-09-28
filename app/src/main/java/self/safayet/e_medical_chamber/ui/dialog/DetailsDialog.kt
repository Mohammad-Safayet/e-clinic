package self.safayet.e_medical_chamber.ui.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.EditText
import androidx.core.view.marginTop
import androidx.core.view.setPadding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import self.safayet.e_medical_chamber.R
import self.safayet.e_medical_chamber.databinding.DialogDetailsBinding
import self.safayet.e_medical_chamber.databinding.DialogSymptomsBinding
import self.safayet.e_medical_chamber.domain.model.Appointment
import self.safayet.e_medical_chamber.domain.model.Doctor
import self.safayet.e_medical_chamber.ui.fragment.patient.PatientAppointmentPageFragment
import self.safayet.e_medical_chamber.ui.viewmodel.UserViewModel
import self.safayet.e_medical_chamber.utils.Utils
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class DetailsDialog(
    private val patientId: String,
    private val doctor: Doctor,
) : DialogFragment() {

    private lateinit var mBinding: DialogDetailsBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { it ->
            val builder = MaterialAlertDialogBuilder(it)

            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_details, null)
            mBinding = DialogDetailsBinding.bind(view)

            mBinding.ivAvatar.setImageDrawable(
                context?.let { it1 ->
                    Utils.assetsImage(
                        it1,
                        "doctor_placeholder.png"
                    )
                }
            )
            mBinding.tvEmail.text = doctor.email
            mBinding.tvFullname.text = "${doctor.title} ${doctor.fullName}"
            mBinding.tvTitle.text = doctor.specialist
            mBinding.tvDescrp.text = doctor.desc

            builder.setView(view)
                .setPositiveButton("Set Appointment", DialogInterface.OnClickListener { _, _ ->
                    AddSymptomsDialog(
                        Appointment(
                            patientId,
                            doctor.id,
                            doctor.fullName,
                            doctor.mobileNo,
                            listOf(),
                            "--",
                        ),
                    ){}.show(parentFragmentManager, "add symptoms")
                }).setNegativeButton("Cancel", DialogInterface.OnClickListener { _, _ ->
                    dialog?.cancel()
                })

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    class AddSymptomsDialog(
        private val appointment: Appointment,
        val action: () -> Unit
    ) : DialogFragment() {

        private lateinit var mBinding: DialogSymptomsBinding
        private val mPatientViewModel: UserViewModel by activityViewModels()

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return activity?.let { it ->
                val builder = MaterialAlertDialogBuilder(it)

                val inflater = requireActivity().layoutInflater
                val view = inflater.inflate(R.layout.dialog_symptoms, null)
                mBinding = DialogSymptomsBinding.bind(view)

                builder
                    .setView(view)
                    .setPositiveButton("Confirm") { _, _ ->
                        appointment.symptoms = mBinding.inputSymptoms.text.toString().split(",")

                        mPatientViewModel.updatePatient(appointment)
                        action()
                    }.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }

                builder.create()
            } ?: throw IllegalStateException("Activity cannot be null")
        }
    }
}