package self.safayet.e_medical_chamber.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import self.safayet.e_medical_chamber.databinding.CardDoctorCategoryBinding
import self.safayet.e_medical_chamber.utils.Utils

class DoctorCategoryAdapter(
    private val context: Context,
    private val categoryList: List<String>,
    private val action: (category: String) -> Unit
) : RecyclerView.Adapter<DoctorCategoryAdapter.DoctorCategoryViewHolder>() {
    inner class DoctorCategoryViewHolder(private val mBinding: CardDoctorCategoryBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
        fun bindItem(client: String) {
            mBinding.ivCategory.setImageDrawable(
                Utils.assetsImage(
                    context,
                    "categories.png"
                )
            )
            mBinding.tvDoctorCategory.text = client
            mBinding.root.setOnClickListener { action(client) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorCategoryViewHolder {
        return DoctorCategoryViewHolder(
            CardDoctorCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return categoryList.count()
    }


    override fun onBindViewHolder(
        holder: DoctorCategoryViewHolder,
        position: Int
    ) {
        val symptom = categoryList[position]
        holder.bindItem(symptom)
    }
}