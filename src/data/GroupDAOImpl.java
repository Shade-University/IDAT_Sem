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
import model.Rating;
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
        statement.close();
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
        statement.close();
        return collection;
    }

    @Override
    public Collection<Group> getUserGroups(User user) throws SQLException {
        Collection<Group> collection = new ArrayList<>();

        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery(
                "SELECT * FROM getSkupinyUzivatele WHERE UZIVATELE_ID_UZIVATEL = " + user.getId());

        while (rs.next()) {
            Group group = getGroup(rs);
            collection.add(group);
        } //Vybere všechny skupiny daného uživatele
        statement.close();
        return collection;
    }

    @Override
    public Group getRatedGroup(Rating rt) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement(
                "SELECT * FROM getSkupinaPodleHodnoceni WHERE ID_hodnoceni = " + rt.getId());
        ResultSet rs = preparedStatement.executeQuery();

        Group group = null;
        if (rs.next())
            group =  getGroup(rs);

        preparedStatement.close();
        return group;
    }

    @Override
    public Group getGroupById(int id) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement(
                "SELECT * FROM GETSKUPINY WHERE ID_SKUPINA = " + id);
        ResultSet rs = preparedStatement.executeQuery();

        if (rs.next()) {
            Group group = getGroup(rs);
            preparedStatement.close();
            return group;
        }
        preparedStatement.close();
        return null;

    }

    @Override
    public Collection<Group> getSubjectGroups(Subject subject) throws SQLException {
        Collection<Group> collection = new ArrayList<>();

        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery(
                "SELECT * FROM getSkupinyPredmetu WHERE PREDMETY_ID_PREDMET = " + subject.getId());

        while (rs.next()) {
            Group group = getGroup(rs);
            collection.add(group);
        }
        statement.close();
        return collection;
    }

    @Override
    public void updateGroup(Group group) throws SQLException {
        CallableStatement callableStatement = conn.prepareCall(
                "call update_skupina(?,?,?)"
        );
        callableStatement.setInt(1, group.getId());
        callableStatement.setString(2, group.getName());
        callableStatement.setString(3, group.getDescription());
        callableStatement.executeUpdate();
        callableStatement.close();
        conn.commit();
        System.out.println("Group updated!");
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
        preparedStatement.close();
        System.out.println("User added to group");
    }

    @Override
    public void removeUserFromGroup(User u, Group s) throws SQLException {
        CallableStatement callableStatement = conn.prepareCall(
                "call delete_uzivatel_skupina(?,?)"
        );
        callableStatement.setInt(1, u.getId());
        callableStatement.setInt(2, s.getId());
        callableStatement.executeQuery();
        conn.commit();
        callableStatement.close();
        System.out.println("User removed from group");
    }

    @Override
    public void insertGroup(Group group) throws SQLException {
        CallableStatement callableStatement = conn.prepareCall(
                "call insert_skupina(?,?)"
        );
        callableStatement.setString(1, group.getName());
        callableStatement.setString(2, group.getDescription());
        callableStatement.executeQuery();
        conn.commit();
        callableStatement.close();
        System.out.println("Group added.");
    }

    @Override
    public void removeGroup(Group group) throws SQLException {
        CallableStatement callableStatement = conn.prepareCall(
                "call delete_skupina(?)"
        );
        System.out.println(group.getId());
        callableStatement.setInt(1, group.getId());
        callableStatement.executeQuery();
        conn.commit();
        callableStatement.close();
        System.out.println("Group deleted");
    }

}
