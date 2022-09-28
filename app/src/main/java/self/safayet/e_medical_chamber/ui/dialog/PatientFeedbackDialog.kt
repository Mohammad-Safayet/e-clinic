//package self.safayet.e_medical_chamber.ui.dialog
//
//import android.app.Dialog
//import android.os.Bundle
//import android.text.InputType
//import android.util.Log
//import android.widget.EditText
//import androidx.core.view.setPadding
//import androidx.fragment.app.DialogFragment
//import androidx.fragment.app.activityViewModels
//import self.safayet.e_medical_chamber.R
//import self.safayet.e_medical_chamber.databinding.DialogPatientFeedbackBinding
//import self.safayet.e_medical_chamber.domain.model.Appointment
//import self.safayet.e_medical_chamber.domain.model.Medicine
//import self.safayet.e_medical_chamber.domain.model.Prescription
//import self.safayet.e_medical_chamber.ui.adapter.PrescriptionListAdapter
//import self.safayet.e_medical_chamber.ui.adapter.SymptomsListAdapter
//import self.safayet.e_medical_chamber.ui.viewmodel.UserViewModel
//import com.google.android.material.dialog.MaterialAlertDialogBuilder
//import java.time.LocalDateTime
//import java.time.format.DateTimeFormatter
//import java.time.format.FormatStyle
//
//class PatientFeedbackDialog(private val appointment: Appointment) : DialogFragment() {
//
//    private lateinit var mBinding: DialogPatientFeedbackBinding
//    private val mUserViewModel: UserViewModel by activityViewModels()
//
//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        return activity?.let {
//            val builder = MaterialAlertDialogBuilder(it)
//
//            val inflater = requireActivity().layoutInflater
//            val view = inflater.inflate(R.layout.dialog_patient_feedback, null)
//            mBinding = DialogPatientFeedbackBinding.bind(view)
//
//            mBinding.tvFeedbackPatientName.text = appointment.name
//            mBinding.rvFeedbackPatientSymptoms.adapter = SymptomsListAdapter(appointment.symptoms)
////            mBinding.rvFeedbackPatientPrescription.adapter =
////                PrescriptionListAdapter(appointment.prescription)
//
//            mBinding.btnAddPrescription.setOnClickListener {
//                dialog?.cancel()
//                AddPrescriptionDialog(appointment).show(parentFragmentManager, "Add Prescription")
//            }
//
//            builder.setView(view)
//                .setPositiveButton("Done") { _, _ ->
//                    mUserViewModel.updateAppointment(appointment)
//                }.setNegativeButton("Cancel") { _, _ ->
//                    dialog?.cancel()
//                }
//
//            builder.create()
//        } ?: throw IllegalStateException("Activity cannot be null")
//    }
//
//    class AddPrescriptionDialog(private val appointment: Appointment) : DialogFragment() {
//
//        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//            return activity?.let { it ->
//                val builder = MaterialAlertDialogBuilder(it)
//                builder
//                    .setTitle("Add Prescription(Follow the rule below)")
//
//                val input = EditText(it)
//                input.hint = "Napa Extra - 1+1+0 - 30 min before meal"
//                input.inputType = InputType.TYPE_CLASS_TEXT
//                input.setPadding(50)
//
//                builder
//                    .setView(input)
//                    .setPositiveButton("OK") { _, _ ->
//                        val list: List<String> = input.text.toString().split("-")
//                        val time: MutableList<Boolean> = listOf<Boolean>().toMutableList()
//                        list[1].trim().split("+").forEach {
//                            if (it == "1")
//                                time.add(true)
//                            else time.add(false)
//                        }
//
//                        val medicine = Medicine(
//                            list[0].trim(),
//                            list[2].trim(),
//                            time[0],
//                            time[1],
//                            time[2],
//                        )
//
//                        // Date of the time
//                        val current = LocalDateTime.now()
//                        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd ")
//                        val formatted = current.format(formatter)
//
////                        appointment.prescription.forEachIndexed { index, it ->
////                            if (it.time == formatted) {
////                                Log.d("AddPrescription", "onCreateDialog: ${it.medicine}")
////                                val medicines = it.medicine.toMutableList()
////                                medicines.add(medicine)
////
////                                appointment.prescription[index].medicine = medicines
////                                PatientFeedbackDialog(appointment).show(
////                                    parentFragmentManager,
////                                    "back"
////                                )
////                                return@setPositiveButton
////                            }
////                        }
//
////                        val prescription = appointment.prescription.toMutableList()
////                        Log.d("AddPrescription", "onCreateDialog: $medicine")
////                        prescription.add(
////                            Prescription(formatted, listOf(medicine))
////                        )
//
//                        appointment.prescription = prescription
//                        PatientFeedbackDialog(appointment).show(
//                            parentFragmentManager,
//                            "back"
//                        )
//                        return@setPositiveButton
//
//                    }.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
//
//                builder.create()
//            } ?: throw IllegalStateException("Activity cannot be null")
//        }
//    }
//}