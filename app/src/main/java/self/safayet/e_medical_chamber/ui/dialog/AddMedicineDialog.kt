package self.safayet.e_medical_chamber.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import self.safayet.e_medical_chamber.R
import self.safayet.e_medical_chamber.databinding.DialogAddPrescriptionBinding
import self.safayet.e_medical_chamber.databinding.DialogShowSymptomsBinding
import self.safayet.e_medical_chamber.domain.model.Medicine
import self.safayet.e_medical_chamber.ui.adapter.SymptomsListAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AddMedicineDialog(
    private val action: (medicine: Medicine) -> Unit
) : DialogFragment() {
    private lateinit var mBinding: DialogAddPrescriptionBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = MaterialAlertDialogBuilder(it)

            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_add_prescription, null)

            mBinding = DialogAddPrescriptionBinding.bind(view)

            mBinding.btnAddMedicine.setOnClickListener {
                val medName = mBinding.inputMedName.text.toString()
                val medNotes = mBinding.inputMedNotes.text.toString()
                val morning = mBinding.cbMorning.isChecked
                val noon = mBinding.cbNoon.isChecked
                val night = mBinding.cbNight.isChecked

                action(
                    Medicine(
                        medName,
                        medNotes,
                        morning,
                        noon,
                        night,
                    )
                )
                dialog?.cancel()
            }

            builder.setView(view)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}