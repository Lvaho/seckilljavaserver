package com.zjc.seckilldemo.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zjc.seckilldemo.pojo.User;
import com.zjc.seckilldemo.service.IUserService;
import com.zjc.seckilldemo.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserUtil {
    private static void createUser(int count) throws Exception {
        List<User> users = new ArrayList<>(count);
        // 生成用户
        for (int i = 0; i < count; i++) {
            User user = new User();
            user.setId(String.valueOf(13000000000L + i));
            user.setLoginCount(1);
            user.setNickname("user" + i);
            user.setRegisterDate(new Date());
            user.setSalt("1a2b3c4d");
            user.setPassword(SM3Util.inputPassToDbPass("123456", user.getSalt()));
            user.setIdentity("testsfz"+String.valueOf(i));
            users.add(user);
        }
        System.out.println("create user");
        ////插入数据库
        Connection conn = getConn();
        String sql = "insert into t_user(login_count, Nickname, register_date, salt, password, id, identity)values(?,?,?,?,?,?,?)";
        String SQL = "insert into t_deposit(id,identity,deposit)values(?,?,?)";
        //PreparedStatement pstmt = conn.prepareStatement(sql);
        PreparedStatement pstmt = conn.prepareStatement(SQL);
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            pstmt.setInt(1,i+2);
            pstmt.setString(2,user.getIdentity());
            pstmt.setBigDecimal(3, BigDecimal.valueOf(10000));
            pstmt.addBatch();
        }
        pstmt.executeBatch();
        pstmt.close();
        conn.close();
        System.out.println("insert to db");
        //登录，生成token
        //String urlString = "http://69.235.139.243:8888/login/doLogin";
        //File file = new File("C:\\Users\\Administrator\\Desktop\\config.txt");
        //if (file.exists()) {
        //    file.delete();
        //}
        //RandomAccessFile raf = new RandomAccessFile(file, "rw");
        //file.createNewFile();
        //raf.seek(0);
        //for (int i = 0; i < users.size(); i++) {
        //    User user = users.get(i);
        //    URL url = new URL(urlString);
        //    HttpURLConnection co = (HttpURLConnection) url.openConnection();
        //    co.setRequestMethod("POST");
        //    co.setDoOutput(true);
        //    OutputStream out = co.getOutputStream();
        //    String params = "mobile=" + user.getId() + "&password=" +
        //            SM3Util.inputPassToFormPass("123456");
        //    out.write(params.getBytes());
        //    out.flush();
        //    InputStream inputStream = co.getInputStream();
        //    ByteArrayOutputStream bout = new ByteArrayOutputStream();
        //    byte buff[] = new byte[1024];
        //    int len = 0;
        //    while ((len = inputStream.read(buff)) >= 0) {
        //        bout.write(buff, 0, len);
        //    }
        //    inputStream.close();
        //    bout.close();
        //    String response = new String(bout.toByteArray());
        //    ObjectMapper mapper = new ObjectMapper();
        //    RespBean respBean = mapper.readValue(response, RespBean.class);
        //    String userTicket = ((String) respBean.getObj());
        //    System.out.println("create userTicket : " + user.getId());
        //    String row = user.getId() + "," + userTicket;
        //    raf.seek(raf.length());
        //    raf.write(row.getBytes());
        //    raf.write("\r\n".getBytes());
        //    System.out.println("write to file : " + user.getId());
        //}
        //raf.close();
        //System.out.println("over");
    }
    private static Connection getConn() throws Exception {
        String url = "jdbc:mysql://localhost:3306/seckill?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai";
        String username = "root";
        String password = "123";
        String driver = "com.mysql.cj.jdbc.Driver";
        Class.forName(driver);
        return DriverManager.getConnection(url, username, password);
    }
    public static void main(String[] args) throws Exception {
        createUser(20000);
    }

}

