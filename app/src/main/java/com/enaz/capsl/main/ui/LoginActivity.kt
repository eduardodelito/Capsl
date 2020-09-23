package com.enaz.capsl.main.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import com.enaz.capsl.main.fragment.CreateUserFragment
import com.enaz.capsl.main.fragment.LoginFragment
import com.enaz.capsl.main.fragment.LoginFragmentDirections
import dagger.android.support.DaggerAppCompatActivity

class LoginActivity : DaggerAppCompatActivity(), LoginFragment.LoginFragmentListener,
    CreateUserFragment.CreateUserFragmentListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
    }

    override fun navigateToMainScreen() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    override fun navigateToCreateUserScreen(view: View) {
        val action = LoginFragmentDirections.actionLoginFragmentToCreateUserFragment()
        view.findNavController().navigate(action)
    }
}

