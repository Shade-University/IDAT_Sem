package data;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.Obor;
import model.Predmet;
import model.Skupina;
import model.Student;
import model.Ucitel;
import model.Uzivatel;

/**
 * @author Tomáš Vondra
 */
public class UserDAOImpl implements UserDAO {

    private Connection conn;

    public UserDAOImpl() {
        try {
            conn = OracleConnection.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Collection<Uzivatel> getAllUsers() {

        Collection<Uzivatel> collection = new ArrayList<>();
        try {

            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(
                    "SELECT * FROM GETUSERS"); //getusers je pohled

            while (rs.next()) {
                Uzivatel uzivatel = getUser(rs);
                collection.add(uzivatel);
            }

            return collection;
        } catch (SQLException ex) {
            Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Uzivatel getUserByLogin(String email, String password) {

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "SELECT * FROM GETUSERS WHERE email=? AND heslo=?");
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                Uzivatel uzivatel = getUser(rs);
                return uzivatel;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Collection<Uzivatel> getAllUsersFromGroup(Skupina skupina) {
        Collection<Uzivatel> collection = new ArrayList<>();
        try {

            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(
                    "SELECT * FROM GETUSERSINGROUPS g WHERE g.id_skupina = " + skupina.getId());
            while (rs.next()) {
                Uzivatel uzivatel = getUser(rs); //getusersingroups pohled má stejný výpis uživatele jako getallusers, takže lze rozparsovat
                collection.add(uzivatel);
            }

            return collection;
        } catch (SQLException ex) {
            Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Uzivatel getUser(ResultSet rs) throws SQLException {
        Uzivatel uzivatel;
        switch (rs.getString("uzivatel_typ")) {
            //TODO Admin vlastní tabulka a třída
            case "admin":
                uzivatel = new Uzivatel(
                        rs.getInt("id_uzivatel"),
                        rs.getString("jmeno"),
                        rs.getString("prijmeni"),
                        rs.getString("email"),
                        rs.getDate("datum_vytvoreni"),
                        "admin"
                );
                break;
            case "student":
                uzivatel = new Student(
                        new Obor(
                                rs.getInt("id_obor"),
                                rs.getString("nazev_obor"),
                                rs.getString("popis_obor")
                        ),
                        rs.getString("rok_studia"),
                        rs.getInt("id_uzivatel"),
                        rs.getString("jmeno"),
                        rs.getString("prijmeni"),
                        rs.getString("email"),
                        rs.getDate("datum_vytvoreni")
                );
                break;
            default:
                uzivatel = new Ucitel(
                        new Predmet(
                                rs.getInt("id_vyucujici_predmet"),
                                rs.getString("nazev_vyucujici_predmet"),
                                rs.getString("popis_vyucujici_predmet")
                        ),
                        rs.getString("katedra"),
                        rs.getInt("id_uzivatel"),
                        rs.getString("jmeno"),
                        rs.getString("prijmeni"),
                        rs.getString("email"),
                        rs.getDate("datum_vytvoreni")
                );
                break;
        }
        return uzivatel;
    } //Metoda rozparsuje a vytvoří uživatele

    @Override
    public void updateUser(Uzivatel uzivatel) throws SQLException{
        PreparedStatement pstm = conn.prepareStatement(
                "UPDATE Uzivatele SET "
                        + "jmeno = ?, "
                        + "prijmeni = ?, "
                        + "email = ? "
                        + "WHERE id_uzivatel = ?"
        );
        pstm.setString(1, uzivatel.getJmeno());
        pstm.setString(2, uzivatel.getPrijmeni());
        pstm.setString(3, uzivatel.getEmail());
        pstm.setInt(4, uzivatel.getId());

        pstm.executeUpdate();
        conn.commit();
        System.out.println("User updated");
    }

    @Override
    public void insertUser(Uzivatel uzivatel) {

        if (uzivatel instanceof Ucitel) {
            insertUcitel((Ucitel) uzivatel);
        } else if (uzivatel instanceof Student) {
            insertStudent((Student) uzivatel);
        } else {
            insertAdmin(uzivatel);
        }

        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT MAX(id_uzivatel) \"id\" FROM UZIVATELE");
            if (rs.next()) {
                uzivatel.setId(rs.getInt("id"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void insertUcitel(Ucitel ucitel) {
        try {
            CallableStatement pstm = conn.prepareCall(
                    "CALL insert_ucitel"
                            + "(?, ?, ?, ?, ?, ?, ?)"
            );
            pstm.setString(1, ucitel.getJmeno());
            pstm.setString(2, ucitel.getPrijmeni());
            pstm.setString(3, ucitel.getEmail());
            pstm.setString(4, ucitel.getHeslo());
            pstm.setDate(5, ucitel.getDatum_vytvoreni());
            pstm.setString(6, ucitel.getKatedra());
            pstm.setInt(7, ucitel.getVyucujici_Predmet().getId());

            pstm.execute();
            conn.commit();
            System.out.println("Teacher inserted");
        } catch (SQLException ex) {
            Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void insertStudent(Student student) {
        try {
            CallableStatement pstm = conn.prepareCall(
                    "CALL insert_student"
                            + "(?, ?, ?, ?, ?, ?, ?)"
            );
            pstm.setString(1, student.getJmeno());
            pstm.setString(2, student.getPrijmeni());
            pstm.setString(3, student.getEmail());
            pstm.setString(4, student.getHeslo());
            pstm.setDate(5, student.getDatum_vytvoreni());
            pstm.setString(6, student.getRokStudia());
            pstm.setInt(7, student.getObor().getId());

            pstm.execute();
            conn.commit();
            System.out.println("Student inserted");
        } catch (SQLException ex) {
            Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void deleteUser(Uzivatel uzivatel) {

        if (uzivatel instanceof Ucitel) {
            deleteUcitel((Ucitel) uzivatel);
        } else if (uzivatel instanceof Student) {
            deleteStudent((Student) uzivatel);
        } else {
            try {
                CallableStatement pstm = conn.prepareCall(
                        "CALL delete_admin(?)"
                );
                pstm.setInt(1, uzivatel.getId());

                pstm.execute();
                conn.commit();
                System.out.println("Admin deleted");
            } catch (SQLException ex) {
                Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    } //TODO Občas používám pstm, občas stm

    private void deleteStudent(Student student) {
        try {
            CallableStatement pstm = conn.prepareCall(
                    "CALL delete_student(?)"
            );
            pstm.setInt(1, student.getId());

            pstm.execute();
            conn.commit();
            System.out.println("Student deleted");
        } catch (SQLException ex) {
            Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void deleteUcitel(Ucitel ucitel) {
        try {
            CallableStatement pstm = conn.prepareCall(
                    "CALL delete_ucitel(?)"
            );
            pstm.setInt(1, ucitel.getId());

            pstm.execute();
            conn.commit();
            System.out.println("Ucitel deleted");
        } catch (SQLException ex) {
            Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void insertAdmin(Uzivatel admin) {
        try {
            PreparedStatement pstm = conn.prepareStatement(
                    "INSERT INTO UZIVATELE(jmeno, prijmeni, email, heslo, datum_vytvoreni, uzivatel_typ) "
                            + "VALUES (?, ?, ?, ?, ?, ?)"
            );
            pstm.setString(1, admin.getJmeno());
            pstm.setString(2, admin.getPrijmeni());
            pstm.setString(3, admin.getEmail());
            pstm.setString(4, admin.getHeslo());
            pstm.setDate(5, admin.getDatum_vytvoreni());
            pstm.setString(6, admin.getUzivatelskyTyp());

            pstm.executeUpdate();
            conn.commit();
            System.out.println("Admin inserted");
        } catch (SQLException ex) {
            Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
