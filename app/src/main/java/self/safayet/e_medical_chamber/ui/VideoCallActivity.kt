package self.safayet.e_medical_chamber.ui

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import self.safayet.e_medical_chamber.databinding.ActivityVideoCallBinding
import io.agora.agorauikit_android.*
import self.safayet.e_medical_chamber.BuildConfig

class VideoCallActivity : AppCompatActivity() {
    // Fill the App ID of your project generated on Agora Console.
    private val appId = BuildConfig.AGORA_ID

    // Fill the temp token generated on Agora Console.
    private val token = BuildConfig.AGORA_TOKEN

    // Fill the channel name.
    private val channelName = "test"

    private var agView: AgoraVideoViewer? = null

    private lateinit var mBinding: ActivityVideoCallBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityVideoCallBinding.inflate(layoutInflater)
        val view = mBinding.root

        setContentView(view)
        val intentExtras = intent.extras

        initializeAndJoinChannel(
            intentExtras!!.getInt("role"),
            intentExtras.getString("room", "")
        )
    }


    @OptIn(ExperimentalUnsignedTypes::class)
    private fun initializeAndJoinChannel(role: Int, channel: String) {
        // Create AgoraVideoViewer instance
        try {
//
            agView = AgoraVideoViewer(
                this,
                AgoraConnectionData(appId),
            )

        } catch (e: Exception) {
            print("Could not initialize AgoraVideoViewer. Check your App ID is valid.")
            print(e.message)
            return
        }
        // Fill the parent ViewGroup (MainActivity)
        this.addContentView(
            agView,
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
        )

        // Check permission and join channel
        if (AgoraVideoViewer.requestPermissions(this)) {
            agView!!.join(
                channelName,
//                token = token,
                role = role
            )

        } else {
            val joinButton = Button(this)
            joinButton.text = "Allow Camera and Microphone, then click here"
            joinButton.setOnClickListener(View.OnClickListener {
                // When the button is clicked, check permissions again and join channel
                // if permissions are granted.
                if (AgoraVideoViewer.requestPermissions(this)) {
                    (joinButton.parent as ViewGroup).removeView(joinButton)
                    agView!!.join(
                        channelName,
//                        token = token,
                        role = role
                    )
                }
            })
            joinButton.setBackgroundColor(Color.GREEN)
            joinButton.setTextColor(Color.RED)
            this.addContentView(
                joinButton,
                FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, 300)
            )
        }
    }
}