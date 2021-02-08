package com.axa.brastlewark.data

import com.axa.brastlewark.data.local.GnomeDao
import com.axa.brastlewark.data.remote.NetworkDataSource
import com.axa.brastlewark.utils.performGetOperation
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BrastlewarkRepository @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val localDataSource: GnomeDao
) {

    fun getGnomes() = performGetOperation(
        databaseQuery = { localDataSource.getGnomes() },
        networkCall = { networkDataSource.getBrastlewark() },
        saveCallResult = { localDataSource.insertAll(it.Brastlewark) }
    )

    fun getGnome(id: Int) = performGetOperation(
        databaseQuery = { localDataSource.getGnome(id) }
    )

}