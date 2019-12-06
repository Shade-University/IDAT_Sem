
package model;

import java.sql.Date;

/**
 *
 * @author Tomáš Vondra
 */
public class Student extends User {

   private final Field field;
   private final String studyYear;

    public String getStudyYear() {
        return studyYear;
    }

    public Field getField() {
        return field;
    }
    
    public Student(Field field, String studyYear, String firstName, String lastName, String email, String password){
        super(firstName, lastName, email, USER_TYPE.STUDENT, password);
        this.field = field;
        this.studyYear = studyYear;
    } //Konstruktor pro vytváření

    public Student(Field field, String studyYear, int id, String firstName, String lastName, String email, Date dateCreated) {
        super(id, firstName, lastName, email, dateCreated, USER_TYPE.STUDENT);
        this.field = field;
        this.studyYear = studyYear;
    } //Konstruktor pro načítání
}
