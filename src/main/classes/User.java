package main.classes;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class User {

    private int id;
    private String username;
    private String name;
    private String surname;
    private String email;
    private String password;

    public User(){
        id = 0;
        username = null;
        name = null;
        surname = null;
        email = null;
        password = null;
    }

    public User(int id, String username, String name, String surname, String email, String password) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getSurname() {
        return surname;
    }

    public String getUsername() {
        return username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId(){
        return this.id;
    }


    public int putUser(Connection c) {
        Statement stmt = null;
        try{
            stmt = c.createStatement();
            String sql = "INSERT INTO users (username,name,surname,email,password) " +
                    "VALUES ('"+this.getUsername()+"','"+this.getName()+"','"+this.getSurname()+"','"+this.getEmail()+"','"+this.getPassword()+"');";
            stmt.executeUpdate(sql);
            int id = stmt.getGeneratedKeys().getInt(1);
            stmt.close();
            c.commit();
            return id;
        }catch (Exception e) {
            return -1;
        }
    }

    public User getUser(Connection c){
        Statement stmt = null;
        try{
            User user = null;
            stmt = c.createStatement();
            String sql = "SELECT * FROM users WHERE username='"+this.username+"' AND password='"+this.password+"';";
            ResultSet rs = stmt.executeQuery( sql );
            while (rs.next()) {
                user = new User(rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        rs.getString("email"),
                        rs.getString("password"));
            }
            rs.close();
            stmt.close();
            c.commit();
            return user;
        }catch (Exception e) {
            return null;
        }
    }
}
