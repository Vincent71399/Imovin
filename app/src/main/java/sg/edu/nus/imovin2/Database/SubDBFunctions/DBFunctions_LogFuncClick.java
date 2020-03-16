package sg.edu.nus.imovin2.Database.SubDBFunctions;

import android.database.sqlite.SQLiteDatabase;

import org.greenrobot.greendao.query.Query;

import java.util.List;

import sg.edu.nus.imovin2.Database.FlagConstant;
import sg.edu.nus.imovin2.GreenDAO.DaoMaster;
import sg.edu.nus.imovin2.GreenDAO.DaoSession;
import sg.edu.nus.imovin2.GreenDAO.LogFuncClick;
import sg.edu.nus.imovin2.GreenDAO.LogFuncClickDao;

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

    private LogFuncClick GenerateLogFuncClick(int year, int month, int day){
        LogFuncClick logFuncClick = new LogFuncClick();
        logFuncClick.setRecordDateYear(year);
        logFuncClick.setRecordDateMonth(month);
        logFuncClick.setRecordDateDay(day);
        logFuncClick.setUpdateFlag(FlagConstant.PendingUpload);
        logFuncClick.setHomeCount(0);
        logFuncClick.setChallengeCount(0);
        logFuncClick.setLibraryCount(0);
        logFuncClick.setSocialCount(0);
        logFuncClick.setForumCount(0);
        logFuncClick.setMonitorCount(0);
        logFuncClick.setGoalCount(0);
        return logFuncClick;
    }

    public void addHomeCount(int year, int month, int day){
        LogFuncClick logFuncClick = queryLogFuncClick_by_Date(year, month, day);
        if(logFuncClick == null){
            logFuncClick = GenerateLogFuncClick(year, month, day);
            logFuncClick.setHomeCount(1);
            insertLogFuncClick(logFuncClick);
        }else{
            int count = logFuncClick.getHomeCount() + 1;
            logFuncClick.setHomeCount(count);
            logFuncClick.setUpdateFlag(FlagConstant.PendingUpload);
            insertLogFuncClick(logFuncClick);
        }
    }

    public void addChallengeCount(int year, int month, int day){
        LogFuncClick logFuncClick = queryLogFuncClick_by_Date(year, month, day);
        if(logFuncClick == null){
            logFuncClick = GenerateLogFuncClick(year, month, day);
            logFuncClick.setChallengeCount(1);
            insertLogFuncClick(logFuncClick);
        }else{
            int count = logFuncClick.getChallengeCount() + 1;
            logFuncClick.setChallengeCount(count);
            logFuncClick.setUpdateFlag(FlagConstant.PendingUpload);
            insertLogFuncClick(logFuncClick);
        }
    }

    public void addLibraryCount(int year, int month, int day){
        LogFuncClick logFuncClick = queryLogFuncClick_by_Date(year, month, day);
        if(logFuncClick == null){
            logFuncClick = GenerateLogFuncClick(year, month, day);
            logFuncClick.setLibraryCount(1);
            insertLogFuncClick(logFuncClick);
        }else{
            int count = logFuncClick.getLibraryCount() + 1;
            logFuncClick.setLibraryCount(count);
            logFuncClick.setUpdateFlag(FlagConstant.PendingUpload);
            insertLogFuncClick(logFuncClick);
        }
    }

    public void addSocialCount(int year, int month, int day){
        LogFuncClick logFuncClick = queryLogFuncClick_by_Date(year, month, day);
        if(logFuncClick == null){
            logFuncClick = GenerateLogFuncClick(year, month, day);
            logFuncClick.setSocialCount(1);
            insertLogFuncClick(logFuncClick);
        }else{
            int count = logFuncClick.getSocialCount() + 1;
            logFuncClick.setSocialCount(count);
            logFuncClick.setUpdateFlag(FlagConstant.PendingUpload);
            insertLogFuncClick(logFuncClick);
        }
    }

    public void addForumCount(int year, int month, int day){
        LogFuncClick logFuncClick = queryLogFuncClick_by_Date(year, month, day);
        if(logFuncClick == null){
            logFuncClick = GenerateLogFuncClick(year, month, day);
            logFuncClick.setForumCount(1);
            insertLogFuncClick(logFuncClick);
        }else{
            int count = logFuncClick.getForumCount() + 1;
            logFuncClick.setForumCount(count);
            logFuncClick.setUpdateFlag(FlagConstant.PendingUpload);
            insertLogFuncClick(logFuncClick);
        }
    }

    public void addMonitorCount(int year, int month, int day){
        LogFuncClick logFuncClick = queryLogFuncClick_by_Date(year, month, day);
        if(logFuncClick == null){
            logFuncClick = GenerateLogFuncClick(year, month, day);
            logFuncClick.setMonitorCount(1);
            insertLogFuncClick(logFuncClick);
        }else{
            int count = logFuncClick.getMonitorCount() + 1;
            logFuncClick.setMonitorCount(count);
            logFuncClick.setUpdateFlag(FlagConstant.PendingUpload);
            insertLogFuncClick(logFuncClick);
        }
    }

    public void addGoalCount(int year, int month, int day){
        LogFuncClick logFuncClick = queryLogFuncClick_by_Date(year, month, day);
        if(logFuncClick == null){
            logFuncClick = GenerateLogFuncClick(year, month, day);
            logFuncClick.setGoalCount(1);
            insertLogFuncClick(logFuncClick);
        }else{
            int count = logFuncClick.getGoalCount() + 1;
            logFuncClick.setGoalCount(count);
            logFuncClick.setUpdateFlag(FlagConstant.PendingUpload);
            insertLogFuncClick(logFuncClick);
        }
    }

    public void updateLogFuncClickFlag_to_IsUploading(LogFuncClick logFuncClick){
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

    public void updateLogFuncClickFlag_to_UploadFinished(LogFuncClick logFuncClick){
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

    public void updateLogFuncClickFlag_to_PendingUpdate(LogFuncClick logFuncClick){
        Query query = logFuncClickDao.queryBuilder()
                .where(LogFuncClickDao.Properties.Id.eq(logFuncClick.getId()))
                .build();
        LogFuncClick queryLogFuncClick = (LogFuncClick) query.unique();
        if(queryLogFuncClick != null) {
            queryLogFuncClick.setUpdateFlag(FlagConstant.PendingUpload);
            insertLogFuncClick(queryLogFuncClick);
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

