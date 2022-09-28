package self.safayet.e_medical_chamber.data.repository

import self.safayet.e_medical_chamber.domain.model.Doctor
import self.safayet.e_medical_chamber.domain.repository.PatientRepository
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class PatientRepositoryImpl(
    private val ref: CollectionReference
) : PatientRepository {
    override suspend fun getAvailableDoctors(): Flow<List<Doctor>> =
        callbackFlow {
            val response = ref.orderBy("fullName").addSnapshotListener { result, error ->
                if (result != null) {
                    val availableDoctors = result.toObjects(Doctor::class.java)

                    trySend(availableDoctors)
                }
            }

            awaitClose {
                response.remove()
            }
        }
}