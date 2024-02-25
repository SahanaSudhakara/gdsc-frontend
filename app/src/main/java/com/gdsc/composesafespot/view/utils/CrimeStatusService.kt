package com.gdsc.composesafespot.view.utils

import com.gdsc.composesafespot.model.data.CrimeStatus
import kotlinx.coroutines.flow.Flow

interface CrimeStatusService {
    val crimeStatus: Flow<List<CrimeStatus>>
    suspend fun getDataFromFirestore(): List<CrimeStatus>

}