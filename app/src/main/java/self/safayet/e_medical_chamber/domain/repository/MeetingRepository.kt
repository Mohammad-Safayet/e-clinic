package self.safayet.e_medical_chamber.domain.repository

import androidx.lifecycle.LiveData
import self.safayet.e_medical_chamber.domain.model.Meeting

interface MeetingRepository {
    val meetingId: LiveData<String>
    val meeting: LiveData<Meeting>

    suspend fun getMeetingId(doctorId: String, patientId: String)

    suspend fun setMeeting(meeting: Meeting)

    suspend fun deleteMeeting(meeting: Meeting)
}