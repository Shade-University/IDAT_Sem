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

import jdk.nashorn.internal.runtime.Debug;
import model.Group;
import model.Subject;
import model.User;

/**
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
    public Collection<Group> getAllGroups() throws SQLException {
        Collection<Group> collection = new ArrayList<>();

        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery(
                "SELECT * FROM getSkupiny");

        while (rs.next()) {
            Group group = getGroup(rs);
            collection.add(group);
        } //Načte všechny existující skupin.
        return collection;
    }

    @Override
    public Collection<Group> getAllGroupWithUserQuantity() throws SQLException {
        Collection<Group> collection = new ArrayList<>();

        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery(
                "SELECT * FROM getPoctyVeSkupinach");

        while (rs.next()) {
            Group group = getGroupWithQuantity(rs);
            collection.add(group);
        } //Načte všechny existující skupiny s počty členů.

        return collection;
    }

    @Override
    public Collection<Group> getUserGroups(User user) throws SQLException {
        Collection<Group> collection = new ArrayList<>();

        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery(
                "SELECT * FROM getSkupiny g WHERE g.id_uzivatel = " + user.getId());

        while (rs.next()) {
            Group group = getGroup(rs);
            collection.add(group);
        } //Vybere všechny skupiny daného uživatele

        return collection;
    }

    @Override
    public void updateGroup(Group group) throws SQLException {
        /*PreparedStatement preparedStatement = conn.prepareStatement(
                "UPDATE SKUPINY SET "
                        + "nazev = ?, "
                        + "popis = ?, "
                        + "id_predmet = ? "
                        + "WHERE id_skupina = ?"
        );
        preparedStatement.setString(1, group.getName());
        preparedStatement.setString(2, group.getDescription());
        preparedStatement.setInt(3, group.getSubject().getId());
        preparedStatement.setInt(4, group.getId());

        preparedStatement.executeUpdate();
        conn.commit();
        System.out.println("Group updated");*/
    }

    @Override
    public Group getGroup(ResultSet rs) throws SQLException {
        Group group = new Group(
                rs.getInt("id_skupina"),
                rs.getString("nazev_skupina"),
                rs.getString("popis_skupina"),
                new ArrayList<Subject>() //TODO SKUPINY
        );
        return group;
    } //Metoda rozparsuje výsledek z getgroups a vytvoří skupinu

    @Override
    public Group getGroupWithQuantity(ResultSet rs) throws SQLException {
        Group group = new Group(
                rs.getInt("id_skupina"),
                rs.getString("nazev_skupina"),
                rs.getString("popis_skupina"),
                rs.getInt("pocet_skupina"),
                new ArrayList<Subject>() //TODO SKUPINY
        );
        return group;
    } //Metoda rozparsuje výsledek z getGroupWithUserQuantity

    @Override
    public void insertUserToGroup(User u, Group s) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement(
                "INSERT INTO UZIVATELE_SKUPINY(UZIVATELE_ID_UZIVATEL, SKUPINY_ID_SKUPINA)\n"
                        + "VALUES(?, ?)"
        );
        preparedStatement.setInt(1, u.getId());
        preparedStatement.setInt(2, s.getId());

        preparedStatement.executeUpdate();
        conn.commit();
        System.out.println("User added to group");
    }

    @Override
    public void removeUserFromGroup(User u, Group s) throws SQLException {
        CallableStatement callableStatement = conn.prepareCall(
                "call delete_uzivatel_skupina(?,?)"
        );
        callableStatement.setInt(1, u.getId());
        callableStatement.setInt(2, s.getId());
        callableStatement.execute();
        conn.commit();
        System.out.println("User removed from group");
    }

    @Override
    public void insertGroup(Group group) throws SQLException {
        /*PreparedStatement preparedStatement = conn.prepareStatement(
                "INSERT INTO SKUPINY(nazev, popis, id_predmet)"
                        + "VALUES(?, ?, ?)"
        );
        preparedStatement.setString(1, group.getName());
        preparedStatement.setString(2, group.getDescription());
        preparedStatement.setInt(3, group.getSubject().getId());

        preparedStatement.executeUpdate();
        conn.commit();
        System.out.println("Group created");

        Statement stm = conn.createStatement();
        ResultSet rs = stm.executeQuery("SELECT MAX(id_skupina) \"id\" FROM SKUPINY");
        if (rs.next()) {
            group.setId(rs.getInt("id"));
        } //Získání id z databáze */
    }

    @Override
    public void removeGroup(Group group) throws SQLException {
        CallableStatement callableStatement = conn.prepareCall(
                "call delete_group(?)"
        );
        callableStatement.setInt(1, group.getId());

        callableStatement.execute();
        conn.commit();
        System.out.println("Group deleted");
    }

}
