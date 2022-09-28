package self.safayet.e_medical_chamber.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import self.safayet.e_medical_chamber.data.repository.MeetingRepositoryImpl
import self.safayet.e_medical_chamber.domain.model.Meeting
import self.safayet.e_medical_chamber.domain.repository.MeetingRepository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class MeetingViewModel(
    application: Application,
) : AndroidViewModel(application) {

    private val meetingRepository: MeetingRepository =
        MeetingRepositoryImpl(Firebase.firestore.collection("meeting"))
    private val TAG = UserViewModel::class.java.toString()

    val meetingId = meetingRepository.meetingId
    val meeting = meetingRepository.meeting

    fun getMeetingId(doctorId: String, patientId: String) {
        viewModelScope.launch {
            meetingRepository.getMeetingId(doctorId, patientId)
        }
    }

    fun setMeeting(meeting: Meeting) {
        viewModelScope.launch {
            meetingRepository.setMeeting(meeting)
        }
    }
}