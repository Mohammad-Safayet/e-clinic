package self.safayet.e_medical_chamber.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import self.safayet.e_medical_chamber.databinding.ItemMedicinePrescriptionBinding
import self.safayet.e_medical_chamber.domain.model.Medicine

class MedicineListAdapter(
    private val list: List<Medicine>
) :
    RecyclerView.Adapter<MedicineListAdapter.MedicineListViewHolder>() {

    inner class MedicineListViewHolder(private val cardBinding: ItemMedicinePrescriptionBinding) :
        RecyclerView.ViewHolder(cardBinding.root) {
        fun bindItem(client: Medicine) {
            var temp = ""
            temp += if(client.morning) "1 + " else "0 + "
            temp += if(client.noon) "1 + " else "0 + "
            temp += if(client.night) "1" else "0"
            val time = "$temp"

            cardBinding.tvMedName.text = client.name
            cardBinding.tvDoseTime.text = time
            cardBinding.tvMedicineNotes.text = client.notes
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicineListViewHolder {
        return MedicineListViewHolder(
            ItemMedicinePrescriptionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.count()
    }

    override fun onBindViewHolder(
        holder: MedicineListAdapter.MedicineListViewHolder,
        position: Int
    ) {
        val prescription = list[position]
        holder.bindItem(prescription)
    }
}