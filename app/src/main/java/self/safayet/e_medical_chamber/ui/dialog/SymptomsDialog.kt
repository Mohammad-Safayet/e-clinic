package self.safayet.e_medical_chamber.ui.dialog

import android.Manifest
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import self.safayet.e_medical_chamber.R
import self.safayet.e_medical_chamber.databinding.DialogShowSymptomsBinding
import self.safayet.e_medical_chamber.databinding.DialogViewPrescriptionBinding
import self.safayet.e_medical_chamber.ui.adapter.SymptomsListAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SymptomsDialog(
    private val symptoms: List<String>
) : DialogFragment()  {

    private lateinit var mBinding: DialogShowSymptomsBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = MaterialAlertDialogBuilder(it)

            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_show_symptoms, null)

            mBinding = DialogShowSymptomsBinding.bind(view)

            mBinding.rvSymptoms.adapter = SymptomsListAdapter(symptoms)

            builder.setView(view)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}