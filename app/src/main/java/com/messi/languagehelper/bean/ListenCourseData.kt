package com.messi.languagehelper.bean


data class ListenCourseData (
        var type: String = "",
        //ListenCourse
        var mp3_url: String = "",
        var start_time: String = "",
        var end_time: String = "",
        var transalte: String = "",
        var answer: String = "",
        var content: String = "",

        var user_result: Boolean = false
)