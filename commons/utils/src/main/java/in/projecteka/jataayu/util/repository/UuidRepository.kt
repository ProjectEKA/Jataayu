package `in`.projecteka.jataayu.util.repository

import java.util.*

interface UuidRepository {
    fun generateUUID(): String
}

class UuidRepositoryImpl() : UuidRepository {

    override fun generateUUID(): String {
        return UUID.randomUUID().toString()
    }


}