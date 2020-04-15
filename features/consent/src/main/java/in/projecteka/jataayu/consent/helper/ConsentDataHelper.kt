package `in`.projecteka.jataayu.consent.helper

import `in`.projecteka.jataayu.core.model.Consent

object ConsentDataHelper {

    public enum class SortOrder { ASC, DESC }

    fun sortConsentListByLastUpdatedDate(list: List<Consent>, sortOrder: SortOrder = SortOrder.DESC): List<Consent> {
        return when(sortOrder) {
            SortOrder.ASC -> {
                list.sortedBy { it.getLastUpdated() }
            }
            SortOrder.DESC -> {
                list.sortedByDescending { it.getLastUpdated() }
            }
        }
    }
}