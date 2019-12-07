package `in`.org.projecteka.jataayu.repository

interface SampleRepository {
    fun getSampleText(): String
}

class SampleRepositoryImpl() : SampleRepository {
    override fun getSampleText(): String {
        return "Sample text from repository"
    }

}