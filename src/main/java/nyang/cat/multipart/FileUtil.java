package nyang.cat.multipart;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.web.multipart.MultipartFile;

public class FileUtil {

    public static void saveFile(MultipartFile file, String directory) throws IOException {
        Path path = Paths.get(directory);
        byte[] bytes = file.getBytes();
        Path filePath = Paths.get(path.toString() + File.separator + file.getOriginalFilename());
        Files.write(filePath, bytes);
    }

}
