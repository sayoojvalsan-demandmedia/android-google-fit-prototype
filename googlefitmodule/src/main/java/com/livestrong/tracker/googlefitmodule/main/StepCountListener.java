package com.livestrong.tracker.googlefitmodule.main;

import java.util.HashMap;

/**
 * Created by shambhavipunja on 1/28/16.
 */
public interface StepCountListener {
        public void stepCountRetrieved(HashMap<Long,Integer> step_map);
    }

