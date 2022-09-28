package self.safayet.e_medical_chamber.ui.fragment.patient

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import self.safayet.e_medical_chamber.ui.fragment.patient.DoctorCategoryPageFragmentDirections
import self.safayet.e_medical_chamber.R
import self.safayet.e_medical_chamber.databinding.CardDoctorCategoryBinding
import self.safayet.e_medical_chamber.databinding.FragmentDoctorCategoryPageBinding
import self.safayet.e_medical_chamber.ui.adapter.DoctorCategoryAdapter
import self.safayet.e_medical_chamber.utils.Utils

class DoctorCategoryPageFragment : Fragment() {

    private lateinit var mBinding: FragmentDoctorCategoryPageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_doctor_category_page, container, false)

        mBinding = FragmentDoctorCategoryPageBinding.bind(view)

        mBinding.rvDoctorCategory.adapter = context?.let {
            DoctorCategoryAdapter(
                it,
                resources.getStringArray(R.array.doctor_categories).toList(),
            ) { category    ->
                val action = DoctorCategoryPageFragmentDirections.navigateToFilterPage(
                    category
                )

                Navigation.findNavController(view).navigate(action)
            }
        }

        return view
    }

    companion object {

    }
}