package com.example.duno.data

import kotlinx.coroutines.flow.Flow

interface MeetingRepository {
    fun getSelectedMeeting(): Flow<Meeting>
    fun getAllMeeting(): Flow<List<Meeting>>
    fun getGeoMeeting(uid: Long): Flow<Meeting>
}