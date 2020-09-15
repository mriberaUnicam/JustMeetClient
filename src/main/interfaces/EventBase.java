package main.interfaces;

import main.classes.User;

import java.time.LocalDateTime;

public interface EventBase {

    /**
     * Ritorna l'id dell'evento
     *
     * @return id
     */
    public int getId();

    /**
     * Imposta l'id dellevento
     *
     * @param id
     */
    public void setId(int id);

    /**
     * Assegna all'evento un determinato tipo
     *
     * @param type
     *              tipo dell'evento
     */
    public void setType(String type);

    /**
     * Restituisce il tipo di evento
     *
     * @return tipo di evento
     */
    public String getType();

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
     * Imposta il nome dell'evento
     *
     * @param title
     */
    public void setDescription(String description);

    /**
     * Restituisce il nome dell'evento
     *
     * @return title
     */
    public String getDescription();

    /**
     * Imposta il nome dell creatore dell'evento
     *
     * @param user
     */
    public void setCreator(User user);

    /**
     * Restituisce il nome del creatore dell'evento
     *
     * @return nome del creatore dell'evento
     */
    public User getCreator();

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
     * Ritorna il numero dei partecipanti che sono inscritti all'evento
     *
     * @return ndei partecipanti
     */
    public int getNPartecipanti();

    /**
     * Imposta il numero massimo di partecipanti che si possono iscrivere all'evento
     *
     * @param nPartecipantiMax
     */
    public void setNPartecipantiMax(int nPartecipantiMax);

    /**
     * Ritorna il numero massimo dei partecipanti che si possono iscrivere all'evento
     *
     * @return ndei partecipanti
     */
    public int getNPartecipantiMax();

    /**
     * Imposta un valore per la recenzione dell'evento
     *
     * @param  review
     */
    public void setActive(int active);

    /**
     * Ritorna il valore della recenzione dell'evento
     *
     * @return review
     */
    public int getActive();
}
