package com.bkav.voicememos;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class VoiceAdapter extends BaseCursorAdapter<VoiceAdapter.ViewHolder>{
    private Context mContext;
    private String mNameDecorder;
    private MediaPlayer mMediaPlayer ;
    public VoiceAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {
        TextView txtNameRecorder,txtDateRecorder;
        ImageView imgPlayVoice;
        ItemClickListener itemClickListener;
        public ViewHolder( View itemView) {
            super(itemView);
            txtNameRecorder = (TextView) itemView.findViewById(R.id.name_recorder);
            txtDateRecorder = (TextView) itemView.findViewById(R.id.date_recorder);
            imgPlayVoice = (ImageView) itemView.findViewById(R.id.play_recorder);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }
        public void setItemClickListener(ItemClickListener itemClickListener){
            this.itemClickListener = itemClickListener;
        }
        @Override
        public void onClick(View v) {
            itemClickListener.onCLick(v,getAdapterPosition(),false);
        }
        @Override
        public boolean onLongClick(View v) {
            itemClickListener.onCLick(v,getAdapterPosition(),true);
            return true;
        }
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final Cursor cursor) {
        if(cursor != null){
            mNameDecorder = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
            holder.txtNameRecorder.setText(takeNameRecorder(mNameDecorder));
            holder.txtDateRecorder.setText(takeTime(mNameDecorder));
            holder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onCLick(View view, int position, boolean isLongClick) {
                    if(isLongClick){

                        //TODO khi longclick hien ra popup
                    }else{
                        //TODO khi click chay file ghi am

                        cursor.moveToPosition(position);
                        Uri uri = Uri.parse((cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))));
                        mMediaPlayer = new MediaPlayer();
                        mMediaPlayer = MediaPlayer.create(mContext,uri);
                        mMediaPlayer.start();
                        Log.d("aaa","a");
                        /*if(mMediaPlayer.isPlaying()){
                            mMediaPlayer.release();
                            playVoice(uri);
                        }else{
                            playVoice(uri);
                        }*/
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        final View itemView = layoutInflater.inflate(R.layout.item_voice_meno, viewGroup, false);
        return new ViewHolder(itemView);
    }
    @Override
    public void swapCursor(Cursor newCursor) {
        super.swapCursor(newCursor);
    }
    /*private void playVoice(Uri uri){
        mMediaPlayer= MediaPlayer.create(mContext,uri);
        mMediaPlayer.start();
    }*/
    private String takeNameRecorder(String name){

        int dem = 0;
        int position = 0;
        for(int i = 0;i<name.length();i++){
            if(name.charAt(i) == '_'){
                dem++;
                if(dem == 3){
                    position = i;
                }
            }
        }
       return name.substring(12,position);
    }
    private String takeTime(String time){
        int dem = 0;
        int position1 = 0;
        int position2 = 0;
        int i = time.length()-1;
        while(i >= 0){
            if(time.charAt(i) == '_'){
                dem++;
                if(dem == 2){
                    position1 = i;
                }
            }
            if(time.charAt(i) == '.'){
                position2 = i;
            }
            i--;
        }
        return time.substring(position1+1,position2);
    }
}
