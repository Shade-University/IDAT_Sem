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
import model.Predmet;
import model.Skupina;
import model.Uzivatel;

/**
 *
 * @author Tomáš Vondra
 */
public class GroupDAOImpl implements GroupDAO {

    private Connection conn;

    public GroupDAOImpl() {
        try {
            conn = OracleConnection.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(GroupDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Collection<Skupina> getAllGroups() {

        Collection<Skupina> collection = new ArrayList<>();
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(
                    "SELECT * FROM GETGROUPS"); //GETGROUPS je pohled v databázi

            while (rs.next()) {
                Skupina skupina = getGroup(rs);
                collection.add(skupina);
            } //Načte všechny existující skupin.

            return collection;
        } catch (SQLException ex) {
            Logger.getLogger(GroupDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    @Override
    public Collection<Skupina> getUserGroups(Uzivatel user) {

        Collection<Skupina> collection = new ArrayList<>();
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(
                    "SELECT * FROM GETUSERSINGROUPS g WHERE g.id_uzivatel = " + user.getId());

            while (rs.next()) {
                Skupina skupina = getGroup(rs);
                collection.add(skupina);
            } //Vybere všechny skupiny daného uživatele

            return collection;
        } catch (SQLException ex) {
            Logger.getLogger(GroupDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;

    }

    @Override
    public void updateGroup(Skupina group) {
        try {
            PreparedStatement pstm = conn.prepareStatement(
                    "UPDATE SKUPINY SET "
                    + "nazev = ?, "
                    + "popis = ?, "
                    + "id_predmet = ? "
                    + "WHERE id_skupina = ?"
            );
            pstm.setString(1, group.getNazev());
            pstm.setString(2, group.getPopis());
            pstm.setInt(3, group.getPredmet().getId());
            pstm.setInt(4, group.getId());

            pstm.executeUpdate();
            conn.commit();
            System.out.println("Group updated");
        } catch (SQLException ex) {
            Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Skupina getGroup(ResultSet rs) throws SQLException {
        Skupina skupina = new Skupina(
                rs.getInt("id_skupina"),
                rs.getString("nazev_skupina"),
                rs.getString("popis_skupina"),
                new Predmet(
                        rs.getInt("id_skupina_predmet"),
                        rs.getString("nazev_skupina_predmet"),
                        rs.getString("popis_skupina_predmet")
                )
        );
        return skupina;
    } //Metoda rozparsuje výsledek z getgroups a vytvoří skupinu

    @Override
    public void insertUserToGroup(Uzivatel u, Skupina s) {
        try {
            PreparedStatement pstm = conn.prepareStatement(
                    "INSERT INTO UZIVATEL_SKUPINA(UZIVATEL_ID_UZIVATEL, SKUPINA_ID_SKUPINA)\n"
                    + "VALUES(?, ?)"
            );
            pstm.setInt(1, u.getId());
            pstm.setInt(2, s.getId());

            pstm.executeUpdate();
            conn.commit();
            System.out.println("User added to group");
        } catch (SQLException ex) {
            Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void insertGroup(Skupina group) {
        try {
            PreparedStatement pstm = conn.prepareStatement(
                    "INSERT INTO SKUPINY(nazev, popis, id_predmet)"
                    + "VALUES(?, ?, ?)"
            );
            pstm.setString(1, group.getNazev());
            pstm.setString(2, group.getPopis());
            pstm.setInt(3, group.getPredmet().getId());

            pstm.executeUpdate();
            conn.commit();
            System.out.println("Group created");
            
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT MAX(id_skupina) \"id\" FROM SKUPINY");
            if (rs.next()) {
                group.setId(rs.getInt("id"));
            } //Získání id z databáze
        } catch (SQLException ex) {
            Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void removeGroup(Skupina group) {
        try {
            CallableStatement pstm = conn.prepareCall(
                    "call delete_group(?)"
            );
            pstm.setInt(1, group.getId());

            pstm.execute();
            conn.commit();
            System.out.println("Group deleted");
        } catch (SQLException ex) {
            Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
