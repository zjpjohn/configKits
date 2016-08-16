package com.zjp.config.core;

import com.google.common.collect.Maps;
import com.zjp.config.core.impl.MapConfiguration;
import com.zjp.config.core.impl.StringConfiguration;
import com.zjp.config.database.dao.impl.ApplicationDao;
import com.zjp.config.database.dao.impl.EnvironmentDao;
import com.zjp.config.entity.Application;
import com.zjp.config.entity.Environment;
import com.zjp.config.zk.DataHandle;
import com.zjp.config.zk.ZKNodeAction;
import com.zjp.config.zk.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * author:zjprevenge
 * time: 2016/8/2
 * copyright all reserved
 */
public class DataLoader implements DataHandle {

    private static final Logger log = LoggerFactory.getLogger(DataLoader.class);

    //string类型标识
    private static final String STRING_TYPE = "String";
    //map类型标识
    private static final String MAP_TYPE = "Map";

    private String zkUrl;

    private String evn;

    private String appName;

    private String charset = "utf-8";

    private static ZkClient zkClient;

    private static DataLoader instance;

    private static ConcurrentHashMap<String, Configuration> configurationMap = new ConcurrentHashMap<String, Configuration>();

    public DataLoader(String zkUrl, String evn, String appName, String charset) throws Exception {
        this.zkUrl = zkUrl;
        this.evn = evn;
        this.appName = appName;
        this.charset = charset;
        //出事话zk客户端
        zkClient = ZkClient.init(zkUrl, evn, appName, charset);
        //添加zk节点监听
        zkClient.build(this);
        //添加数据库记录
        loadDb(appName, evn);

    }

    /**
     * 添加响应的数据库记录
     *
     * @param appName 应用名称
     * @param env     环境名称
     */
    public void loadDb(String appName, String env) throws Exception {
        EnvironmentDao environmentDao = new EnvironmentDao();
        Map<String, Object> map = Maps.newHashMap();
        map.put("envName", env);
        Integer envId;
        Date date = new Date();
        Environment environment = environmentDao.queryByParam(map);
        if (environment == null) {
            environment = new Environment();
            environment.setEnvName(evn);
            environment.setCreateTime(date);
            environment.setUpdateTime(date);
            envId = environmentDao.insert(environment);
        } else {
            envId = environment.getId();
        }
        ApplicationDao applicationDao = new ApplicationDao();
        Map<String, Object> param = Maps.newHashMap();
        param.put("appName", appName);
        param.put("envId", envId);
        Application application = applicationDao.queryByParam(param);
        if (application == null) {
            application = new Application();
            application.setAppName(appName);
            application.setEnvId(envId);
            application.setCreateTime(date);
            application.setUpdateTime(date);
            applicationDao.insert(application);
        }
    }

    /**
     * 初始化zk信息
     *
     * @param zkUrl
     * @param evn
     * @param appName
     * @param charset
     * @return
     */
    public synchronized static DataLoader init(String zkUrl,
                                               String evn,
                                               String appName,
                                               String charset) {

        if (instance == null) {
            try {
                instance = new DataLoader(zkUrl, evn, appName, charset);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return instance;
    }

    public String getZkUrl() {
        return zkUrl;
    }

    public void setZkUrl(String zkUrl) {
        this.zkUrl = zkUrl;
    }

    public String getEvn() {
        return evn;
    }

    public void setEvn(String evn) {
        this.evn = evn;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }


    /**
     * 添加field监听
     *
     * @param name
     * @param targetObj
     * @param field
     * @return
     */
    public static PropertyListener addConfigListener(String name,
                                                     Object targetObj,
                                                     Field field) throws Exception {

        Configuration configuration = configurationMap.get(name);
        //为空创建
        if (configuration == null) {
            if (field.getType().getSimpleName().equals(STRING_TYPE)) {
                //获取String类型的值
                String data = zkClient.getString(name);
                //设置字段的值
                field.set(targetObj, data);
                configuration = new StringConfiguration(name, data);
            } else if (field.getType().getSimpleName().equals(MAP_TYPE)) {
                //获取Map类型的值
                Map<String, String> data = property2Map(zkClient.getDataMap(name));
                //设置字段的值
                field.set(targetObj, data);
                configuration = new MapConfiguration(name, data);
            }
            configurationMap.put(name, configuration);
        }
        return configuration.addListener(field, targetObj);
    }

    /**
     * 添加method监听器
     *
     * @param name
     * @param targetObj
     * @param method
     * @return
     */
    public static PropertyListener addConfigListener(String name,
                                                     Object targetObj,
                                                     Method method) throws Exception {
        Class<?>[] types = method.getParameterTypes();
        String type = types[0].getSimpleName();
        Configuration configuration = configurationMap.get(name);
        //为空创建
        if (configuration == null) {
            if (type.equals(STRING_TYPE)) {
                String data = zkClient.getString(name);
                configuration = new StringConfiguration(name, data);
            } else if (type.equals(MAP_TYPE)) {
                Map<String, String> data = property2Map(zkClient.getDataMap(name));
                configuration = new MapConfiguration(name, data);
            }
            configurationMap.put(name, configuration);
        }
        return configuration.addListener(method, targetObj);
    }

    /**
     * 数据处理
     *
     * @param propsMap     数据
     * @param zkNodeAction zk节点操作类型
     */
    public void handle(Map<String, String> propsMap, ZKNodeAction zkNodeAction) {
        switch (zkNodeAction) {
            case NODE_ADDED:
                //创建节点，无需处理，因为后台已经创建了节点
                break;
            case NODE_REMOVED:
                //删除节点，无需处理
                break;
            case NODE_UPDATED:
                //更新节点数据，数据更新进行监听
                listenUpdate(propsMap);
                break;
            default:
                break;
        }
    }

    /**
     * 处理监听数据节点数据更新
     *
     * @param paramMap
     */
    public void listenUpdate(Map<String, String> paramMap) {
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            Configuration configuration = configurationMap.get(entry.getKey());
            //如果数据是以String类型存储
            if (configuration.getType().equals(STRING_TYPE)) {
                ((StringConfiguration) configuration).setValue(entry.getValue());
                configuration.trigger();
            } else if (configuration.getType().equals(MAP_TYPE)) {
                Properties properties = new Properties();
                try {
                    properties.load(new StringReader(entry.getValue()));
                } catch (IOException e) {
                    log.info("string load to properties error:{}", e);
                }
                Map<String, String> map = property2Map(properties);
                ((MapConfiguration) configuration).setValue(map);
                configuration.trigger();
            }
        }
    }

    /**
     * 属性文件转换成Map
     *
     * @param properties
     * @return
     */
    public static Map<String, String> property2Map(Properties properties) {
        Map<String, String> map = Maps.newHashMap();
        for (Object key : properties.keySet()) {
            map.put((String) key, properties.getProperty((String) key));
        }
        return map;
    }

    public static void close() throws Exception {
        zkClient.close();
        if (configurationMap != null) {
            configurationMap.clear();
        }
    }
}
