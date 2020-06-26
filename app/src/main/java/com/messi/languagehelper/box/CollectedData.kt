package com.messi.languagehelper.box

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Index

@Entity
data class CollectedData (
    @Id var id: Long = 0,
    @Index var objectId: String? = null,
    var name: String? = null,
    var json: String? = null,
    var type: String? = null
)