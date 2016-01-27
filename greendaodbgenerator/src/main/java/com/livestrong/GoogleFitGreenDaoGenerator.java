package com.livestrong;

import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.DaoGenerator;

public class GoogleFitGreenDaoGenerator {
    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(1, "com.livestrong.tracker.googlefitmodule.model");
        Entity fitnessData = schema.addEntity("FitnessData");
        fitnessData.addIdProperty();
        fitnessData.addDateProperty("Date");
        fitnessData.addIntProperty("Fitness_step_count");
        fitnessData.addFloatProperty("FItness_distance");
        fitnessData.addFloatProperty("Fitness_calorie_count");
        fitnessData.addDateProperty("Fitness_time");
        new DaoGenerator().generateAll(schema, "../googlefitmodule/src/main/java");
    }
    }

