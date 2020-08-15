package com.shift.timer.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


@Entity
data class Shift(@PrimaryKey(autoGenerate = true) val id: Int = 0,
                 val workplaceId: Int = 0,
                 val start: Date, val end: Date?) {
    val isProgress: Boolean
        get() = end != null
}

@Entity
data class Workplace(@PrimaryKey(autoGenerate = true) val id: Int = 0, val description: String)