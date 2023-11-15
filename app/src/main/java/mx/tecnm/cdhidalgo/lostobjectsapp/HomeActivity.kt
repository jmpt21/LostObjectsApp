package mx.tecnm.cdhidalgo.lostobjectsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import mx.tecnm.cdhidalgo.lostobjectsapp.databinding.ActivityHomeBinding
import mx.tecnm.cdhidalgo.lostobjectsapp.entities.UserDataClass

class HomeActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomeBinding
    private lateinit var fab: FloatingActionButton
    private lateinit var firestore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firestore = Firebase.firestore
        firestore.collection("users").document(auth.currentUser?.email!!).get()
            .addOnSuccessListener {
                if (!it.exists()) {
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                } else {
                    userProfile = it.toObject(UserDataClass::class.java)
                }
            }
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarHome)

        val navController = findNavController(R.id.nav_host_fragment_content_home)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        fab = binding.btnGoToReport
        fab.setOnClickListener {
            navController.navigate(R.id.action_HomeFragment_to_ReportFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        firestore.collection("users").document(auth.currentUser?.email!!).get()
            .addOnSuccessListener {
                if (!it.exists()) {
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                } else {
                    userProfile = it.toObject(UserDataClass::class.java)
                }
            }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    fun hideFab() {
        fab.isEnabled = false
        fab.hide()
    }

    fun showFab() {
        fab.isEnabled = true
        fab.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_profile -> {
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.action_my_reports -> true
            R.id.action_sign_out -> {
                val auth = Firebase.auth
                auth.signOut()
                userProfile = null
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_home)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}