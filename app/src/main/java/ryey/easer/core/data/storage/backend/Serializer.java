package ryey.easer.core.data.storage.backend;

public interface Serializer<T> {
    String serialize(T data)throws UnableToSerializeException;
}
