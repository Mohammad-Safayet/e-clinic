package self.safayet.e_medical_chamber.domain.repository

import self.safayet.e_medical_chamber.domain.model.Appointment
import self.safayet.e_medical_chamber.domain.model.Doctor
import self.safayet.e_medical_chamber.domain.model.Patient
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    val isRegistrationSuccess: Flow<Boolean>

    suspend fun getPatientInfo(email: String, password: String): Flow<Patient?>

    suspend fun registerPatient(patient: Patient)

    suspend fun getDoctorInfo(email: String, password: String): Flow<Doctor?>

    suspend fun registerDoctor(doctor: Doctor)

    suspend fun updatePatient(patient: Patient)

    suspend fun updateDoctor(appointment: Appointment)

    suspend fun updateAppointment(doctor: Doctor?, appointment: Appointment)

    suspend fun getAppointmentsById(patientId: String, doctorId: String)

    suspend fun getUserById(id: String): Flow<Patient?>

    suspend fun updateDoctor(doctor: Doctor)

    suspend fun createAppointment(isDoctor: Boolean, appointment: Appointment)
}