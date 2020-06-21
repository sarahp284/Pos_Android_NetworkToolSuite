package com.example.pos_networktoolsuite.files;
import org.apache.commons.net.ftp.FTPClient;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FTPDownloader {

    /**
     * FTP-Dateienliste.
     * @return String-Array der Dateinamen auf dem FTP-Server
     */
    public static String[] list( String host, int port, String usr, String pwd ) throws IOException
    {
        FTPClient ftpClient = new FTPClient();
        String[]  filenameList;

        try {
            ftpClient.connect( host, port );
            ftpClient.login( usr, pwd );
            filenameList = ftpClient.listNames();
            ftpClient.logout();
        } finally {
            ftpClient.disconnect();
        }

        return filenameList;
    }

    /**
     * FTP-Client-Download.
     * @return true falls ok
     */
    public static boolean download( String localResultFile, String remoteSourceFile,
                                    String host, int port, String usr, String pwd, boolean showMessages ) throws IOException
    {
        FTPClient        ftpClient = new FTPClient();
        FileOutputStream fos = null;
        boolean          resultOk = true;

        try {
            ftpClient.connect( host, port );
            if( showMessages ) { System.out.println( ftpClient.getReplyString() ); }
            resultOk &= ftpClient.login( usr, pwd );
            if( showMessages ) { System.out.println( ftpClient.getReplyString() ); }
            fos = new FileOutputStream( localResultFile );
            resultOk &= ftpClient.retrieveFile( remoteSourceFile, fos );
            if( showMessages ) { System.out.println( ftpClient.getReplyString() ); }
            resultOk &= ftpClient.logout();
            if( showMessages ) { System.out.println( ftpClient.getReplyString() ); }
        } finally {
            try { if( fos != null ) { fos.close(); } } catch( IOException e ) {/* nothing to do */}
            ftpClient.disconnect();
        }

        return resultOk;
    }

    /**
     * FTP-Client-Upload.
     * @return true falls ok
     */
    public static boolean upload( String localSourceFile, String remoteResultFile,
                                  String host, int port, String usr, String pwd, boolean showMessages ) throws IOException
    {
        FTPClient       ftpClient = new FTPClient();
        FileInputStream fis = null;
        boolean         resultOk = true;

        try {
            ftpClient.connect( host, port );
            if( showMessages ) { System.out.println( ftpClient.getReplyString() ); }
            resultOk &= ftpClient.login( usr, pwd );
            if( showMessages ) { System.out.println( ftpClient.getReplyString() ); }
            fis = new FileInputStream( localSourceFile );
            resultOk &= ftpClient.storeFile( remoteResultFile, fis );
            if( showMessages ) { System.out.println( ftpClient.getReplyString() ); }
            resultOk &= ftpClient.logout();
            if( showMessages ) { System.out.println( ftpClient.getReplyString() ); }
        } finally {
            try { if( fis != null ) { fis.close(); } } catch( IOException e ) {/* nothing to do */}
            ftpClient.disconnect();
        }

        return resultOk;
    }
}
