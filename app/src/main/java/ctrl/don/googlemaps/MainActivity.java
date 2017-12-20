package ctrl.don.googlemaps;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import android.media.Image;
import android.os.Bundle;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String DUBLIN = "DUBLIN";
    public static final String TOKYO = "TOKYO";
    public static final String TOKYO1 = "TOKYO1";
    public static final String TOKYO2 = "TOKYO2";
    public static final String TOKYO3 = "TOKYO3";
    public static final String SEATTLE = "SEATTLE";
    public static final String NEWYORK = "NEWYORK";
    boolean mapReady = false;


    LatLng dublin = new LatLng(53.3478, -6.2597);
    LatLng seattle = new LatLng(47.6204, -122.3491);
    LatLng newYork = new LatLng(40.784, -73.9857);
    LatLng tokyo = new LatLng(35.6895, 139.6917);
    LatLng tokyo1 = new LatLng(35.6895, 139.6917);
    LatLng tokyo2 = new LatLng(35.6895, 139.6917);
    LatLng tokyo3 = new LatLng(35.6895, 139.6917);


    private ViewGroup infoWindow;
    private TextView infoTitle;
    private ImageView infoImage;
    private Button infoButton;
    private OnInfoWindowElemTouchListener infoButtonListener;

    GoogleMap mMaps;


    //set three button variable
    Button btnMap, btnSatellite, btnHybrid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
//
//        btnMap = (Button) findViewById(R.id.btn_map);
//        btnMap.setOnClickListener(this);
//
//        btnSatellite = (Button) findViewById(R.id.btn_satelite);
//        btnSatellite.setOnClickListener(this);
//
//        btnHybrid = (Button) findViewById(R.id.btn_hybrid);
//        btnHybrid.setOnClickListener(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapReady = true;
        mMaps = googleMap;
        mMaps.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMaps.getUiSettings().setZoomControlsEnabled(true);

        mMaps.addMarker(myCustomMarker(dublin, DUBLIN));
        mMaps.addMarker(myCustomMarker(tokyo, TOKYO));
        mMaps.addMarker(myCustomMarker(tokyo1, TOKYO1));
        mMaps.addMarker(myCustomMarker(tokyo2, TOKYO2));
        mMaps.addMarker(myCustomMarker(tokyo3, TOKYO3));
        mMaps.addMarker(myCustomMarker(seattle, SEATTLE));
        mMaps.addMarker(myCustomMarker(newYork, NEWYORK));


        //final MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
        final MapWrapperLayout mapWrapperLayout = (MapWrapperLayout) findViewById(R.id.map_relative_layout);
        //final GoogleMap map = mapFragment.getMap();

        // MapWrapperLayout initialization
        // 39 - default marker height
        // 20 - offset between the default InfoWindow bottom edge and it's content bottom edge
        mapWrapperLayout.init(googleMap, getPixelsFromDp(this, 39 + 20));


        // We want to reuse the info window for all the markers,
        // so let's create only one class member instance
        infoWindow = (ViewGroup) getLayoutInflater().inflate(R.layout.item_info_window, null);
        infoTitle = (TextView) infoWindow.findViewById(R.id.tv_title);
        infoImage = (ImageView) infoWindow.findViewById(R.id.iv_info);
        infoButton = (Button) infoWindow.findViewById(R.id.btn_order);
        Picasso.with(getApplicationContext())
                .load(R.drawable.foto)
                .resize(100, 100)
                .centerCrop()
                .into(infoImage);

        // Setting custom OnTouchListener which deals with the pressed state
        // so it shows up
        infoButtonListener =
                new OnInfoWindowElemTouchListener(infoButton,
                        getResources().getDrawable(R.drawable.round_but_green_sel), //btn_default_normal_holo_light
                        getResources().getDrawable(R.drawable.round_but_red_sel)) //btn_default_pressed_holo_light
                {
                    @Override
                    protected void onClickConfirmed(View v, Marker marker) {
                        // Here we can perform some action triggered after clicking the button
                        Toast.makeText(MainActivity.this, marker.getTitle() + "'s button clicked!", Toast.LENGTH_SHORT).show();
                    }
                };
        infoButton.setOnTouchListener(infoButtonListener);
        googleMap.setInfoWindowAdapter(new InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                // Setting up the infoWindow with current's marker info
                infoTitle.setText(marker.getTitle());

                infoButtonListener.setMarker(marker);

                // We must call this to set the current marker and infoWindow references
                // to the MapWrapperLayout
                mapWrapperLayout.setMarkerWithInfoWindow(marker, infoWindow);
                return infoWindow;
            }
        });

        flyTo(myCustomLocation(newYork, 15, 90));
    }


    public static int getPixelsFromDp(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }


    private void flyTo(CameraPosition target) {
//        m_map.animateCamera(CameraUpdateFactory.newCameraPosition(target));
        mMaps.animateCamera(CameraUpdateFactory.newCameraPosition(target), 5000, null);
    }

    @NonNull
    private CameraPosition myCustomLocation(LatLng latLng, float zoom, float bearing) {
        return CameraPosition.builder()
                .target(latLng)
                .zoom(zoom)
                .bearing(bearing)
                .build();
    }

    @NonNull
    private MarkerOptions myCustomMarker(LatLng latLng, String title) {
        return new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher))
                .title(title);
    }

}
