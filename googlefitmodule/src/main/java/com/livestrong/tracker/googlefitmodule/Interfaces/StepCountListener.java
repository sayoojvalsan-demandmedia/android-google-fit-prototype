package com.livestrong.tracker.googlefitmodule.Interfaces;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by shambhavipunja on 1/28/16.
 */
public interface StepCountListener {
        public void stepCountRetrieved(Map<Date,Integer> step_map);
    }

