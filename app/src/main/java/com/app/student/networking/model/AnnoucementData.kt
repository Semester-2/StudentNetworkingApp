package com.app.student.networking.model

import java.io.Serializable


data class AnnoucementData( var topic: String?=null, var description: String?=null, var dateTime: String?=null, var image: String?=null) : Serializable


