package com.jagdiv.android.gogleapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.api.services.drive.model.File;

import java.util.List;

/**
 * Created by Dell on 4/07/2016.
 */

public class PDFAdapter extends ArrayAdapter<File> {


    private final Context context;
    private final List<File> values;
    // private final File[] values;


    public PDFAdapter(Context context, List<File> resource) {
        super(context,R.layout.rowview_pdf,resource);
        //context, R.layout.rowview_pdf, resource);

        this.context = context;
        this.values = resource;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.rowview_pdf, parent, false);
        ImageView imageViewpdf = (ImageView) rowView.findViewById(R.id.pdficon);
        imageViewpdf.setImageResource(R.drawable.appicon);
        TextView textView = (TextView) rowView.findViewById(R.id.newsletterdoc);
        ImageView imageViewarrow = (ImageView) rowView.findViewById(R.id.rightarrow);
        textView.setText(""+values.get(position).getName());
        imageViewarrow.setImageResource(R.drawable.arrow_right);
        // Change the icon for Windows and iPhone
     //   String s = values[position]+"";
      /*  if (s.startsWith("Windows7") || s.startsWith("iPhone")
                || s.startsWith("Solaris")) {
            imageView.setImageResource(R.drawable.no);
        } else {
            imageView.setImageResource(R.drawable.ok);
        }*/

        return rowView;
    }

    @Override
    public File getItem(int position) {
        return super.getItem(position);
    }
}
