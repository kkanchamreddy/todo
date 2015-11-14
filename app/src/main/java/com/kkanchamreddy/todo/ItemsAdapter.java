package com.kkanchamreddy.todo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by kkanchamreddy on 11/10/2015.
 */
public class ItemsAdapter extends ArrayAdapter<Item> {

    private static class ViewHolder {
        TextView tvItemText;
        TextView tvPriority;
    }

    public ItemsAdapter(Context context, ArrayList<Item> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Item item = getItem(position);

        ViewHolder viewHolder = new ViewHolder();
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }

        viewHolder.tvItemText = (TextView) convertView.findViewById(R.id.tvName);
        viewHolder.tvPriority = (TextView) convertView.findViewById(R.id.tvPriority);

        convertView.setTag(viewHolder);

        viewHolder.tvItemText.setText(item.text);
        viewHolder.tvPriority.setText(item.priority);
       
        // Return the completed view to render on screen
        return convertView;
    }
}
