package self.safayet.e_medical_chamber.ui.dialog

import android.app.AlarmManager
import android.app.Dialog
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ContextThemeWrapper
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import self.safayet.e_medical_chamber.R
import self.safayet.e_medical_chamber.databinding.DialogAddAlarmBinding
import self.safayet.e_medical_chamber.domain.model.Alarm
import self.safayet.e_medical_chamber.ui.viewmodel.AlarmViewModel
import self.safayet.e_medical_chamber.utils.AlarmReceiver
import self.safayet.e_medical_chamber.utils.Utils
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*


class AddAlarmDialog : DialogFragment() {

    private lateinit var mBinding: DialogAddAlarmBinding
    private val mAlarmViewModel: AlarmViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = MaterialAlertDialogBuilder(
                it
            )

            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_add_alarm, null)
            mBinding = DialogAddAlarmBinding.bind(view)
            val calender = Calendar.getInstance()

            mBinding.inputMedicineTime.setOnClickListener {

                val alarmManager =
                    requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val intent = Intent(requireContext(), AlarmReceiver::class.java)

                val timePicker = Utils.openTimePicker(
                    requireContext(),
                    "Select Medicine Time",
                ) { time, hour, minute ->
                    mBinding.inputMedicineTime.setText(time)

                    calender[Calendar.HOUR_OF_DAY] = hour
                    calender[Calendar.MINUTE] = minute
                    calender[Calendar.SECOND] = 0
                    calender[Calendar.MILLISECOND] = 0

                    val pendingIntent = PendingIntent.getBroadcast(
                        requireContext(),
                        0,
                        intent,
                        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                    )

                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calender.timeInMillis,
//                        AlarmManager.INTERVAL_DAY,
                        pendingIntent
                    )

                }

                timePicker.show(childFragmentManager, "Alarm Time Picker")
            }

            builder.setView(view)
                .setPositiveButton("Save", DialogInterface.OnClickListener { _, _ ->
                    val name = mBinding.inputMedicineName.text.toString()
                    val time = mBinding.inputMedicineTime.text.toString()
                    val days = mBinding.inputMedicineDays.text.toString()

                    val alarm = Alarm(0, name, time, days.toInt(), true)

                    mAlarmViewModel.addAlarm(alarm)

                    Toast.makeText(
                        requireContext(),
                        "Alarm is set. ${calender.time}",
                        Toast.LENGTH_LONG
                    ).show()

                    dialog?.cancel()
                }).setNegativeButton("Cancel", DialogInterface.OnClickListener { _, _ ->
                    dialog?.cancel()
                })

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun setTime(text: String) {
        mBinding.inputMedicineTime.setText(text)
    }

}