package utils;


import classes.Event;

import java.util.TimerTask;

public class TimeThread extends TimerTask {
    Event event = new Event();
    DatabaseConnection db = new DatabaseConnection();

    @Override
    public void run() {
        event.NotifyUpcomingEvents(db.getConnection());
        event.getDeleteEvents(db.getConnection());
    }

}
