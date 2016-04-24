package com.android.safetycheck.app;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.BounceInterpolator;
import android.widget.Toast;

import com.android.safetycheck.model.Earthquake;
import com.android.safetycheck.model.Person;
import com.android.safetycheck.service.AsyncCollectionConnect;
import com.android.safetycheck.service.MethodInformation;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapActivity extends AppCompatActivity {

    private SupportMapFragment mapFragment;
    private GoogleMap map;

    /*
     * Define a request code to send to Google Play services This code is
     * returned in Activity.onActivityResult
     */
    private static final int LOCATION_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);
        requestPermission(Manifest.permission.ACCESS_FINE_LOCATION,
                LOCATION_REQUEST_CODE);

        mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
        if (mapFragment != null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap map) {
                    loadMap(map);
                }
            });
        } else {
            Toast.makeText(this, "Error - Map Fragment was null!!", Toast.LENGTH_SHORT).show();
        }

    }

    protected void loadMap(GoogleMap googleMap) {
        map = googleMap;
        if (map != null) {
            // Map is ready
            // Once map is loaded
            // Supported types include: MAP_TYPE_NORMAL, MAP_TYPE_SATELLITE
            // MAP_TYPE_TERRAIN, MAP_TYPE_HYBRID
            map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            //map.setMyLocationEnabled(true);
            UiSettings mapUiSettings = map.getUiSettings();
            mapUiSettings.setZoomControlsEnabled(true);
            mapUiSettings.setZoomGesturesEnabled(true);
            mapUiSettings.setMyLocationButtonEnabled(true);
            mapUiSettings.setScrollGesturesEnabled(true);
            Toast.makeText(this, "Map Fragment was loaded properly!", Toast.LENGTH_SHORT).show();
            // Attach marker click listener to the map here
            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                public boolean onMarkerClick(Marker marker) {
                    // Handle marker click here
                    marker.showInfoWindow();
                    return false;
                }
            });
            performAction();
        } else {
            Toast.makeText(this, "Error - Map was null!!", Toast.LENGTH_SHORT).show();
        }
    }

    protected void requestPermission(String permissionType, int requestCode) {
        int permission = ContextCompat.checkSelfPermission(this,
                permissionType);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{permissionType}, requestCode
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST_CODE: {

                if (grantResults.length == 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Unable to show location - permission required", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void performAction() {
        Intent i = getIntent();
        String action = i.getStringExtra("action");
        switch (action) {
            case "mapEarthquake":
                final Earthquake eq = (Earthquake) i.getSerializableExtra("earthquake");
                showEarthquake(eq, true);
                showImpactedPersons(this, new HashMap<String, String>() {{
                    put("earthquake", eq.getId());
                }});
                break;
            case "mapEarthquakes":
                //final List<Earthquake> earthquakes = (ArrayList<Earthquake>) i.getSerializableExtra("earthquakes");
                final List<Earthquake> earthquakes = ((SafetyCheckApp) this.getApplicationContext()).getEarthquakesData();
                showEarthquakes(earthquakes);
                break;
            case "mapPersons":
                final List<Person> persons = (ArrayList<Person>) i.getSerializableExtra("persons");
                showPersons(persons);
                break;
        }

    }

    private void dropPinEffect(final Marker marker) {
        // Handler allows us to repeat a code block after a specified delay
        final android.os.Handler handler = new android.os.Handler();
        final long start = SystemClock.uptimeMillis();
        final long duration = 1500;

        // Use the bounce interpolator
        final android.view.animation.Interpolator interpolator =
                new BounceInterpolator();

        // Animate marker with a bounce updating its position every 15ms
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                // Calculate t for bounce based on elapsed time
                float t = Math.max(
                        1 - interpolator.getInterpolation((float) elapsed
                                / duration), 0);
                // Set the anchor
                marker.setAnchor(0.5f, 1.0f + 14 * t);

                if (t > 0.0) {
                    // Post this event again 15ms from now.
                    handler.postDelayed(this, 15);
                } else { // done elapsing, show window
                    marker.showInfoWindow();
                }
            }
        });
    }

    private void dropCircleEffect(float lat, float lon, float rad) {
        // Instantiates a new CircleOptions object and defines the center and radius
        LatLng cen = new LatLng(lat, lon);
        CircleOptions circleOptions = new CircleOptions()
                .center(cen)
                .radius(rad)
                .strokeColor(Color.rgb(100, 50, 0))
                .strokeWidth(5)
                .fillColor(Color.argb(70, 255, 0, 50))
                .zIndex(30);
        // In meters
        // Get back the mutable Circle
        Circle circle = map.addCircle(circleOptions);
    }

    private void dropMarker(float lat, float lon, float color, String title, String snippet) {
        LatLng cen = new LatLng(lat, lon);
        Marker marker = map.addMarker(new MarkerOptions()
                .position(cen).title(title).snippet(snippet)
                .icon(BitmapDescriptorFactory.defaultMarker(
                        color)));
        //dropPinEffect(marker);
    }

    /*
    private boolean isGooglePlayServicesAvailable() {
        // Check that Google Play services is available
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d("Location Updates", "Google Play services is available.");
            return true;
        } else {
            // Get the error dialog from Google Play services
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                    CONNECTION_FAILURE_RESOLUTION_REQUEST);

            // If Google Play services can provide an error dialog
            if (errorDialog != null) {
                // Create a new DialogFragment for the error dialog
                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
                errorFragment.setDialog(errorDialog);
                errorFragment.show(getChildFragmentManager(), "Location Updates");
            }

            return false;
        }
    }*/

    private void showEarthquake(Earthquake eq, boolean doZoom) {
        try {
            float lat = eq.getLatitude();
            float lon = eq.getLongitude();
            float rad = computeEarthquakeRadius(eq.getMagnitude());
            dropMarker(lat, lon, BitmapDescriptorFactory.HUE_RED, eq.getId(), eq.getTime().toGMTString() + "\n" + eq.getMagnitude());
            dropCircleEffect(lat, lon, rad * 1000);
            if (doZoom) zoomTo(lat, lon, 7);
        } catch (Exception e) {
            Log.e(this.getLocalClassName(), "unable to put marker for earthquake: " + eq, e);
        }
    }

    public void showEarthquakes(List<Earthquake> earthquakes) {
        for (Earthquake eq : earthquakes) showEarthquake(eq, false);
        zoomTo(0, 0, 0);
    }

    private void showPerson(Person per) {
        dropMarker(per.getLatitude(), per.getLongitude(), BitmapDescriptorFactory.HUE_BLUE, per.getName(), per.getLocation());
    }


    public void showPersons(List<Person> persons) {
        for (Person per : persons) showPerson(per);
    }

    private void zoomTo(float lat, float lon, float zoom) {
        LatLng latLng = new LatLng(lat, lon);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
        map.animateCamera(cameraUpdate);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_back_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_back:
                // User chose the "Back Button" item. Log it
                android.util.Log.d(this.getClass().getSimpleName(), "back button selected");
                finish();
                return true;

            default:
                // If we get here, the user's action is not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void showImpactedPersons(Activity activity, Map<String, String> params) {
        try {
            MethodInformation mi = new MethodInformation(activity, getString(R.string.defaultURL) + "persons", "mapPersons",
                    params);
            AsyncCollectionConnect ac = (AsyncCollectionConnect) new AsyncCollectionConnect().execute(mi);
        } catch (Exception ex) {
            android.util.Log.w(this.getClass().getSimpleName(), "Exception creating adapter: " +
                    ex.getMessage());
        }
    }

    // References:
    // http://www.aees.org.au/wp-content/uploads/2015/12/Paper_134.pdf
    // http://www.cqsrg.org/tools/perceptionradius/
    // http://seismo.cqu.edu.au/CQSRG/PerceptionRadius/
    private float computeEarthquakeRadius(float mag) {
        return (float) Math.exp((mag - 0.13) / 1.01);
    }

}