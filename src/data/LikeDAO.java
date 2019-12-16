package data;

import model.Message;
import model.Like;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public interface LikeDAO {

    void createLike(Like like) throws SQLException;

    void deleteLike(Like like) throws SQLException;

    Collection<Like> getAllLikes() throws SQLException;

    Like getLike(ResultSet rs) throws SQLException;

    int getLikeCount(Message msg) throws SQLException;

}
