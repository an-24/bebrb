package biz.gelicon.dbcp;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Менеджер соединений с БД.
 * @author lan
 */
public class ConnectionManager {

    private final Map<String, ConnectionPool> map = new HashMap<String, ConnectionPool>();

    /**
     * Регистрация пула.<br>
     * Если пул с таким псевдонимом присутствует, то он заменяется.
     * @param alias псевдоним
     * @param pool пул соединений с БД
     */
    public void registerPool(String alias, ConnectionPool pool) {
        synchronized (this) {
            unregisterPool(alias);
            map.put(alias, pool);
        }
    }

    /**
     * Удаление пула и списка.
     * @param alias псевдоним
     */
    public void unregisterPool(String alias) {
        synchronized (this) {
            ConnectionPool tmp = (ConnectionPool) map.remove(alias);
            if (tmp != null) tmp.close();
        }
    }

    /**
     * Возвращает соединение с БД.
     * @param alias псевдоним
     * @return gelicon.dbcp.PoolableConnection
     * @throws SQLException
     */
    public PoolableConnection getConnection(String alias) throws SQLException {
        ConnectionPool pool = (ConnectionPool) map.get(alias);
        if (pool == null)
            throw new SQLException("Database alias \"" + alias + "\" not found");
        return ((ConnectionPool) map.get(alias)).getConnection();

    }

    /**
     * Закрытие всех соединений во всех пулах.
     */
    public void close() {
        close(false);
    }

    /**
     * Закрытие всех соединений во всех пулах и очистка списка пулов.
     */
    public void clear() {
        close(true);
    }

    private void close(boolean clear) {
        synchronized (this) {
            Collection<ConnectionPool> list = map.values();
            for (ConnectionPool cp : list) cp.close();
            if (clear) map.clear();
        }
    }

}
