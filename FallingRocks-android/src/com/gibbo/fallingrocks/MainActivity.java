package com.gibbo.fallingrocks;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class MainActivity extends AndroidApplication {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useGL20 = true;
        
        initialize(new FallingRocks(), cfg);
        
//        Swarm.setActive(this);
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	
//    	Swarm.setActive(this);
//    	Swarm.init(this, 9550, "2803f87f01284ee2d72e1d4df1b741d5");
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
//    	Swarm.setActive(this);O
    }
}