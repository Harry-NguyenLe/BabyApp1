package adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import model.SongInfo;
import vn.edu.csc.babyapp.R;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongVH> {

    Context context;
    int layout;
    ArrayList<SongInfo> listSong;
    public static MediaPlayer m;

    private static CheckBox lastChecked = null;
    private static int lastCheckedPos = 0;


    public SongAdapter(Context context, int layout, ArrayList<SongInfo> songInfos) {
        this.context = context;
        this.layout = layout;
        this.listSong = songInfos;
    }

    @NonNull
    @Override
    public SongVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        m = new MediaPlayer();
        View convertView = layoutInflater.inflate(layout, parent, false);
        return new SongVH(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull final SongVH holder, int position) {
        final SongInfo songInfo = listSong.get(position);
        holder.tvSongTitle.setText(songInfo.getSongTittle());


//        //Get position where users click
//        if (listSong.get(holder.getAdapterPosition()).getChecked() == 1) {
//            holder.ckbSong.setChecked(true);
//
//        } else {
//            holder.ckbSong.setChecked(false);
//        }
//
//
//        holder.ckbSong.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if (b) {
//                    listSong.get(holder.getAdapterPosition()).setChecked(1);
//                    songInfo.setChecked(1);
//                    playBeep(songInfo.getSongTittle());
//
//                    SharedPreferences sharedPreferences = context.getSharedPreferences("Pref_Transfer_Time", Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putString("songInfo", songInfo.getSongTittle());
//                    editor.apply();
//
//                    Toast.makeText(context, songInfo.getSongTittle(), Toast.LENGTH_SHORT).show();
//
//                } else {
//                    if (m.isPlaying()) {
//                        m.stop();
//                        m.release();
//                        m = new MediaPlayer();
//                    }
////                    Toast.makeText(context, "OKE", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        holder.ckbSong.setChecked(listSong.get(position).isSelected());
        holder.ckbSong.setTag(new Integer(position));

        //for default check in first item
        if (position == 0 && listSong.get(0).isSelected() && holder.ckbSong.isChecked()) {
            lastChecked = holder.ckbSong;
            lastCheckedPos = 0;
        }

        holder.ckbSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                int clickedPos = ((Integer) cb.getTag()).intValue();

                if (cb.isChecked()) {
                    if (lastChecked != null) {
                        lastChecked.setChecked(false);
                        listSong.get(lastCheckedPos).setSelected(false);

                    }

                    lastChecked = cb;
                    lastCheckedPos = clickedPos;

                    SharedPreferences sharedPreferences = context.getSharedPreferences("Pref_Transfer_Time", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("songInfo", songInfo.getSongTittle());
                    editor.apply();

                    playBeep(songInfo.getSongTittle());
                } else
                    lastChecked = null;

                listSong.get(clickedPos).setSelected(cb.isChecked());
            }
        });
    }


    @Override
    public int getItemCount() {
        return listSong.size();
    }

    class SongVH extends RecyclerView.ViewHolder {
        TextView tvSongTitle;
        CheckBox ckbSong;

        public SongVH(@NonNull View itemView) {
            super(itemView);
            tvSongTitle = itemView.findViewById(R.id.tvSongTittle);
            ckbSong = itemView.findViewById(R.id.ckbSong);
        }
    }

    public void playBeep(String songTitle) {
        try {

            if (m.isPlaying()) {
                m.stop();
                m.release();
                m = new MediaPlayer();
            }

            AssetFileDescriptor descriptor = context.getAssets().openFd("sound/" + songTitle);
            m.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();

            m.prepare();
            m.setVolume(1f, 1f);
            m.setLooping(false);
            m.start();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
