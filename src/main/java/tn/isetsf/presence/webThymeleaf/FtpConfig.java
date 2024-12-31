//package tn.isetsf.presence.webThymeleaf;
//
//import org.apache.commons.net.ftp.FTPFile;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.integration.config.EnableIntegration;
//import org.springframework.integration.file.remote.session.CachingSessionFactory;
//import org.springframework.integration.file.remote.session.SessionFactory;
//import org.springframework.integration.ftp.session.DefaultFtpSessionFactory;
//import org.springframework.integration.scheduling.PollerMetadata;
//
//@Configuration
//@EnableIntegration
//public class FtpConfig {
//
//    @Bean
//    public SessionFactory<FTPFile> ftpSessionFactory() {
//        DefaultFtpSessionFactory factory = new DefaultFtpSessionFactory();
//        factory.setHost("51.77.210.237"); // Remplacez par votre adresse IP
//        factory.setPort(21);
//        factory.setUsername("spring"); // Nom d'utilisateur FTP
//        factory.setPassword("123456"); // Mot de passe FTP
//        return new CachingSessionFactory<>(factory);
//    }
//
//    // Vous pouvez ajouter d'autres beans ou configurations ici si nécessaire
//}
