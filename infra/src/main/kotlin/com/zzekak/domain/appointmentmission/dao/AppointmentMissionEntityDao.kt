package com.zzekak.domain.appointmentmission.dao

import com.zzekak.domain.appointmentmission.entity.AppointmentMissionEntity
import com.zzekak.domain.appointmentmission.entity.AppointmentMissionId
import com.zzekak.domain.mission.model.AppointmentUserMissionQuery
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

interface AppointmentMissionEntityDao {
    fun save(apms: AppointmentMissionEntity): AppointmentMissionEntity

    fun <T> findByAppointmentId(value: UUID): List<T>

    fun findById(appointmentMissionId: AppointmentMissionId): AppointmentMissionEntity
}

@Repository
interface AppointmentMissionDaoJpaRepository : JpaRepository<AppointmentMissionEntity, AppointmentMissionId> {
    override fun findById(appointmentMissionId: AppointmentMissionId): Optional<AppointmentMissionEntity>
}

@Repository
class AppointmentMissionEntityDaoImpl(
    val repo: AppointmentMissionDaoJpaRepository,
    @PersistenceContext
    val entityManager: EntityManager
) : AppointmentMissionEntityDao {
    override fun save(apms: AppointmentMissionEntity): AppointmentMissionEntity = repo.save(apms)

    override fun <T> findByAppointmentId(appointmentId: UUID): List<T> {
        val jpql =
            """
            SELECT new com.zzekak.domain.mission.model.AppointmentUserMissionQuery(
                  am.id.apntMisnId,
                  am.appointment.appointmentId,
                  u.name,
                  case when am.missionStepOneCompleteAt is null then 'N' else 'Y' end,
                  case when am.missionStepTwoCompleteAt is null then 'N' else 'Y' end,
                  am.missionStepOneCompleteAt,
                  am.missionStepTwoCompleteAt,
                  u.userId
              )
             FROM AppointmentMissionEntity am
             JOIN UserEntity u ON am.user = u
             WHERE am.appointment.appointmentId = :appointmentId
            """.trimIndent()

        val query = entityManager.createQuery(jpql, AppointmentUserMissionQuery::class.java)
        query.setParameter("appointmentId", appointmentId)

        return query.resultList as List<T>
    }

    override fun findById(appointmentMissionId: AppointmentMissionId): AppointmentMissionEntity =
        repo.findById(
            appointmentMissionId,
        ).get()
}
