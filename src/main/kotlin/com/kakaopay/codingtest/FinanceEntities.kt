package com.kakaopay.codingtest

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class Institute(
        var code: String,
        var name: String,
        @Id @GeneratedValue var id: Long? = null
)
