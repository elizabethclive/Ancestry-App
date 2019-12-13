package com.example.familymap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapter.ExpandableListAdapter;
import Model.Model;
import Model.Event;
import Model.Person;
import Util.ListItem;

public class PersonActivity extends AppCompatActivity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<ListItem>> listDataChild;
    private ArrayList<Float> colors = new ArrayList();
    private TextView textViewFirstName;
    private TextView textViewLastName;
    private TextView textViewGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        findViewById(android.R.id.content);

        View v = findViewById(android.R.id.content);
        textViewFirstName = v.findViewById(R.id.first_name);
        textViewLastName = v.findViewById(R.id.last_name);
        textViewGender = v.findViewById(R.id.gender);
//        colors.addAll(Arrays.asList(R.color.rose, HUE_RED, HUE_ORANGE, HUE_YELLOW, HUE_GREEN, HUE_CYAN, HUE_AZURE, HUE_BLUE, HUE_VIOLET, HUE_MAGENTA));

        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        prepareListData();
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                String header = listDataHeader.get(groupPosition);
                ListItem listItem = listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);
                if (header.equals("Life Events")) {
                    Model.getInstance().setSelectedEvent(Model.getInstance().getEventFromId(listItem.getId()));
                    Intent myIntent = new Intent(PersonActivity.this, EventActivity.class);
                    startActivity(myIntent);
                } else {
                    Model.getInstance().setSelectedPerson(Model.getInstance().getPersonFromId(listItem.getId()));
                    Intent myIntent = new Intent(PersonActivity.this, PersonActivity.class);
                    startActivity(myIntent);
                }
                System.out.println(listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition));
                return false;
            }
        });
    }

    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<ListItem>>();

        // Adding child data
        listDataHeader.add("Life Events");
        listDataHeader.add("Family");

        Person person = Model.getInstance().getSelectedPerson();
        textViewFirstName.setText(person.getFirstName());
        textViewLastName.setText(person.getLastName());
        textViewGender.setText(person.getGender().equals("M") ? "Male" : "Female");
        ArrayList<Event> events = Model.getInstance().getPersonEvents(person);

        // Adding child data
        List<ListItem> lifeEvents = new ArrayList<ListItem>();
        for (int i = 0; i < events.size(); i++) {
            if (!Model.getInstance().getColorMap().containsKey(events.get(i).getEventType())) {
                Model.getInstance().addToColorMap(events.get(i).getEventType());
            }
//            options.icon(defaultMarker(colors.get(Model.getInstance().getColorMap().get(events.get(i).getEventType()))));
            Drawable genderIcon;
            genderIcon = new IconDrawable(this, FontAwesomeIcons.fa_map_marker).
                        colorRes(R.color.event_icon).sizeDp(40); // TODO: HELP CAN'T GET COLORS TO WORK
            lifeEvents.add(new ListItem(Model.getInstance().getEventDetails(events.get(i)), Model.getInstance().getFullName(person), genderIcon, events.get(i).getId()));
        }

        List<ListItem> family = new ArrayList<ListItem>();
        HashMap<Person, String> persons = Model.getInstance().getImmediateRelations(person);
        for (Map.Entry<Person, String> entry : persons.entrySet()) {
            Drawable genderIcon;
            String gender = entry.getKey().getGender().toLowerCase();
            if (entry.getKey().getGender().equals("m")) {
                genderIcon = new IconDrawable(this, FontAwesomeIcons.fa_male).
                        colorRes(R.color.male_icon).sizeDp(40);
            } else if (entry.getKey().getGender().equals("f")) {
                genderIcon = new IconDrawable(this, FontAwesomeIcons.fa_female).
                        colorRes(R.color.female_icon).sizeDp(40);
            } else {
                genderIcon = new IconDrawable(this, FontAwesomeIcons.fa_map_marker).
                        colorRes(R.color.male_icon).sizeDp(40);
            }
            family.add(new ListItem(Model.getInstance().getFullName(entry.getKey()), entry.getValue(), genderIcon, entry.getKey().getId()));
        }


        listDataChild.put(listDataHeader.get(0), lifeEvents); // Header, Child data
        listDataChild.put(listDataHeader.get(1), family);
    }
}
