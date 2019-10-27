package adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import controller.DBHelper;
import controller.OnRecyclerViewClickListener;
import model.Child;
import vn.edu.csc.babyapp.R;

public class PassengerManagerAdapter extends RecyclerView.Adapter<PassengerManagerAdapter.PassengerVH> {
    Context context;
    int layout;
    ArrayList<Child> listChild;

    private static OnRecyclerViewClickListener listener;

    public static void setOnItemClickListener(OnRecyclerViewClickListener listener) {
        PassengerManagerAdapter.listener = listener;
    }


    class PassengerVH extends RecyclerView.ViewHolder {
        com.joooonho.SelectableRoundedImageView ivPassengerIcon;
        TextView tvPassengerName;
        Button btnDeletePassenger;

        public PassengerVH(@NonNull View itemView) {
            super(itemView);
            ivPassengerIcon = itemView.findViewById(R.id.ivPassengerIcon);
            tvPassengerName = itemView.findViewById(R.id.tvPassengerName);
            btnDeletePassenger = itemView.findViewById(R.id.btnDeletePassenger);
        }
    }


    public PassengerManagerAdapter(Context context, int layout, ArrayList<Child> listChild) {
        this.context = context;
        this.layout = layout;
        this.listChild = listChild;
    }

    @NonNull
    @Override
    public PassengerVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View convertView = layoutInflater.inflate(layout, parent, false);

        return new PassengerVH(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull PassengerVH holder, final int position) {
        final Child child = listChild.get(position);

        if (child.getChildImage() == null) {
            holder.ivPassengerIcon.setImageResource(R.drawable.ic_baby);
            holder.tvPassengerName.setText(child.getChildName());
        } else {
            Bitmap bitmap = BitmapFactory.decodeByteArray(child.getChildImage(), 0, child.getChildImage().length);
            holder.tvPassengerName.setText(child.getChildName());
            holder.ivPassengerIcon.setImageBitmap(bitmap);
            holder.ivPassengerIcon.setCornerRadiiDP(100f, 100f, 100f, 100f);
            holder.ivPassengerIcon.setRotation(-90);
            holder.ivPassengerIcon.setScaleType(ImageView.ScaleType.CENTER_CROP);
            holder.ivPassengerIcon.isOval();
        }

        holder.btnDeletePassenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.custom_dialog);
                Button btnYes = dialog.findViewById(R.id.btnYes);
                Button btnNo = dialog.findViewById(R.id.btnNo);

                //Delete in database and in listview together
                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DBHelper dbHelper = new DBHelper(context);
                        dbHelper.deleteChild(child.getId());
                        listChild.remove(position);
                        notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });

                btnNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(false);
                dialog.show();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.OnItemRecyclerViewClickListener(position, view);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return listChild.size();
    }

}
