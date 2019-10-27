package adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.joooonho.SelectableRoundedImageView;

import java.util.ArrayList;

import controller.OnRecyclerViewClickListener;
import model.Child;
import vn.edu.csc.babyapp.R;

public class ChildAdapterRecycle extends RecyclerView.Adapter<ChildAdapterRecycle.ChildHolder> {
    private ArrayList<Child> listChild;
    private Context context;
    private int layout;


    private static OnRecyclerViewClickListener listener;

    public static void setOnItemClickListener(OnRecyclerViewClickListener listener) {
        ChildAdapterRecycle.listener = listener;
    }


    public ChildAdapterRecycle(ArrayList<Child> listChild, Context context, int layout) {
        this.listChild = listChild;
        this.context = context;
        this.layout = layout;
    }

    class ChildHolder extends RecyclerView.ViewHolder {
        TextView tvChildName;
        SelectableRoundedImageView ivChildImage;
        CheckBox checkBox;

        ChildHolder(@NonNull View itemView) {
            super(itemView);
            tvChildName = itemView.findViewById(R.id.tvChildName);
            ivChildImage = itemView.findViewById(R.id.ivChildImage);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }

    @NonNull
    @Override
    public ChildHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View convertView = layoutInflater.inflate(layout, parent, false);


        return new ChildHolder(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ChildHolder holder, final int position) {
        final Child child = listChild.get(position);

        if (child.getChildImage() == null) {
            holder.ivChildImage.setImageResource(R.drawable.ic_baby);
            holder.tvChildName.setText(child.getChildName());

        } else {
            Bitmap bitmap = BitmapFactory.decodeByteArray(child.getChildImage(), 0, child.getChildImage().length);
            holder.tvChildName.setText(child.getChildName());
            holder.ivChildImage.setImageBitmap(bitmap);
            holder.ivChildImage.setCornerRadiiDP(100f, 100f, 100f, 100f);
            holder.ivChildImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            holder.ivChildImage.setRotation(-90);
            holder.ivChildImage.isOval();

        }

        //Get position where users click
        if (listChild.get(holder.getAdapterPosition()).getChecked() == 1) {
            holder.checkBox.setChecked(true);

        } else {
            holder.checkBox.setChecked(false);
        }


        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    listChild.get(holder.getAdapterPosition()).setChecked(1);
                    child.setChecked(1);

                } else {
                    listChild.get(holder.getAdapterPosition()).setChecked(0);
                    child.setChecked(0);
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.OnItemRecyclerViewClickListener(position, v);
                }
            }
        });


    }


    @Override
    public int getItemCount() {
        if (listChild != null) {
            return listChild.size();
        } else return 0;
    }

}
