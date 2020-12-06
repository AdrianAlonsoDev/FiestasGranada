package es.fiestasgranada.main.fragments;

import android.annotation.SuppressLint;
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
import es.fiestasgranada.main.management.LocalManagement;
import es.fiestasgranada.main.util.DirectionParser;
import es.fiestasgranada.main.util.ImageConverter;

public class MapaFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    // Keys para guardar la posición de la camara y localidad.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    //Permisos garantizados o no por el dispositivo.
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    //Lugar por defecto (Granada, Spain) y zoom por defecto cuando el permiso no ha sido dado.
    private static final LatLng PEDROANTONIO = new LatLng(37.177, -3.609);
    private static final int DEFAULT_ZOOM = 15;
    public static int idDest;
    //Llamadas al mapa.
    private static GoogleMap mMap;
    //TAG para del bug y buscarlo en LOGCAT.
    private final String TAG = "DEBUG";
    //Rutas de los locales con Directions.
    public LatLng mOrigin;
    //Managers y llamadas de las imágenes
    ImageConverter convertidor = new ImageConverter();
    private LinearLayout dirSheet;
    //Resources
    LinearLayout tapactionlayout;
    private LinearLayout descSheet;
    private boolean mLocationPermissionGranted;
    private LatLng mDestination;
    //Context usado para Attach.
    private Context context;
    // El punto de entrada de Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;
    /* El lugar geofráfico donde se encuentra el dispositivo. Es decir,
     * el último lugar recibido por el  Fused Location Provider. */
    private Location mLastKnownLocation;
    //Para controlar la ventana deslizante de información de los locales.
    private BottomSheetBehavior<View> mBottomSheetBehavior1;
    //Viewbinding que sustituye a findViewById
    private FragmentMapaBinding binding;
    //Llamada y manager de view.
    private View view;
    private boolean clickMarcador = false;

    private boolean expandido = false;


    public MapaFragment() {
        /* Constructor vacío necesario obligatorio
         * en todos los fragments*/
    }

    /**
     * Pedir dirección desde Google Direction API
     *
     * @param requestedUrl mirar {@link #buildRequestUrl(LatLng, LatLng)}
     * @return JSON data rutas/dirección
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
        if (httpURLConnection != null)
            httpURLConnection.disconnect();
        return responseString;
    }

    /**
     * Guarda el estado del mapa cuando la actividad es pausada .
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Infla la layout para el fagmento con View binding.
        binding = FragmentMapaBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        // Construye el mapa.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        //Attach context
        context = getContext();

        // Recibe el lugar y la posición de la camara guardada en la instancia.
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
        }


        // Construye un PlacesClient
        if (context != null) {
            Places.initialize(context, getString(R.string.google_maps_key));
            // Construye un FusedLocationProviderClient.
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        }

        //Controlador de la ventana deslizante de información.
        View bottomSheet = binding.bottomJsoft.bottomSheet1;
        mBottomSheetBehavior1 = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior1.setPeekHeight(120);
        mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mBottomSheetBehavior1.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    binding.bottomJsoft.bottomSheet1.setBackgroundResource(R.drawable.corner_radius);
                    expandido = false;
                    if (!clickMarcador) {
                        binding.bottomJsoft.tapActionLayoutLocal.setVisibility(View.GONE);
                        binding.bottomJsoft.MainSheet.setVisibility(View.VISIBLE);
                        binding.bottomJsoft.tapActionLayout.setVisibility(View.VISIBLE);
                        binding.bottomJsoft.descSheet.setVisibility(View.VISIBLE);
                        binding.bottomJsoft.dirSheet.setVisibility(View.VISIBLE);
                        Log.d(TAG, "Collapsed No Review");


                    } else if (clickMarcador) {

                        binding.bottomJsoft.descSheet.setVisibility(View.GONE);
                        binding.bottomJsoft.dirSheet.setVisibility(View.GONE);
                        binding.bottomJsoft.MainSheet.setVisibility(View.GONE);
                        binding.bottomJsoft.tapActionLayout.setVisibility(View.GONE);
                        binding.bottomJsoft.tapActionLayoutLocal.setVisibility(View.VISIBLE);

                        Log.d(TAG, "Collapsed Review de click en marcador");
                    }
                }
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    expandido = true;
                    binding.bottomJsoft.MainSheet.setVisibility(View.VISIBLE);
                    if (clickMarcador) {
                        binding.bottomJsoft.descSheet.setVisibility(View.VISIBLE);
                        binding.bottomJsoft.dirSheet.setVisibility(View.VISIBLE);
                        binding.bottomJsoft.tapActionLayoutLocal.setVisibility(View.VISIBLE);
                        binding.bottomJsoft.tapActionLayout.setVisibility(View.GONE);
                        Log.d(TAG, "Expanded Review de click en marcador");

                    } else {
                        binding.bottomJsoft.tapActionLayout.setVisibility(View.VISIBLE);
                        binding.bottomJsoft.tapActionLayoutLocal.setVisibility(View.GONE);
                        binding.bottomJsoft.descSheet.setVisibility(View.GONE);
                        binding.bottomJsoft.dirSheet.setVisibility(View.GONE);
                        Log.d(TAG, "Expanded No Review");

                    }
                    binding.bottomJsoft.bottomSheet1.setBackgroundResource(R.drawable.corner_radius_opened);

                }
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    binding.bottomJsoft.MainSheet.setVisibility(View.VISIBLE);
                    expandido = true;
                    if (clickMarcador) {
                        binding.bottomJsoft.tapActionLayout.setVisibility(View.GONE);
                        binding.bottomJsoft.tapActionLayoutLocal.setVisibility(View.VISIBLE);
                        binding.bottomJsoft.descSheet.setVisibility(View.VISIBLE);
                        binding.bottomJsoft.dirSheet.setVisibility(View.VISIBLE);
                        Log.d(TAG, "Dragging Review de click en marcador");

                    } else {

                        binding.bottomJsoft.tapActionLayout.setVisibility(View.VISIBLE);
                        binding.bottomJsoft.tapActionLayoutLocal.setVisibility(View.GONE);
                        binding.bottomJsoft.descSheet.setVisibility(View.GONE);
                        binding.bottomJsoft.dirSheet.setVisibility(View.GONE);
                        Log.d(TAG, "Dragging No Review ");

                    }
                    binding.bottomJsoft.bottomSheet1.setBackgroundResource(R.drawable.corner_radius_opened);
                }
            }

            @Override
            public void onSlide(View bottomSheet, float slideOffset) {
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @SuppressLint("PotentialBehaviorOverride")
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        MapStyleOptions mapStyleOptions = MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style);
        map.setMapStyle(mapStyleOptions);

        new Thread(() -> {
            try {

                for (int i = 0; i < LocalManagement.mValues.size(); i++) {
                    final int finalI = i;

                    /** Usa glide para descargar la imagen y convertirla en Bitmap (resource)
                     para que pueda sustituir al Maker por defecto de Google */
                    Glide.with(context)
                            .asBitmap()
                            .load(LocalManagement.mValues.get(i).getURLIcono()) //Or URLImagen
                            .into(new CustomTarget<Bitmap>(180, 180) {
                                @Override
                                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                    final Marker marcador = mMap.addMarker(new MarkerOptions().position(new LatLng(LocalManagement.mValues.get(finalI).getLatitud(), LocalManagement.mValues.get(finalI).getLongitud()))
                                            .title(LocalManagement.mValues.get(finalI).getTitulo())
                                            .snippet(LocalManagement.mValues.get(finalI).getDescripcion()).icon(BitmapDescriptorFactory.fromBitmap(resource)));
                                    marcador.setTag(finalI);
                                }

                                @Override
                                public void onLoadCleared(Drawable placeholder) {
                                }
                            });
                }

            } catch (Exception ignored) {

            }
        }).start();

        //Listener de mMap al hacer click.
        mMap.setOnMarkerClickListener(this);

        //Haer rutas con TaskDirectionRequest se necesitan Origen y Destino.
        //new TaskDirectionRequest().execute(buildRequestUrl(mOrigin, mDestination));


        // Pide al usuario permiso.
        getLocationPermission();

        //Activa la capa de Localidad y control del mapa
        updateLocationUI();

        // Obtiene la ubicación del dispositivo y la actualiza en  el mapa.
        getDeviceLocation();

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        int id = 0;
        if (marker.getTag() != null) {
            id = (Integer) marker.getTag();
        } else {
            Log.d(TAG, "MapaFragemnt -> onMarkerClick -> Marker Tag is null l:321");
        }

        clickMarcador = true;

        convertidor.download(getContext(), LocalManagement.mValues.get(id).getURLImagen(), binding.bottomJsoft.ImgMarker);
        Glide.with(context)
                .asBitmap()
                .load(LocalManagement.mValues.get(id).getURLIcono()) //Or URLImagen

                .into(new CustomTarget<Bitmap>(140, 140) {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        binding.bottomJsoft.tapActionIconM.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadCleared(Drawable placeholder) {
                    }
                });

        //convertidor.download(getContext(),LocalManagement.mValues.get(id).getURLImagen(), binding.bottomJsoft.tapActionIconM);
        binding.bottomJsoft.txtNombreLocal.setText(LocalManagement.mValues.get(id).getTitulo());
        binding.bottomJsoft.txtNombreLocal.setTextColor(getResources().getColor(R.color.quantum_white_100));
        binding.bottomJsoft.txtDescripcion.setText(LocalManagement.mValues.get(id).getDescripcion());
        binding.bottomJsoft.txtDireccion.setText(LocalManagement.mValues.get(id).getDireccion());

        if (expandido && clickMarcador) {
            binding.bottomJsoft.MainSheet.setVisibility(View.VISIBLE);
            binding.bottomJsoft.descSheet.setVisibility(View.VISIBLE);
            binding.bottomJsoft.dirSheet.setVisibility(View.VISIBLE);
        } else {
            binding.bottomJsoft.MainSheet.setVisibility(View.GONE);
            binding.bottomJsoft.descSheet.setVisibility(View.GONE);
            binding.bottomJsoft.dirSheet.setVisibility(View.GONE);
        }

        binding.bottomJsoft.tapActionLayout.setVisibility(View.GONE);
        binding.bottomJsoft.tapActionLayoutLocal.setVisibility(View.VISIBLE);
        binding.bottomJsoft.tapActionName.setText(LocalManagement.mValues.get(id).getTitulo());

        mBottomSheetBehavior1.setPeekHeight(340);

        if (LocalManagement.mValues.get(id).isAbierto().equals("si")) {
            binding.bottomJsoft.tapActionEstado.setText("Abierto");
            binding.bottomJsoft.tapActionEstado.setTextColor(getResources().getColor(R.color.lightGreen));
            Log.e(TAG, "onMarkerClick: ABIERTO");
        } else {
            binding.bottomJsoft.tapActionEstado.setText("Cerrado");
            binding.bottomJsoft.tapActionEstado.setTextColor(getResources().getColor(R.color.lightRed));
            Log.e(TAG, "onMarkerClick: CERRADO");

        }
        // mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_EXPANDED);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(LocalManagement.mValues.get(id).getLatitud(),
                        LocalManagement.mValues.get(id).getLongitud()), 17)); //18 is ZOOM

        return false;
    }

    @Override
    public void onStart() {
        Fragment f = getChildFragmentManager().findFragmentById(R.id.map);
        super.onStart();
        if (f != null)
            f.onStart();
    }

    @Override
    public void onResume() {
        Fragment f = getChildFragmentManager().findFragmentById(R.id.map);
        super.onResume();
        if (f != null)
            f.onResume();
    }

    @Override
    public void onPause() {
        Fragment f = getChildFragmentManager().findFragmentById(R.id.map);
        super.onPause();
        if (f != null)
            f.onPause();
    }

    @Override
    public void onStop() {
        binding = null;
        view = null;
        Fragment f = getChildFragmentManager().findFragmentById(R.id.map);
        super.onStop();
        if (f != null)
            f.onStop();
    }

    @Override
    public void onLowMemory() {
        Fragment f = getChildFragmentManager().findFragmentById(R.id.map);
        super.onLowMemory();
        if (f != null)
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

    /**
     * Maneja el resultado de solicitar el permiso de Ubicación.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        mLocationPermissionGranted = false;
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
            }
        }
        updateLocationUI();
    }

    /**
     * Actualiza las opciones de ID del mapa basado
     * si el usuario ha dado permisos o no.
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
            Log.d(TAG, "MapFrg->dException: %s " + e.getMessage());
        }
    }

    public void hacerRuta(LatLng origin, LatLng destination) {
        new TaskDirectionRequest().execute(buildRequestUrl(origin, destination));
    }

    //TO-DO Make this a setting for users to choose driving or walking routes.
    public String buildRequestUrl(LatLng origin, LatLng destination) {
        String strOrigin = "origin=" + origin.latitude + "," + origin.longitude;
        String strDestination = "destination=" + destination.latitude + "," + destination.longitude;
        String sensor = "sensor=false";
        String mode = "mode=transit"; //walking, transit,driving,bicycling

        String param = strOrigin + "&" + strDestination + "&" + sensor + "&" + mode;
        String output = "json";
        String APIKEY = context.getResources().getString(R.string.google_maps_key);

        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + param + "&key=" + APIKEY;
        Log.d("TAG", url);
        return url;
    }

    /**
     * Le pide al usuario permiso para usar la ubicación.
     */
    private void getLocationPermission() {
        /*
         * Le solicita permiso de ubicación, para poder obtener el lugar donde
         * se encuentra el dispotivo. El resultado del solicitado de permiso es
         * manejado por un callback onRequestPermissionsResult
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(context,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }


    /**
     * Obtiene la ubicación actual del dispositivo y posiciona la camara del mapa.
     */
    private void getDeviceLocation() {
        /*
         * Obtiene la mejor y más reciente ubicación del dispositivo of the device,
         * que puede ser null en casos raros donde la ubicación no está disponible.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(requireActivity(), new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Pone la posición de camara del mapa a la ubicación actual del dispositivo.
                            mLastKnownLocation = task.getResult();
                            if (mLastKnownLocation != null) {
                                Log.d(TAG, " MapaFragemnt -> getDeviceLocation() -> onComplete: Succesfully got device location!");
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(mLastKnownLocation.getLatitude(),
                                                mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                                mOrigin = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());

                                if (TaskDirectionRequest.routaNecesitada = true) {
                                    LatLng destino = new LatLng(LocalManagement.mValues.get(idDest).getLatitud(), LocalManagement.mValues.get(idDest).getLongitud());
                                    hacerRuta(mOrigin, destino);
                                    TaskDirectionRequest.routaNecesitada = false;
                                }
                            }
                        } else {
                            Log.d(TAG, " MapaFragemnt -> getDeviceLocation() -> onComplete: La ubicación actual es null. Usando defaults.");
                            Log.e(TAG, " Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(PEDROANTONIO, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(" MapaFragm>Exception:%s", e.getMessage());
        }
    }


    //Obtiene JSON data desde Google Direction
    public static class TaskDirectionRequest extends AsyncTask<String, Void, String> {
        public static boolean routaNecesitada = true;

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

        //Parsea JSON Object desde Google Direction API & muestralo en el mapa.
        public static class TaskParseDirection extends AsyncTask<String, Void, List<List<HashMap<String, String>>>> {
            public static final int PATTERN_DASH_LENGTH_PX = 20;
            public static final int PATTERN_GAP_LENGTH_PX = 10;
            public final PatternItem DOT = new Dot();
            public final PatternItem DASH = new Dash(PATTERN_DASH_LENGTH_PX);
            public final PatternItem GAP = new Gap(PATTERN_GAP_LENGTH_PX);
            public final List<PatternItem> PATTERN_POLYGON_ALPHA = Arrays.asList(DOT, GAP);

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

            @Override
            protected void onPostExecute(List<List<HashMap<String, String>>> lists) {

                super.onPostExecute(lists);

                PolylineOptions polylineOptions = null;

                for (List<HashMap<String, String>> path : lists) {
                    ArrayList<LatLng> points = new ArrayList<>();
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
                    Log.e("DEBUG", "MapaFragment -> onPostExecute: -> polylineOptions es null");
                }
            }
        }

    }
}
