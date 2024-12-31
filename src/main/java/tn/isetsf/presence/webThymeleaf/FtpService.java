package tn.isetsf.presence.webThymeleaf;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;

@Service
public class FtpService {

    @Value("${ftp.server}")
    private String server;

    @Value("${ftp.port}")
    private int port;

    @Value("${ftp.username}")
    private String user;

    @Value("${ftp.password}")
    private String pass;

    private final String uploadsDirectory = "/home/spring/ftp/uploads/";
    // Répertoire sur le serveur FTP

    // Méthode pour téléverser un fichier sur le serveur FTP
    public void uploadFile(MultipartFile file, String remoteFileName) throws IOException {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            if (!ftpClient.isConnected()) {
                System.out.println("Échec de la connexion au serveur FTP.");
            }

            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            String fullRemotePath =uploadsDirectory  + remoteFileName; // Chemin relatif depuis le répertoire d'accueil de l'utilisateur
            ftpClient.enterLocalPassiveMode();

            try (InputStream input = file.getInputStream()) {
                boolean success = ftpClient.storeFile(fullRemotePath, input);
                if (success) {
                    System.out.println("Fichier téléversé : " + fullRemotePath);
                } else {
                    System.out.println("Échec du téléversement : " + fullRemotePath);
                }

            }
        } finally {
            ftpClient.logout();
            ftpClient.disconnect();
        }

    }

    // Méthode pour supprimer un fichier sur le serveur FTP
    public void deleteFile(String fileName) throws IOException {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(server, port);
            ftpClient.login(user, pass);

            String fullRemotePath = fileName; // Chemin relatif depuis le répertoire d'accueil de l'utilisateur

            boolean deleted = ftpClient.deleteFile(fullRemotePath);
            if (deleted) {
                System.out.println("Fichier supprimé : " + fullRemotePath);
            } else {
                System.out.println("Échec de la suppression du fichier : " + fullRemotePath);
            }
        } finally {
            ftpClient.logout();
            ftpClient.disconnect();
        }
    }}
