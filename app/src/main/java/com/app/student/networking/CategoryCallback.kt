package com.app.student.networking

import com.app.student.networking.model.CategoryGridData

interface CategoryCallback {

    fun categorySelected(category : CategoryGridData)

}