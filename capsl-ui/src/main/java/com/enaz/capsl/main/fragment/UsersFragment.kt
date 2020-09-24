package com.enaz.capsl.main.fragment

import com.enaz.capsl.common.fragment.RtcBaseFragment
import com.enaz.capsl.main.BR
import com.enaz.capsl.main.R
import com.enaz.capsl.main.databinding.UsersFragmentBinding
import com.enaz.capsl.main.viewmodel.UsersViewModel
import javax.inject.Inject

class UsersFragment : RtcBaseFragment<UsersFragmentBinding, UsersViewModel>() {

    companion object {
        fun newInstance() = UsersFragment()
    }

    @Inject
    override lateinit var viewModel: UsersViewModel

    override fun createLayout() = R.layout.users_fragment

    override fun getBindingVariable() = BR.usersViewModel

    override fun initViews() {
    }

    override fun subscribeUi() {
    }
}