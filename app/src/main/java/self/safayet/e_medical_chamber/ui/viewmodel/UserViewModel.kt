package self.safayet.e_medical_chamber.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import self.safayet.e_medical_chamber.data.repository.UserRepositoryImpl
import self.safayet.e_medical_chamber.domain.model.Appointment
import self.safayet.e_medical_chamber.domain.model.Doctor
import self.safayet.e_medical_chamber.domain.model.Patient
import self.safayet.e_medical_chamber.domain.repository.UserRepository
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class UserViewModel(
    application: Application,
) : AndroidViewModel(application) {
    private val patientRepository: UserRepository =
        UserRepositoryImpl(Firebase.firestore.collection("patient"))
    private val doctorRepository: UserRepository =
        UserRepositoryImpl(Firebase.firestore.collection("doctor"))
    private val TAG = UserViewModel::class.java.toString()

    private val _user = MutableLiveData<Any>()
    val user: LiveData<Any> = _user

    private val _regSuccess = MutableStateFlow<Boolean>(false)
    val regSuccess = _regSuccess

    var appointments: LiveData<List<Appointment>> = MutableLiveData()

    val loading = MutableLiveData(false)

    fun getPatient(email: String, password: String) {
        viewModelScope.launch {
            val result = patientRepository.getPatientInfo(email, password)

            result.collect {
                if (it != null)
                    _user.value = it
                Log.d(TAG, "getPatient: $it")
            }
        }
    }

    fun getDoctor(email: String, password: String) {
        viewModelScope.launch {
            val result = doctorRepository.getDoctorInfo(email, password)

            result.collect {
                if (it != null)
                    _user.value = it
                Log.d(TAG, "getDoctor: $it")
            }
        }
    }

    fun registerPatient(patient: Patient) {
        viewModelScope.launch {
            patientRepository.registerPatient(patient)
            val result = patientRepository.isRegistrationSuccess

            result.collect {
                _regSuccess.value = it
                Log.d(TAG, "registerPatient: $it")
            }
        }
    }

    fun registerDoctor(doctor: Doctor) {
        viewModelScope.launch {
            doctorRepository.registerDoctor(doctor)
            val result = doctorRepository.isRegistrationSuccess

            result.collect {
                _regSuccess.value = it
                Log.d(TAG, "registerPatient: $doctor $it")
            }
        }
    }

    fun updatePatient(appointment: Appointment) {
        viewModelScope.launch {
            val patient: Patient = user.value as Patient

            // Check if the patient is already registered
            patient.appointment.forEach {
                if (appointment.name == it.name && it.isActive) {
                    return@launch
                }
            }

            patient.appointment += appointment
            val doctorAppointment = Appointment(
                patient.id,
                appointment.doctorId,
                patient.fullName,
                patient.email,
                appointment.symptoms,
                "--",
                appointment.prescription
            )
            doctorRepository.updateDoctor(doctorAppointment)
            patientRepository.updatePatient(patient)
        }
    }

    fun updateAppointment(appointment: Appointment) {
        viewModelScope.launch {
            val doctor: Doctor = user.value as Doctor

            Log.d(TAG, "updateAppointment: done $appointment")
            doctorRepository.updateAppointment(doctor, appointment)
            appointment.contact = doctor.mobileNo
            patientRepository.updateAppointment(null, appointment)
        }
    }

    fun getAppointmentById(patientId: String, doctorId: String) {
        viewModelScope.launch {
            if (patientRepository is UserRepositoryImpl) {
                val result = async { patientRepository.getAppointmentsById(patientId, doctorId) }
                result.await()
                appointments = patientRepository.appointments
            }
        }
    }

    fun getUserById(id: String, isPatient: Boolean): MutableLiveData<Patient> {
        val patient = MutableLiveData<Patient>()

        viewModelScope.launch {
            if (isPatient) {
                patientRepository.getUserById(id).collect {
                    if (it != null) {
                        patient.value = it
                    }
                }
            } else {
                doctorRepository.getUserById(id).collect {

                }
            }
        }

        return patient
    }

    fun updateDoctor(doctor: Doctor) {
        viewModelScope.launch {
            doctorRepository.updateDoctor(doctor)
        }
    }

    fun updatePatient(patient: Patient) {
        viewModelScope.launch {
            patientRepository.updatePatient(patient)
        }
    }

    fun createAppointment(appointment: Appointment) {
        viewModelScope.launch {

        }
    }
}