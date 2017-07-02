package com.nonvoid.barcrawler.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nonvoid.barcrawler.R;
import com.nonvoid.barcrawler.model.BreweryLocation;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Matt on 5/11/2017.
 */

public class BreweryMapFragment extends Fragment implements OnMapReadyCallback {

    private static final String LOCATION_ITEMS = "location_items";
    @BindView(R.id.brewery_mapview)
    MapView mapView;

//    private BreweryLocation location;
    private List<BreweryLocation> locationList;
    private List<Marker> markerList = new ArrayList<>();

    public static BreweryMapFragment newInstance(BreweryLocation location){
        ArrayList<BreweryLocation> list = new ArrayList<>();
        list.add(location);
        return newInstance(list);
    }

    public static BreweryMapFragment newInstance(ArrayList<BreweryLocation> breweryLocations) {
        BreweryMapFragment fragment = new BreweryMapFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(LOCATION_ITEMS, breweryLocations);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationList = getArguments().getParcelableArrayList(LOCATION_ITEMS);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.brewery_map_fragment, container, false);
        ButterKnife.bind(this, view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if(!locationList.isEmpty()){
            if(locationList.size()==1){
                //show details of one location
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(locationList.get(0).getLatLng());
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_map_location));

                Marker marker = googleMap.addMarker(markerOptions);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 14));
                markerList.add(marker);
            }else {
                //put all location marker on the map
                LatLngBounds.Builder builder = LatLngBounds.builder();
                for(BreweryLocation location : locationList){
                    builder.include(location.getLatLng());

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(location.getLatLng());
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_map_location));
                    markerOptions.title(location.getName());
                    Marker marker = googleMap.addMarker(markerOptions);
                    markerList.add(marker);
                }
                googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 10));
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        if(mapView!=null && outState != null) {
//            mapView.onSaveInstanceState(outState);
//        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    private void buildMapIntent(BreweryLocation location){
        String formatted = String.format("geo:%s,%s?q=%s", String.valueOf( location.getLatLng().latitude), String.valueOf( location.getLatLng().longitude ), location.getStreetAddress());
        Uri gmmIntentUri = Uri.parse(formatted);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }
}
