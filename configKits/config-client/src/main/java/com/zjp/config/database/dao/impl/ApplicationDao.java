package com.zjp.config.database.dao.impl;

import com.zjp.config.database.common.CommonConst;
import com.zjp.config.database.dao.BaseDao;
import com.zjp.config.database.db.DbHelper;
import com.zjp.config.entity.Application;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

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
 * Module Desc:com.zjp.config.database.dao.impl
 * User: zjprevenge
 * Date: 2016/8/16
 * Time: 14:34
 */

public class ApplicationDao implements BaseDao<Application> {

    /**
     * 更新对象
     *
     * @param obj
     */
    public void update(Application obj) throws Exception {
        Connection connection = DbHelper.getInstance().getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(CommonConst.APPLICATION_UPDATE);
            preparedStatement.setString(1, obj.getAppName());
            preparedStatement.setInt(2, obj.getEnvId());
            preparedStatement.setDate(3, new Date(obj.getUpdateTime().getTime()));
            preparedStatement.setInt(4, obj.getId());
            preparedStatement.execute();
        } finally {
            connection.close();
        }

    }

    /**
     * 添加对象
     *
     * @param obj
     */
    public int insert(Application obj) throws Exception {
        Connection connection = DbHelper.getInstance().getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int key = 0;
        try {
            preparedStatement = connection.prepareStatement(CommonConst.APPLICATION_INSERT);
            preparedStatement.setString(1, obj.getAppName());
            preparedStatement.setInt(2, obj.getEnvId());
            preparedStatement.setDate(3, new Date(obj.getCreateTime().getTime()));
            preparedStatement.setDate(4, new Date(obj.getUpdateTime().getTime()));
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            key = resultSet.getInt(1);
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            connection.close();
        }
        return key;
    }

    /**
     * 根据参数查询对象
     *
     * @param param
     * @return
     */
    public Application queryByParam(Map<String, Object> param) throws Exception {
        Application application = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Connection connection = DbHelper.getInstance().getConnection();
        try {
            statement = connection.prepareStatement(CommonConst.APPLICATION_QUERY_BY_PARAM);
            statement.setString(1, (String) param.get("appName"));
            statement.setInt(2, (Integer) param.get("envId"));
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                application = new Application();
                application.setId(resultSet.getInt(1));
                application.setAppName(resultSet.getString(2));
                application.setEnvId(resultSet.getInt(3));
                application.setCreateTime(resultSet.getDate(4));
                application.setUpdateTime(resultSet.getDate(5));
            }
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return application;
    }

    /**
     * 根据ID查询对象
     *
     * @param id
     * @return
     */
    public Application queryById(Integer id) throws Exception {
        Application application = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Connection connection = DbHelper.getInstance().getConnection();
        try {
            statement = connection.prepareStatement(CommonConst.APPLICATION_QUERY_BY_ID);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                application = new Application();
                application.setId(resultSet.getInt(1));
                application.setAppName(resultSet.getString(2));
                application.setEnvId(resultSet.getInt(3));
                application.setCreateTime(resultSet.getDate(4));
                application.setUpdateTime(resultSet.getDate(5));
            }
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return application;
    }

    /**
     * 根据名称查询应用信息
     *
     * @param appName
     * @return
     */
    public Application queryByName(String appName) throws Exception {
        Application application = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Connection connection = DbHelper.getInstance().getConnection();
        try {
            statement = connection.prepareStatement(CommonConst.APPLICATION_QUERY_BY_NAME);
            statement.setString(1, appName);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                application = new Application();
                application.setId(resultSet.getInt(1));
                application.setAppName(resultSet.getString(2));
                application.setEnvId(resultSet.getInt(3));
                application.setCreateTime(resultSet.getDate(4));
                application.setUpdateTime(resultSet.getDate(5));
            }
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return application;
    }
}
