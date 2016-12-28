package map.entry

/**
 * Created by vicboma on 28/12/16.
 */
class MapEntry<K,V>(val _key: K, val _value: V) : Map.Entry<K,V> {

    override val key: K
        get() = _key

    override val value: V
        get() = _value

}