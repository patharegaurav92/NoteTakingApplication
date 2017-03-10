package edu.csulb.android.notetakingapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Gaurav on 09-03-2017.
 */

public class PhotoAdapter extends ArrayAdapter<String> {
    public PhotoAdapter(Context context, ArrayList<String> photo_names) {
        super(context,0,photo_names);

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }
        String photo = getItem(position);
        TextView photoItem = (TextView) listItemView.findViewById(R.id.photo_item_text_view);
        photoItem.setText(photo);
        return listItemView;
    }
}
