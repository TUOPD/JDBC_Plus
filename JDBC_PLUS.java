import com.mysql.cj.conf.ConnectionUrlParser;

import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;

public class JDBC_PLUS {
    private JDBC_PLUS(){};
    private  static Connection conn;
    //SELECT
    public static <T> List<T> select(String table, T e) throws Exception {
        Field[] fields = e.getClass().getDeclaredFields();
        Class<?> type = e.getClass();
        List<T> list = new ArrayList<T>();
        String querySQL = "SELECT * FROM " + table;
        PreparedStatement preparedStatement = conn.prepareStatement(querySQL);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            T instance = (T) e.getClass().getDeclaredConstructor().newInstance();
            for (Field field : fields) {
                field.setAccessible(true);
                String fieldName = field.getName();
                Object value = resultSet.getObject(fieldName);
                if (value != null) {
                    if (field.getType().isPrimitive()) {
                        if (field.getType() == int.class) {
                            field.setInt(instance, (int) value);
                        } else if (field.getType() == float.class) {
                            field.setFloat(instance, (float) value);
                        }
                    } else {
                        field.set(instance, value);
                    }
                }
            }
            list.add(instance);
        }
        return list;
    }
    public static <T> T  selectone(String table, Map<Object,Object> map, T e) throws Exception {
        Field[] fields =e.getClass().getDeclaredFields();
        if (map.size()>1){
            System.out.println("The map of this interface only needs one key-value pair");
            return null;
        }else {
            String querySQL = "SELECT * FROM " + table+" WHERE "+map.entrySet().iterator().next().getKey()+" = '"+map.entrySet().iterator().next().getValue()+"' ";
            PreparedStatement preparedStatement = conn.prepareStatement(querySQL);
            ResultSet resultSet = preparedStatement.executeQuery();
            T instance = (T) e.getClass().getDeclaredConstructor().newInstance();
            while (resultSet.next()) {
                for (Field field : fields) {
                    field.setAccessible(true);
                    String fieldName = field.getName();
                    Object value = resultSet.getObject(fieldName);
                    if (value != null) {
                        if (field.getType().isPrimitive()) {
                            if (field.getType() == int.class) {
                                field.setInt(instance, (int) value);
                            } else if (field.getType() == float.class) {
                                field.setFloat(instance, (float) value);
                            }
                        } else {
                            field.set(instance, value);
                        }
                    }
                }
            }
            return instance;
        }
    }
    public static <T> List<T> selectlike(String table,Map<Object,Object> map,T e) throws Exception{
        Field[] fields =e.getClass().getDeclaredFields();
        List<T> list = new ArrayList<T>();
        if (map.size()>1){
            System.out.println("The map of this interface only needs one key-value pair");
            return null;
        }else {
            String querySQL = "SELECT * FROM " + table+" WHERE "+map.entrySet().iterator().next().getKey()+" LIKE '%"+map.entrySet().iterator().next().getValue()+ "%'";
            PreparedStatement preparedStatement = conn.prepareStatement(querySQL);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                T instance = (T) e.getClass().getDeclaredConstructor().newInstance();
                for (Field field : fields) {
                    field.setAccessible(true);
                    String fieldName = field.getName();
                    Object value = resultSet.getObject(fieldName);
                    if (value != null) {
                        if (field.getType().isPrimitive()) {
                            if (field.getType() == int.class) {
                                field.setInt(instance, (int) value);
                            } else if (field.getType() == float.class) {
                                field.setFloat(instance, (float) value);
                            }
                        } else {
                            field.set(instance, value);
                        }
                    }
                }
                list.add(instance);
            }
            return list;
        }

    }
    public static <T> List<T> selectnot(String table,Map<Object,Object> map,T e) throws Exception {
        Field[] fields = e.getClass().getDeclaredFields();
        List<T> list = new ArrayList<T>();
        if (map.size() > 1) {
            System.out.println("The map of this interface only needs one key-value pair");
            return null;
        } else {
            String querySQL = "SELECT * FROM " + table + " WHERE " + map.entrySet().iterator().next().getKey() + " !=  '" + map.entrySet().iterator().next().getValue()+"' ";
            PreparedStatement preparedStatement = conn.prepareStatement(querySQL);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                T instance = (T) e.getClass().getDeclaredConstructor().newInstance();
                for (Field field : fields) {
                    field.setAccessible(true);
                    String fieldName = field.getName();
                    Object value = resultSet.getObject(fieldName);
                    if (value != null) {
                        if (field.getType().isPrimitive()) {
                            if (field.getType() == int.class) {
                                field.setInt(instance, (int) value);
                            } else if (field.getType() == float.class) {
                                field.setFloat(instance, (float) value);
                            }
                        } else {
                            field.set(instance, value);
                        }
                    }
                }
                list.add(instance);
            }
            return list;
        }
    }
    public static <T> T selectid(String table,T e,int id) throws Exception {
        Field[] fields = e.getClass().getDeclaredFields();
        String querySQL = "SELECT * FROM " + table + " WHERE id = '"+id+"' ";
        PreparedStatement preparedStatement = conn.prepareStatement(querySQL);
        ResultSet resultSet = preparedStatement.executeQuery();
        T instance = (T) e.getClass().getDeclaredConstructor().newInstance();
        while (resultSet.next()) {
            for (Field field : fields) {
                field.setAccessible(true);
                String fieldName = field.getName();
                Object value = resultSet.getObject(fieldName);
                if (value != null) {
                    if (field.getType().isPrimitive()) {
                        if (field.getType() == int.class) {
                            field.setInt(instance, (int) value);
                        } else if (field.getType() == float.class) {
                            field.setFloat(instance, (float) value);
                        }
                    } else {
                        field.set(instance, value);
                    }
                }
            }
        }
        return instance;
    }
    public static  int Count(String table) throws Exception {
        String querySQL = "SELECT COUNT(*) FROM " + table;
        PreparedStatement preparedStatement = conn.prepareStatement(querySQL);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
        else {
            return 0;
        }
    }
    //insert
    public static int insert(String table, Object object) throws Exception {
        Field[] fields = object.getClass().getDeclaredFields();
        StringBuilder columns = new StringBuilder();
        StringBuilder values = new StringBuilder();

        for (Field field : fields) {
            field.setAccessible(true);
            Object value = field.get(object);

            if (!field.getName().equals("id")) {
                columns.append(field.getName()).append(",");
                values.append("'").append(value).append("',");
            }
        }

        String sql = "INSERT INTO " + table + " (" + columns.substring(0, columns.length() - 1) + ") VALUES (" + values.substring(0, values.length() - 1) + ")";

        PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        int result = preparedStatement.executeUpdate();

        if (result > 0) {
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                int generatedId = rs.getInt(1);
                // 这里可以将生成的主键值设置回对象中
            }
            return result;
        } else {
            return 0;
        }
    }


    //update
    public static <T> int updateid(String table , T e,int id) throws Exception {
        Field[] fields = e.getClass().getDeclaredFields();
        String sql="UPDATE "+table+" SET ";
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            if(i==fields.length-1){
                sql+=fields[i].getName()+" = '"+fields[i].get(e)+"'";
            }else {
                sql+=fields[i].getName()+" = '"+fields[i].get(e)+"',";
            }
        }

        sql+=" WHERE id= '"+id+"' ";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        int  result = preparedStatement.executeUpdate();
        if (result>0) {
            return result;
        }else {
            return 0;
        }
    }
    public static <T> int update(String table , T e,Map<Object,Object> map) throws Exception {
        Field[] fields = e.getClass().getDeclaredFields();
        if (map.size() > 1) {
            System.out.println("The map of this interface only needs one key-value pair");
            return 0;
        }else {
            String sql="UPDATE "+table+" SET ";
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                if(i==fields.length-1){
                    sql+=fields[i].getName()+" = '"+fields[i].get(e)+"'";
                }else {
                    sql+=fields[i].getName()+" = '"+fields[i].get(e)+"',";
                }
            }
            sql+=" WHERE "+map.entrySet().iterator().next().getKey()+" = '"+map.entrySet().iterator().next().getValue()+"' ";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            int  result = preparedStatement.executeUpdate();
            if (result>0) {
                return result;
            }
            else {
                return 0;
            }
        }
    }
    //DELETE
    public static  int deleteid(String table,int id) throws Exception {
        String sql = "DELETE FROM "+ table+" WHERE id = " + id;
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        int  result = preparedStatement.executeUpdate();
        if (result>0) {
            return result;
        }else {
            return 0;
        }
    }
    public static int deletemap(String table,Map<Object,Object> map) throws Exception {
        String sql="DELETE FROM "+table+" WHERE "+map.entrySet().iterator().next().getKey()+" = '"+map.entrySet().iterator().next().getValue()+"' ";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        int  result = preparedStatement.executeUpdate();
        if (result>0) {
            return result;
        }else {
            return 0;
        }
    }
    public static <T> int  deleteObject(String table,T e) throws Exception {
        Field[] fields = e.getClass().getDeclaredFields();
        String sql="DELETE FROM "+ table+" WHERE ";
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            if(i==fields.length-1){
                sql+=fields[i].getName()+" = '"+fields[i].get(e)+"'";
            }
            else{
                sql+=fields[i].getName()+" = '"+fields[i].get(e)+"' AND ";
            }
        }
        System.out.println(sql);
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        int  result = preparedStatement.executeUpdate();
        if (result>0) {
            return result;
        }else {
            return 0;
        }
    }
    static {
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            FileInputStream fileInputStream= null;
            fileInputStream = new FileInputStream("path");
            Properties properties = new Properties();
            properties.load(fileInputStream);
            Set<String> set = properties.stringPropertyNames();
            int i=0;
            String url="jdbc:mysql://localhost:3306/";
            String user="";
            String password="";
            for (String key : set) {
                if (i==1){
                    url+=properties.getProperty(key);
                } else if (i==2) {
                    user=properties.getProperty(key);
                }
                else if (i==0) {
                    password=properties.getProperty(key);
                }
                i++;
            }
            conn = DriverManager.getConnection(url, user, password);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}
