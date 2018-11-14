package books.utils;

import com.sun.javafx.PlatformUtil;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Utils {
    public static String getDriverPath() {
        Path currentRelativePath = Paths.get("");
        String projectRootPath = currentRelativePath.toAbsolutePath().toString();
        File projectRootPathAsFile = new File(projectRootPath);
        File binPathFile = new File(projectRootPathAsFile, "bin");
        File driverPath;
        if (PlatformUtil.isWindows()) {
            driverPath = new File(binPathFile, "geckodriver-windows.exe");
        } else {
            driverPath = new File(binPathFile, "geckodriver-mac");
        }
        return driverPath.getAbsolutePath();
    }
}
