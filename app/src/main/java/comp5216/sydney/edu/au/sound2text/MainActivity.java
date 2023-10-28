package comp5216.sydney.edu.au.sound2text;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.Manifest;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    Fragment Main;
    Fragment Records;
    Fragment Profile;
    BottomNavigationView bottomNav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        bottomNav = findViewById(R.id.bottom_nav);
        Main = new MainFragment(); // 需要你有一个名为MainFragment的Fragment类
        Records = new RecordsFragment(); // 需要你有一个名为RecordsFragment的Fragment类
        Profile = new ProfileFragment(); // 需要你有一个名为ProfileFragment的Fragment类


        requestMicrophonePermission();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, Main)
                    .commit();
        }

        //为底部导航栏设置监听器
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.navigation_Main) {
                selectedFragment = Main;
            } else if (item.getItemId() == R.id.navigation_Records) {
                selectedFragment = Records;
            } else if (item.getItemId() == R.id.navigation_Profile) {
                selectedFragment = Profile;
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }
            return true;
        });
    }
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;

    private void requestMicrophonePermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION);
        } else {
            // Permission already granted, you can use the microphone
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_RECORD_AUDIO_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, you can use the microphone
                } else {
                    // Permission denied, inform the user or handle the situation
                }
                break;
        }
    }

}
