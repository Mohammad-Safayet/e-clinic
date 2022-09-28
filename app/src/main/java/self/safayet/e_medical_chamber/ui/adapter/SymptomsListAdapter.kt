package self.safayet.e_medical_chamber.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import self.safayet.e_medical_chamber.databinding.ItemSymptomsBinding

class SymptomsListAdapter(
    private val symptoms: List<String>
) :
    RecyclerView.Adapter<SymptomsListAdapter.SymptomsListViewHolder>() {

    inner class SymptomsListViewHolder(private val mBinding: ItemSymptomsBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
        fun bindItem(client: String) {
            mBinding.tvSymptoms.text = client
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SymptomsListViewHolder {
        return SymptomsListViewHolder(
            ItemSymptomsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return symptoms.count()
    }

    override fun onBindViewHolder(
        holder: SymptomsListAdapter.SymptomsListViewHolder,
        position: Int
    ) {
        val symptom = symptoms[position]
        holder.bindItem(symptom)
    }
}