package es.fiestasgranada.main.fragments;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.jetbrains.annotations.NotNull;
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
import es.fiestasgranada.main.databinding.FragmentMapaBinding;
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

    // A default location (Granada, Spain) and default zoom to use when location permission is
    // not granted.
    private static final LatLng PEDROANTONIO = new LatLng(37.177, -3.609);
    //Context used to Attach.
    private Context context;
    //Map calls
    private static GoogleMap mMap;
    private final LatLng mOrigin = PEDROANTONIO;


    //Managers and calls of images
    ImageCoverter convertidor = new ImageCoverter();

    // The entry point to the Places API.
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

    private FragmentMapaBinding binding;

    private View view;

    public MapaFragment() {
        // Required empty public constructor
    }

    /**
     * Saves the state of the map when the activity is paused.
     */
    @Override
    public void onSaveInstanceState(@NotNull Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMapaBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        // Build the map.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        context = getContext();

        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
        }

        // Retrieve the content view that renders the map.

        // Construct a PlacesClient
        Places.initialize(getContext(), getString(R.string.google_maps_key));

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        View bottomSheet = binding.bottomJsoft.bottomSheet1;
        mBottomSheetBehavior1 = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior1.setPeekHeight(120);
        mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mBottomSheetBehavior1.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    binding.bottomJsoft.tapActionLayout.setVisibility(View.VISIBLE);
                }
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    binding.bottomJsoft.tapActionLayout.setVisibility(View.GONE);
                }
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    binding.bottomJsoft.tapActionLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });

        bottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBottomSheetBehavior1.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });
        return view;
    }

    @Override
    public void onAttach(@NotNull Context context) {
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

        MapStyleOptions mapStyleOptions = MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style);
        map.setMapStyle(mapStyleOptions);

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
                                .into(new CustomTarget<Bitmap>(180, 180) {
                                    @Override
                                    public void onResourceReady(@NotNull Bitmap resource, Transition<? super Bitmap> transition) {
                                        final Marker marcador = mMap.addMarker(new MarkerOptions().position(new LatLng(LocalManagement.mValues.get(finalI).getLatitud(), LocalManagement.mValues.get(finalI).getLongitud()))
                                                .title(LocalManagement.mValues.get(finalI).getTitulo())
                                                .snippet(LocalManagement.mValues.get(finalI).getDescripcion()).icon(BitmapDescriptorFactory.fromBitmap(resource)));
                                        marcador.setTag(finalI);
                                    }

                                    @Override
                                    public void onLoadCleared(@Nullable Drawable placeholder) {
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


    @Override
    public boolean onMarkerClick(Marker marker) {
        id = (Integer) marker.getTag();

        convertidor.donwload(getContext(), LocalManagement.mValues.get(id).getURLImagen(), binding.bottomJsoft.ImgMarker);
        // convertidor.donwload(getApplicationContext(),LocalManagement.mValues.get(id).getURLIcono(),icon);
        binding.bottomJsoft.txtNombreLocal.setText(LocalManagement.mValues.get(id).getTitulo());
        binding.bottomJsoft.txtDescripcion.setText(LocalManagement.mValues.get(id).getDescripcion());
        binding.bottomJsoft.txtDireccion.setText(LocalManagement.mValues.get(id).getUltimaFecha());
        mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_EXPANDED);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(LocalManagement.mValues.get(id).getLatitud(),
                        LocalManagement.mValues.get(id).getLongitud()), 17)); //18 is ZOOM

        return false;
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
            }
        }
        updateLocationUI();
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
        binding = null;
        view = null;
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

        binding = null;
        view = null;

        Fragment f = getChildFragmentManager().findFragmentById(R.id.map);

        if (f != null)
            getChildFragmentManager().beginTransaction()
                    .remove(f).commitAllowingStateLoss();

        super.onDestroyView();
    }


    public void hacerRuta(LatLng origin, LatLng destination) {

        new TaskDirectionRequest().execute(buildRequestUrl(origin, destination));
    }

    public String buildRequestUrl(LatLng origin, LatLng destination) {
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

            StringBuilder stringBuffer = new StringBuilder();
            String line;
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
            JSONObject jsonObject;

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
            ArrayList points;
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
            }  //Toast.makeText(getContext(), "Direction not found", Toast.LENGTH_LONG).show();

        }
    }

}
