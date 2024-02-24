package com.gdsc.composesafespot.data

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class DataViewModel :ViewModel()
{
    val state= mutableStateOf(CrimeStatus())
    private fun getData()
    {
        viewModelScope.launch {
            state.value= getDataFromFireStore()
        }
    }
}
suspend fun getDataFromFireStore():CrimeStatus{
    val db=FirebaseFirestore.getInstance()
    var crimeStatus=CrimeStatus()
    try {
db.collection("crimestatus").get().await().map {
 val result=  it.toObject(CrimeStatus::class.java)
    crimeStatus=result
}
    }catch(e:FirebaseFirestoreException)
    {
Log.d("error","get Data from fire store failed:$e")
    }
    return crimeStatus
}