package com.example.liedetector.model

public class Language {
    private var name: String = ""
    private var value: String = ""

    constructor(name: String, value: String) {
        this.name = name
        this.value = value
    }


    fun getName(): String {
        return name
    }

    fun setName(name: String) {
        this.name = name
    }

    fun getValue(): String {
        return value
    }

    fun setValue(value: String) {
        this.value = value
    }
}
