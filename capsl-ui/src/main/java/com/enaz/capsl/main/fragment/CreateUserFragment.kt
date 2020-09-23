package com.enaz.capsl.main.fragment

import android.content.Context
import com.enaz.capsl.common.fragment.BaseFragment
import com.enaz.capsl.main.BR
import com.enaz.capsl.main.R
import com.enaz.capsl.main.databinding.CreateUserFragmentBinding
import com.enaz.capsl.main.viewmodel.CreateUserViewModel
import kotlinx.android.synthetic.main.create_user_fragment.*
import javax.inject.Inject

class CreateUserFragment : BaseFragment<CreateUserFragmentBinding, CreateUserViewModel>() {

    private var listener: CreateUserFragmentListener? = null

    companion object {
        fun newInstance() = CreateUserFragment()
    }

    @Inject
    override lateinit var viewModel: CreateUserViewModel

    override fun createLayout() = R.layout.create_user_fragment

    override fun getBindingVariable() = BR.createUserViewModel

    override fun initViews() {
        register.setOnClickListener {
            listener?.navigateToMainScreen()
        }
    }

    override fun subscribeUi() {

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CreateUserFragmentListener) {
            listener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface CreateUserFragmentListener {
        fun navigateToMainScreen()
    }
}