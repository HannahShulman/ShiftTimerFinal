package com.shift.timer.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WageSetting(@PrimaryKey(autoGenerate = true) val id: Int, val workplaceId: Int, val wage: Int)