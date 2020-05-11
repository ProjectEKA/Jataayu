package `in`.projecteka.jataayu.core.model

enum class RequestStatus(status: String) {
    REQUESTED("REQUESTED"), GRANTED("GRANTED"), DENIED("DENIED"), EXPIRED("EXPIRED")
}