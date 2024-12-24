package com.digitalestate.gestionprofile.utils;
import com.digitalestate.gestionprofile.exception.ApiFileException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
public class FileUtils {
    static Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);
    public static final int DEFAULT_BUFFER_SIZE = 8192;

    public static void moveFileDirectory(Path source, Path target) {
        try {
            Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Directory moved successfully.");
        } catch (Exception e) {
            System.err.println("Failed to move directory: " + e.getMessage());
        }
    }

    public static void copyInputStreamToFile(InputStream inputStream, File file)
            throws IOException {

        // append = false
        try (FileOutputStream outputStream = new FileOutputStream(file, false)) {
            int read;
            byte[] bytes = new byte[DEFAULT_BUFFER_SIZE];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        }

    }

    public static void copyDirectoryAndContent(Path source, Path target) {
        try {
            Files.walk(source)
                    .forEach(sourcePath -> {
                        Path destinationPath = target.resolve(source.relativize(sourcePath));
                        try {
                            Files.copy(sourcePath, destinationPath);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveFile(MultipartFile file, String userConfDirectoryPath) throws IOException {
        LOGGER.info("Upload file  {} in destination {}", file.getOriginalFilename(), userConfDirectoryPath);
        String fileName = file.getOriginalFilename();
        Files.write(Paths.get(userConfDirectoryPath + File.separator + fileName), file.getBytes());
    }

    public static void deleteFile(String pathToFile) {
        try {
            // Get the Path object for the file
            Path filePath = Paths.get(pathToFile);

            // Delete the file
            Files.delete(filePath);
            System.out.println("File deleted successfully.");
        } catch (IOException e) {
            System.err.println("Error deleting the file: " + e.getMessage());
        }
    }


    public static void createFolder(String folderPath) {
        Path path = Paths.get(folderPath);
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            throw new ApiFileException("Cannot create folder : " + folderPath);
        }
    }

    /**
     * @param zipFilePath   : Path of the eip file to Unzip
     * @param destDirectory : Destination where the file will be unzipped
     */
    public static void unzipFile(String zipFilePath, String destDirectory) {
        LOGGER.info(" Unzip file : {}, in the destination : {}", zipFilePath, destDirectory);
        try {
            File destDirFile = new File(destDirectory);
            if (!destDirFile.exists()) {
                destDirFile.mkdir();
            }

            FileInputStream fis = new FileInputStream(zipFilePath);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                String fileName = zipEntry.getName();
                File newFile = new File(destDirectory + File.separator + fileName);

                if (zipEntry.isDirectory()) {
                    newFile.mkdirs();
                } else {
                    FileOutputStream fos = new FileOutputStream(newFile);
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                    fos.close();
                }
                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();
            zis.close();
            fis.close();
        } catch (Exception e) {
            LOGGER.error(" ERROR while unzip file : {}, in the destination : {}", zipFilePath, destDirectory);
            throw new ApiFileException("ERROR whyle unzip file : " + zipFilePath + ", in the destination : " + destDirectory);
        }

    }

    public static void deleteDirectory(String pathDirectory) throws IOException {
        Path directory = Paths.get(pathDirectory);
        if (Files.isDirectory(directory)) {
            try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(directory)) {
                for (Path entry : dirStream) {
                    if (Files.isDirectory(entry)) {
                        deleteDirectory(String.valueOf(entry)); // Recursively delete subdirectories
                    } else {
                        Files.delete(entry);    // Delete files
                    }
                }
            }
        }
        Files.delete(directory); // Delete the directory itself
    }

    public static byte[] convertFileToByteArray(File file) {
        try {
            if (!file.exists()) {
                String filePath = file.getAbsolutePath();
                LOGGER.error("File not found: " + filePath);
                throw new FileNotFoundException("File not found: " + filePath);
            }
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            LOGGER.error("Error converting file to byte array: " + file.getName(), e);
            throw new ApiFileException("Error converting file to byte array: " + file.getName());
        }
    }
}
