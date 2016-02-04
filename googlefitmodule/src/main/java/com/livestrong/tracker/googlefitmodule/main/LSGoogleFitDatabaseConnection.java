package com.livestrong.tracker.googlefitmodule.main;

import android.database.sqlite.SQLiteDatabase;

import com.livestrong.tracker.googlefitmodule.greendao.DaoMaster;
import com.livestrong.tracker.googlefitmodule.greendao.DaoSession;
import com.livestrong.tracker.googlefitmodule.greendao.FitnessData;
import com.livestrong.tracker.googlefitmodule.greendao.FitnessDataDao;

import java.util.Date;
import java.util.List;

/**
 * Created by shambhavipunja on 1/28/16.
 */
public class LSGoogleFitDatabaseConnection {
    private static LSGoogleFitDatabaseConnection sLsGoogleFitDatabaseConnection;
    private DaoMaster.DevOpenHelper mHelper;
    private SQLiteDatabase mdb;
    private DaoSession mDaoSession;
    private DaoMaster mDaoMaster;
    private FitnessDataDao mDao;
    public static final String DATABASE_NAME = "FitnessData";



    private LSGoogleFitDatabaseConnection(){

        //Start Session
        mHelper = new DaoMaster.DevOpenHelper(LSGoogleFitManager.getLsGoogleFitManager().getContext(), DATABASE_NAME, null);
        mdb = mHelper.getWritableDatabase();
        mDaoMaster = new DaoMaster(mdb);
        mDaoSession = mDaoMaster.newSession();
        mDao = mDaoSession.getFitnessDataDao();
    }

    public static synchronized LSGoogleFitDatabaseConnection getInstance() {
        if ( sLsGoogleFitDatabaseConnection == null ) {
            sLsGoogleFitDatabaseConnection = new LSGoogleFitDatabaseConnection();
        }
        return sLsGoogleFitDatabaseConnection;

    }

    /**
     *
     * @param dataList:List of rows to insert.
     */
    public void insert(List<FitnessData> dataList){
        if(dataList.size()>0){
            mDao.insertOrReplaceInTx(dataList);
        }
    }

    /**
     *
     * @param date
     * @return Entry that has the input date in fitness data entity
     */
    public FitnessData getRowByDate(Date date) {
        List<FitnessData> list = mDao.queryBuilder()
                .where(FitnessDataDao.Properties.Date.eq(date)).list();
        if(list.size() == 0)
            return null;
        return list.get(0);
    }

    /**
     *
     * @return Most recent date in the fitness data entity
     */
    public Date getLastInsertDate(){
         List<FitnessData> list = mDao.queryBuilder().orderDesc(FitnessDataDao.Properties.Date).limit(1).list();
         if(list.size() == 1){
             return list.get(0).getDate();
         } else
            return null;
    }

    /**
     *
     * @return All entries in fitness data entity
     */
    public List<FitnessData> getAll() {
        List<FitnessData> list = mDao.queryBuilder().list();
        if(list.size() == 0)
            return null;
        return list;
    }


}
