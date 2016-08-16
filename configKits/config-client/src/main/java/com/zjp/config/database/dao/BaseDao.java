package com.zjp.config.database.dao;

import java.io.Serializable;
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
 * Module Desc:com.zjp.config.database.dao
 * User: zjprevenge
 * Date: 2016/8/16
 * Time: 14:30
 */

public interface BaseDao<T extends Serializable> {

    /**
     * 更新对象
     *
     * @param obj
     */
    void update(T obj) throws Exception;

    /**
     * 添加对象
     *
     * @param obj
     */
    int insert(T obj) throws Exception;

    /**
     * 根据参数查询对象
     *
     * @param param
     * @return
     */
    T queryByParam(Map<String, Object> param) throws Exception;

    /**
     * 根据ID查询对象
     *
     * @param id
     * @return
     */
    T queryById(Integer id) throws Exception;
}
