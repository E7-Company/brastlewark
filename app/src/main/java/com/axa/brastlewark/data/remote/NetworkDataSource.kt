package com.axa.brastlewark.data.remote

import javax.inject.Inject

class NetworkDataSource @Inject constructor(
    private val brastlewarkAPI: BrastlewarkAPI
): BaseDataSource() {
    suspend fun getBrastlewark() = getResult { brastlewarkAPI.getBrastlewark() }
}