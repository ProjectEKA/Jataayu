package `in`.projecteka.jataayu.consent.extension

import `in`.projecteka.jataayu.core.model.Consent
import `in`.projecteka.jataayu.core.model.RequestStatus.*

enum class SortOrder { ASC, DESC }

/* Sorts List of consents by last updated time */
fun List<Consent>.getSortedConsentListByLastUpdatedDate(sortOrder: SortOrder = SortOrder.DESC): List<Consent> {
    return when(sortOrder) {
        SortOrder.ASC -> {
            sortedBy { it.getLastUpdated() }
        }
        SortOrder.DESC -> {
            sortedByDescending { it.getLastUpdated() }
        }
    }
}
/*Returns only granted list with sorted by last updated date*/
fun List<Consent>.grantedConsentList(sortOrder: SortOrder = SortOrder.DESC): List<Consent>  {
   return filter { it.status == GRANTED }.getSortedConsentListByLastUpdatedDate(sortOrder)
}

/* Returns only requested list with sorted by last updated date*/
fun List<Consent>.requestedConsentList(sortOrder: SortOrder = SortOrder.DESC): List<Consent>  {
    return filter { it.status in arrayOf(REQUESTED, DENIED) }.getSortedConsentListByLastUpdatedDate(sortOrder)
}
