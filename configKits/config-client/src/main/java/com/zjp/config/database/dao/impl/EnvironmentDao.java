package com.zjp.config.database.dao.impl;

import com.zjp.config.database.common.CommonConst;
import com.zjp.config.database.dao.BaseDao;
import com.zjp.config.database.db.DbHelper;
import com.zjp.config.entity.Environment;

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
 * Time: 14:35
 */

public class EnvironmentDao implements BaseDao<Environment> {

    /**
     * 更新对象
     *
     * @param obj
     */
    public void update(Environment obj) throws Exception {
        Connection connection = DbHelper.getInstance().getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(CommonConst.ENVIRONMENT_UPDATE);
            statement.setString(1, obj.getEnvName());
            statement.setDate(2, new Date(obj.getUpdateTime().getTime()));
            statement.setInt(3, obj.getId());
            statement.execute();
        } finally {
            connection.close();
        }
    }

    /**
     * 添加对象
     *
     * @param obj
     */
    public int insert(Environment obj) throws Exception {
        Connection connection = DbHelper.getInstance().getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        int key = 0;
        try {
            statement = connection.prepareStatement(CommonConst.ENVIRONMENT_INSERT);
            statement.setString(1, obj.getEnvName());
            statement.setDate(2, new Date(obj.getCreateTime().getTime()));
            statement.setDate(3, new Date(obj.getUpdateTime().getTime()));
            statement.executeUpdate();
            resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                key = resultSet.getInt(1);
            }
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
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
    public Environment queryByParam(Map<String, Object> param) throws Exception {
        Environment environment = null;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DbHelper.getInstance().getConnection();
            statement = connection.prepareStatement(CommonConst.ENVIRONMENT_QUERY_PARAM);
            statement.setString(1, (String) param.get("envName"));
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                environment.setId(resultSet.getInt(1));
                environment.setEnvName(resultSet.getString(2));
                environment.setCreateTime(resultSet.getDate(3));
                environment.setUpdateTime(resultSet.getDate(4));
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
        return environment;
    }

    /**
     * 根据ID查询对象
     *
     * @param id
     * @return
     */
    public Environment queryById(Integer id) throws Exception {
        Environment environment = null;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DbHelper.getInstance().getConnection();
            statement = connection.prepareStatement(CommonConst.ENVIRONMENT_QUERY_ID);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                environment.setId(resultSet.getInt(1));
                environment.setEnvName(resultSet.getString(2));
                environment.setCreateTime(resultSet.getDate(3));
                environment.setUpdateTime(resultSet.getDate(4));
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
        return environment;
    }
}
