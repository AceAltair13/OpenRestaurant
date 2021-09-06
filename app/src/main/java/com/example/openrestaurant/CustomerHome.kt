package com.example.openrestaurant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.openrestaurant.Fragments.AboutFragment
import com.example.openrestaurant.Fragments.FavouritesFragment
import com.example.openrestaurant.Fragments.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class CustomerHome : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_home)

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