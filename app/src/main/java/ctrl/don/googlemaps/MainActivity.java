package ctrl.don.googlemaps;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnInfoWindowClickListener, OnMapReadyCallback {

    public static final String DUBLIN = "DUBLIN";
    public static final String TOKYO = "TOKYO";
    public static final String SEATTLE = "SEATTLE";
    public static final String NEWYORK = "NEWYORK";
    GoogleMap m_map;
    boolean mapReady = false;


    LatLng dublin = new LatLng(53.3478, -6.2597);
    LatLng seattle = new LatLng(47.6204, -122.3491);
    LatLng newYork = new LatLng(40.784, -73.9857);
    LatLng tokyo = new LatLng(35.6895, 139.6917);


    private ViewGroup infoWindow;
    private TextView infoTitle;
    private TextView infoSnippet;
    private Button infoButton;
    private OnInfoWindowElemTouchListener infoButtonListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);

        final MapWrapperLayout mapWrapperLayout = (MapWrapperLayout) findViewById(R.id.map_relative_layout);

        mapWrapperLayout.init(m_map, getPixelsFromDp(this, 39 + 20));


        Button btnMap = (Button) findViewById(R.id.btn_map);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mapReady)
                    flyTo(myCustomLocation(dublin, 15, 10));
//                    m_map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }
        });

        Button btnSatellite = (Button) findViewById(R.id.btn_satelite);
        btnSatellite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mapReady)
                    flyTo(myCustomLocation(seattle, 15, 30));
//                    m_map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            }
        });

        Button btnHybrid = (Button) findViewById(R.id.btn_hybrid);
        btnHybrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mapReady)
                    flyTo(myCustomLocation(tokyo, 15, 40));

//                    m_map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            }
        });


        // We want to reuse the info window for all the markers,
        // so let's create only one class member instance
        this.infoWindow = (ViewGroup) getLayoutInflater().inflate(R.layout.item_info_window, null);
        this.infoTitle = (TextView) infoWindow.findViewById(R.id.tv_title);

        this.infoButton = (Button) infoWindow.findViewById(R.id.btn_order);
        // Setting custom OnTouchListener which deals with the pressed state
        // so it shows up
        this.infoButtonListener = new OnInfoWindowElemTouchListener(infoButton,
                getResources().getDrawable(android.R.drawable.btn_default),
                getResources().getDrawable(android.R.drawable.btn_default_small)) {
            @Override
            protected void onClickConfirmed(View v, Marker marker) {
                // Here we can perform some action triggered after clicking the button
                Toast.makeText(MainActivity.this, marker.getTitle() + "'s button clicked!", Toast.LENGTH_SHORT).show();
            }
        };
        this.infoButton.setOnTouchListener(infoButtonListener);


        m_map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                // Setting up the infoWindow with current's marker info
                infoTitle.setText(marker.getTitle());
                infoSnippet.setText(marker.getSnippet());
                infoButtonListener.setMarker(marker);

                // We must call this to set the current marker and infoWindow references
                // to the MapWrapperLayout
                mapWrapperLayout.setMarkerWithInfoWindow(marker, infoWindow);
                return infoWindow;
            }
        });


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapReady = true;
        m_map = googleMap;
        m_map.setOnMarkerClickListener(this);
        m_map.setOnInfoWindowClickListener(this);
        m_map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        m_map.addMarker(myCustomMarker(dublin, DUBLIN));
        m_map.addMarker(myCustomMarker(tokyo, TOKYO));
        m_map.addMarker(myCustomMarker(seattle, SEATTLE));
        m_map.addMarker(myCustomMarker(newYork, NEWYORK));

        flyTo(myCustomLocation(newYork, 15, 90));
    }

    private void flyTo(CameraPosition target) {
//        m_map.animateCamera(CameraUpdateFactory.newCameraPosition(target));
        m_map.animateCamera(CameraUpdateFactory.newCameraPosition(target), 5000, null);

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


    @Override
    public boolean onMarkerClick(Marker marker) {
        switch (marker.getTitle()) {
            case TOKYO:
                Toast.makeText(MainActivity.this, "TOKYO", Toast.LENGTH_SHORT).show();
                break;
            case DUBLIN:
                Toast.makeText(MainActivity.this, "DUBLIN", Toast.LENGTH_SHORT).show();
                break;
            case NEWYORK:
                Toast.makeText(MainActivity.this, "NEWYORK", Toast.LENGTH_SHORT).show();
                break;
            case SEATTLE:
                Toast.makeText(MainActivity.this, "SEATTLE", Toast.LENGTH_SHORT).show();

                break;
        }
        return false;
    }


    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    public static int getPixelsFromDp(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

}
