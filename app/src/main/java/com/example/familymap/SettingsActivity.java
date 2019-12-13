package com.example.familymap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import Model.Model;
import Util.Settings;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    private Switch lifeStoryLinesSwitch;
    private Switch familyTreeLinesSwitch;
    private Switch spouseLinesSwitch;
    private Switch fathersSideSwitch;
    private Switch mothersSideSwitch;
    private Switch maleEventsSwitch;
    private Switch femaleEventsSwitch;
    private Settings settings;
    private LinearLayout logout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        settings = Model.getInstance().getSettings();
        findViewById(android.R.id.content);
        View v = findViewById(android.R.id.content);
        lifeStoryLinesSwitch = v.findViewById(R.id.life_story_lines);
        familyTreeLinesSwitch = v.findViewById(R.id.family_tree_lines);
        spouseLinesSwitch = v.findViewById(R.id.spouse_lines);
        fathersSideSwitch = v.findViewById(R.id.fathers_side);
        mothersSideSwitch = v.findViewById(R.id.mothers_side);
        maleEventsSwitch = v.findViewById(R.id.male_events);
        femaleEventsSwitch = v.findViewById(R.id.female_events);

        logout = v.findViewById(R.id.logout_inner_wrapper);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Model.getInstance().clear();
                finish();
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        prepareData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.life_story_lines:
                settings.setLifeStoryLines(lifeStoryLinesSwitch.isChecked());
            case R.id.family_tree_lines:
                settings.setFamilyTreeLines(familyTreeLinesSwitch.isChecked());
            case R.id.spouse_lines:
                settings.setSpouseLines(spouseLinesSwitch.isChecked());
            case R.id.fathers_side:
                settings.setFathersSide(fathersSideSwitch.isChecked());
            case R.id.mothers_side:
                settings.setMothersSide(mothersSideSwitch.isChecked());
            case R.id.male_events:
                settings.setMaleEvents(maleEventsSwitch.isChecked());
            case R.id.female_events:
                settings.setFemaleEvents(femaleEventsSwitch.isChecked());
        }
        Model.getInstance().setSettings(settings);
    }

    protected void prepareData() {
        lifeStoryLinesSwitch.setChecked(Model.getInstance().getSettings().showLifeStoryLines());
        familyTreeLinesSwitch.setChecked(Model.getInstance().getSettings().showFamilyTreeLines());
        spouseLinesSwitch.setChecked(Model.getInstance().getSettings().showSpouseLines());
        fathersSideSwitch.setChecked(Model.getInstance().getSettings().filterFathersSide());
        mothersSideSwitch.setChecked(Model.getInstance().getSettings().filterMothersSide());
        maleEventsSwitch.setChecked(Model.getInstance().getSettings().filterMaleEvents());
        femaleEventsSwitch.setChecked(Model.getInstance().getSettings().filterFemaleEvents());

        lifeStoryLinesSwitch.setOnClickListener(this);
        familyTreeLinesSwitch.setOnClickListener(this);
        spouseLinesSwitch.setOnClickListener(this);
        fathersSideSwitch.setOnClickListener(this);
        mothersSideSwitch.setOnClickListener(this);
        maleEventsSwitch.setOnClickListener(this);
        femaleEventsSwitch.setOnClickListener(this);
    }

}
