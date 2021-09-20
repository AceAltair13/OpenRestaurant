package com.example.openrestaurant.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.openrestaurant.R
import com.example.openrestaurant.fragments.AboutFragment
import com.example.openrestaurant.fragments.FavouritesFragment
import com.example.openrestaurant.fragments.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.paperdb.Paper

class CustomerHomeActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        Paper.init(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_home)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)
        val searchFragment = SearchFragment()
        val favouritesFragment = FavouritesFragment()
        val aboutFragment = AboutFragment()

        makeCurrentFragment(searchFragment, "Search")

        findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnItemSelectedListener {
            when (it.itemId) {
                R.id.searchNavButton -> makeCurrentFragment(searchFragment, "Search")
                R.id.favoriteNavButton -> makeCurrentFragment(favouritesFragment, "Favourites")
                R.id.infoNavButton -> makeCurrentFragment(aboutFragment, "About Us")
            }
            true
        }

    }

    private fun makeCurrentFragment(fragment: Fragment, appBarTitle: String) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
            title = appBarTitle
        }
}