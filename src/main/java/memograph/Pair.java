package memograph;

final class Pair<Key, Value> {
    private final Key key;
    private final Value value;

    Pair(Key key, Value value) {
        this.key = key;
        this.value = value;
    }

    Key getKey() {
        return this.key;
    }

    Value getValue() {
        return this.value;
    }
}
