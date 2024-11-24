package ru.nascar.bms.bar.repository.jdbc

import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import ru.nascar.bms.bar.repository.BarRepository
import ru.nascar.bms.bar.repository.entity.BarEntity
import ru.nascar.bms.infra.getUtcInstant
import ru.nascar.bms.infra.toOffsetDateTime

@Repository
class JdbcBarRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
) : BarRepository {

    companion object {
        private const val SELECT = """
            select 
                b.id,
                b.name,
                b.address,
                b.created_by,
                b.created_at,
                b.updated_by,
                b.updated_at
            from bars b
        """

        private const val SELECT_BY_IDS = """
            $SELECT
            where b.id in (:ids)
        """

        private const val SELECT_BY_NAME_AND_ADDRESS = """
            $SELECT
            where b.name = :name
                and b.address = :address
        """

        private const val UPSERT = """
            insert into bars (id, name, address, created_by, created_at, updated_by, updated_at)
            values (:id, :name, :address, :user, :now, :user, :now)
            on conflict (id) do update set
                name = :name,
                address = :address,
                updated_by = :user,
                updated_at = :now
        """

        private val BAR_MAPPER = RowMapper { rs, _ ->
            BarEntity(
                id = rs.getString("id"),
                name = rs.getString("name"),
                address = rs.getString("address"),
                createdBy = rs.getString("created_at"),
                createdAt = rs.getUtcInstant("created_at"),
                updatedBy = rs.getString("updated_by"),
                updatedAt = rs.getUtcInstant("updated_at"),
            )
        }
    }

    override fun findAll(): List<BarEntity> {
        return jdbcTemplate.query(
            SELECT,
            BAR_MAPPER
        )
    }

    override fun findByIds(ids: Collection<String>): List<BarEntity> {
        if (ids.isEmpty()) {
            return emptyList()
        }

        return jdbcTemplate.query(
            SELECT_BY_IDS,
            mapOf("ids" to ids),
            BAR_MAPPER
        )
    }

    override fun findByNameAndAddress(name: String, address: String): BarEntity? {
        return jdbcTemplate.query(
            SELECT_BY_NAME_AND_ADDRESS,
            mapOf(
                "name" to name,
                "address" to address
            ),
            BAR_MAPPER
        ).firstOrNull()
    }

    override fun save(bar: BarEntity) {
        // TODO: take user from context
        val params = mapOf(
            "id" to bar.id,
            "name" to bar.name,
            "address" to bar.address,
            "user" to bar.updatedBy,
            "now" to bar.updatedAt.toOffsetDateTime(),
        )

        jdbcTemplate.update(
            UPSERT,
            params
        )
    }
}