package com.jagdiv.android.gogleapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Dell on 4/07/2016.
 */

public class PDFAdapter extends BaseAdapter {


    private final Context mcontext;
  //  private final List<File> values;
    private ArrayList<PDFEntity> mList;
    // private final File[] values;
    // View Type for Separators
    private static final int ITEM_VIEW_TYPE_SEPARATOR = 0;
    // View Type for Regular rows
    private static final int ITEM_VIEW_TYPE_REGULAR = 1;
    // Types of Views that need to be handled
    // -- Separators and Regular rows --
    private static final int ITEM_VIEW_TYPE_COUNT = 2;


    public PDFAdapter(Context context, ArrayList resource) {
        //super(context,R.layout.rowview_pdf,resource);
        //context, R.layout.rowview_pdf, resource);

        this.mcontext = context;
        this.mList = resource;
    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public PDFEntity getItem(int position) {
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
        /*LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.rowview_pdf, parent, false);
        ImageView imageViewpdf = (ImageView) rowView.findViewById(R.id.pdficon);
        imageViewpdf.setImageResource(R.drawable.appicon);
        TextView textView = (TextView) rowView.findViewById(R.id.newsletterdoc);
        ImageView imageViewarrow = (ImageView) rowView.findViewById(R.id.rightarrow);
        textView.setText(""+mList.get(position).getName());
        imageViewarrow.setImageResource(R.drawable.arrow_right);


        return rowView;
 */
        View rowView;

        PDFEntity contact = mList.get(position);
        int itemViewType = getItemViewType(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (itemViewType == ITEM_VIEW_TYPE_SEPARATOR) {
                // If its a section ?
                rowView = inflater.inflate(R.layout.pdf_section_header, null);
            }
            else {
                // Regular row
                rowView = inflater.inflate(R.layout.rowview_pdf, null);
            }
        }
        else {
            rowView = convertView;
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
            TextView separatorView = (TextView) rowView.findViewById(R.id.separator);
            separatorView.setText(dts);
        }
        else {
            // If regular
         //   View rowView = inflater.inflate(R.layout.rowview_pdf, parent, false);
            ImageView imageViewpdf = (ImageView) rowView.findViewById(R.id.pdficon);
            imageViewpdf.setImageResource(R.drawable.appicon);
            TextView textView = (TextView) rowView.findViewById(R.id.newsletterdoc);
            ImageView imageViewarrow = (ImageView) rowView.findViewById(R.id.rightarrow);
            textView.setText(""+mList.get(position).getName());
            imageViewarrow.setImageResource(R.drawable.arrow_right);



        }

        return rowView;
    }

}
