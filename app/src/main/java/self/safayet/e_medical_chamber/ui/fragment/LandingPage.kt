package self.safayet.e_medical_chamber.ui.fragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import self.safayet.e_medical_chamber.ui.fragment.LandingPageDirections
import self.safayet.e_medical_chamber.R
import self.safayet.e_medical_chamber.databinding.FragmentLandingPageBinding
import self.safayet.e_medical_chamber.utils.Utils
import java.io.InputStream

class LandingPage : Fragment(R.layout.fragment_landing_page) {

    private lateinit var mBinding: FragmentLandingPageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_landing_page, container, false)

        mBinding = FragmentLandingPageBinding.bind(view)

        mBinding.ivAppIcon.setImageDrawable(
            Utils.assetsImage(this.requireContext(), "stethoscope.png")
        )

        mBinding.btnPatient.setOnClickListener {
            val action = LandingPageDirections.navigateToLoginPg(
                null, null, true
            )
            Navigation.findNavController(view).navigate(action)
        }

        mBinding.btnDoctor.setOnClickListener {
            val action = LandingPageDirections.navigateToLoginPg(
                null, null, false
            )
            Navigation.findNavController(view).navigate(action)
        }

        return view
    }

    companion object {

    }
}