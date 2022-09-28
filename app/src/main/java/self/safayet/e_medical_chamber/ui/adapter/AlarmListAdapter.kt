package self.safayet.e_medical_chamber.ui.adapter

import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import self.safayet.e_medical_chamber.databinding.CardMedicineAlarmBinding
import self.safayet.e_medical_chamber.domain.model.Alarm

class AlarmListAdapter(
    private val clientList: List<Alarm>,
) : RecyclerView.Adapter<AlarmListAdapter.AlarmListViewHolder>() {

    inner class AlarmListViewHolder(private val cardBinding: CardMedicineAlarmBinding) :
        RecyclerView.ViewHolder(cardBinding.root) {
        fun bindItem(alarm: Alarm) {
            cardBinding.tvName.text = alarm.medicineName
            cardBinding.tvTime.text = alarm.time
            cardBinding.tvDays.text = alarm.days.toString() + " Days"
            cardBinding.swIsActive.isChecked = alarm.isActive
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmListViewHolder {
        return AlarmListViewHolder(
            CardMedicineAlarmBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AlarmListViewHolder, position: Int) {
        try {
            val profile = clientList[position]
            holder.bindItem(profile)
        } catch (error: IndexOutOfBoundsException) {

        }
    }

    override fun getItemCount(): Int {
        return clientList.count()
    }
}