package `in`.projecteka.jataayu.user.account.ui.fragment


import `in`.projecteka.jataayu.core.model.CreateAccountRequest
import `in`.projecteka.jataayu.core.model.CreateAccountResponse
import `in`.projecteka.jataayu.network.model.ErrorResponse
import `in`.projecteka.jataayu.network.utils.ResponseCallback
import `in`.projecteka.jataayu.presentation.showAlertDialog
import `in`.projecteka.jataayu.presentation.showErrorDialog
import `in`.projecteka.jataayu.presentation.ui.fragment.BaseFragment
import `in`.projecteka.jataayu.user.account.R
import `in`.projecteka.jataayu.user.account.databinding.FragmentCreateAccountBinding
import `in`.projecteka.jataayu.user.account.listener.AccountCreationClickHandler
import `in`.projecteka.jataayu.user.account.listener.CredentialsInputListener
import `in`.projecteka.jataayu.user.account.listener.PasswordChangeWatcher
import `in`.projecteka.jataayu.user.account.listener.UsernameChangeWatcher
import `in`.projecteka.jataayu.user.account.viewmodel.UserAccountsViewModel
import `in`.projecteka.jataayu.util.extension.setTitle
import `in`.projecteka.jataayu.util.extension.show
import `in`.projecteka.jataayu.util.extension.showLongToast
import `in`.projecteka.jataayu.util.extension.toUtc
import `in`.projecteka.jataayu.util.sharedPref.setAuthToken
import `in`.projecteka.jataayu.util.sharedPref.setUserAccountCreated
import `in`.projecteka.jataayu.util.ui.DateTimeUtils
import `in`.projecteka.jataayu.util.sharedPref.setAuthToken
import `in`.projecteka.jataayu.util.startProvider
import android.app.Activity
import android.os.Bundle
import android.text.InputType
import android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.widget.AppCompatCheckedTextView
import androidx.lifecycle.Observer
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_create_account.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.*


class CreateAccountFragment : BaseFragment(),
    AccountCreationClickHandler, CredentialsInputListener, ResponseCallback, AdapterView.OnItemSelectedListener {

    private lateinit var binding: FragmentCreateAccountBinding

    private val disposables = CompositeDisposable()

    private val viewModel: UserAccountsViewModel by sharedViewModel()

    private var isCriteriaMatch: Boolean = true
    private var selectedYob: Int? = null
    companion object {
        fun newInstance() = CreateAccountFragment()
        const val SPACE = " "
        const val YOB = "yyyy"
        const val usernameCriteria = "^[a-zA-Z0-9.-]{3,150}$"
        /*^                 # start-of-string
        (?=.*[0-9])       # a digit must occur at least once
        (?=.*[a-z])       # a lower case letter must occur at least once
        (?=.*[A-Z])       # an upper case letter must occur at least once
        (?=.*[@#$%^&+=])  # a special character must occur at least once
        (?=\S+$)          # no whitespace allowed in the entire string
        .{8,}             # anything, at least eight places though
        $                 # end-of-string*/
        const val passwordCriteria = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%^&+=]).{8,}\$"
        const val DEFAULT_CHECKED_ID = -1
    }

    override fun showOrHidePassword(view: View) {
        when (binding.etPassword.inputType) {
            getPasswordInputType() -> {
                binding.passwordInputType = getVisiblePasswordInputType()
                binding.btnShowHidePassword.text = getString(R.string.hide)
            }
            getVisiblePasswordInputType() -> {
                binding.passwordInputType = getPasswordInputType()
                binding.btnShowHidePassword.text = getString(R.string.show)
            }
        }
        binding.etPassword.post { binding.etPassword.setSelection(binding.etPassword.text.length) }
    }

    override fun createAccount(view: View) {
            if (binding.usernameErrorText.visibility == View.GONE && binding.passwordErrorText.visibility == View.GONE){
            if (validateFields()) {
                showProgressBar(true)
                viewModel.createAccount(this, getCreateAccountRequest())
            }
        }
    }
    private fun getCreateAccountRequest(): CreateAccountRequest {
        return CreateAccountRequest(getUsername(), et_password?.text.toString(),
            et_name?.text.toString(), getGender(), selectedYob)
    }
    private fun getProviderName(): String {
        return binding.tvProviderName.text.toString()
    }

    private fun getUsername(): String {
        return et_username?.text.toString() + getProviderName()
    }

    private fun getGender(): String {
        return when(cg_gender.checkedChipId) {
            chip_male.id -> "M"
            chip_female.id -> "F"
            else -> "O"
        }
    }

    private fun validateFields(): Boolean {
        var valid = true
        if (binding.etUsername.text.isEmpty()) {
            binding.usernameErrorText.show(true)
            valid = false
        }
        if (binding.etPassword.text.isEmpty()) {
            binding.passwordErrorText.show(true)
            valid = false
        }
        if (binding.etName.text?.isEmpty()!!) {
            binding.etName.error = getString(R.string.should_not_be_empty)
            valid = false
        }
        if (binding.cgGender.checkedChipId == DEFAULT_CHECKED_ID){
            binding.tvErrGender.show(true)
            valid = false
        } else {
            binding.tvErrGender.show(false)
        }
        return valid
    }

    override fun onUsernameEdit(username: String) {
        editTextDisposable(username, usernameCriteria, binding.usernameErrorText)?.let { disposables.add(it) }
    }

    override fun onPasswordEdit(password: String) {
        editTextDisposable(password, passwordCriteria, binding.passwordErrorText)?.let { disposables.add(it) }
    }

    private fun editTextDisposable(text: String, criteria: String, errorText: TextView): Disposable? {
        return Observable.just(text)
            .map { viewModel.isValid(text, criteria) }
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                errorText.show(!it)
                isCriteriaMatch = it
            }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateAccountBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSpinner()
        initBindings()
        initObservers()
    }

    private fun initObservers(){
        viewModel.createAccountResponse.observe(this,
            Observer<CreateAccountResponse> {
                context?.setAuthToken(viewModel.getAuthTokenWithTokenType(it))
                activity?.setUserAccountCreated(true)
                showLongToast(getString(R.string.registered_successfully))
                startProvider(activity!!)
                activity?.finish()
            })
    }

    private fun initSpinner() {
        val arrayAdapter = ArrayAdapter<String>(
            context!!,
            android.R.layout.simple_spinner_dropdown_item, android.R.id.text1, getYearsToPopulate())
        binding.spinnerYob.adapter = arrayAdapter
        arrayAdapter.notifyDataSetChanged()
        binding.spinnerYob.setSelection(0)
    }

    private fun getYearsToPopulate(): List<String> {
        var years = arrayListOf<String>()
        years.add(YOB)
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        for (year in currentYear downTo (currentYear-120)){
            years.add(year.toString())
        }
        return years
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

    private fun initBindings() {
        binding.passwordInputType = getPasswordInputType()
        binding.clickHandler = this
        binding.userNameChangeWatcher = UsernameChangeWatcher(this)
        binding.passwordChangeWatcher = PasswordChangeWatcher(this)
        binding.listener = this
    }

    private fun getVisiblePasswordInputType(): Int {
        return InputType.TYPE_CLASS_TEXT + InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
    }

    private fun getPasswordInputType(): Int {
        return InputType.TYPE_CLASS_TEXT + TYPE_TEXT_VARIATION_PASSWORD
    }

    override fun onVisible() {
        super.onVisible()
        setTitle(R.string.create_account)
    }

    override fun <T> onSuccess(body: T?) {
        showProgressBar(false)
    }

    override fun onFailure(errorBody: ErrorResponse) {
        showProgressBar(false)
        context?.showAlertDialog(getString(R.string.failure), errorBody.error.message, getString(android.R.string.ok))
    }

    override fun onFailure(t: Throwable) {
        showProgressBar(false)
        context?.showErrorDialog(t.localizedMessage)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (position != 0){
            selectedYob = (view as AppCompatCheckedTextView).text.toString().toInt()
        }
    }
}