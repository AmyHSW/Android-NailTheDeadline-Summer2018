package edu.neu.madcourse.shuwanhuang.nailthedeadline;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

public class CatsImageAdapter extends ArrayAdapter<Cat> {

    private static final int PADDING = 16;
    private Context mContext;

    public CatsImageAdapter(Context context, ArrayList<Cat> cats) {
        super(context, 0, cats);
        mContext = context;
    }

    @Override
    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(400, 400));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(PADDING, PADDING, PADDING, PADDING);
        } else {
            imageView = (ImageView) convertView;
        }
        Cat cat = getItem(position);
        imageView.setImageResource(cat.getImage());
        return imageView;
    }
}
