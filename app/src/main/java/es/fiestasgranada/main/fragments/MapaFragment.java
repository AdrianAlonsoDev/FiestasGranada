package es.fiestasgranada.main.fragments;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import es.fiestasgranada.main.R;
import es.fiestasgranada.main.local.LocalManagement;
import es.fiestasgranada.main.util.DirectionParser;
import es.fiestasgranada.main.util.ImageCoverter;

public class MapaFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {


    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    public static boolean routaNecesitada = false;

    // Used for selecting the current place.
    private static final int M_MAX_ENTRIES = 5;
    // A default location (Granada, Spain) and default zoom to use when location permission is
    // not granted.
    private static final LatLng PEDROANTONIO = new LatLng(37.177, -3.609);
    //Context used to Attach.
    public static Context context;
    //Map calls
    private static GoogleMap mMap;
    ImageView imgmarker;
    ImageView iconM;
    private final LatLng mOrigin = PEDROANTONIO;
    View bottomSheet;
    TextView txtnombre_local, txtDescripcion, txtDireccion;
    //Managers and calls of images
    ImageCoverter convertidor = new ImageCoverter();
    private CameraPosition mCameraPosition;

    // The entry point to the Places API.
    private PlacesClient mPlacesClient;
    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private boolean mLocationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;
    //Resources
    LinearLayout tapactionlayout;
    private LatLng mDestination;
    public static int idDest = 0;
    private int id;

    private BottomSheetBehavior mBottomSheetBehavior1;

    public MapaFragment() {
        // Required empty public constructor
    }

    /**
     * Saves the state of the map when the activity is paused.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    public static String buildRequestUrl(LatLng origin, LatLng destination) {
        String strOrigin = "origin=" + origin.latitude + "," + origin.longitude;
        String strDestination = "destination=" + destination.latitude + "," + destination.longitude;
        String sensor = "sensor=false";
        String mode = "mode=driving";

        String param = strOrigin + "&" + strDestination + "&" + sensor + "&" + mode;
        String output = "json";
        String APIKEY = context.getResources().getString(R.string.google_maps_key);

        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + param + "&key=" + APIKEY;
        Log.d("TAG", url);
        return url;
    }

    /**
     * Request direction from Google Direction API
     *
     * @param requestedUrl see {@link #buildRequestUrl(LatLng, LatLng)}
     * @return JSON data routes/direction
     */
    private static String requestDirection(String requestedUrl) {
        String responseString = "";
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(requestedUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            inputStream = httpURLConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);

            StringBuffer stringBuffer = new StringBuffer();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }
            responseString = stringBuffer.toString();
            bufferedReader.close();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        httpURLConnection.disconnect();
        return responseString;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        id = (Integer) marker.getTag();

        convertidor.donwload(getContext(), LocalManagement.mValues.get(id).getURLImagen(), imgmarker);
        // convertidor.donwload(getApplicationContext(),LocalManagement.mValues.get(id).getURLIcono(),icon);
        txtnombre_local.setText(LocalManagement.mValues.get(id).getTitulo());
        txtDescripcion.setText(LocalManagement.mValues.get(id).getDescripcion());
        txtDireccion.setText(LocalManagement.mValues.get(id).getFecha());
        mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_EXPANDED);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(LocalManagement.mValues.get(id).getLatitud(),
                        LocalManagement.mValues.get(id).getLongitud()), 17)); //18 is ZOOM

        /*Glide.with(this)
                .asBitmap()
                .load(LocalManagement.mValues.get(id).getURLIcono()) //Or URLImagen
                .into(new SimpleTarget<Bitmap>(200, 200) {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        marker.setIcon(BitmapDescriptorFactory.fromBitmap(resource));
                    }
                });*/

        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mapa, container, false);

        // Build the map.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        context = getContext();

        //Initialize and Assign Variable
        BottomNavigationView bottomNavigationView = view.findViewById(R.id.bottom_nav);

        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        // Retrieve the content view that renders the map.

        // Construct a PlacesClient
        Places.initialize(getContext(), getString(R.string.google_maps_key));
        mPlacesClient = Places.createClient(getContext());

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        imgmarker = view.findViewById(R.id.ImgMarker);
        iconM = view.findViewById(R.id.iconM);

        txtnombre_local = view.findViewById(R.id.txtNombreLocal);
        txtDescripcion = view.findViewById(R.id.txtDescripcion);
        txtDireccion = view.findViewById(R.id.txtDireccion);
        tapactionlayout = view.findViewById(R.id.tap_action_layout);
        bottomSheet = view.findViewById(R.id.bottomJsoft);
        mBottomSheetBehavior1 = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior1.setPeekHeight(120);
        mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mBottomSheetBehavior1.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    tapactionlayout.setVisibility(View.VISIBLE);
                }
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    tapactionlayout.setVisibility(View.GONE);
                }
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    tapactionlayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });

        tapactionlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBottomSheetBehavior1.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });
        return view;
    }

    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                mMap.getUiSettings().setMapToolbarEnabled(true);

            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        MapStyleOptions mapStyleOptions = MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.map_style);
        map.setMapStyle(mapStyleOptions);
        //JUST IN CASE IT CRASHES
        try {
            Thread.sleep(600);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    for (int i = 0; i < LocalManagement.mValues.size(); i++) {
                        final int finalI = i;

                        //Usa glide para descargar la imagen y convertirla en Bitmap (resource) para que pueda sustituir al Maker
                        //por defecto de Google
                        Glide.with(getContext())
                                .asBitmap()
                                .load(LocalManagement.mValues.get(i).getURLIcono()) //Or URLImagen
                                .into(new SimpleTarget<Bitmap>(180, 180) {
                                    @Override
                                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                        final Marker marcador = mMap.addMarker(new MarkerOptions().position(new LatLng(LocalManagement.mValues.get(finalI).getLatitud(), LocalManagement.mValues.get(finalI).getLongitud()))
                                                .title(LocalManagement.mValues.get(finalI).getTitulo())
                                                .snippet(LocalManagement.mValues.get(finalI).getDescripcion()).icon(BitmapDescriptorFactory.fromBitmap(resource)));
                                        marcador.setTag(finalI);
                                    }
                                });
                    }

                } catch (Exception ignored) {

                }
            }
        }).start();
        //LISTENER
        mMap.setOnMarkerClickListener(this);

        //MAKE ROUTES
        //new TaskDirectionRequest().execute(buildRequestUrl(mOrigin, mDestination));

        if (routaNecesitada = true) {
            LatLng destino = new LatLng(LocalManagement.mValues.get(idDest).getLatitud(), LocalManagement.mValues.get(idDest).getLongitud());
            hacerRuta(mOrigin, destino);
            routaNecesitada = false;
        }
        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();


    }

    public void hacerRuta(LatLng origin, LatLng destination) {

        new TaskDirectionRequest().execute(buildRequestUrl(origin, destination));
    }

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            if (mLastKnownLocation != null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(mLastKnownLocation.getLatitude(),
                                                mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            }
                        } else {
                            // Log.d(TAG, "Current location is null. Using defaults.");
                            //Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(PEDROANTONIO, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    @Override
    public void onStart() {
        Fragment f = getChildFragmentManager().findFragmentById(R.id.map);
        super.onStart();
        f.onStart();
    }

    @Override
    public void onResume() {
        Fragment f = getChildFragmentManager().findFragmentById(R.id.map);
        super.onResume();
        f.onResume();
    }

    @Override
    public void onPause() {
        Fragment f = getChildFragmentManager().findFragmentById(R.id.map);
        super.onPause();
        f.onPause();
    }

    @Override
    public void onStop() {
        Fragment f = getChildFragmentManager().findFragmentById(R.id.map);
        super.onStop();
        f.onStop();
    }

    @Override
    public void onLowMemory() {
        Fragment f = getChildFragmentManager().findFragmentById(R.id.map);
        super.onLowMemory();
        f.onLowMemory();
    }

    @Override
    public void onDestroyView() {
        Fragment f = getChildFragmentManager().findFragmentById(R.id.map);

        if (f != null)
            getChildFragmentManager().beginTransaction()
                    .remove(f).commitAllowingStateLoss();

        super.onDestroyView();
    }

    //Get JSON data from Google Direction
    public static class TaskDirectionRequest extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String responseString = "";
            try {
                responseString = requestDirection(strings[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String responseString) {
            super.onPostExecute(responseString);
            //Json object parsing
            TaskParseDirection parseResult = new TaskParseDirection();
            parseResult.execute(responseString);
        }
    }

    //Parse JSON Object from Google Direction API & display it on Map
    public static class TaskParseDirection extends AsyncTask<String, Void, List<List<HashMap<String, String>>>> {
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonString) {
            List<List<HashMap<String, String>>> routes = null;
            JSONObject jsonObject = null;

            try {
                jsonObject = new JSONObject(jsonString[0]);
                DirectionParser parser = new DirectionParser();
                routes = parser.parse(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return routes;
        }

        public static final int PATTERN_DASH_LENGTH_PX = 20;
        public static final int PATTERN_GAP_LENGTH_PX = 10;
        public final PatternItem DOT = new Dot();
        public final PatternItem DASH = new Dash(PATTERN_DASH_LENGTH_PX);
        public final PatternItem GAP = new Gap(PATTERN_GAP_LENGTH_PX);
        public final List<PatternItem> PATTERN_POLYGON_ALPHA = Arrays.asList(DOT, GAP);

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {

            super.onPostExecute(lists);
            ArrayList points = null;
            PolylineOptions polylineOptions = null;

            for (List<HashMap<String, String>> path : lists) {
                points = new ArrayList();
                polylineOptions = new PolylineOptions();

                for (HashMap<String, String> point : path) {
                    double lat = Double.parseDouble(point.get("lat"));
                    double lon = Double.parseDouble(point.get("lng"));

                    points.add(new LatLng(lat, lon));

                }
                polylineOptions.addAll(points);
                polylineOptions.width(14f);
                polylineOptions.color(Color.argb(240, 100, 0, 0));
                polylineOptions.geodesic(true);
                polylineOptions.pattern(PATTERN_POLYGON_ALPHA);

            }
            if (polylineOptions != null) {
                mMap.addPolyline(polylineOptions);
            } else {
                //Toast.makeText(getContext(), "Direction not found", Toast.LENGTH_LONG).show();
            }
        }
    }

}
