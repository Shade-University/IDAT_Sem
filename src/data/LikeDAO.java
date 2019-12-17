package data;

import model.Message;
import model.Like;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public interface LikeDAO {

    /**
     * Create like in db
     * @param like
     * @throws SQLException
     */
    void createLike(Like like) throws SQLException;

    /**
     * Delete like in db
     * @param like
     * @throws SQLException
     */
    void deleteLike(Like like) throws SQLException;

    /**
     * Return all likes from db
     * @return Collection of likes
     * @throws SQLException
     */
    Collection<Like> getAllLikes() throws SQLException;

    /**
     * Parser method from ResultSet
     * @param rs
     * @return Like
     * @throws SQLException
     */
    Like getLike(ResultSet rs) throws SQLException;

    /**
     * Return number of likes in db
     * @param msg
     * @return int
     * @throws SQLException
     */
    int getLikeCount(Message msg) throws SQLException;

}
