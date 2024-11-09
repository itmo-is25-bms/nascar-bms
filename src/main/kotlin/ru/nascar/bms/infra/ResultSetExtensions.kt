package ru.nascar.bms.infra

import java.sql.ResultSet
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset

fun Instant.toOffsetDateTime(): OffsetDateTime = OffsetDateTime.ofInstant(this, ZoneOffset.UTC)

fun ResultSet.getUtcInstant(column: String): Instant = getUtcInstantNullable(column)!!

fun ResultSet.getOffsetDateTime(column: String): OffsetDateTime = getOffsetDateTimeNullable(column)!!

fun ResultSet.getUtcInstantNullable(column: String): Instant? = getOffsetDateTimeNullable(column)?.toInstant()

fun ResultSet.getOffsetDateTimeNullable(column: String): OffsetDateTime? = getObject(column, OffsetDateTime::class.java)