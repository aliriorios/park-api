package br.com.example.park_api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class JasperService {
    private final ResourceLoader resourceLoader;
    private final DataSource dataSource; // Database connection (Spring.Datasource)

    private Map<String, Object> params = new HashMap<>();

    private static final String JASPER_SOURCE = "classpath:reports/"; // "classpath" is all that's in "resources"

    public void addParams(String key, Object value) {
        this.params.put("IMAGE_SOURCE", JASPER_SOURCE);
        this.params.put("REPORT_LOCALE", new Locale("en", "US"));
        this.params.put(key, value);
    }

    public byte[] generatedPdf() {
        byte[] bytes = null;

        try {
            Resource resource = resourceLoader.getResource(JASPER_SOURCE.concat("Parking.jasper"));
            InputStream stream = resource.getInputStream();

            JasperPrint print = JasperFillManager.fillReport(stream, params, dataSource.getConnection());
            bytes = JasperExportManager.exportReportToPdf(print);

        } catch (IOException | JRException | SQLException e) {
            log.error("Jasper Reports ::: ", e.getCause());

            throw new RuntimeException(e);
        }

        return bytes;
    }
}
