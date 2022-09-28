package self.safayet.e_medical_chamber.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import self.safayet.e_medical_chamber.databinding.ItemPreviousPrescriptionBinding
import self.safayet.e_medical_chamber.domain.model.Appointment
import self.safayet.e_medical_chamber.ui.dialog.PatientPrescriptionDialog

class PreviewPrescriptionAdapter(
    private val appointments: List<Appointment>,
    private val action: (client: Appointment) -> Unit
) : RecyclerView.Adapter<PreviewPrescriptionAdapter.PreviewPrescriptionViewHolder>() {

    inner class PreviewPrescriptionViewHolder(private val cardBinding: ItemPreviousPrescriptionBinding) :
        RecyclerView.ViewHolder(cardBinding.root) {
        fun bindItem(client: Appointment) {
            cardBinding.tvPastTime.text = "Appointment: " + client.time
            cardBinding.btnView.setOnClickListener {
                action(client)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreviewPrescriptionViewHolder {
        return PreviewPrescriptionViewHolder(
            ItemPreviousPrescriptionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return appointments.count()
    }

    override fun onBindViewHolder(holder: PreviewPrescriptionViewHolder, position: Int) {
        val prescription = appointments[position]
        holder.bindItem(prescription)
    }
}