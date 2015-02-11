package database;

import java.nio.file.Path;

public interface Converter {
	void conversionComplete(Path weatherDB);
}
