package com.app.student.networking

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.app.student.networking.fragments.ChatsFragment
import com.app.student.networking.fragments.SearchFragment
import com.app.student.networking.fragments.SettingsFragment
import com.app.student.networking.fragments.model.Chat
import com.app.student.networking.fragments.model.Users
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.main_chat_activity.*

class ChatMainActivity: AppCompatActivity() {
    var refUsers: DatabaseReference? = null
    var firebaseUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_chat_activity)

        firebaseUser = FirebaseAuth.getInstance().currentUser
        refUsers = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)

        val tabLayout: TabLayout = findViewById(R.id.tabLayout)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)

        val ref = FirebaseDatabase.getInstance().reference.child("Chats")

        viewPagerAdapter.addFragment(ChatsFragment(), title = "Chats")
        viewPagerAdapter.addFragment(SearchFragment(), title = "Search")
        viewPagerAdapter.addFragment(SettingsFragment(), title = "Settings")

        viewPager.adapter = viewPagerAdapter
        tabLayout.setupWithViewPager(viewPager)

        ref!!.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {

                var countUnreadMessages = 0

                for (dataSnapshot in p0.children) {
                    val chat = dataSnapshot.getValue(Chat::class.java)
                    if (chat!!.getReceiver().equals(firebaseUser!!.uid) && !chat.isIsSeen()) {
                        countUnreadMessages += 1

                    }
                }

                if (countUnreadMessages == 0) {
                    viewPagerAdapter.updateTitle("Chats")

                }else
                    viewPagerAdapter.updateTitle("($countUnreadMessages) Chats")

            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })

        //Display the username and profile picture
        refUsers!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {

                    val user: Users? = snapshot.getValue(Users::class.java)

                    username_main.text = user!!.getUserName()

                    //Picasso.get().load(new File(imageStringUrl))
                    //.into(iview);

                    //if (image.isEmpty()) {
                    //   iview.setImageResource(R.drawable.placeholder);
                    //} else{
                    //    Picasso.get().load(image).into(iview);
                    //  }

                    if ((user.getProfile()!!).trim().isEmpty())
                    {
                        Picasso.get().load(R.drawable.profile)
                                .into(profile_image)
                    }
                    else{

                        Picasso.get().load(user.getProfile())
                                .into(profile_image)
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.signOutItem -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()

                return true
            }
        }
        return false

    }

    internal class ViewPagerAdapter(fragmentManager: FragmentManager) :
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

        fun updateTitle(title: String) {
            titles[0] = title
            notifyDataSetChanged()
        }

        override fun getPageTitle(i: Int): CharSequence? {
            return titles[i]
        }
    }

    private fun updateStatus(status: String)
    {
        val ref = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)

        val hashMap = HashMap<String, Any>()
        hashMap["status"] = status
        ref!!.updateChildren(hashMap)
    }

    override fun onResume() {
        super.onResume()

        updateStatus("online")
    }

    override fun onPause() {
        super.onPause()

        updateStatus("offline")
    }
}


