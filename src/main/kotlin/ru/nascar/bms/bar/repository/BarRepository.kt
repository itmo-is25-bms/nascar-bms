package ru.nascar.bms.bar.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.nascar.bms.bar.repository.entity.BarEntity

@Repository
interface BarRepository : JpaRepository<BarEntity, String>
