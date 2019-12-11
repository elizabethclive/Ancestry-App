package com.example.familymap;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import Model.Event;
import Model.Model;
import Model.Person;

import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_AZURE;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_BLUE;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_CYAN;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_GREEN;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_MAGENTA;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_ORANGE;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_RED;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_ROSE;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_VIOLET;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_YELLOW;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.defaultMarker;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private TextView textViewName;
    private TextView textViewLocation;
    private ImageView imageViewIcon;
    private RelativeLayout banner;
    private ArrayList<Float> colors = new ArrayList();

    public static MapFragment newInstance(){
        return new MapFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(!Model.getInstance().getInEventActivity());
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        super.onCreateView(layoutInflater, viewGroup, bundle);
        View v = layoutInflater.inflate(R.layout.fragment_map, viewGroup, false);
        textViewName = v.findViewById(R.id.name);
        textViewLocation = v.findViewById(R.id.location);
        imageViewIcon = v.findViewById(R.id.icon);
        imageViewIcon.setImageResource(R.drawable.android_icon_foreground);
        banner = v.findViewById(R.id.banner);

        mapFragment = (SupportMapFragment)getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        colors.addAll(Arrays.asList(HUE_ROSE, HUE_RED, HUE_ORANGE, HUE_YELLOW, HUE_GREEN, HUE_CYAN, HUE_AZURE, HUE_BLUE, HUE_VIOLET, HUE_MAGENTA));

        banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!textViewName.getText().toString().equals("Click on a marker to see event details")) {
                    Intent myIntent = new Intent(getActivity(), PersonActivity.class);
                    getActivity().startActivity(myIntent);
                }
            }
        });

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.map_menu, menu);
//        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.search) {
                Toast.makeText(getContext(), "Search selected", Toast.LENGTH_SHORT).show();
                Intent searchIntent = new Intent(getActivity(), SearchActivity.class);
                getActivity().startActivity(searchIntent);
        } else if (item.getItemId() == R.id.settings) {
                Toast.makeText(getContext(), "Settings selected", Toast.LENGTH_SHORT).show();
                Intent settingsIntent = new Intent(getActivity(), SettingsActivity.class);
                getActivity().startActivity(settingsIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Event[] events = Model.getInstance().getEvents();
        for (int i = 0; i < events.length-1; i++) {
            LatLng location = new LatLng(events[i].getLatitude(), events[i].getLongitude());
            addMarker(events[i].getCity(), location, events[i].getEventType(), events[i].getId());
//            mMap.addMarker(new MarkerOptions().position(location).title(events[i].getUsername()).icon(defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
            if (i == 0) {
                mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
            }
        }
        if (Model.getInstance().getInEventActivity()) {
            LatLng location = new LatLng(Model.getInstance().getSelectedEvent().getLatitude(), Model.getInstance().getSelectedEvent().getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
            setDisplayText(Model.getInstance().getSelectedEvent().getId());
        }
        setMarkerListener();
    }

    void addMarker(String city, LatLng latLng, String eventType, String eventID) {
        MarkerOptions options = new MarkerOptions().position(latLng).title(city);
        eventType = eventType.toLowerCase();
        if (!Model.getInstance().getColorMap().containsKey(eventType)) {
            Model.getInstance().addToColorMap(eventType);
//            Model.getInstance().getColorMap().put(eventType, colorIndex);
//            options.icon(defaultMarker(colors.get(colorIndex)));
//            if (colorIndex == colors.size() - 1) colorIndex = 0; else colorIndex++;
        }
        options.icon(defaultMarker(colors.get(Model.getInstance().getColorMap().get(eventType))));

        Marker marker = mMap.addMarker(options);
        marker.setTag(eventID);
    }

    void setMarkerListener() {
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String eventID = (String)marker.getTag();
                setDisplayText(eventID);
                return false;
            }
        });
    }

    void setDisplayText(String eventID) {
        Event[] events = Model.getInstance().getEvents();
        Event event = null;
        Person[] persons = Model.getInstance().getPersons();
        StringBuilder sb = new StringBuilder();
        Drawable genderIcon;
        for (int i = 0; i < events.length; i++) {
            if (events[i].getId() == eventID) {
                event = events[i];
            }
        }
        for (int i = 0; i < persons.length; i++) {
            if (persons[i].getId().equals(event.getPersonID())) {
                Model.getInstance().setSelectedPerson(persons[i]);
            }
        }

        textViewName.setText(Model.getInstance().getFullName(Model.getInstance().getSelectedPerson()));
        textViewLocation.setText(Model.getInstance().getEventDetails(event));
        String gender = Model.getInstance().getSelectedPerson().getGender().toLowerCase();
        if (gender.equals("m")) {
            genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_male).
                    colorRes(R.color.male_icon).sizeDp(40);
        } else if (gender.equals("f")) {
            genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_female).
                    colorRes(R.color.female_icon).sizeDp(40);
        } else {
            genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_map_marker).
                    colorRes(R.color.event_icon).sizeDp(40);
        }
        imageViewIcon.setImageDrawable(genderIcon);
    }
}

