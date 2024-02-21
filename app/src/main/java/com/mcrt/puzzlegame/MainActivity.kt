package com.mcrt.puzzlegame

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.mcrt.puzzlegame.config.ConfigFragment
import com.mcrt.puzzlegame.databinding.ActivityMainBinding
import com.mcrt.puzzlegame.score.ScoreFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)//Icono izquierda
        var drawable = ContextCompat.getDrawable(this, R.drawable.baseline_menu_24)
        supportActionBar?.setHomeAsUpIndicator(drawable)

        binding.navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_scores -> {
                    val fragment = ScoreFragment.newInstance()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, fragment)
                        .addToBackStack("replacement")
                        .commit()
                    binding.drawerLayout.closeDrawers() // Cerrar el Drawer después de hacer clic
                    true
                }
                else -> false
            }
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            android.R.id.home -> {
                binding.drawerLayout.openDrawer(GravityCompat.START)
                true
            }
            R.id.nav_scores -> {
                val fm = supportFragmentManager // Obtén el FragmentManager correctamente
                fm.beginTransaction().apply { // Comienza la transacción del fragmento
                    replace(R.id.fragmentContainerView, ScoreFragment.newInstance())
                    addToBackStack("replacement")
                }.commit() // Realiza la transacción
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }
}