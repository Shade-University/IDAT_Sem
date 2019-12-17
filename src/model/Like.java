package model;

/**
 * Like model
 */
public class Like {

    private Integer id_user;
    private Integer id_message;

    public Like(Integer id_user, Integer id_message) {
        this.id_user = id_user;
        this.id_message = id_message;
    }

    public Integer getId_user() {
        return id_user;
    }

    public void setId_user(Integer id_user) {
        this.id_user = id_user;
    }

    public Integer getId_message() {
        return id_message;
    }

    public void setId_message(Integer id_message) {
        this.id_message = id_message;
    }
}
