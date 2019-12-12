package `in`.org.projecteka.jataayu.provider.callback

interface TextWatcherCallback {
    fun onTextChanged(changedText : CharSequence?, clearButtonVisibility : Int)
    fun onTextCleared(clearButtonVisibility : Int)
}
