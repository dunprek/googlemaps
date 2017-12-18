package ctrl.don.googlemaps;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,GoogleMap.OnMarkerClickListener {

    public  static final String DUBLIN = "DUBLIN";
    public  static final String TOKYO = "TOKYO";
    public  static final String SEATTLE = "SEATTLE";
    public  static final String NEWYORK = "NEWYORK";
    GoogleMap m_map;
    boolean mapReady = false;


    LatLng dublin = new LatLng(53.3478, -6.2597);
    LatLng seattle = new LatLng(47.6204, -122.3491);
    LatLng newYork = new LatLng(40.784, -73.9857);
    LatLng tokyo = new LatLng(35.6895, 139.6917);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



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

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapReady = true;
        m_map = googleMap;
        m_map.setOnMarkerClickListener(this);
        m_map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        m_map.addMarker(myCustomMarker(dublin,DUBLIN));
        m_map.addMarker(myCustomMarker(tokyo,TOKYO));
        m_map.addMarker(myCustomMarker(seattle,SEATTLE));
        m_map.addMarker(myCustomMarker(newYork,NEWYORK));


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
        switch (marker.getTitle()){
            case TOKYO:
                Toast.makeText(MainActivity.this,"TOKYO",Toast.LENGTH_SHORT).show();
                break;
            case DUBLIN:
                Toast.makeText(MainActivity.this,"DUBLIN",Toast.LENGTH_SHORT).show();
                break;
            case NEWYORK:
                Toast.makeText(MainActivity.this,"NEWYORK",Toast.LENGTH_SHORT).show();
                break;
            case SEATTLE:
                Toast.makeText(MainActivity.this,"SEATTLE",Toast.LENGTH_SHORT).show();

                break;
        }
        return false;
    }


    
}
