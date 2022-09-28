package self.safayet.e_medical_chamber.ui.fragment.patient

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import self.safayet.e_medical_chamber.R
import self.safayet.e_medical_chamber.databinding.DialogAddAlarmBinding
import self.safayet.e_medical_chamber.databinding.FragmentPatientAlarmPageBinding
import self.safayet.e_medical_chamber.domain.model.Alarm
import self.safayet.e_medical_chamber.ui.viewmodel.AlarmViewModel
import self.safayet.e_medical_chamber.ui.adapter.AlarmListAdapter
import self.safayet.e_medical_chamber.ui.dialog.AddAlarmDialog
import self.safayet.e_medical_chamber.utils.Utils.openTimePicker

class PatientAlarmPage : Fragment(R.layout.fragment_patient_alarm_page) {

    private lateinit var mBinding: FragmentPatientAlarmPageBinding

    private val mAlarmViewModel: AlarmViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_patient_alarm_page, container, false)

        mBinding = FragmentPatientAlarmPageBinding.bind(view)

        mAlarmViewModel.alarms.observe(viewLifecycleOwner) { alarmList: List<Alarm> ->
            if (alarmList.isEmpty()) {
                mBinding.tvAlarmText.isVisible = true
                mBinding.rvAlarmList.isVisible = false
            } else {
                mBinding.tvAlarmText.isVisible = false
                mBinding.rvAlarmList.isVisible = true
                mBinding.rvAlarmList.adapter = AlarmListAdapter(alarmList)
                Log.d(TAG, "onCreateView: $alarmList")
            }
        }

        mBinding.fabAddAlarm.setOnClickListener {
            AddAlarmDialog().show(parentFragmentManager, "New Alarm")
        }

        return view
    }

    companion object {
        val TAG = PatientAlarmPage::class.java.toString()
    }

}