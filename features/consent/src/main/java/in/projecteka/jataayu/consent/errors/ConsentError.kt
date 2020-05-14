package `in`.projecteka.jataayu.consent.errors

enum class ConsentError(val errorCode: Int) {

    CONSENT_EXPIRED_DENY(1025),
    CONSENT_EXPIRED_GRANT(1009)
}