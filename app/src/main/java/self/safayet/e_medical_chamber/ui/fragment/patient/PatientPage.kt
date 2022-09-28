package self.safayet.e_medical_chamber.ui.fragment.patient

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import self.safayet.e_medical_chamber.R
import self.safayet.e_medical_chamber.databinding.FragmentPatientPageBinding


class PatientPage : Fragment(R.layout.fragment_patient_page) {

    private lateinit var mBinding: FragmentPatientPageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_patient_page, container, false)

        mBinding = FragmentPatientPageBinding.bind(view)

        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.nav_patient) as NavHostFragment
        val controller = navHostFragment.navController
        mBinding.bnvPatientHome.setupWithNavController(controller)

        return view
    }

    companion object {
        val TAG = PatientPage::class.java.toString()
    }
}