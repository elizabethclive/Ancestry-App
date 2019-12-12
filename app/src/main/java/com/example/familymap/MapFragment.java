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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
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
    private Event[] events;

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
        inflater.inflate(R.menu.menu, menu);

        return;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.search) {
                Intent searchIntent = new Intent(getActivity(), SearchActivity.class);
                getActivity().startActivity(searchIntent);
        } else if (item.getItemId() == R.id.settings) {
                Intent settingsIntent = new Intent(getActivity(), SettingsActivity.class);
                getActivity().startActivity(settingsIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mMap == null) return;
        mMap.clear();

        addEvents();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        addEvents();
    }

    public void addEvents() {
        events = Model.getInstance().getEvents();
        for (Event currentEvent : events) {
            LatLng location = new LatLng(currentEvent.getLatitude(), currentEvent.getLongitude());
            String city = currentEvent.getCity();
            String eventType = currentEvent.getEventType();
            String ID = currentEvent.getId();
            addMarker(currentEvent.getCity(), location, currentEvent.getEventType(), currentEvent.getId());
//            if (i == 0) {
//                mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
//            }
        }
        if (Model.getInstance().getInEventActivity()) {
            LatLng location = new LatLng(Model.getInstance().getSelectedEvent().getLatitude(), Model.getInstance().getSelectedEvent().getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
            setDisplayText(Model.getInstance().getSelectedEvent().getId());
        }
        setMarkerListener();
    }

    void addMarker(String city, LatLng latLng, String eventType, String eventID) {
        if (eventID.equals("Mrs_Jones_Surf")) {
            System.out.println("adsfs");
        }
        MarkerOptions options = new MarkerOptions().position(latLng).title(city);
        eventType = eventType.toLowerCase();
        if (!Model.getInstance().getColorMap().containsKey(eventType)) {
            Model.getInstance().addToColorMap(eventType);
        }

        options.icon(defaultMarker(colors.get(Model.getInstance().getColorMap().get(eventType))));

        Marker marker = mMap.addMarker(options);
        marker.setTag(eventID);

    }

    void setMarkerListener() {
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
//                clearLines();
                String eventID = (String)marker.getTag();
                setDisplayText(eventID);
                addLines(eventID);
                return false;
            }
        });
    }

    void setDisplayText(String eventID) {
        Event event = null;
        Person[] persons = Model.getInstance().getPersons();
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

    void addLines(String eventID) {
        if (Model.getInstance().getSettings().showFamilyTreeLines()) {
            addFamilyLines(eventID);
        }
        if (Model.getInstance().getSettings().showLifeStoryLines()) {
            addLifeStoryLines(eventID);
        }
        if (Model.getInstance().getSettings().showSpouseLines()) {
            addSpouseLines(eventID);
        }
    }

    void addSpouseLines(String eventID) {
        Event event = Model.getInstance().getEventFromId(eventID);
        Person person = Model.getInstance().getPersonFromId(event.getPersonID());
        if (person.getSpouseID() != null) {
//            Event personEvent = Model.getInstance().getPersonEvents(person).get(0);
            LatLng selectedPersonLoc = new LatLng(event.getLatitude(), event.getLongitude());

            Person spouse = Model.getInstance().getPersonFromId(person.getSpouseID());
            Event spouseEvent = Model.getInstance().getPersonEvents(spouse).get(0);
            LatLng spouseLoc = new LatLng(spouseEvent.getLatitude(), spouseEvent.getLongitude());
            drawLine(selectedPersonLoc, spouseLoc, 10, Color.RED);
        }
    }

    void addFamilyLines(String eventID) {

    }

    void addLifeStoryLines(String eventID) {

    }

    void drawLine(LatLng point1, LatLng point2, Integer lineWidth, Integer color) {
        Polyline line = mMap.addPolyline(new PolylineOptions()
                .add(point1, point2)
                .width(lineWidth)
                .color(color));
    }

    void clearLines() {

    }
}

