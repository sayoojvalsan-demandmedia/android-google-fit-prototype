package com.livestrong.tracker.googlefitmodule.main;

import android.database.sqlite.SQLiteDatabase;

import com.livestrong.tracker.googlefitmodule.model.DaoMaster;
import com.livestrong.tracker.googlefitmodule.model.DaoSession;
import com.livestrong.tracker.googlefitmodule.model.FitnessData;
import com.livestrong.tracker.googlefitmodule.model.FitnessDataDao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by shambhavipunja on 1/28/16.
 */
public class LSGoogleFitDatabaseManager {
    private static LSGoogleFitDatabaseManager slsGoogleFitDatabaseManager;
    private DaoMaster.DevOpenHelper mhelper;
    private SQLiteDatabase mdb;
    private DaoSession mdaoSession;
    private DaoMaster mdaoMaster;
    private FitnessDataDao mdao;
    private List<GoogleFitObserver> mobservers = new ArrayList<GoogleFitObserver>();
    private int mobserverState;


    private LSGoogleFitDatabaseManager(){
        String dbName = "FitnessData";
        //Start Session
        mhelper = new DaoMaster.DevOpenHelper(LSGoogleFitManager.getLsGoogleFitManager().getcontext(),dbName,null);
        mdb = mhelper.getWritableDatabase();
        mdaoMaster = new DaoMaster(mdb);
        mdaoSession = mdaoMaster.newSession();
        mdao = mdaoSession.getFitnessDataDao();
    }

    public static synchronized LSGoogleFitDatabaseManager getinstance() {
        if ( slsGoogleFitDatabaseManager == null ) {
            slsGoogleFitDatabaseManager = new LSGoogleFitDatabaseManager();
        }
        return slsGoogleFitDatabaseManager;

    }

    public void setState(int state) {
        this.mobserverState = state;
        notifyAllObservers();
    }

    public void attach(GoogleFitObserver observer){
        mobservers.add(observer);
    }

    public void notifyAllObservers() {
        for (GoogleFitObserver observer : mobservers) {
            observer.onDatabaseUpdated();
        }
    }

    /**
     *
     * @param dataList:List of rows to insert.
     */
    public void insert(List<FitnessData> dataList){
        if(dataList.size()>0){
            mdao.insertOrReplaceInTx(dataList);
            /*mdb.beginTransaction();
            try{
                for (FitnessData data:dataList) {
                    FitnessData oldRow = getRowByDate(data.getDate());
                    if(oldRow == null){
                        mdao.insert(data);

                    } else{
                        oldRow.setFitness_step_count(data.getFitness_step_count());
                        mdao.update(oldRow);
                        mdao.updateInTx();

                    }
                }
                mdb.setTransactionSuccessful();
            } catch (Exception ex){
                ex.printStackTrace();
            } finally {
                mdb.endTransaction();
            }*/
        }
    }

    public FitnessData getRowByDate(Date date) {
        List<FitnessData> list = mdao.queryBuilder()
                .where(FitnessDataDao.Properties.Date.eq(date)).list();
        if(list.size() == 0)
            return null;
        return list.get(0);
    }

    public Date getLastInsertDate(){
         List<FitnessData> list = mdao.queryBuilder().orderDesc(FitnessDataDao.Properties.Date).limit(1).list();
         if(list.size() == 1){
             return list.get(0).getDate();
         } else
            return null;
    }

    public List<FitnessData> getAll() {
        List<FitnessData> list = mdao.queryBuilder().list();
        if(list.size() == 0)
            return null;
        return list;
    }


}
