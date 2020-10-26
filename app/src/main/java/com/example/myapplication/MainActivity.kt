package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.myapplication.Model.Users
import com.example.myapplication.`interface`.IMainActivity
import com.example.myapplication.fragment.ChatFragment
import com.example.myapplication.fragment.SearchFragment
import com.example.myapplication.fragment.SettingFragment
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),IMainActivity {

    private var firebaseUser : FirebaseUser? = null
    private var refUser : DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()

        onGetDataUser()


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater: MenuInflater = menuInflater

        inflater.inflate(R.menu.menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.logout -> {

                FirebaseAuth.getInstance().signOut()

                startActivity(Intent(this@MainActivity, WellcomeActivity::class.java))
                finish()

            }
            else -> super.onOptionsItemSelected(item)

        }

        return true
    }

    internal class viewPagerAdapter(fragmentManager: FragmentManager) :
        FragmentPagerAdapter(fragmentManager) {

        private val fragments: ArrayList<Fragment> = ArrayList<Fragment>()
        private val titles: ArrayList<String> = ArrayList<String>()


        override fun getItem(position: Int): Fragment {

            return fragments[position]
        }

        override fun getCount(): Int {

            return fragments.size
        }

        fun addFragment(fragment: Fragment, title: String) {

            fragments.add(fragment)
            titles.add(title)

        }

        override fun getPageTitle(position: Int): CharSequence? {

            return titles[position]

        }


    }

    override fun initView() {

        val mToolbar: Toolbar = findViewById(R.id.toolbar_home)
        setSupportActionBar(mToolbar)

        val mTabbar: TabLayout = findViewById(R.id.tablayout_home)
        val mViewpager: ViewPager = findViewById(R.id.viewpager_home)

        val mViewpagerAdapter = viewPagerAdapter(supportFragmentManager)

        mViewpagerAdapter.addFragment(ChatFragment(), "Chat")
        mViewpagerAdapter.addFragment(SettingFragment(), "Setting")
        mViewpagerAdapter.addFragment(SearchFragment(), "Search")

        mViewpager.adapter = mViewpagerAdapter

        mTabbar.setupWithViewPager(mViewpager)

    }

    override fun onGetDataUser() {

        firebaseUser = FirebaseAuth.getInstance().currentUser

        refUser = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid.toString())


        refUser!!.addValueEventListener(object : ValueEventListener{

            override fun onCancelled(p0: DatabaseError) {


            }

            override fun onDataChange(p0: DataSnapshot) {

                if(p0.exists()){

                    val users : Users? = p0.getValue(Users::class.java)

                    txt_namehome.text = users!!.getName()

                    if (users.getProfile() == ""){
                        txt_imghome.setBackgroundResource(R.drawable.ic_launcher_background);
                    }else{
                        Log.d("Datbt_Main","getInfo : ${users.getCover().toString()} :Profile ${users.getProfile().toString()}")
                        Picasso.get().load(users.getProfile()).placeholder(R.drawable.ic_launcher_background).into(txt_imghome)
                    }

                }

            }


        })

    }
}
