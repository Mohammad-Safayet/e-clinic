package self.safayet.e_medical_chamber.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import self.safayet.e_medical_chamber.domain.model.Meeting
import self.safayet.e_medical_chamber.domain.repository.MeetingRepository
import com.google.firebase.firestore.CollectionReference

class MeetingRepositoryImpl(
    private val ref: CollectionReference
): MeetingRepository {

    private val _meetingId = MutableLiveData("undefined")
    override val meetingId: LiveData<String> = _meetingId

    private val _meeting= MutableLiveData<Meeting>()
    override val meeting: LiveData<Meeting> = _meeting

    override suspend fun getMeetingId(doctorId: String, patientId: String) {
        ref.get().addOnSuccessListener {
            val meetings = it.toObjects(Meeting::class.java)

            meetings.forEach { meeting ->
                if (meeting.doctorId == doctorId && meeting.patientId == patientId) {
                    _meetingId.value = meeting.id
                    _meeting.value = meeting
                    return@addOnSuccessListener
                }
            }
        }
    }

    override suspend fun setMeeting(meeting: Meeting) {
        val id = ref.document().id

        meeting.id = id
        ref.document(id).set(meeting).addOnSuccessListener {
            Log.d("MeetingRepository", "setMeeting: Meeting is set")
        }
    }

    override suspend fun deleteMeeting(meeting: Meeting) {
        val id = ref.document().id

        meeting.id = id
        ref.document(id).delete().addOnSuccessListener {
            Log.d("MeetingRepository", "setMeeting: Meeting is set")
        }
    }
}