package com.axa.brastlewark.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gnomes")
data class Gnome (
    @PrimaryKey
    val id: Int = 0,
    val name: String,
    val thumbnail: String,
    val age: Int,
    val weight: Float,
    val height: Float,
    val hair_color: String,
    val professions: List<String>,
    val friends: List<String>
)
