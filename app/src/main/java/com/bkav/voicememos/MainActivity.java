package com.bkav.voicememos;

import android.content.Loader;
import android.database.Cursor;
import android.provider.MediaStore;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    final static int ID_LOADER=1;
    private CursorLoader mCursorLoader;
    private RecyclerView mRecyclerView;
    private VoiceAdapter mVoiceAdapter;

    private void init(){
        mRecyclerView = (RecyclerView) findViewById(R.id.list_voice_memos);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        getLoaderManager().initLoader(ID_LOADER,null,this);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this
                ,LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mVoiceAdapter = new VoiceAdapter(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(ID_LOADER,null,this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String aritis = MediaStore.Audio.Media.ARTIST+ "=?";
        String sort = MediaStore.Audio.Media.DATE_MODIFIED;
        if(id == ID_LOADER){
            mCursorLoader=new CursorLoader(this
                    , MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    ,null
                    ,aritis
                    ,new String[]{"BkavCallRecorder"}
                    ,sort + " DESC ");
            return mCursorLoader;
        }
        return null;
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mVoiceAdapter.swapCursor(data);
        mRecyclerView.setAdapter(mVoiceAdapter);
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mVoiceAdapter.swapCursor(null);
    }
}
