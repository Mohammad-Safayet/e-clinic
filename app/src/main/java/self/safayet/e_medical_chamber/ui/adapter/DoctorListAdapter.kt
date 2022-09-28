package self.safayet.e_medical_chamber.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import self.safayet.e_medical_chamber.databinding.CardPatientBinding
import self.safayet.e_medical_chamber.domain.model.Doctor
import self.safayet.e_medical_chamber.utils.Utils

class DoctorListAdapter(
    private val context: Context,
    private val clientList: List<Doctor>,
    private val action: (Doctor) -> Unit,
) : RecyclerView.Adapter<DoctorListAdapter.DoctorListViewHolder>() {

    inner class DoctorListViewHolder(private val cardBinding: CardPatientBinding) :
        RecyclerView.ViewHolder(cardBinding.root) {
        fun bindItem(client: Doctor) {
            cardBinding.ivDoctorAvatar.setImageDrawable(
                Utils.assetsImage(
                    context,
                    "doctor_placeholder.png"
                )
            )
            cardBinding.tvClientName.text = client.title + " " + client.fullName
            cardBinding.tvClientDescription.text = client.specialist

            cardBinding.root.setOnClickListener {
                action(
                    client
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorListViewHolder {
        return DoctorListViewHolder(
            CardPatientBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: DoctorListViewHolder, position: Int) {
        val profile = clientList[position]
        holder.bindItem(profile)
    }

    override fun getItemCount(): Int {
        return clientList.count()
    }

}