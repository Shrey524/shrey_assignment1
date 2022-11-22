package com.example.myapplication.ViewModel

import android.util.Log
import com.airbnb.mvrx.*
import com.example.myapplication.Model.ApiData
import com.example.myapplication.Network.RetroService
import com.example.myapplication.Network.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

data class MainActivityState(
    val response: ApiData? = ApiData(null),
    val text: String? = null
): MavericksState
class MainActivityViewModel : MavericksViewModel<MainActivityState>(initialState = MainActivityState()){

    fun makeApiCall(flag: Boolean) : String?{
        var text: String? = "failed"
        val retrofitInstance = RetrofitInstance.getRetroInstance().create(RetroService::class.java)
        GlobalScope.launch{
            if(flag){
                val response = retrofitInstance.successApi()
                text = "success"

            }else{
                val response = retrofitInstance.failureApi()
                text = "failure"
            }
        }
        return text
    }
}