package com.vichitra.raut.uplay.data

import java.io.Serializable

data class AudioModel(
    var path: String,
    var title: String,
    var duration: String) : Serializable
