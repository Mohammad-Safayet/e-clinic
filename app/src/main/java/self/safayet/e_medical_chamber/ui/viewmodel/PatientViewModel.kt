package self.safayet.e_medical_chamber.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import self.safayet.e_medical_chamber.data.repository.PatientRepositoryImpl
import self.safayet.e_medical_chamber.domain.model.Doctor
import self.safayet.e_medical_chamber.domain.repository.PatientRepository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PatientViewModel(
    application: Application,
) : AndroidViewModel(application) {

    private val TAG = "PatientViewModel"
    private val repo: PatientRepository =
        PatientRepositoryImpl(Firebase.firestore.collection("doctor"))


    private val _availableDoctors = MutableLiveData<List<Doctor>>()
    val availableDoctors: LiveData<List<Doctor>> = _availableDoctors

    private val _doctor = MutableLiveData<Doctor>()
    val doctor: LiveData<Doctor> = _doctor

    fun getAvailableDoctors() {
        viewModelScope.launch {
            val response = repo.getAvailableDoctors()

            response.collectLatest {
                _availableDoctors.value = it
                Log.d(TAG, "getAvailableDoctors: $it")
            }
        }
    }

    fun getDoctor(doctorId: String) {
        viewModelScope.launch {
            val response = repo.getAvailableDoctors()

            response.collectLatest {
                it.forEach { person: Doctor ->
                    if (person.id == doctorId) {
                        _doctor.value = person
                        return@collectLatest
                    }
                }
            }
        }
    }

    fun filterDoctor(text: String) {
        viewModelScope.launch {
            val response = repo.getAvailableDoctors()

            response.collectLatest {
                _availableDoctors.value = it.filter { doctor ->
                    Log.d(TAG, "onQueryTextChange: $text -> ${doctor.fullName.contains(text)}")
                    doctor.fullName.contains(text)
                }
                Log.d(TAG, "getAvailableDoctors: $it")
            }
        }
    }
}