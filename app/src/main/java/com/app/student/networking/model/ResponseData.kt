package com.app.student.networking.model

import java.io.Serializable

data class ResponseData(var key:String?, var announcements : AnnoucementData) : Serializable
