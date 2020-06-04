package sg.edu.rp.webservices.smsreceiver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        Fragment f1 = new FragmentSMSNumber();
        ft.replace(R.id.frame1, f1);

        Fragment f2 = new FragmentSMSKeyWords();
        ft.replace(R.id.frame2, f2);

        ft.commit();
    }
}
