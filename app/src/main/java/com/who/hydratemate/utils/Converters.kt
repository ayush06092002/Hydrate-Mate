package com.who.hydratemate.utils

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class Converters {

    companion object {
        fun epochToLocalDateTime(epoch: Long): String {
            return LocalDateTime.ofInstant(
                Instant.ofEpochMilli(epoch),
                ZoneId.systemDefault()
            ).format(DateTimeFormatter.ofPattern("HH:mm"))
        }

        fun localDateTimeToEpoch(localDateTime: LocalDateTime): Long {
            return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        }

        fun _epochToLocalDateTime(epoch: Long): LocalDateTime {
            return LocalDateTime.ofInstant(
                Instant.ofEpochMilli(epoch),
                ZoneId.systemDefault()
            )
        }

        fun epochToLocalDate(epoch: Long): String {
            return LocalDateTime.ofInstant(
                Instant.ofEpochMilli(epoch),
                ZoneId.systemDefault()
            ).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        }
    }
}