package self.safayet.e_medical_chamber.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import self.safayet.e_medical_chamber.domain.model.Appointment
import self.safayet.e_medical_chamber.domain.model.Doctor
import com.google.firebase.firestore.CollectionReference
import self.safayet.e_medical_chamber.domain.model.Patient
import self.safayet.e_medical_chamber.domain.repository.UserRepository
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*

class UserRepositoryImpl(
    private val ref: CollectionReference
) : UserRepository {

    private val _isRegistrationSuccess = MutableStateFlow<Boolean>(false)
    override val isRegistrationSuccess = _isRegistrationSuccess.asStateFlow()

    private val _appointments = MutableLiveData<List<Appointment>>()
    val appointments: LiveData<List<Appointment>> = _appointments

    override suspend fun getPatientInfo(email: String, password: String): Flow<Patient?> =
        callbackFlow {
            var user: Patient? = null

            val listener = ref.addSnapshotListener { result, error ->

                if (result != null) {
                    val patients = result.toObjects(Patient::class.java)

                    patients.forEach { patient ->
                        Log.d(TAG, "getPatientInfo: ${patients}")
                        if (patient.email == email && patient.password == password) {
                            user = patient
                            trySend(user)
                            return@addSnapshotListener
                        }
                    }
                }
            }

            awaitClose {
                listener.remove()
            }
        }

    override suspend fun registerPatient(patient: Patient) {
        val id = ref.document().id

        patient.id = id
        ref.document(id).set(patient).addOnCompleteListener {
            Log.d(TAG, "DocumentSnapshot successfully written!")
            _isRegistrationSuccess.value = true
        }
    }

    override suspend fun getDoctorInfo(email: String, password: String): Flow<Doctor?> =
        callbackFlow {
            var user: Doctor? = null

            val listener = ref.addSnapshotListener { result, error ->

                if (result != null) {
                    Log.d(TAG, "getDoctorInfo: ${result.toObjects(Doctor::class.java)}")
                    val doctors = result.toObjects(Doctor::class.java)

                    doctors.forEach { doctor ->
                        if (doctor.email == email && doctor.password == password) {
                            user = doctor
                            trySend(user)
                            return@addSnapshotListener
                        }
                    }
                }
            }

            awaitClose {
                listener.remove()
            }
        }

    override suspend fun registerDoctor(doctor: Doctor) {
        val id = ref.document().id

        doctor.id = id
        ref.document(id).set(doctor).addOnCompleteListener {
            Log.d(TAG, "DocumentSnapshot successfully written!")
            _isRegistrationSuccess.value = true
        }
    }

    override suspend fun updatePatient(patient: Patient) {
        ref.document(patient.id).set(patient)
    }

    override suspend fun updateDoctor(appointment: Appointment) {
        ref.get().addOnSuccessListener {
            val doctors = it.toObjects(Doctor::class.java)

            doctors.forEach { doctor ->
                if (doctor.id == appointment.doctorId) {
                    doctor.appointments += appointment

                    ref.document(doctor.id).set(doctor)
                }
            }
        }
    }

    override suspend fun updateAppointment(doctor: Doctor?, appointment: Appointment) {
        Log.d(TAG, "updateAppointment: $doctor")
        if (doctor == null) {
            ref.document(appointment.patientId).get().addOnSuccessListener {
                val patient = it.toObject(Patient::class.java)
                val appointments = patient?.appointment?.toMutableList()

                patient?.appointment?.forEachIndexed { index, item ->
                    if (item.doctorId == appointment.doctorId && item.isActive) {
                        appointments?.set(index, appointment)

                        patient.appointment = appointments!!
                        ref.document(appointment.patientId).set(patient)
                    }
                }
            }
        } else {
            ref.document(appointment.doctorId).get().addOnSuccessListener {
                val doctor = it.toObject(Doctor::class.java)
                val appointments = doctor?.appointments?.toMutableList()

                doctor?.appointments?.forEachIndexed { index, item ->
                    if (item.doctorId == appointment.doctorId && item.isActive) {
                        appointments?.set(index, appointment)

                        doctor.appointments = appointments!!
                        ref.document(appointment.doctorId).set(doctor)
                    }
                }
            }
        }
    }

    override suspend fun getAppointmentsById(patientId: String, doctorId: String) {
        ref.document(patientId).get().addOnSuccessListener {
            val patient = it.toObject(Patient::class.java)
            val list: MutableList<Appointment> = mutableListOf()

            patient?.appointment?.forEach { appointment: Appointment ->
                if (appointment.doctorId == doctorId) {
                    Log.d(TAG, "getAppointmentsById: $appointment")
                    list.add(appointment)
                }
            }

            _appointments.value = list
        }
    }

    override suspend fun getUserById(id: String): Flow<Patient?> =
        callbackFlow {
            var user: Patient? = null

            val listener = ref.addSnapshotListener { result, _ ->

                if (result != null) {
                    val patients = result.toObjects(Patient::class.java)

                    patients.forEach { patient ->
                        Log.d(TAG, "getPatientInfo: ${patients}")
                        if (patient.id == id) {
                            user = patient
                            trySend(user)
                            return@addSnapshotListener
                        }
                    }
                }
            }

            awaitClose {
                listener.remove()
            }
        }

    override suspend fun updateDoctor(doctor: Doctor) {
        ref.document(doctor.id).set(doctor).addOnCompleteListener {
            Log.d(TAG, "updateDoctor: Profile is updated")
        }
    }

    override suspend fun createAppointment(isDoctor: Boolean, appointment: Appointment) {
        if (isDoctor) {
            ref.document(appointment.doctorId).get().addOnSuccessListener {
                val doctor = it.toObject(Doctor::class.java)
                val appointments = doctor?.appointments?.toMutableList()

                appointments?.forEach { item ->
                    if (item.patientId == appointment.patientId && item.isActive) return@addOnSuccessListener
                }

                appointments?.add(appointment)
                doctor?.appointments = appointments!!

                ref.document(appointment.doctorId).set(doctor)
            }
        } else {

            ref.document(appointment.patientId).get().addOnSuccessListener {
                val patient = it.toObject(Patient::class.java)
                val appointments = patient?.appointment?.toMutableList()

                appointments?.forEach { item ->
                    if (item.doctorId == appointment.doctorId && item.isActive) return@addOnSuccessListener
                }

                appointments?.add(appointment)
                patient?.appointment = appointments!!

                ref.document(appointment.patientId).set(patient)
            }
        }
    }

    companion object {
        val TAG = UserRepositoryImpl::class.java.toString()
    }
}