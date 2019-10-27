package adapter;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import controller.ConverterTimer;
import controller.DBHelper;
import controller.OnRecyclerViewClickListener;
import model.Route;
import vn.edu.csc.babyapp.CountDownTimeActivity;
import vn.edu.csc.babyapp.R;

import static android.content.Context.MODE_PRIVATE;

public class RouteAdapterRecycle extends RecyclerView.Adapter<RouteAdapterRecycle.RouteVH> {
    private ArrayList<Route> listRoute;

    private Context context;
    private int layout;
    int routeID;
    DBHelper dbHelper;

    long timer;

    public static BroadcastReceiver mBroadcast;

    private static OnRecyclerViewClickListener Listener;

    public static void setOnItemClickListener(OnRecyclerViewClickListener listener) {
        Listener = listener;
    }


    public RouteAdapterRecycle(ArrayList<Route> listRoute, Context context, int layout) {
        this.listRoute = listRoute;
        this.context = context;
        this.layout = layout;
    }

    class RouteVH extends RecyclerView.ViewHolder {

        TextView tvRouteName, tvAlarmTime, tvRouteChildName;
        ImageView ivClock, ivIconRoute;


        private RouteVH(View itemView) {
            super(itemView);
            tvRouteName = itemView.findViewById(R.id.tvRouteName);
            tvRouteChildName = itemView.findViewById(R.id.tvRouteChildName);
            tvAlarmTime = itemView.findViewById(R.id.tvAlarmTime);
            ivClock = itemView.findViewById(R.id.ivClock);
            ivIconRoute = itemView.findViewById(R.id.ivIconRoute);
        }
    }

    @NonNull
    @Override
    public RouteVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View convertView = layoutInflater.inflate(layout, parent, false);

        SharedPreferences sharedPreferences1 = context.getSharedPreferences("Pref_Transfer_Time", MODE_PRIVATE);
        routeID = sharedPreferences1.getInt("routeID", 0);

        dbHelper = new DBHelper(context);
        listRoute = dbHelper.getRoute();

        return new RouteVH(convertView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final RouteVH holder, final int position) {
        final Route route = listRoute.get(position);
        holder.tvRouteName.setText(route.getRouteName());
        holder.tvRouteChildName.setText(route.getRouteChildName());

        if (route.getIcon() == null) {
            holder.ivIconRoute.setVisibility(View.GONE);

        } else {
            Bitmap bitmap = BitmapFactory.decodeByteArray(route.getIcon(), 0, route.getIcon().length);
            holder.ivIconRoute.setImageBitmap(bitmap);
            holder.ivIconRoute.setVisibility(View.VISIBLE);
        }

        //---------------Get route's position checking by routeID---------------//
        if (((listRoute.get(position).getRouteID() - 1) == routeID)) {

            mBroadcast = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    timer = intent.getLongExtra("countdown", 0);

                    ArrayList<String> listTime = ConverterTimer.convertTime(timer);
                    String hours = listTime.get(0);
                    String minutes = listTime.get(1);
                    String seconds = listTime.get(2);

                    holder.tvAlarmTime.setText(hours + " : " + minutes + " : " + seconds + " mins");
                    holder.tvAlarmTime.setTextColor(context.getResources().getColor(R.color.colorTextviewAlarmTime));
                    holder.ivClock.setImageDrawable(context.getDrawable(R.drawable.ic_clock_update));
                }
            };
        }

        holder.tvAlarmTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listRoute = dbHelper.getRoute();

                for (int i = 0; i < listRoute.size(); i++) {
                    Route route1 = listRoute.get(i);
                    if (route1.getRouteID() == (position + 1)) {

                        Intent intent = new Intent(context, CountDownTimeActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("routeName", route1.getRouteName());
                        bundle.putString("childName", route1.getRouteChildName());
                        bundle.putInt("routeID", route1.getRouteID());
                        bundle.putString("routeTime", route1.getRouteTime());
                        bundle.putInt("routeTimeInt", route1.getTimeInInteger());

                        intent.putExtra("bundleAdapter", bundle);
                        context.startActivity(intent);
                    }
                }
            }
        });

        holder.tvAlarmTime.setText(route.getRouteTime());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Listener != null) {
                    Listener.OnItemRecyclerViewClickListener(position, view);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (listRoute != null) {
            return listRoute.size();
        } else {
            return 0;
        }
    }


}
