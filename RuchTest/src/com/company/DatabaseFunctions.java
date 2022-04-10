package com.company;

import java.sql.*;

public class DatabaseFunctions {
    private final String url = "jdbc:postgresql://sxterm.mat.umk.pl/mgorski?user=mgorski&password=Damoria12.,mn&ssl=false";
    public void addUser(User user) throws SQLException {
        Connection conn = DriverManager.getConnection(url);
        String SQL = "INSERT INTO users(id_u, login, password)" +
                "VALUES (?, ?, ? )";
        PreparedStatement pstmt = conn.prepareStatement(SQL);
        pstmt.setInt(1,this.getUsersTableSize() +1);
        pstmt.setString(2, user.getLogin());
        pstmt.setString(3, user.getPassword());
        pstmt.executeUpdate();
        pstmt.close();
        conn.close();
    }

    public boolean checkIfUserExistsInDatabase(User user) throws SQLException {
        boolean flag;
        Connection conn = DriverManager.getConnection(url);
        String SQL = "SELECT id_u FROM users WHERE login = ? ";
        PreparedStatement pstmt = conn.prepareStatement(SQL);
        pstmt.setString(1, user.getLogin());
        ResultSet rs = pstmt.executeQuery();
        if(rs.next() == false){
            flag = false;
        }
        else{
            flag =  true;
        }
        rs.close();
        pstmt.close();
        conn.close();
        return flag;
    }

    public int getUsersTableSize() {
        int size = 0;
        try {

            Connection conn = DriverManager.getConnection(this.url);
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rset = stmt.executeQuery("SELECT * FROM users");
            if (rset != null)
            {
                rset.last();
                size = rset.getRow();
            }
            rset.close();
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            System.err.println("Blad Postgres: " + e.getErrorCode() + " " + e.getMessage());
        }
        return size;

    }



}
