package ge.asurguladze.finalproject

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ge.asurguladze.finalproject.adapters.ViewPagerAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var plusButton: FloatingActionButton
    private lateinit var homeButton: ImageButton
    private lateinit var settingsButton: ImageButton

    private lateinit var bottomAppBar: BottomAppBar

    private lateinit var viewPager: ViewPager2

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth

        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser

        if(currentUser == null){
            goToAuthorizationPage()
        }else {
            initializeViews()
            setListeners()
        }
    }


    private fun initializeViews() {
        plusButton = findViewById(R.id.plus)
        homeButton = findViewById(R.id.home)
        settingsButton = findViewById(R.id.settings)
        bottomAppBar = findViewById(R.id.bottomAppBar)

        viewPager = findViewById(R.id.vpg)
        viewPager.adapter = ViewPagerAdapter(this)

    }

    private fun setListeners() {

        plusButton.setOnClickListener{
            goToSearchPage()
        }

        homeButton.setOnClickListener {
            viewPager.currentItem = 0
        }

        settingsButton.setOnClickListener {
            viewPager.currentItem = 1
        }

    }

    fun hideBottomAppBar(){
        bottomAppBar.performHide()
    }

    fun showBottomAppBar(){
        bottomAppBar.performShow()
    }

    private fun goToSearchPage(){
        val intent = Intent(this, SearchPage::class.java)
        startActivity(intent)
    }

    private fun goToAuthorizationPage(){
        val intent = Intent(this, AuthorizationPage::class.java)
        startActivity(intent)
    }

}