package com.sunasterisk.getitdone.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun AppCompatActivity.addFragment(containerId: Int, fragment: Fragment) {
    supportFragmentManager
        .beginTransaction()
        .add(containerId, fragment)
        .commit()
}

fun AppCompatActivity.addFragment(containerId: Int, fragment: Fragment, name: String) {
    supportFragmentManager
        .beginTransaction()
        .add(containerId, fragment)
        .addToBackStack(name)
        .commit()
}

fun AppCompatActivity.popFragment(){
    supportFragmentManager.popBackStack()
}
