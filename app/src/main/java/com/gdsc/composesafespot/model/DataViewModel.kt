package com.gdsc.composesafespot.model
import androidx.lifecycle.ViewModel
import com.gdsc.composesafespot.view.utils.CrimeStatusService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DataViewModel @Inject constructor(private val crimeStatusService: CrimeStatusService): ViewModel() {

     val crimeStatusList =crimeStatusService.crimeStatus

}


