package self.safayet.e_medical_chamber.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContentProviderCompat.requireContext
import self.safayet.e_medical_chamber.R
import self.safayet.e_medical_chamber.ui.dialog.PendingPatientDialog
import self.safayet.e_medical_chamber.ui.fragment.patient.PatientAlarmPage
import self.safayet.e_medical_chamber.ui.fragment.patient.PatientRegisterPage
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import io.agora.agorauikit_android.*
import io.agora.rtc.Constants
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    val formatEqn1 = SimpleDateFormat("HH:m")
    val formatEqn2 = SimpleDateFormat("hh:mm aa")
    val formatEqn3 = SimpleDateFormat("HH:mm")

    fun calculateAge(dobLong: Long): Long {
        var date = Date(dobLong)
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        try {
            date = sdf.parse(date.toString()) as Date
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        val dob: Calendar = Calendar.getInstance()
        val today: Calendar = Calendar.getInstance()

        dob.time = date

        val year: Int = dob.get(Calendar.YEAR)
        val month: Int = dob.get(Calendar.MONTH)
        val day: Int = dob.get(Calendar.DAY_OF_MONTH)

        dob.set(year, month + 1, day)

        var age: Int = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR)

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--
        }

        return age.toLong()
    }

    fun showDateTimePicker(
        mCalendarConstraints: CalendarConstraints.Builder,
        lmd: (String, Long) -> Unit
    ): MaterialDatePicker<Long> {
        val mDatePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select Birthdate")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .setCalendarConstraints(mCalendarConstraints.build())
            .build()

        mDatePicker.addOnPositiveButtonClickListener {
            lmd(mDatePicker.headerText, it!!)
        }

        return mDatePicker
    }

    fun openTimePicker(
        context: Context,
        title: String,
        lmd: (String, Int, Int) -> Unit
    ): MaterialTimePicker {
        val isSystem24Hour = DateFormat.is24HourFormat(context)
        val clockFormat = if (isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H

        val picker = MaterialTimePicker.Builder()
            .setTimeFormat(clockFormat)
            .setHour(12)
            .setMinute(0)
            .setTitleText(title)
            .build()

        var time = ""

        picker.addOnPositiveButtonClickListener {

            time = "${picker.hour}:${picker.minute}"

            val date = formatEqn1.parse(time)
            Log.d(PatientAlarmPage.TAG, "openTimePicker: $date")
            time = formatEqn2.format(date!!)

            lmd(time, picker.hour, picker.minute)
        }

        return picker
    }

    fun assetsImage(context: Context, fileName: String): Drawable? {
        val ims = context.assets.open(fileName)

        return Drawable.createFromStream(ims, null)
    }

}