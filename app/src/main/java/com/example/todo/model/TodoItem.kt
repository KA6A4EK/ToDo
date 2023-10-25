package com.example.todo.model


enum class impotance {
    LOW, MEDIUM, HIGH
}

fun impotanceFromString(impot: String?): impotance {
    if (impot == "HIGH") {
        return impotance.HIGH
    } else if (impot == "MEDIUM") {
        return impotance.MEDIUM
    }
    return impotance.LOW

}


data class TodoItem(
    val id: Int? = 0,
    val text: String?,
    val impotance: impotance?,
    val deadline: String?,
    val dateOfCreation: String?,
    val dateOfChange: String?
)