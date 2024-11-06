package ru.nascar.bms.bars

import org.springframework.data.repository.CrudRepository
import ru.nascar.bms.bars.contracts.Bar

interface BarRepository : CrudRepository<Bar, String>