package com.zjp.config.database.common;

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
 * Module Desc:com.zjp.config.database.common
 * User: zjprevenge
 * Date: 2016/8/16
 * Time: 14:52
 */

public class CommonConst {

    public static final String APPLICATION_INSERT = "INSERT INTO application(app_name,env_id,create_time,update_time) VALUES(?,?,?,?)";

    public static final String APPLICATION_UPDATE = "UPDATE application SET app_name =?,env_id=?,update_time=? WHERE id=?";

    public static final String APPLICATION_QUERY_BY_ID = "SELECT id,app_name,env_id,create_time,update_time FROM application WHERE id=?";

    public static final String APPLICATION_QUERY_BY_NAME = "SELECT id,app_name,env_id,create_time,update_time FROM application WHERE app_name=?";

    public static final String APPLICATION_QUERY_BY_PARAM = "SELECT id,app_name,env_id,create_time,update_time FROM application WHERE app_name=? and evn_id=?";

    public static final String ENVIRONMENT_INSERT = "INSERT INTO environment(evn_name,create_time,update_time) VALUES(?,?,?)";

    public static final String ENVIRONMENT_UPDATE = "UPDATE environment SET env_name=?,update_time=? WHERE id=?";

    public static final String ENVIRONMENT_QUERY_ID = "SELECT id,env_name,create_time,update_time FROM environment WHERE id=?";

    public static final String ENVIRONMENT_QUERY_PARAM = "SELECT id,env_name,create_time,update_time FROM environment WHERE env_name=?";

}
