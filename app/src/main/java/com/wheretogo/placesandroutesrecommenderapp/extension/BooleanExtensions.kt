package com.wheretogo.placesandroutesrecommenderapp.extension

fun Boolean?.ifTrue(block: Boolean.() -> Unit): Boolean? {
    if (this == true) {
        block()
    }
    return this
}

fun Boolean?.ifFalse(block: Boolean.() -> Unit): Boolean? {
    if (this == false) {
        block()
    }
    return this
}

fun Boolean?.isNotNullAndTrue(): Boolean {
    return this != null && this == true
}

fun Boolean?.isNotNullAndFalse(): Boolean {
    return this != null && this == false
}