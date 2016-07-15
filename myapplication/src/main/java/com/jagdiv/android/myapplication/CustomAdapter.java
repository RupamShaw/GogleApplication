package com.jagdiv.android.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Dell on 15/07/2016.
 */
public class CustomAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Person> mList;

    // View Type for Separators
    private static final int ITEM_VIEW_TYPE_SEPARATOR = 0;
    // View Type for Regular rows
    private static final int ITEM_VIEW_TYPE_REGULAR = 1;
    // Types of Views that need to be handled
    // -- Separators and Regular rows --
    private static final int ITEM_VIEW_TYPE_COUNT = 2;

    public CustomAdapter(Context context, ArrayList list) {
        mContext = context;
        mList = list;
    }
   /* public void addSectionHeaderItem(final String item) {
        mList.add(item);
        sectionHeader.add(mList.size() - 1);
        notifyDataSetChanged();
    }
*/
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Person  getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return ITEM_VIEW_TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        boolean isSection = mList.get(position).mIsSeparator;

        if (isSection) {
            return ITEM_VIEW_TYPE_SEPARATOR;
        }
        else {
            return ITEM_VIEW_TYPE_REGULAR;
        }
    }

    @Override
    public boolean isEnabled(int position) {
        return getItemViewType(position) != ITEM_VIEW_TYPE_SEPARATOR;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;

        Person contact = mList.get(position);
        int itemViewType = getItemViewType(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (itemViewType == ITEM_VIEW_TYPE_SEPARATOR) {
                // If its a section ?
                view = inflater.inflate(R.layout.person_section_header, null);
            }
            else {
                // Regular row
                view = inflater.inflate(R.layout.rowview_name, null);
            }
        }
        else {
            view = convertView;
        }


        if (itemViewType == ITEM_VIEW_TYPE_SEPARATOR) {
        /*    char[] nameArray;
            nameArray = contact.mName.toCharArray();
            String a=""+nameArray[0];
                   TextView separatorView = (TextView) view.findViewById(R.id.separator);
            separatorView.setText(a);
          */
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dt=contact.getEnterDt();
            String dts=sdf.format(dt);
            TextView separatorView = (TextView) view.findViewById(R.id.separator);
            separatorView.setText(dts);
        }
        else {
            // If regular

            // Set contact name and number
            TextView contactNameView = (TextView) view.findViewById(R.id.contact_name);
            TextView phoneNumberView = (TextView) view.findViewById(R.id.phone_number);

            contactNameView.setText( contact.mName );
            phoneNumberView.setText( contact.mNumber );
        }

        return view;
    }
}