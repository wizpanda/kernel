package com.wizpanda.utils.file

import java.nio.file.Files
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

/**
 * Utility class to zip and unzip a given file.
 *
 * @author Shashank Agrawal
 */
class ZipFileUtils {

    /**
     * Give a file or directory as {@link File} and create a zip file out of it in the same directory where the file exists.
     *
     * @param file File or directory which needs to be zipped.
     * @return Zipped{@link File}
     */
    static File zip(File file) {
        boolean isDirectory = file.isDirectory()

        StringBuilder zipFilePath = new StringBuilder(file.getParent())
                .append(File.pathSeparator)

        if (isDirectory) {
            zipFilePath.append(file.name)
        } else {
            // Replace file extension with the zip file
            zipFilePath.append(file.name.take(file.name.lastIndexOf(".")))
        }

        zipFilePath.append(".zip")
        String zipFileName = zipFilePath.toString()

        ZipOutputStream outputStream = new ZipOutputStream(new FileOutputStream(zipFileName))
        if (isDirectory) {
            addDirectoryToZip(outputStream, file)
        } else {
            addFileToZip(file, outputStream)
        }

        outputStream.closeEntry()
        outputStream.close()

        return new File(zipFileName)
    }

    private static void addFileToZip(File file, ZipOutputStream outputStream) {
        outputStream.putNextEntry(new ZipEntry(file.getName()))
        InputStream inputStream = new FileInputStream(file)
        Files.copy(inputStream, outputStream)
        inputStream.close()
    }

    private static void addDirectoryToZip(ZipOutputStream outputStream, File directory) {
        directory.eachFile() { File file ->
            addFileToZip(file, outputStream)
        }
    }

    File unzip(File file) {
        // TODO implement me
    }
}
