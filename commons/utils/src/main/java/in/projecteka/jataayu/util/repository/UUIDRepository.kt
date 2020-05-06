package `in`.projecteka.jataayu.util.repository

import java.util.*

interface UUIDRepository {
    fun generateUUID(): String
}

class UUIDRepositoryImpl() : UUIDRepository {

    override fun generateUUID(): String {
        return UUID.randomUUID().toString()
    }


}