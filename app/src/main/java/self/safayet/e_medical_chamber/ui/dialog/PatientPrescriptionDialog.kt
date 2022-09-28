package self.safayet.e_medical_chamber.ui.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import self.safayet.e_medical_chamber.R
import self.safayet.e_medical_chamber.databinding.DialogAddPrescriptionBinding
import self.safayet.e_medical_chamber.databinding.DialogPrescriptionBinding
import self.safayet.e_medical_chamber.domain.model.Medicine
import self.safayet.e_medical_chamber.ui.adapter.MedicineListAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class PatientPrescriptionDialog(
    private val time: String,
    private val meds: List<Medicine>
): DialogFragment() {

    private lateinit var mBinding: DialogPrescriptionBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = MaterialAlertDialogBuilder(it)

            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_prescription, null)

            mBinding = DialogPrescriptionBinding.bind(view)

            mBinding.tvDate.text = time
            mBinding.rvMedicineList.adapter = MedicineListAdapter(meds)

            builder.setView(view)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}