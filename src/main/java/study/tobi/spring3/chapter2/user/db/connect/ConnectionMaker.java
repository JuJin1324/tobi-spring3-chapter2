package study.tobi.spring3.chapter2.user.db.connect;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Yoo Ju Jin(jujin1324@daum.net)
 * Created Date : 09/09/2019
 */
public interface ConnectionMaker {

    Connection makeConnection() throws ClassNotFoundException, SQLException;
}
