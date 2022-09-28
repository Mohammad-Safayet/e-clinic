package self.safayet.e_medical_chamber.domain.repository

import self.safayet.e_medical_chamber.domain.model.Doctor
import self.safayet.e_medical_chamber.domain.model.Patient
import kotlinx.coroutines.flow.Flow

interface PatientRepository {
    suspend fun getAvailableDoctors(): Flow<List<Doctor>>
}