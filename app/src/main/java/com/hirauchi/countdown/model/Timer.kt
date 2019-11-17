package com.hirauchi.countdown.model

import java.io.Serializable

data class Timer(
    val id: Int,
    var name: String,
    var time: Long
):Serializable