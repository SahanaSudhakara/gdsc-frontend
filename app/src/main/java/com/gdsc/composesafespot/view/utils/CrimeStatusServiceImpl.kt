package com.gdsc.composesafespot.view.utils

import android.util.Log
import com.gdsc.composesafespot.model.data.CrimeStatus
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CrimeStatusServiceImpl @Inject constructor() : CrimeStatusService {

    override val crimeStatus: Flow<List<CrimeStatus>> = flow {
        emit(getDataFromFirestore())
    }

     override suspend fun getDataFromFirestore(): List<CrimeStatus> {
        val db = FirebaseFirestore.getInstance()
        return try {
            val querySnapshot = db.collection("crimestatus")
                .whereIn("incident_category", listOf("Assault", "Burglary", "Drug Offense", "Disorderly Conduct"))
                .get()
                .await()

            val crimeStatusList = mutableListOf<CrimeStatus>()
            querySnapshot.forEach { document ->
                val result = document.toObject(CrimeStatus::class.java)
                crimeStatusList.add(result)
            }

            crimeStatusList // Return the list directly

        } catch (e: FirebaseFirestoreException) {
            Log.d("error", "Failed to get data from Firestore: $e")
            throw e // Re-throw the exception to handle it in the caller
        }
    }
}
