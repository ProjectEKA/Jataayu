package `in`.projecteka.jataayu.core.model.approveconsent

import `in`.projecteka.jataayu.core.model.HiType
import `in`.projecteka.jataayu.core.model.Links

data class HiTypeAndLinks(val hiTypes: ArrayList<HiType>, val linkedAccounts: List<Links?>)