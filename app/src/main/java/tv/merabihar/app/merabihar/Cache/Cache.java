package tv.merabihar.app.merabihar.Cache;

/**
 * Created by ZingoHotels Tech on 22-11-2018.
 */

public interface Cache<K, V> {

    void put(K key, V value);

    V get(K key);

    V remove(K key);

    void clear();
}