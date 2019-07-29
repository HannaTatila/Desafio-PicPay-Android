package com.hanna.picpaydesafio.apresentacao.base

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar

open class BaseActivity : AppCompatActivity() {

    protected fun configuraToolBar(toolbar: Toolbar, mostraBotao: Boolean = true) {
        toolbar.title = ""
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(mostraBotao)
        supportActionBar?.setHomeButtonEnabled(mostraBotao)
    }
}