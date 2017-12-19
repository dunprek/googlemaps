package ctrl.don.googlemaps;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by GideonST on 12/19/2017.
 */

class PopupAdapter implements GoogleMap.InfoWindowAdapter {
    private View popup = null;
    private LayoutInflater inflater = null;

    PopupAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return (null);
    }

    @SuppressLint("InflateParams")
    @Override
    public View getInfoContents(Marker marker) {
        if (popup == null) {
            popup = inflater.inflate(R.layout.item_info_window, null);
        }

        TextView tv = (TextView) popup.findViewById(R.id.tv_title);
        tv.setText(marker.getTitle());

        return (popup);
    }
}

