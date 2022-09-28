package self.safayet.e_medical_chamber.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import self.safayet.e_medical_chamber.databinding.CardAppointmentBinding
import self.safayet.e_medical_chamber.domain.model.Appointment

class AppointmentListAdapter(
    private val appointments: List<Appointment>,
    private val action: (appointment: Appointment) -> Unit
) :
    RecyclerView.Adapter<AppointmentListAdapter.AppointmentListViewHolder>() {

    inner class AppointmentListViewHolder(private val cardBinding: CardAppointmentBinding) :
        RecyclerView.ViewHolder(cardBinding.root) {
        fun bindItem(client: Appointment) {
            cardBinding.tvAppointmentName.text = client.name
            if (client.isActive) {
                if (client.time == "--") cardBinding.tvAppointmentTime.text =
                    "Appointment Time is not set."
                else cardBinding.tvAppointmentTime.text = "Next appointment: ${client.time}"
            } else {
                cardBinding.tvAppointmentTime.text = "Appointment is done"
            }

            cardBinding.root.setOnClickListener {
                action(client)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentListViewHolder {
        return AppointmentListViewHolder(
            CardAppointmentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return appointments.count()
    }

    override fun onBindViewHolder(holder: AppointmentListViewHolder, position: Int) {
        val prescription = appointments[position]
        holder.bindItem(prescription)
    }
}