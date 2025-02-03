package tn.isetsf.presence.report;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import org.springframework.stereotype.Service;
import tn.isetsf.presence.Entity.LigneAbsenceDTO;
import tn.isetsf.presence.Entity.PrintType;

import javax.persistence.Enumerated;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Service

public class Reporter {
    @Enumerated
    private PrintType printType;




    public byte[] reports(Map<String, Object> params, List<LigneAbsenceDTO> list,PrintType printType) throws JRException, IOException {
        InputStream inputStream = null;
        if (printType==PrintType.ABSENCE_GLOBAL) {
            inputStream = getClass().getResourceAsStream("/reports/etatGlobal.jrxml");
       }
        if (printType==PrintType.AVIS_SIMPLE){
            inputStream=getClass().getResourceAsStream("/reports/etatSimple.jrxml");
        }
System.out.println(params);
        if (inputStream == null) {
            throw new FileNotFoundException("Le fichier de rapport 'etatGlobal.jrxml' est introuvable !");
        }

        try {
            JRBeanArrayDataSource dataSource = new JRBeanArrayDataSource(list.toArray());
            JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
            JasperPrint print = JasperFillManager.fillReport(jasperReport, params, dataSource);

            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                JasperExportManager.exportReportToPdfStream(print, outputStream);
                return outputStream.toByteArray();
            }
        } finally {
            inputStream.close(); // Fermeture du fichier .jrxml
        }
    }




}