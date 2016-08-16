package com.zjp.config.database.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * ━━━━━━南无阿弥陀佛━━━━━━
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛
 * 　　　　┃　　　┃stay hungry stay foolish
 * 　　　　┃　　　┃Code is far away from bug with the animal protecting
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * ━━━━━━萌萌哒━━━━━━
 * Module Desc:com.zjp.config.database.db
 * User: zjprevenge
 * Date: 2016/8/16
 * Time: 14:36
 */

public class DbHelper {
    private static final Logger log = LoggerFactory.getLogger(DbHelper.class);

    private static final String jdbcUrl = "jdbc:mysql://";
    private static final String dbDriver = "com.mysql.jdbc.Driver";
    public static final String userName = "root";
    public static final String password = "root";

    private static DbHelper instance;

    private DbHelper() {
        try {
            Class.forName(dbDriver);
        } catch (ClassNotFoundException e) {
            log.error("ClassNotFoundException:{}", e);
        }
    }

    public static DbHelper getInstance() {
        if (instance == null) {
            synchronized (DbHelper.class) {
                if (instance == null) {
                    instance = new DbHelper();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() throws Exception {
        return DriverManager.getConnection(jdbcUrl, userName, password);
    }
}
