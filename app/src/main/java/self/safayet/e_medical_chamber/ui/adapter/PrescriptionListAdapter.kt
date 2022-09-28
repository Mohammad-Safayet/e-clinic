package self.safayet.e_medical_chamber.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import self.safayet.e_medical_chamber.databinding.DialogPrescriptionBinding
import self.safayet.e_medical_chamber.domain.model.Prescription

class PrescriptionListAdapter(
    private val prescriptions: List<Prescription>
) :
    RecyclerView.Adapter<PrescriptionListAdapter.PrescriptionListViewHolder>() {

    inner class PrescriptionListViewHolder(private val cardBinding: DialogPrescriptionBinding) :
        RecyclerView.ViewHolder(cardBinding.root) {
        fun bindItem(client: Prescription) {
            cardBinding.tvDate.text = client.time
            cardBinding.rvMedicineList.adapter = MedicineListAdapter(client.medicine)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrescriptionListViewHolder {
        return PrescriptionListViewHolder(
            DialogPrescriptionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return prescriptions.count()
    }

    override fun onBindViewHolder(
        holder: PrescriptionListAdapter.PrescriptionListViewHolder,
        position: Int
    ) {
        val prescription = prescriptions[position]
        holder.bindItem(prescription)
    }
}