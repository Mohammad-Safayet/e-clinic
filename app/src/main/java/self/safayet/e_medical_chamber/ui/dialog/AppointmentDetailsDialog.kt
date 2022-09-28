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
import androidx.fragment.app.activityViewModels
import self.safayet.e_medical_chamber.R
import self.safayet.e_medical_chamber.databinding.DialogViewPrescriptionBinding
import self.safayet.e_medical_chamber.domain.model.Appointment
import self.safayet.e_medical_chamber.ui.adapter.PrescriptionListAdapter
import self.safayet.e_medical_chamber.ui.viewmodel.UserViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlin.math.log


class AppointmentDetailsDialog(
    private val appointment: Appointment
) : DialogFragment() {

    private lateinit var mBinding: DialogViewPrescriptionBinding
    private val mPatientViewModel: UserViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = MaterialAlertDialogBuilder(it)

            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_view_prescription, null)
            mBinding = DialogViewPrescriptionBinding.bind(view)

            mBinding.tvAppointDoctorName.text = appointment.name
            if (appointment.time == "--") {
                mBinding.tvAppointTime.text = "Next appointment: Pending"
                mBinding.textView7.isVisible = false
                mBinding.rvPrescription.isVisible = false
            } else {
                mBinding.tvAppointTime.text = "Next appointment: ${appointment.time}"
                mBinding.textView7.isVisible = true
                mBinding.rvPrescription.isVisible = true

//                mBinding.rvPrescription.adapter = PrescriptionListAdapter(appointment.prescription)
            }
            builder.setView(view)
                .setPositiveButton("Call", DialogInterface.OnClickListener { _, _ ->
                    val callIntent = Intent(Intent.ACTION_CALL)
                    callIntent.data = Uri.parse("tel:" + appointment.contact)

                    if (ActivityCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.CALL_PHONE
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return@OnClickListener
                    }

                    Log.d("CallDoctor", "onCreateDialog: calling")
                    it.startActivity(callIntent)
                }).setNegativeButton("Cancel", DialogInterface.OnClickListener { _, _ ->
                    dialog?.cancel()
                })

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}