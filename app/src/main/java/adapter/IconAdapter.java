package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import model.Icon;
import vn.edu.csc.babyapp.R;

public class IconAdapter extends ArrayAdapter<Icon> {
    Context context;
    int layout;
    ArrayList<Icon> listIcon;

    public IconAdapter(Context context, int layout, ArrayList<Icon> listIcon) {
        super(context, layout, listIcon);
        this.context = context;
        this.layout = layout;
        this.listIcon = listIcon;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        convertView = layoutInflater.inflate(layout, parent, false);

        ImageView imageView = convertView.findViewById(R.id.ivIcon);
        Icon icon = listIcon.get(position);

        imageView.setImageResource(icon.getIcon());
        return convertView;
    }
}
