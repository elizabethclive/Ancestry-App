package com.example.familymap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import Model.Model;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

public class MainActivity extends AppCompatActivity {
    private LoginFragment loginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Iconify.with(new FontAwesomeModule());

        FragmentManager fm = this.getSupportFragmentManager();
        loginFragment = new LoginFragment();
        fm.beginTransaction().replace(R.id.login_fragment, loginFragment).commit();

        Model.getInstance().setInEventActivity(false);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
