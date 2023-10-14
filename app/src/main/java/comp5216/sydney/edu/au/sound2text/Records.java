package comp5216.sydney.edu.au.sound2text;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Records extends Activity {
    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.records);

        //为底部导航栏设置监听器
        bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_Main) {
                Intent intentMain = new Intent(Records.this, MainActivity.class);
                startActivity(intentMain);
                return true;
            }else if (itemId == R.id.navigation_Profile) {
                Intent intentProfile = new Intent(Records.this, Profile.class);
                startActivity(intentProfile);
                return true;
            }
            return false;
        });
    }
}
