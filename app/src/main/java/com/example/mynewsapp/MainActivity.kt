package com.example.mynewsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.example.network.Interfaces.newsInterface
import com.example.models.newsFeed
import com.example.network.clients.RetrofitClient
import com.google.android.material.navigation.NavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    //Drawer toggle for action bar
    lateinit var toggle:ActionBarDrawerToggle
    private lateinit var drawerLayout:DrawerLayout
    private lateinit var navView:NavigationView


    private lateinit var myApi: newsInterface
    private lateinit var listView:ListView

    private lateinit var topStoriesId:ArrayList<Int>
    private var newInfo : ArrayList<newsFeed> = ArrayList()
    private var newsTitles: ArrayList<String> = ArrayList()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //set up for nav view
        setupNavView()

        listView = findViewById(R.id.myListView)
        val retrofit = RetrofitClient.getRetroInstance()
        myApi = retrofit.create(newsInterface::class.java)

        getTopStories()


        //listview item click listener
        listView.setOnItemClickListener{
            parent,view,position,id->
            val url = newInfo.get(position).url
            intent = Intent(this,storyWebView::class.java)
            intent.putExtra("URL",url)
            startActivity(intent)

        }


        //navigation view listener
        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.top_stories -> Toast.makeText(applicationContext,"Top_Stories",Toast.LENGTH_SHORT).show()
                R.id.latest_stories -> Toast.makeText(applicationContext,"latest_stories",Toast.LENGTH_SHORT).show()
                R.id.feedBack -> Toast.makeText(applicationContext,"feedBack",Toast.LENGTH_SHORT).show()
                R.id.about_me -> Toast.makeText(applicationContext,"about_me",Toast.LENGTH_SHORT).show()
            }
            true
        }



    }

    fun setupNavView(){
        //actionbar drawer toggle
        drawerLayout = findViewById(R.id.drawerLayout)
        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView = findViewById(R.id.nav_view)

    }

    @Override
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(toggle.onOptionsItemSelected(item))
            return true

        return super.onOptionsItemSelected(item)
    }

    private fun getTopStories(){
        val call: Call<List<Int>> = myApi.getTopStories()

        Log.d("getTopStories","entering call.enqueue")
        call.enqueue(object:Callback<List<Int>>{

            override fun onResponse(call: Call<List<Int>>, response: Response<List<Int>>) {
                if(response.isSuccessful){
                    Log.d("getTopStories","call.enqeue in response")
                    topStoriesId = response.body() as ArrayList<Int>

                    Log.d("list",topStoriesId.toString())
                    getTitleById(topStoriesId)
                }
            }

            override fun onFailure(call: Call<List<Int>>, t: Throwable) {
                Log.d("getTopStories","call.enqeue in failure")
                Toast.makeText(applicationContext,"Response Fail for topStoriesfetch",Toast.LENGTH_LONG).show()

            }

        })
    }

    private fun getTitleById(a:List<Int>) {

        Log.i("for",a.toString())
        for(i in a) {
            Log.d("inside for loop",i.toString())
            val call: Call<newsFeed> = myApi.getPosts(i)

        call.enqueue(object:Callback<newsFeed>{
            override fun onResponse(
                call: Call<newsFeed>,
                response: Response<newsFeed>
            ) {
                if (response.isSuccessful) {
                    Log.d("DEBUG","inside response is successful")
                    response.body()?.let { newInfo.add(it) }
                    response.body()?.title?.let{ newsTitles.add(it)}
                    ArrayAdapter<String>(baseContext,android.R.layout.simple_list_item_1,newsTitles)
                    listView.dividerHeight =  2


                } else {
                    Log.d("DEBUG","inside response is Failure")
                    newsTitles.add("in on Response method something is wrong at ${i}")
                }
        Log.i("should be before",newInfo.size.toString())
            }
            override fun onFailure(call: Call<newsFeed>, t: Throwable) {
                newsTitles.add("in OnFailure method response failed")

            }
        }
        )
        }

        onComplete()


    }
    fun onComplete(){
        Log.d("oncomplet====>","inside on complete")
        listView.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,newsTitles)
        listView.dividerHeight =  2
    }
}