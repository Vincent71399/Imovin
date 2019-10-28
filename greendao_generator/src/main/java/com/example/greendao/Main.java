package com.example.greendao;

import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;

public class Main {
    private static final String PACKAGE_PATH = "sg.edu.nus.imovin.GreenDAO";
    private static final String OUT_DIR = "D:/AndroidStudioProjects/Imovin/app/src/main/java";

    public static void main(String[] args) throws Exception{
        Schema schema = new Schema(1, PACKAGE_PATH);

        Entity logFuncClick = schema.addEntity("LogFuncClick");
        logFuncClick.addIdProperty().autoincrement().primaryKey();
        logFuncClick.addIntProperty("RecordDateYear").notNull();
        logFuncClick.addIntProperty("RecordDateMonth").notNull();
        logFuncClick.addIntProperty("RecordDateDay").notNull();
        logFuncClick.addIntProperty("UpdateFlag").notNull();
        logFuncClick.addIntProperty("HomeCount").notNull();
        logFuncClick.addIntProperty("ChallengeCount").notNull();
        logFuncClick.addIntProperty("LibraryCount").notNull();
        logFuncClick.addIntProperty("SocialCount").notNull();
        logFuncClick.addIntProperty("ForumCount").notNull();
        logFuncClick.addIntProperty("MonitorCount").notNull();
        logFuncClick.addIntProperty("GoalCount").notNull();

        DaoGenerator dg = new DaoGenerator();
        dg.generateAll(schema, OUT_DIR);
    }
}
