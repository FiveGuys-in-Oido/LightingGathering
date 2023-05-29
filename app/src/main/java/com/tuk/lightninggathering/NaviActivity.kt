import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.tuk.lightninggathering.*
import com.tuk.lightninggathering.databinding.ActivityNaviBinding

private const val TAG_LIKE = "like_fragment"
private const val TAG_HOME = "home_fragment"
private const val TAG_SEARCH = "search_fragment"
private const val TAG_USER = "user_fragment"

class NaviActivity : AppCompatActivity() {

    private lateinit var binding : ActivityNaviBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNaviBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFragment(TAG_HOME, HomeFragment())

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.tab_like -> setFragment(TAG_LIKE, LikeFragment())
                R.id.tab_home -> setFragment(TAG_HOME, HomeFragment())
                R.id.tab_user -> setFragment(TAG_USER, UserFragment())
                R.id.tab_search -> setFragment(TAG_SEARCH, SearchFragment())
            }
            true
        }
    }

    private fun setFragment(tag: String, fragment: Fragment) {
        val manager: FragmentManager = supportFragmentManager
        val fragTransaction = manager.beginTransaction()

        if (manager.findFragmentByTag(tag) == null){
            fragTransaction.add(R.id.mainFrameLayout, fragment, tag)
        }

        val like = manager.findFragmentByTag(TAG_LIKE)
        val home = manager.findFragmentByTag(TAG_HOME)
        val user = manager.findFragmentByTag(TAG_USER)
        val search = manager.findFragmentByTag(TAG_SEARCH)

        listOf(like, home, user, search).forEach { frag ->
            frag?.let { fragTransaction.hide(it) }
        }

        when(tag) {
            TAG_LIKE -> like?.let { fragTransaction.show(it) }
            TAG_HOME -> home?.let { fragTransaction.show(it) }
            TAG_USER -> user?.let { fragTransaction.show(it) }
            TAG_SEARCH -> search?.let { fragTransaction.show(it) }
        }

        fragTransaction.commitAllowingStateLoss()
    }
}
