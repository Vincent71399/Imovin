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

    public void updateLogFuncClick(LogFuncClick logFuncClick){
        dbFunctions_logFuncClick.updateLogFuncClick(logFuncClick);
    }

    public void updateLogFuncClickFlag_to_IsUploading(List<LogFuncClick> logFuncClickList){
        dbFunctions_logFuncClick.updateLogFuncClickFlag_to_IsUploading(logFuncClickList);
    }

    public void updateLogFuncClickFlag_to_UploadFinished(List<LogFuncClick> logFuncClickList){
        dbFunctions_logFuncClick.updateLogFuncClickFlag_to_UploadFinished(logFuncClickList);
    }

    public LogFuncClick queryLogFuncClick_by_Date(int year, int month, int day){
        return dbFunctions_logFuncClick.queryLogFuncClick_by_Date(year, month, day);
    }

    public List<LogFuncClick> queryLogFuncClick_need_upload(){
        return dbFunctions_logFuncClick.queryLogFuncClick_need_upload();
    }
}
