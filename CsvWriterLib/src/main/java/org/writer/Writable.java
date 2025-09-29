package org.writer;

import java.io.IOException;
import java.util.List;

public interface Writable<T> {

    void writeToCsv(List<T> data, String filePath) throws IOException;
}
