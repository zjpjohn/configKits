package com.zjp.config.zk;

import com.google.common.collect.Maps;
import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.framework.CuratorFrameworkFactory;
import com.netflix.curator.framework.recipes.cache.PathChildrenCache;
import com.netflix.curator.framework.recipes.cache.PathChildrenCacheEvent;
import com.netflix.curator.framework.recipes.cache.PathChildrenCacheListener;
import com.netflix.curator.retry.RetryNTimes;
import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringReader;
import java.net.InetAddress;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * author:zjprevenge
 * time: 2016/7/31
 * copyright all reserved
 */
public class ZkClient {
    private static final Logger log = LoggerFactory.getLogger(ZkClient.class);
    //zk根目录
    private static final String ZK_ROOT = "config";
    private static final String ZK_CONFIG_DATA_NODE = "data";
    private static final String ZK_CONFIG_MACHINE_NODE = "machine";
    private static final int ZK_RETRY_TIMES = Integer.MAX_VALUE;
    private static final int ZK_RETRY_WAIT_TIME_MS = 5000;
    private static final String ZK_PATH_SEPARATOR = "/";
    private CuratorFramework curator;
    private PathChildrenCache nodeCache;
    private String zkUrl;
    private String appName;
    private String env;
    private String charset = "UTF-8";
    private String dataPath;
    private String machinePath;
    private static ZkClient zkClient;
    private ExecutorService pool;

    private ZkClient(String zkUrl, String appName, String env, String charset) {
        this.zkUrl = zkUrl;
        this.appName = appName;
        this.env = env;
        if (StringUtils.isNotBlank(charset)) {
            this.charset = charset;
        }
    }

    public synchronized static ZkClient init(String zkUrl,
                                             String appName,
                                             String env,
                                             String charset) {
        if (zkClient == null) {
            zkClient = new ZkClient(zkUrl, appName, env, charset);

        }
        return zkClient;
    }

    public ZkClient build(DataHandle dataHandle) throws Exception {
        log.info("init config zk...");
        //创建zk客户端
        curator = CuratorFrameworkFactory
                .builder()
                .connectString(zkUrl)
                .connectionTimeoutMs(5000)
                .sessionTimeoutMs(2000)
                .canBeReadOnly(false)
                .namespace(ZK_ROOT)
                .retryPolicy(
                        new RetryNTimes(ZK_RETRY_TIMES, ZK_RETRY_WAIT_TIME_MS))
                .build();
        //启动连接
        curator.start();
        //构建存储数据的路径:/config/data/appName/env
        dataPath = new StringBuilder(ZK_ROOT).append(ZK_PATH_SEPARATOR)
                .append(ZK_CONFIG_DATA_NODE)
                .append(ZK_PATH_SEPARATOR)
                .append(appName)
                .append(ZK_PATH_SEPARATOR)
                .append(env)
                .toString();
        //构建机器连接的路径:/config/data/appName/env/machine
        machinePath = new StringBuilder(ZK_ROOT).append(ZK_PATH_SEPARATOR)
                .append(ZK_CONFIG_DATA_NODE)
                .append(ZK_PATH_SEPARATOR)
                .append(appName)
                .append(ZK_PATH_SEPARATOR)
                .append(env)
                .append(ZK_CONFIG_MACHINE_NODE)
                .toString();
        //创建缓存节点
        nodeCache = new PathChildrenCache(curator, dataPath, true);
        pool = Executors.newSingleThreadExecutor();
        //创建机器连接节点
        createMachineNode();
        //添加监听
        addWatcher(dataHandle);
        return this;
    }

    /**
     * 监听数据变化
     *
     * @param dataHandle
     */
    public void addWatcher(final DataHandle dataHandle) throws Exception {
        nodeCache.getListenable().addListener(
                new PathChildrenCacheListener() {
                    public void childEvent(CuratorFramework curatorFramework,
                                           PathChildrenCacheEvent event) throws Exception {
                        //获取事件的路径
                        String path = event.getData().getPath();
                        //数据节点的名称
                        path = path.substring(path.indexOf("/") + 1);
                        //获取监听的数据
                        byte[] data = event.getData().getData();
                        Map<String, String> map = Maps.newHashMap();
                        map.put(path, new String(data, "utf-8"));

                        switch (event.getType()) {
                            case CHILD_ADDED://添加子节点
                                dataHandle.handle(map, ZKNodeAction.NODE_ADDED);
                                break;
                            case CHILD_REMOVED://删除子节点
                                dataHandle.handle(map, ZKNodeAction.NODE_REMOVED);
                                break;
                            case CHILD_UPDATED://更新子节点
                                dataHandle.handle(map, ZKNodeAction.NODE_UPDATED);
                                break;
                            default:
                                break;
                        }
                    }
                }
                , pool);
        nodeCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
    }

    /**
     * 获取数据节点的子节点
     *
     * @return
     * @throws Exception
     */
    public List<String> getDataChildren() throws Exception {
        return curator.getChildren().forPath(dataPath);
    }

    /**
     * 获取机器节点的子节点
     *
     * @return
     * @throws Exception
     */
    public List<String> getMachineChildren() throws Exception {
        return curator.getChildren().forPath(machinePath);
    }

    /**
     * 获取指定路径的数据，返回为String类型
     *
     * @param dPath
     * @return
     */
    public String getString(String dPath) throws Exception {
        String path = new StringBuilder(dataPath).append(ZK_PATH_SEPARATOR)
                .append(dPath)
                .toString();
        //判断路径是否存在
        Stat stat = curator.checkExists().forPath(path);
        if (stat == null) {
            return null;
        }
        byte[] data = curator.getData().forPath(path);
        return new String(data, charset);
    }

    /**
     * 获取指定名称的数据
     *
     * @param propsFile 节点名称
     * @return 属性类型
     * @throws Exception
     */
    public Properties getDataMap(String propsFile) throws Exception {
        String path = new StringBuilder(dataPath).append(ZK_PATH_SEPARATOR)
                .append(propsFile)
                .toString();
        //判断路径是否存在
        Stat stat = curator.checkExists().forPath(path);
        if (stat == null) {
            return null;
        }
        Properties properties = new Properties();
        byte[] data = curator.getData().forPath(path);
        //将节点下的数据加载到属性类中
        properties.load(new StringReader(new String(data, charset)));
        return properties;
    }

    /**
     * 获取本地ip地址
     *
     * @return
     * @throws Exception
     */
    private String getHostIdentity() throws Exception {
        return new StringBuilder(InetAddress.getLocalHost().getHostAddress())
                .append("$").append(appName).toString();
    }

    /**
     * 创建机器连接临时节点
     * <p></p>
     * 机器断开，节点消失
     */
    public void createMachineNode() {
        try {
            curator.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath(
                            new StringBuilder(machinePath)
                                    .append(ZK_PATH_SEPARATOR)
                                    .append(getHostIdentity().toString())
                                    .toString());
        } catch (Exception e) {
            log.error("create machine node error:{}", e);
            throw new RuntimeException("create machine node error");
        }
    }

    /**
     * 设置数据节点的值,节点存储的值为String类型
     *
     * @param dataPath 节点路径
     * @param data     节点值
     */
    public void setData(String dataPath, String data) throws Exception {
        String path = new StringBuilder(dataPath).append(ZK_PATH_SEPARATOR)
                .append(dataPath)
                .toString();
        Stat stat = curator.checkExists().forPath(path);
        //如果节点不存在，创建节点
        if (stat == null) {
            curator.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.PERSISTENT)
                    .forPath(path);
        }
        //设置数据
        curator.setData().forPath(path, data.getBytes());
    }

    /**
     * 设置节点的值，节点存储的值为Map类型
     *
     * @param dataPath 节点路径
     * @param data     节点值
     */
    public void setData(String dataPath, Map<String, String> data) throws Exception {
        String path = new StringBuilder(dataPath).append(ZK_PATH_SEPARATOR)
                .append(dataPath)
                .toString();
        Stat stat = curator.checkExists().forPath(path);
        //如果节点不存在，创建节点
        if (stat == null) {
            curator.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.PERSISTENT)
                    .forPath(path);
        }
        StringBuilder sbuilder = new StringBuilder();
        //将map构造成String
        for (Map.Entry<String, String> entry : data.entrySet()) {
            sbuilder.append(entry.getKey())
                    .append("=")
                    .append(entry.getValue())
                    .append(";");
        }
        //设置数据
        curator.setData().forPath(path, sbuilder.toString().getBytes());
    }

    /**
     * 查询指定应用和环境下的所有配置文件
     * /config/data/appName/evn
     *
     * @param appName 应用名称
     * @param evn     环境
     * @return
     * @throws Exception
     */
    public Map<String, String> getEvnAppData(String appName, String evn) throws Exception {
        String path = new StringBuilder(ZK_ROOT)
                .append(ZK_PATH_SEPARATOR)
                .append(ZK_CONFIG_DATA_NODE)
                .append(ZK_PATH_SEPARATOR)
                .append(appName)
                .append(ZK_PATH_SEPARATOR)
                .append(evn).toString();
        List<String> strings = curator.getChildren().forPath(path);
        Map<String, String> map = Maps.newHashMap();
        for (String string : strings) {
            map.put(string.substring(string.indexOf("/") + 1), string);
        }
        return map;
    }

    public void close() throws Exception {
        if (curator != null) {
            curator.close();
        }
        if (nodeCache != null) {
            nodeCache.close();
        }
        if (pool != null) {
            pool.shutdown();
        }

    }
}
