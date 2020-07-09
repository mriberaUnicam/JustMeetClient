package main.interfaces;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Date;

public interface Event {

    /**
     * Tipo di evento generico
     */
    public static int GENERAL_TYPE = 0;

    /**
     * Tipo di evento giocabile
     */
    public static int GAME_TYPE = 1;

    /**
     * Tipo di evento di studio
     */
    public static int STUDY_TYPE = 2;

    /**
     * Assegna all'evento un determinato tipo
     *
     * @param type
     *              tipo dell'evento
     */
    public void setType(int type);

    /**
     * Restituisce il tipo di evento
     *
     * @return tipo di evento
     */
    public int getType();

    /**
     * Restituisce l'evento
     *
     * @return l'evento in questione
     */
    public Event getEvent();

    /**
     * Imposta il nome dell'evento
     *
     * @param title
     */
    public void setTitle(String title);

    /**
     * Restituisce il nome dell'evento
     *
     * @return title
     */
    public String getTitle();

    /**
     * Imposta il nome dell creatore dell'evento
     *
     * @param username
     */
    public void setCreator(String username);

    /**
     * Restituisce il nome del creatore dell'evento
     *
     * @return nome del creatore dell'evento
     */
    public String getCreator();

    /**
     * Imposta data ed orario dell'evento
     *
     * @param datetime
     */
    public void setDateTime(LocalDateTime dateTime);

    /**
     * Ritorna la data e l'orario dell'evento
     *
     * @return ora e data dell'evento
     */
    public LocalDateTime getDateTime();

    /**
     * Imposta il numero massimo di partecipanti che si possono iscrivere all'evento
     *
     * @param nPartecipanti
     */
    public void setNPartecipanti(int nPartecipanti);

    /**
     * Ritorna il numero massimo dei partecipanti che si possono iscrivere all'evento
     *
     * @return ndei partecipanti
     */
    public int getNPartecipanti();

}
