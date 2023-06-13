package com.ixam97.carStatsViewer.database.tripData

import com.ixam97.carStatsViewer.utils.InAppLogger

class LocalTripDataSource(
    private val tripDao: TripDao
): TripDataSource {

    override suspend fun addDrivingPoint(drivingPoint: DrivingPoint) {
        val activeSessionIds = tripDao.getActiveDrivingSessionIds()
        if (!tripDao.drivingPointExists(drivingPoint.driving_point_epoch_time))
            tripDao.insertDrivingPoint(drivingPoint)
        for (sessionId in activeSessionIds) {
            tripDao.insertDrivingSessionPointCrossRef(DrivingSessionPointCrossRef(sessionId, drivingPoint.driving_point_epoch_time))
        }
    }

    override suspend fun getLatestDrivingPoint(): DrivingPoint? {
        return tripDao.getLatestDrivingPoint()
    }

    override suspend fun supersedeDrivingSession(prevSessionId: Long, timestamp: Long): Long? {
        endDrivingSession(timestamp, prevSessionId)?.let {
            InAppLogger.d("[DB] Driving session with ID $prevSessionId has been superseded")
            return startDrivingSession(timestamp, it)
        }
        InAppLogger.w("[DB] Driving session with ID $prevSessionId has not been superseded!")
        return null
    }

    override suspend fun endDrivingSession(timestamp: Long, sessionId: Long): Int? {
        tripDao.getDrivingSessionById(sessionId)?.let {
            tripDao.upsertDrivingSession(it.copy(end_epoch_time = timestamp))
            return it.session_type
        }
        return null
    }

    override suspend fun startDrivingSession(timestamp: Long, type: Int): Long {
        val session = DrivingSession(
            start_epoch_time = timestamp,
            end_epoch_time = 0,
            session_type = type,
            used_energy = 0.0,
            driven_distance = 0.0,
            drive_time = 0,
            note = "",
            used_soc = 0.0,
            used_soc_energy = 0.0,
            last_edited_epoch_time = timestamp
        )
        return tripDao.upsertDrivingSession(session)
    }

    override suspend fun getActiveDrivingSessionsIds(): List<Long> {
        return tripDao.getActiveDrivingSessionIds()
    }

    override suspend fun getActiveDrivingSessions(): List<DrivingSession> {
        return tripDao.getActiveDrivingSessions()
    }

    override suspend fun getDrivingSession(sessionId: Long): DrivingSession? {
        return tripDao.getDrivingSessionById(sessionId)
    }

    override suspend fun updateDrivingSession(drivingSession: DrivingSession) {
        tripDao.upsertDrivingSession(drivingSession)
    }

    override suspend fun getActiveDrivingSessionsIdsMap(): Map<Int, Long> {
        val idsMap: MutableMap<Int, Long> = mutableMapOf()
        getActiveDrivingSessionsIds().forEach {
            idsMap[tripDao.getDrivingSessionById(it)!!.session_type] = it
        }
        return idsMap
    }

    override suspend fun getPastDrivingSessionIds(): List<Long> {
        return tripDao.getPastDrivingSessionIds()
    }

    override suspend fun getPastDrivingSessions(): List<DrivingSession> {
        return tripDao.getPastDrivingSessions()
    }

    override suspend fun getFullDrivingSession(sessionId: Long): DrivingSession {
        return tripDao.getCompleteDrivingSessionById(sessionId)
    }

    override suspend fun startMarker(timestamp: Long, type: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun endMarker(timestamp: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteDrivingSessionById(sessionId: Long) {
        val deletedSessions = tripDao.deleteDrivingSessionByID(sessionId)
        val earliestEpochTime = tripDao.getEarliestEpochTime()
        val deletedCrossRefs = tripDao.clearOldDrivingSessionPointCrossRefs(sessionId)
        val deletedDrivingPoints = tripDao.clearOldDrivingPoints(earliestEpochTime)
        InAppLogger.d("[DB] Deleted session ID: $sessionId ($deletedSessions sessions, $deletedCrossRefs cross refs, $deletedDrivingPoints driving points)")
    }

    override suspend fun addChargingPoint(chargingPoint: ChargingPoint) {
        tripDao.insertChargingPoint(chargingPoint)
    }

    override suspend fun startChargingSession(timestamp: Long, outsideTemp: Float, lat: Float?, lon: Float?): Long {
        val newChargingSession = ChargingSession(
            start_epoch_time = timestamp,
            end_epoch_time = null,
            charged_energy = 0.0,
            charged_soc = 0f,
            outside_temp = outsideTemp,
            lat = lat,
            lon = lon
        )
        val chargingSessionId = tripDao.upsertChargingSession(newChargingSession)

        for (drivingSessionId in tripDao.getActiveDrivingSessionIds()) {
            tripDao.insertDrivingChargingCrossRef(DrivingChargingCrossRef(drivingSessionId, chargingSessionId))
        }

        return chargingSessionId
    }

    override suspend fun endChargingSession(timestamp: Long, sessionId: Long?) {
        val id = if (sessionId == null) {
            val sessionIds = tripDao.getActiveChargingSessionIds()
            if (sessionIds.isEmpty()) return
            else {
                sessionIds.first()
            }
        } else sessionId

        val session = tripDao.getChargingSessionById(id)
        tripDao.upsertChargingSession(session.copy(end_epoch_time = timestamp))
    }

}