package sg.edu.nus.imovin.Database.SubDBFunctions;

import android.database.sqlite.SQLiteDatabase;

import org.greenrobot.greendao.query.Query;

import java.util.List;

import sg.edu.nus.imovin.Database.FlagConstant;
import sg.edu.nus.imovin.GreenDAO.DaoMaster;
import sg.edu.nus.imovin.GreenDAO.DaoSession;
import sg.edu.nus.imovin.GreenDAO.LogFuncClick;
import sg.edu.nus.imovin.GreenDAO.LogFuncClickDao;

public class DBFunctions_LogFuncClick {
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private LogFuncClickDao logFuncClickDao;

    public DBFunctions_LogFuncClick(SQLiteDatabase dataBase) {
        this.daoMaster = new DaoMaster(dataBase);
        this.daoSession = daoMaster.newSession();
        this.logFuncClickDao = daoSession.getLogFuncClickDao();
    }

    public void insertLogFuncClick(LogFuncClick logFuncClick){
        logFuncClickDao.insertOrReplace(logFuncClick);
    }

    private void updateLogFuncClickFlag_to_IsUploading(LogFuncClick logFuncClick){
        Query query = logFuncClickDao.queryBuilder()
                .where(LogFuncClickDao.Properties.Id.eq(logFuncClick.getId()))
                .where(LogFuncClickDao.Properties.UpdateFlag.eq(FlagConstant.PendingUpload))
                .build();
        LogFuncClick queryLogFuncClick = (LogFuncClick) query.unique();
        if(queryLogFuncClick != null) {
            queryLogFuncClick.setUpdateFlag(FlagConstant.IsUploading);
            insertLogFuncClick(queryLogFuncClick);
        }
    }

    public void updateLogFuncClickFlag_to_IsUploading(List<LogFuncClick> logFuncClickList){
        for(LogFuncClick logFuncClick : logFuncClickList){
            updateLogFuncClickFlag_to_IsUploading(logFuncClick);
        }
    }

    private void updateLogFuncClickFlag_to_UploadFinished(LogFuncClick logFuncClick){
        Query query = logFuncClickDao.queryBuilder()
                .where(LogFuncClickDao.Properties.Id.eq(logFuncClick.getId()))
                .where(LogFuncClickDao.Properties.UpdateFlag.eq(FlagConstant.IsUploading))
                .build();
        LogFuncClick queryLogFuncClick = (LogFuncClick) query.unique();
        if(queryLogFuncClick != null) {
            queryLogFuncClick.setUpdateFlag(FlagConstant.UploadFinished);
            insertLogFuncClick(queryLogFuncClick);
        }
    }

    public void updateLogFuncClickFlag_to_UploadFinished(List<LogFuncClick> logFuncClickList){
        for(LogFuncClick logFuncClick : logFuncClickList){
            updateLogFuncClickFlag_to_UploadFinished(logFuncClick);
        }
    }

    public void updateLogFuncClick(LogFuncClick logFuncClick){
        LogFuncClick existLogFuncClick = queryLogFuncClick_by_Date(logFuncClick.getRecordDateYear(), logFuncClick.getRecordDateMonth(), logFuncClick.getRecordDateDay());
        if(existLogFuncClick != null){
            logFuncClick.setId(existLogFuncClick.getId());
        }
        insertLogFuncClick(logFuncClick);
    }

    public LogFuncClick queryLogFuncClick_by_Date(int year, int month, int day){
        Query query = logFuncClickDao.queryBuilder()
                .where(LogFuncClickDao.Properties.RecordDateYear.eq(year))
                .where(LogFuncClickDao.Properties.RecordDateMonth.eq(month))
                .where(LogFuncClickDao.Properties.RecordDateDay.eq(day))
                .build();
        return (LogFuncClick) query.unique();
    }

    public List<LogFuncClick> queryLogFuncClick_need_upload(){
        Query query = logFuncClickDao.queryBuilder()
                .where(LogFuncClickDao.Properties.UpdateFlag.eq(FlagConstant.PendingUpload))
                .build();
        return query.list();
    }
}

