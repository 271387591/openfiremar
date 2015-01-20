package com.ozstrategy.util;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by lihao on 1/20/15.
 */
public class FileHelper {
    public static void deleteDirectory(File directory){
        try {
            FileUtils.deleteDirectory(directory);
        } catch (IOException e) {
        }
    }
    public static File fileToZip(File sourceFolder, File targetFolder, String targetZipFileName) throws Exception{
        if (!targetZipFileName.endsWith(".zip")) {
            targetZipFileName = targetZipFileName + ".zip";
        }
        File zipFile = new File(targetFolder, targetZipFileName);
        FileOutputStream out = new FileOutputStream(zipFile);
        ZipOutputStream zipOutStream = new ZipOutputStream(out);
        zipToStream(zipOutStream, sourceFolder, "");
        zipOutStream.close();
        return zipFile;
    }
    public static void zipToStream(ZipOutputStream zipOutStream, File sourceFolder, String base) throws Exception{
        if (sourceFolder.isDirectory()) {
            File[] fileList = sourceFolder.listFiles();

            zipOutStream.putNextEntry(new ZipEntry(base + File.separator));

            base = ((base.length() == 0) ? "" : (base + File.separator));

            for (int i = 0; i < fileList.length; i++) {
                zipToStream(zipOutStream, fileList[i], base + fileList[i].getName());
            }
        } else {
            if (base == "") {
                base = sourceFolder.getName();
            }

            zipOutStream.putNextEntry(new ZipEntry(base));

            writeFile(zipOutStream, sourceFolder);
        }
    }
    public static void writeFile(ZipOutputStream zipOutStream, File sourceFile) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(sourceFile);
        int             len;
        byte[]          readBuffer      = new byte[2048];

        while ((len = fileInputStream.read(readBuffer, 0, 2048)) != -1) {
            zipOutStream.write(readBuffer, 0, len);
        }
        fileInputStream.close();
    }
}
