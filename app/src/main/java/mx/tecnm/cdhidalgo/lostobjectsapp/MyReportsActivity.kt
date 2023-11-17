package mx.tecnm.cdhidalgo.lostobjectsapp

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import mx.tecnm.cdhidalgo.lostobjectsapp.ui.myreports.SectionsPagerAdapter
import mx.tecnm.cdhidalgo.lostobjectsapp.databinding.ActivityMyReportsBinding

class MyReportsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyReportsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMyReportsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        tabs.setupWithViewPager(viewPager)
        val fab: FloatingActionButton = binding.fab

        fab.setOnClickListener { _ ->
            finish()
        }
    }
}