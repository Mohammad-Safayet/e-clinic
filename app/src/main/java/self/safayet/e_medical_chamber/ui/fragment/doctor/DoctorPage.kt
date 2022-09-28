package self.safayet.e_medical_chamber.ui.fragment.doctor

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import self.safayet.e_medical_chamber.R
import self.safayet.e_medical_chamber.databinding.FragmentDoctorPageBinding

class DoctorPage : Fragment(R.layout.fragment_doctor_page) {

    private lateinit var mBinding: FragmentDoctorPageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_doctor_page, container, false)

        mBinding = FragmentDoctorPageBinding.bind(view)

        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.nav_doctor) as NavHostFragment
        val controller = navHostFragment.navController

        mBinding.bnvDoctorHome.setupWithNavController(controller)

        return view
    }

    companion object {

    }
}