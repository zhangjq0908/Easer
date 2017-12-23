package ryey.easer.commons.plugindef;

import android.support.annotation.NonNull;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;

public interface DataFactory<T extends StorageData> {
    /**
     * @return The class of the expected data
     */
    @NonNull
    Class<T> dataClass();

    /**
     * Get an empty data
     * @return empty data
     */
    @NonNull
    T emptyData();

    /**
     * Get a valid but dummy data.
     * Mainly for testing.
     * @return dummy data
     */
    @NonNull
    T dummyData();

    /**
     * Parse data from the given input to reconstruct the saved data
     * @param data The data to be parsed
     * @param format The format of the underlying storage (e.g. JSON)
     * @param version The version of the data to-be-parsed
     * @return The reconstructed data
     * @throws IllegalStorageDataException If the {@param data} contains error or can't be recognized
     */
    @NonNull
    T parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException;
}
