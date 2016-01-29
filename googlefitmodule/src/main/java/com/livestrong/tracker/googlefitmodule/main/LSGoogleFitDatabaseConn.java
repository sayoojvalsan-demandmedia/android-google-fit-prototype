package com.livestrong.tracker.googlefitmodule.main;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.livestrong.tracker.googlefitmodule.model.DaoMaster;
import com.livestrong.tracker.googlefitmodule.model.DaoSession;
import com.livestrong.tracker.googlefitmodule.model.FitnessData;
import com.livestrong.tracker.googlefitmodule.model.FitnessDataDao;

import java.util.Date;
import java.util.List;

/**
 * Created by shambhavipunja on 1/28/16.
 */
public class LSGoogleFitDatabaseConn {
    private static LSGoogleFitDatabaseConn slsGoogleFitDatabaseConn;
    private DaoMaster.DevOpenHelper mhelper;
    private SQLiteDatabase mdb;
    private DaoSession mdaoSession;
    private DaoMaster mdaoMaster;
    private FitnessDataDao mdao;

    private LSGoogleFitDatabaseConn(Context context){
        String dbName = "FitnessData";

        //Start Session
        mhelper = new DaoMaster.DevOpenHelper(context,dbName,null);
        mdb = mhelper.getWritableDatabase();
        mdaoMaster = new DaoMaster(mdb);
        mdaoSession = mdaoMaster.newSession();
        mdao = mdaoSession.getFitnessDataDao();
    }

    public static synchronized LSGoogleFitDatabaseConn getDbCon(Context context) {
        if ( slsGoogleFitDatabaseConn == null ) {
            slsGoogleFitDatabaseConn = new LSGoogleFitDatabaseConn(context);
        }
        return slsGoogleFitDatabaseConn;

    }

    public Long Insert(FitnessData fitnessData){
        long resultcode = -1;

        if(fitnessData != null){
            mdao.insert(fitnessData);
            resultcode = fitnessData.getId();
        }
        return resultcode;
    }

    public List<FitnessData> GetRowByDate (Date date) {
        List<FitnessData> list = mdao.queryBuilder()
                .where(FitnessDataDao.Properties.Date.eq(date)).list();
        return list;
    }



}
