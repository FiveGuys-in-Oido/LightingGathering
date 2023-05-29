package com.tuk.lightninggathering;

import android.os.Bundle;
import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.tuk.lightninggathering.databinding.ActivityNaviBinding;

public class NaviActivity extends AppCompatActivity {
    private static final String TAG_LIKE = "like_fragment";
    private static final String TAG_HOME = "home_fragment";
    private static final String TAG_SEARCH = "search_fragment";
    private static final String TAG_USER = "user_fragment";

    private ActivityNaviBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNaviBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setFragment(TAG_HOME, new HomeFragment());

        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.tab_like:
                    setFragment(TAG_LIKE, new LikeFragment());
                    break;
                case R.id.tab_home:
                    setFragment(TAG_HOME, new HomeFragment());
                    break;
                case R.id.tab_user:
                    setFragment(TAG_USER, new UserFragment());
                    break;
                case R.id.tab_search:
                    setFragment(TAG_SEARCH, new SearchFragment());
                    break;
            }
            return true;
        });
    }

    private void setFragment(String tag, Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction fragTransaction = manager.beginTransaction();

        if (manager.findFragmentByTag(tag) == null) {
            fragTransaction.add(R.id.mainFrameLayout, fragment, tag);
        }

        Fragment like = manager.findFragmentByTag(TAG_LIKE);
        Fragment home = manager.findFragmentByTag(TAG_HOME);
        Fragment user = manager.findFragmentByTag(TAG_USER);
        Fragment search = manager.findFragmentByTag(TAG_SEARCH);

        Fragment[] fragments = {like, home, user, search};
        for (Fragment frag : fragments) {
            if (frag != null) {
                fragTransaction.hide(frag);
            }
        }

        switch (tag) {
            case TAG_LIKE:
                if (like != null) fragTransaction.show(like);
                break;
            case TAG_HOME:
                if (home != null) fragTransaction.show(home);
                break;
            case TAG_USER:
                if (user != null) fragTransaction.show(user);
                break;
            case TAG_SEARCH:
                if (search != null) fragTransaction.show(search);
                break;
        }

        fragTransaction.commitAllowingStateLoss();
    }
}
