package com.example.duno.data

import kotlinx.coroutines.flow.Flow

class MeetingRepositoryImpl : MeetingRepository{
    override fun getSelectedMeeting(): Flow<Meeting> {
        TODO("Not yet implemented")
    }

    override fun getAllMeeting(): Flow<List<Meeting>> {
        TODO("Not yet implemented")
    }

    override fun getGeoMeeting(uid: Long): Flow<Meeting> {
        TODO("Not yet implemented")
    }
}

/*
(uid: Long): Flow<Account> = flow {
    emit(LocalAccountsDataProvider.getContactAccountByUid(uid))
}*/
