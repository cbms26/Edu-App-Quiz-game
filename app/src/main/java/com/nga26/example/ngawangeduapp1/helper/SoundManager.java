package com.nga26.example.ngawangeduapp1.helper;

import android.content.Context;
import android.media.SoundPool;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {
    private SoundPool pool;
    private final Context context;
    private final Map<Integer, Integer> soundStreamIds = new HashMap<>(); // To keep track of sound stream IDs

    public SoundManager(Context context){
        this.context = context;
        SoundPool.Builder builder = new SoundPool.Builder();
        builder.setMaxStreams(10);
        pool = builder.build();
    }

    public int addSound(int resourceID){
        return pool.load(context, resourceID, 1);
    }

    public void play(int soundID, float rate){
        // play sound and keep track of the stream ID
        int streamId = pool.play(soundID, 1, 1, 1, 0, rate);
        soundStreamIds.put(soundID, streamId);
    }

    // Add this method to stop a sound by its soundID
    public void stop(int soundID){
        Integer streamId = soundStreamIds.get(soundID);
        if(streamId != null){
            pool.stop(streamId);
        }
    }

}
