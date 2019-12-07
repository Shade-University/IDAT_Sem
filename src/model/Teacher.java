
package model;

import java.awt.image.BufferedImage;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tomáš Vondra
 */
public class Teacher extends User {

    private List<Subject> subjects = new ArrayList<>();
    private String institute;

    public String getInstitute() {
        return institute;
    }
    public void setInstitute(String institute) {this.institute = institute;}

    public List<Subject> getSubjects() {
        return subjects;
    }
    public void setSubjects(List<Subject> subjects) {this.subjects = subjects;}
    
    public Teacher(List<Subject> subjects, String institute, String firstName, String lastName, String email, String password, BufferedImage userAvatar){
        super(firstName, lastName, email, USER_TYPE.TEACHER, password, userAvatar);
        this.subjects = subjects;
        this.institute = institute;
    } //Konstruktor pro vytváření

    public Teacher(List<Subject> subjects, String institute, int id, String firstName, String lastName, String email, Date dateCreated, BufferedImage userAvatar) {
        super(id, firstName, lastName, email, dateCreated, USER_TYPE.TEACHER, userAvatar);
        this.subjects = subjects;
        this.institute = institute;
    } //Konstruktor pro načítání
}
