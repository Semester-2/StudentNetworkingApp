package com.app.student.networking.model

import java.io.Serializable

data class AnnoucementData( var topic: String?=null, var description: String?=null, var dateTime: Long?=null, var image: String?=null,
                            var location: String?=null, var publishedBy: String?=null, var category: String?=null, var enrollments : HashMap<String,Boolean>?=null
                            ) : Serializable


