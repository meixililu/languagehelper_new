package com.messi.languagehelper.bean


data class ListenCourseData (
        var type: String = "",
        var mp3_url: String = "",
        var start_time: String = "",
        var end_time: String = "",
        var transalte: String = "",
        var answer: String = "",
        var content: String = "",
        var img: String = "",
        var tips: String = "",
        var user_result: Boolean = false,
        var title: String = "",
        var question: String = "",
        var play_content: String = "",
        var video_url: String = "",
        var video_type: String = "",
        var options: ArrayList<String> = ArrayList()

)