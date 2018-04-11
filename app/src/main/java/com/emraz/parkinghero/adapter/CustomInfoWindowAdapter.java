package com.emraz.parkinghero.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.emraz.parkinghero.R;
import com.emraz.parkinghero.rest.model.PromotionLocation;
import com.emraz.parkinghero.util.FontManager;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by Yousuf on 7/12/2017.
 */

public class CustomInfoWindowAdapter implements InfoWindowAdapter {

    // These are both viewgroups containing an ImageView with id "badge" and two TextViews with id
    // "title" and "snippet".
    // private final View mWindow;

    private Context mContext;
    private final View mContents;


    public CustomInfoWindowAdapter(Context context) {
        this.mContext = context;
        //mWindow = mContext.getLayoutInflater().inflate(R.layout.custom_info_window, null);
        mContents = LayoutInflater.from(mContext).inflate(R.layout.custom_info_content, null);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        render(marker, mContents);
        return mContents;
    }

    private void render(Marker marker, View view) {

        String title = marker.getTitle();
        TextView titleUi = ((TextView) view.findViewById(R.id.textViewHeader));
        if (title != null) {
            // Spannable string allows us to edit the formatting of the text.
            // SpannableString titleText = new SpannableString(title);
            // titleText.setSpan(new ForegroundColorSpan(Color.RED), 0, titleText.length(), 0);
            titleUi.setText(title);
        } else {
            titleUi.setText("");
        }


        String snippet = marker.getSnippet();
        TextView snippetUi = ((TextView) view.findViewById(R.id.textViewDescription));
        FontManager.setFontType(snippetUi, FontManager.getTypeface(mContext, FontManager.RALEWAY_REGULAR));

        if (snippet != null) {
            snippetUi.setTextColor(mContext.getResources().getColor(R.color.colorGreyLight));
            if (snippet.equals("Low Availability"))
                snippetUi.setTextColor(mContext.getResources().getColor(R.color.red_marker));

            if (snippet.equals("Medium Availability"))
                snippetUi.setTextColor(mContext.getResources().getColor(R.color.yellow_marker));

            if (snippet.equals("High Availability"))
                snippetUi.setTextColor(mContext.getResources().getColor(R.color.green_marker));
            snippetUi.setText(snippet);
        } else {
            snippetUi.setText("");
        }

        if (marker.getTag() != null && marker.getTag() instanceof PromotionLocation) {
            snippetUi.setText("");
            snippetUi.setVisibility(View.GONE);
        } else {
            snippetUi.setVisibility(View.VISIBLE);
        }


    }
}