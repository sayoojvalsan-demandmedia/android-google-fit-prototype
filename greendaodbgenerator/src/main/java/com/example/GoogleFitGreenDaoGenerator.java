package com.example;

import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class GoogleFitGreenDaoGenerator {
    public static void main(String args[]){
        Schema schema = new Schema(1, "com.example.googlefitmodule.model");
        Entity UserDa = schema.addEntity("Person");
        person.addIdProperty();
        person.addStringProperty("name");
        person.addStringProperty("comment");

    }
    }
}
