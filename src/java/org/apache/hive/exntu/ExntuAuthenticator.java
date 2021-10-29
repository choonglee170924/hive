package org.apache.hive.exntu;

import org.apache.hive.service.auth.PasswdAuthenticationProvider;
import org.springframework.security.providers.encoding.Md5PasswordEncoder;
import javax.security.sasl.AuthenticationException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ExntuAuthenticator implements PasswdAuthenticationProvider {
    private static final Md5PasswordEncoder md5Encoder = new Md5PasswordEncoder();
//    private static final ShaPasswordEncoder sha256Encoder = new ShaPasswordEncoder(256);

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://hadoop-3:3306/ranger";
        String id = "root";
        String pw = "exntu";
        Connection conn = DriverManager.getConnection(url,id,pw);
        return conn;
    }

    @Override
    public void Authenticate(String user, String password) throws AuthenticationException {
        String encryptPassword = md5Encoder.encodePassword(password, user);
        ArrayList<Map> resultList = new ArrayList<>();
        Date date = new Date();
        try {
            Connection conn = getConnection();
            String sql = "select id, login_id, password from x_portal_user where login_id=? and password=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user);
            pstmt.setString(2, encryptPassword);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                Map<String, String> map = new HashMap<>();
                String id = rs.getString("id");
                String loginId = rs.getString("login_id");
                map.put("id", id);
                map.put("login_id", loginId);
                System.out.println(date.toString() + "ExntuAuthenticator - id : " + id + ", LoginId : " + loginId);
                resultList.add(map);
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(date.toString() + "ExntuAuthenticator - unable to get JDBC connection -> " + e.getMessage());
        }
        System.out.println(date.toString() + "ExntuAuthenticator - resultList size: " + resultList.size());
        if (resultList.size() == 1)
            return;
        throw new AuthenticationException("org.apache.hive.exntu.ExntuAuthenticator: Error validating user");
    }
}
