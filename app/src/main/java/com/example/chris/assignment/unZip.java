package com.example.chris.assignment;



import android.app.Application;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

//reference source, this unzips zip file using in both language
public class unZip extends Application{
    private String ZIP_FILE;
    private String LOCATION;

    public unZip(String zipFile, String location) {
        ZIP_FILE = zipFile;
        LOCATION = location;
        DirChecker("");
    }

    public void unzip() {
        try  {

            FileInputStream fin = new FileInputStream(ZIP_FILE);

            ZipInputStream zin = new ZipInputStream(fin);
            ZipEntry ze = null;

            while ((ze = zin.getNextEntry()) != null) {
                Log.v("Decompress", "Unzipping " + ze.getName());

                if (ze.isDirectory()) {
                    DirChecker(ze.getName());
                } else {
                    FileOutputStream fout = new FileOutputStream(LOCATION + ze.getName());
                    for (int c = zin.read(); c != -1; c = zin.read()) {
                        fout.write(c);
                    }

                    zin.closeEntry();
                    fout.close();
                }

            }
            zin.close();
        } catch(Exception e) {
            Log.e("Decompress", "unzip", e);
        }

    }

    private void DirChecker(String dir) {
        File f = new File(LOCATION + dir);

        if(!f.isDirectory()) {
            f.mkdirs();
        }
    }
}
