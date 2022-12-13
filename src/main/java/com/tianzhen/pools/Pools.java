package com.tianzhen.pools;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.sql.Connection;

public class Pools {
    private static Driver driver;
    static {
        try {
            driver = new org.postgresql.Driver();
            DriverManager.registerDriver(driver);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    private static Map<Connection,Integer> pool = new HashMap<>();
    private static String url="jdbc:postgresql://localhost:5432/postgres";
    private static String user="postgres";
    private static String password="141592";
    /**
     * 从连接池中获取一个空闲连接，如果没有则创建一个新的连接返回
     * synchronized确保并发请求时，数据库连接的正确性
     * @return
     */
    public synchronized static Connection getConnection(){
        for (Map.Entry entry:pool.entrySet()){
            if (entry.getValue().equals(1)) {
                entry.setValue(0);
                return (Connection) entry.getKey();
            }
        }
        Connection connection=null;
        try {
            connection=DriverManager.getConnection(url,user,password);
            pool.put(connection,0);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return connection;
    }
    /**
     * 释放connection连接对象
     * @param connection
     */
    public synchronized static void releaseConnection(Connection connection){
        pool.put(connection,1);
    }
}
