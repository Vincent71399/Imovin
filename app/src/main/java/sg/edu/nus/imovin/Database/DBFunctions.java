package sg.edu.nus.imovin.Database;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import sg.edu.nus.imovin.Database.SubDBFunctions.DBFunctions_LogFuncClick;
import sg.edu.nus.imovin.GreenDAO.LogFuncClick;

public class DBFunctions {
    private SQLiteDatabase db;
    private DBFunctions_LogFuncClick dbFunctions_logFuncClick;

    public DBFunctions(SQLiteDatabase dataBase) {
        db = dataBase;
        dbFunctions_logFuncClick = new DBFunctions_LogFuncClick(dataBase);
    }

    //LogFuncClick Funcs
    public void insertLogFuncClick(LogFuncClick logFuncClick) {
        dbFunctions_logFuncClick.insertLogFuncClick(logFuncClick);
    }

    public void addHomeCount(int year, int month, int day){
        dbFunctions_logFuncClick.addHomeCount(year, month, day);
    }

    public void addChallengeCount(int year, int month, int day){
        dbFunctions_logFuncClick.addChallengeCount(year, month, day);
    }

    public void addLibraryCount(int year, int month, int day){
        dbFunctions_logFuncClick.addLibraryCount(year, month, day);
    }

    public void addSocialCount(int year, int month, int day){
        dbFunctions_logFuncClick.addSocialCount(year, month, day);
    }

    public void addForumCount(int year, int month, int day){
        dbFunctions_logFuncClick.addForumCount(year, month, day);
    }

    public void addMonitorCount(int year, int month, int day){
        dbFunctions_logFuncClick.addMonitorCount(year, month, day);
    }

    public void addGoalCount(int year, int month, int day){
        dbFunctions_logFuncClick.addGoalCount(year, month, day);
    }

    public void updateLogFuncClick(LogFuncClick logFuncClick){
        dbFunctions_logFuncClick.updateLogFuncClick(logFuncClick);
    }

    public void updateLogFuncClickFlag_to_IsUploading(LogFuncClick logFuncClick){
        dbFunctions_logFuncClick.updateLogFuncClickFlag_to_IsUploading(logFuncClick);
    }

    public void updateLogFuncClickFlag_to_UploadFinished(LogFuncClick logFuncClick){
        dbFunctions_logFuncClick.updateLogFuncClickFlag_to_UploadFinished(logFuncClick);
    }

    public LogFuncClick queryLogFuncClick_by_Date(int year, int month, int day){
        return dbFunctions_logFuncClick.queryLogFuncClick_by_Date(year, month, day);
    }

    public List<LogFuncClick> queryLogFuncClick_need_upload(){
        return dbFunctions_logFuncClick.queryLogFuncClick_need_upload();
    }
}
