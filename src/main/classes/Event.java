package main.classes;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import main.interfaces.EventBase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;

public class Event implements EventBase {

    private int id;
    private int type;
    private String title;
    private User creator;
    private LocalDateTime dateTime;
    private int nPartecipanti;
    private double review;

    public Event() {
        id = 0;
        type = 0;
        title = null;
        creator = null;
        dateTime = null;
        nPartecipanti = 0;
        review = 0;
    }

    public Event(int id, int type, String title, User creator, LocalDateTime dateTime, int nPartecipanti, double review) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.creator = creator;
        this.dateTime = dateTime;
        this.nPartecipanti = nPartecipanti;
        this.review = review;
    }

    @Override
    public void setId(int id){
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int getType() {
        return this.type;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public void setCreator(User user) {
        this.creator = user;
    }

    @Override
    public User getCreator() {
        return this.creator;
    }

    @Override
    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public LocalDateTime getDateTime() {
        return this.dateTime;
    }

    @Override
    public void setNPartecipanti(int nPartecipanti) {
        this.nPartecipanti = nPartecipanti;
    }

    @Override
    public int getNPartecipanti() {
        return this.nPartecipanti;
    }

    @Override
    public void setReview(double review) {
        this.review = review;
    }

    @Override
    public double getReview() {
        return this.review;
    }

    public int putEvent(Connection c) {
        Statement stmt = null;
        try{
            stmt = c.createStatement();
            String sql = "INSERT INTO events (eventType,title,dateTime,nPartecipanti,review,creatorId) " +
                    "VALUES ('"+this.getType()+"','"+this.getTitle()+"','"+this.getDateTime().toString()+"','"+this.getNPartecipanti()+"','"+this.getReview()+"', "+this.getCreator().getId()+");";
            stmt.executeUpdate(sql);
            int id = stmt.getGeneratedKeys().getInt(1);
            stmt.close();
            c.commit();
            return id;
        }catch (Exception e) {
            System.out.println(e.getClass().getName() + ": "+ e.getMessage());
            return -1;
        }
    }

    public void updateEvent(Connection c) {
        Statement stmt = null;
        try{
            stmt = c.createStatement();
            String sql = "UPDATE events SET eventType = " + this.getType() +
                    ", title = '" + this.getTitle() + "'" +
                    ", dateTime = '" +this.getDateTime().toString() + "'" +
                    ", nPartecipanti = " + this.getNPartecipanti() +
                    " WHERE id = " + this.getId();
            stmt.executeUpdate(sql);
            int id = stmt.getGeneratedKeys().getInt(1);
            stmt.close();
            c.commit();
        }catch (Exception e) {
            System.out.println(e.getClass().getName() + ": "+ e.getMessage());
        }
    }

    public void deleteEvent(Connection c, int id){
        Statement stmt = null;
        try{
            stmt = c.createStatement();
            String sql = "DELETE FROM events WHERE id = " + id;
            stmt.executeUpdate(sql);
            stmt.close();
            c.commit();
        }catch (Exception e) {
            System.out.println(e.getClass().getName() + ": "+ e.getMessage());
        }
    }

    public ArrayList<Event> getUserCreatedEvents (User user, Connection c){
        Statement stmt = null;
        ArrayList<Event> createdEventSet = new ArrayList<>();
        try {
            stmt = c.createStatement();
            String sql = "SELECT * FROM events WHERE creatorId = " + user.getId();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                createdEventSet.add(
                        new Event(
                                rs.getInt("id"),
                                rs.getInt("eventType"),
                                rs.getString("title"),
                                new User(),
                                LocalDateTime.parse(rs.getString("dateTime")),
                                rs.getInt("nPartecipanti"),
                                rs.getDouble("review")
                        )
                );
            }
            rs.close();
            stmt.close();
            c.commit();
            return createdEventSet;
        }catch (Exception e){
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return null;
    }

}
