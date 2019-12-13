package com.example.familymap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;

import Adapter.SearchAdapter;
import Model.Model;
import Model.Person;
import Model.Event;
import Util.ListItem;

public class SearchActivity extends AppCompatActivity {
    private SearchAdapter searchAdapter;
    private RecyclerView recyclerView;
    private SearchView searchBar;
    private ArrayList<ListItem> matches = new ArrayList<>();
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        searchBar = findViewById(R.id.search_bar);

        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                searchAdapter = new SearchAdapter(matches, context);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(searchAdapter);
                prepareData(s);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

    }

    private void prepareData(String input) {
        input = input.toLowerCase();
        matches.clear(); // didn't work for ben
        Person[] persons = Model.getInstance().getPersons();
        Event[] events = Model.getInstance().getEvents();
        for (Person person : persons) {
            if (person.getFirstName().toLowerCase().contains(input) || person.getLastName().toLowerCase().contains(input)) {
                Drawable genderIcon;
                String gender = person.getGender().toLowerCase();
                if (gender.equals("m")) {
                    genderIcon = new IconDrawable(this, FontAwesomeIcons.fa_male).
                            colorRes(R.color.male_icon).sizeDp(40);
                } else if (gender.equals("f")) {
                    genderIcon = new IconDrawable(this, FontAwesomeIcons.fa_female).
                            colorRes(R.color.female_icon).sizeDp(40);
                } else {
                    genderIcon = new IconDrawable(this, FontAwesomeIcons.fa_map_marker).
                            colorRes(R.color.male_icon).sizeDp(40);
                }
                matches.add(new ListItem(Model.getInstance().getFullName(person), "", genderIcon, person.getId()));
            }
        }
        for (Event event : events) {
            if (Model.getInstance().getPersonFromId(event.getPersonID()).getFirstName().toLowerCase().contains(input) ||
                    Model.getInstance().getPersonFromId(event.getPersonID()).getLastName().toLowerCase().contains(input) ||
                    event.getCountry().toLowerCase().contains(input) ||
                    event.getCity().toLowerCase().contains(input) ||
                    event.getEventType().toLowerCase().contains(input) ||
                    Integer.toString(event.getYear()).contains(input)) {
                if (!Model.getInstance().getColorMap().containsKey(event.getEventType())) {
                    Model.getInstance().addToColorMap(event.getEventType());
                }
    //            options.icon(defaultMarker(colors.get(Model.getInstance().getColorMap().get(event.getEventType()))));
                Drawable genderIcon;
                genderIcon = new IconDrawable(this, FontAwesomeIcons.fa_map_marker).
                        colorRes(R.color.event_icon).sizeDp(40); // TODO: HELP CAN'T GET COLORS TO WORK
                matches.add(new ListItem(Model.getInstance().getEventDetails(event), Model.getInstance().getFullName(Model.getInstance().getPersonFromId(event.getPersonID())), genderIcon, event.getId()));
            }
        }
        searchAdapter.notifyDataSetChanged();
    }
}
