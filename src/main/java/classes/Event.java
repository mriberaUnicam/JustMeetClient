package classes;

import interfaces.EventBase;
import utils.SendMail;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class Event implements EventBase {

    private int id;
    private String type;
    private String title;
    private String description;
    private User creator;
    private LocalDateTime dateTime;
    private int nPartecipanti;
    private int nPartecipantiMax;
    private int active;

    public Event() {
        id = 0;
        type = null;
        title = null;
        creator = null;
        dateTime = null;
        description = null;
        nPartecipanti = 0;
        nPartecipantiMax = 0;
        active = 1;
    }

    public Event(int id, String type, String title, String description, User creator, LocalDateTime dateTime, int nPartecipanti, int nPartecipantiMax, int active) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.creator = creator;
        this.dateTime = dateTime;
        this.nPartecipanti = nPartecipanti;
        this.nPartecipantiMax = nPartecipantiMax;
        this.active = active;
        this.description = description;
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
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getType() {
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
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return this.description;
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
    public void setNPartecipantiMax(int nPartecipanti) {
        this.nPartecipantiMax = nPartecipanti;
    }

    @Override
    public int getNPartecipantiMax() {
        return this.nPartecipantiMax;
    }

    @Override
    public int getNPartecipanti() {
        return nPartecipanti;
    }

    @Override
    public void setActive(int active) {
        this.active = active;
    }

    @Override
    public int getActive() {
        return active;
    }

    public double getReview(Connection c) {
        Statement stmt = null;
        double review = 0;
        try {
            stmt = c.createStatement();
            String sql = "SELECT AVG(rating) FROM user_event_rating WHERE eventId = " + this.getId();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                review = rs.getDouble("avg(rating)");
            }
            rs.close();
            stmt.close();
            c.commit();
            return review;
        }catch (Exception e){
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return 0;
    }

    public int putEvent(Connection c) {
        Statement stmt = null;
        try{
            stmt = c.createStatement();
            String sql = "INSERT INTO events (eventType,title,description,dateTime,nPartecipantiMax,active,creatorId) " +
                    "VALUES (\'"+this.getType().replace("'","''")+"\',\'"+this.getTitle().replace("'","''")+"\',\'"+this.getDescription().replace("'","''")+"\',\'"+this.getDateTime().toString()+"\',"+this.getNPartecipantiMax()+",\'"+this.getActive()+"\', "+this.getCreator().getId()+");";
            stmt.executeUpdate(sql);
            int id = stmt.getGeneratedKeys().getInt(1);
            stmt.close();
            c.commit();
            this.setId(id);
            creator.userJoinEvent(c, this);
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
            String sql = "UPDATE events SET eventType = \'" + this.getType().replace("'","''") + "\'"+
                    ", title = \'" + this.getTitle().replace("'","''") + "\'" +
                    ", description = \'" + this.getDescription().replace("'","''") + "\'" +
                    ", dateTime = \'" + this.getDateTime().toString() + "\'" +
                    ", nPartecipantiMax = " + this.getNPartecipantiMax() +
                    ", active = " + this.getActive() +
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
            String sql = "UPDATE events SET active = 0 WHERE id = " + id;
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
            String sql = "SELECT * FROM events WHERE creatorId = " + user.getId() + " AND active = 1";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                createdEventSet.add(
                        new Event(
                                rs.getInt("id"),
                                rs.getString("eventType"),
                                rs.getString("title"),
                                rs.getString("description"),
                                new User(),
                                LocalDateTime.parse(rs.getString("dateTime")),
                                rs.getInt("nPartecipanti"),
                                rs.getInt("nPartecipantiMax"),
                                rs.getInt("active")
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

    public ArrayList<Event> getAllActiveEvents(Connection c){
        Statement stmt = null;
        ArrayList<Event> createdEventSet = new ArrayList<>();
        try {
            stmt = c.createStatement();
            String sql = "SELECT * FROM events WHERE active = 1";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                createdEventSet.add(
                        new Event(
                                rs.getInt("id"),
                                rs.getString("eventType"),
                                rs.getString("title"),
                                rs.getString("description"),
                                new User(),
                                LocalDateTime.parse(rs.getString("dateTime")),
                                rs.getInt("nPartecipanti"),
                                rs.getInt("nPartecipantiMax"),
                                rs.getInt("active")
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

    public ArrayList<Event> getAvailableEvents(Connection c, User user){
        Statement stmt = null;
        ArrayList<Event> availableEvents = new ArrayList<>();
        try {
            stmt = c.createStatement();
            String sql = "SELECT * FROM events WHERE id NOT IN (SELECT eventId FROM user_event WHERE userId = " + user.getId() + ") AND active = 1";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                availableEvents.add(
                        new Event(
                                rs.getInt("id"),
                                rs.getString("eventType"),
                                rs.getString("title"),
                                rs.getString("description"),
                                new User(),
                                LocalDateTime.parse(rs.getString("dateTime")),
                                rs.getInt("nPartecipanti"),
                                rs.getInt("nPartecipantiMax"),
                                rs.getInt("active")
                        )
                );
            }
            rs.close();
            stmt.close();
            c.commit();
            return availableEvents;
        }catch (Exception e){
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return null;
    }

    public ArrayList<Event> getUserSignedEvent  (Connection c, User user){
        Statement stmt = null;
        ArrayList<Event> userSignedEvent = new ArrayList<>();
        try {
            stmt = c.createStatement();
            String sql = "SELECT * FROM events WHERE id IN (SELECT eventId FROM user_event WHERE userId = " + user.getId() + ")";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                userSignedEvent.add(
                        new Event(
                                rs.getInt("id"),
                                rs.getString("eventType"),
                                rs.getString("title"),
                                rs.getString("description"),
                                new User(),
                                LocalDateTime.parse(rs.getString("dateTime")),
                                rs.getInt("nPartecipanti"),
                                rs.getInt("nPartecipantiMax"),
                                rs.getInt("active")
                        )
                );
            }
            rs.close();
            stmt.close();
            c.commit();
            return userSignedEvent;
        }catch (Exception e){
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return null;
    }

    public ArrayList<Event> getUserPastEvents(Connection c, User user){
        Statement stmt = null;
        ArrayList<Event> userPastEvents = new ArrayList<>();
        try {
            stmt = c.createStatement();
            String sql = "SELECT * FROM events WHERE id IN (SELECT eventId FROM user_event WHERE userId = " + user.getId() + ") AND dateTime < '"+LocalDateTime.now().toString()+"';";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                userPastEvents.add(
                        new Event(
                                rs.getInt("id"),
                                rs.getString("eventType"),
                                rs.getString("title"),
                                rs.getString("description"),
                                new User(),
                                LocalDateTime.parse(rs.getString("dateTime")),
                                rs.getInt("nPartecipanti"),
                                rs.getInt("nPartecipantiMax"),
                                rs.getInt("active")
                        )
                );
            }
            rs.close();
            stmt.close();
            c.commit();
            return userPastEvents;
        }catch (Exception e){
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return null;
    }

    public void addPartecipant(Connection c) {
        this.nPartecipanti++;
        Statement stmt = null;
        try {
            stmt = c.createStatement();
            String sql = "UPDATE events SET nPartecipanti = " + this.getNPartecipanti() +
                    " WHERE id = " + this.getId();
            stmt.executeUpdate(sql);
            int id = stmt.getGeneratedKeys().getInt(1);
            stmt.close();
            c.commit();
        } catch (Exception e) {
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void removePartecipant(Connection c){
        this.nPartecipanti--;
        Statement stmt = null;
        try{
            stmt = c.createStatement();
            String sql = "UPDATE events SET nPartecipanti = " + this.getNPartecipanti() +
                    " WHERE id = " + this.getId();
            stmt.executeUpdate(sql);
            int id = stmt.getGeneratedKeys().getInt(1);
            stmt.close();
            c.commit();
        }catch (Exception e) {
            System.out.println(e.getClass().getName() + ": "+ e.getMessage());
        }
    }

    public void NotifyUpcomingEvents(Connection c){
        Statement stmt = null;
        try {
            stmt = c.createStatement();
            String sql = "SELECT events.title, events.dateTime, username, email FROM events JOIN user_event ue on events.id = ue.eventId JOIN users u on ue.userId = u.id AND '"+LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES).plusHours(3).toString()+"' = dateTime;";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                new SendMail().send(rs.getString("email"), rs.getString("username"), rs.getString("title"));
            }
            rs.close();
            stmt.close();
            c.commit();
        }catch (Exception e){
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void getDeleteEvents(Connection c){
        Statement stmt = null;
        try {
            stmt = c.createStatement();
            String sql = "SELECT id FROM events WHERE '"+LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES).minusHours(24).toString()+"' < dateTime;";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                    this.deleteEvent(c, rs.getInt("id"));
            }
            rs.close();
            stmt.close();
            c.commit();
        }catch (Exception e){
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public ArrayList<String> getEventTypes(Connection c){
        Statement stmt = null;
        ArrayList<String> eventType = new ArrayList<>();
        try {
            stmt = c.createStatement();
            String sql = "SELECT * FROM event_type;";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                eventType.add(rs.getString("id") + " - " + rs.getString("event_type"));
            }
            rs.close();
            stmt.close();
            c.commit();
            return eventType;
        }catch (Exception e){
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return null;
    }

    public ArrayList<Event> findEvent(Connection c){
        Statement stmt = null;
        ArrayList<Event> createdEventSet = new ArrayList<>();
        try {
            stmt = c.createStatement();
            String sql = "SELECT * FROM events WHERE (" +
                    "(title LIKE '%"+this.getTitle()+"%') " +
                    "AND (dateTime LIKE '%"+this.getDateTime().toString().split("T")[0]+"%') " +
                    "AND (eventType LIKE '%"+this.getType()+"%'))";
            System.out.println(sql);
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                createdEventSet.add(
                        new Event(
                                rs.getInt("id"),
                                rs.getString("eventType"),
                                rs.getString("title"),
                                rs.getString("description"),
                                new User(),
                                LocalDateTime.parse(rs.getString("dateTime")),
                                rs.getInt("nPartecipanti"),
                                rs.getInt("nPartecipantiMax"),
                                rs.getInt("active")
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
