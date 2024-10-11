/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package khanzahmsservicesatusehat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import fungsi.ApiMobileJKN;
import fungsi.ApiSatuSehat;
import fungsi.SatuSehatCekNIK;
import fungsi.koneksiDB;
import fungsi.sekuel;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

/**
 *
 * @author windiartonugroho
 */
public class frmUtama extends javax.swing.JFrame {

    private Connection koneksi = koneksiDB.condb();
    private sekuel Sequel = new sekuel();
    private String json = "", requestJson, URL = "", link = "", linkbpjs = "", nol_jam = "", nol_menit = "", nol_detik = "", jam = "", menit = "", detik = "", iddokter = "", idpasien = "", sistole = "0", diastole = "0", signa1 = "1", signa2 = "1", datajam = "", utc = "";
    private ApiSatuSehat api = new ApiSatuSehat();
    private ApiMobileJKN apiBPJS = new ApiMobileJKN();
    private HttpHeaders headers;
    private HttpEntity requestEntity;
    private ObjectMapper mapper = new ObjectMapper();
    private JsonNode root;
    private JsonNode response;
    private PreparedStatement ps;
    private ResultSet rs;
    private String[] arrSplit;
    private JsonNode nameNode;
    private SimpleDateFormat tanggalFormat = new SimpleDateFormat("yyyy-MM-dd");
    private Date date = new Date();
    private SatuSehatCekNIK cekViaSatuSehat = new SatuSehatCekNIK();
    private Socket socket;
    private String initialMessage; // Store the message
    private String eventdituju;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Date parsedDate;

    /**
     * Creates new form frmUtama
     */
    public frmUtama() {
        initComponents();
        try {
            link = koneksiDB.URLFHIRSATUSEHAT();
            linkbpjs = koneksiDB.URLAPIMOBILEJKN();
        } catch (Exception e) {
            System.out.println("Notif : " + e);
        }

        this.setSize(1280, 720);

        date = new Date();
        Tanggal1.setText(tanggalFormat.format(date));
        Tanggal2.setText(tanggalFormat.format(date));

        try {

            // Set options for automatic reconnection
            IO.Options options = new IO.Options();
            options.reconnection = true; // Enable automatic reconnection
            options.reconnectionAttempts = Integer.MAX_VALUE; // Unlimited reconnection attempts
            options.reconnectionDelay = 1000; // 1 second delay before each reconnection attempt

            // Connect to the Socket.IO server
            socket = IO.socket(koneksiDB.URLWEBSOCKET());
            socket.connect();

            // Define event listeners
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println("Connected to IO server");
                }
            });

            // Define dynamic listener for all events in the 'satusehat' list
            String[] events = {"kirimencounter", "updateencounter", "hapusencounter", "kirimtaskid", "kirimcondition", "updatecondition", "deletecondition", "insertprocedure", "updateprocedure"};
            for (String event : events) {
                socket.on(event, new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        try {
                            JSONObject data = new JSONObject(args[0].toString());
                            handleEvent(event, data);
                        } catch (Exception e) {
                            System.out.println("Error handling event: " + event + " - " + e);
                        }
                    }
                });
            }

            socket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println("Disconnected from server");
                }
            });

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        TeksArea = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        Nik = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        Tanggal1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        Tanggal2 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        ChkSatusehat = new javax.swing.JCheckBox();
        ChkTaskID = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Service Realtime SatuSehat & BPJS : RS INDRIATI BOYOLALI");

        TeksArea.setColumns(20);
        TeksArea.setRows(5);
        jScrollPane1.setViewportView(TeksArea);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel1.setPreferredSize(new java.awt.Dimension(986, 68));

        jButton2.setText("Generate Token Satu Sehat");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2);

        jButton3.setText("Get ID");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3);

        jButton4.setText("Get ID Practitioner");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton4);

        Nik.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel1.add(Nik);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Tanggal :");
        jLabel1.setPreferredSize(new java.awt.Dimension(70, 23));
        jPanel1.add(jLabel1);

        Tanggal1.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel1.add(Tanggal1);

        jLabel3.setText("s.d.");
        jLabel3.setPreferredSize(new java.awt.Dimension(28, 23));
        jPanel1.add(jLabel3);

        Tanggal2.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel1.add(Tanggal2);

        jLabel2.setPreferredSize(new java.awt.Dimension(30, 23));
        jPanel1.add(jLabel2);

        jButton1.setText("Keluar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1);

        ChkSatusehat.setSelected(true);
        ChkSatusehat.setText("Satusehat");
        jPanel1.add(ChkSatusehat);

        ChkTaskID.setSelected(true);
        ChkTaskID.setText("TaskID");
        jPanel1.add(ChkTaskID);

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_END);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        try {

            TeksArea.append("Bearer :" + api.TokenSatuSehat() + "\n");

        } catch (Exception ea) {
            System.out.println("Notifikasi Bridging : " + ea);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        TeksArea.setText("ID PASIEN : " + cekViaSatuSehat.tampilIDPasien(Nik.getText()));
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        TeksArea.setText("ID PRACTITIONER : " + cekViaSatuSehat.tampilIDParktisi(Nik.getText()));
    }//GEN-LAST:event_jButton4ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frmUtama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmUtama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmUtama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmUtama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmUtama().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox ChkSatusehat;
    private javax.swing.JCheckBox ChkTaskID;
    private javax.swing.JTextField Nik;
    private javax.swing.JTextField Tanggal1;
    private javax.swing.JTextField Tanggal2;
    private javax.swing.JTextArea TeksArea;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

    private void kirimencounter(String norawat) {

        //kirim encounter
        try {
            ps = koneksi.prepareStatement(
                    "select reg_periksa.tgl_registrasi,reg_periksa.jam_reg,reg_periksa.no_rawat,pasien.nm_pasien,pasien.no_ktp,"
                    + "pegawai.nama,pegawai.no_ktp as ktpdokter,poliklinik.nm_poli,satu_sehat_mapping_lokasi_ralan.id_lokasi_satusehat,"
                    + "reg_periksa.status_lanjut,concat(nota_jalan.tanggal,'T',nota_jalan.jam,'+07:00') as pulang,ifnull(satu_sehat_encounter.id_encounter,'') as id_encounter "
                    + "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis inner join pegawai on pegawai.nik=reg_periksa.kd_dokter "
                    + "inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli inner join satu_sehat_mapping_lokasi_ralan on satu_sehat_mapping_lokasi_ralan.kd_poli=poliklinik.kd_poli "
                    + "left join nota_jalan on nota_jalan.no_rawat=reg_periksa.no_rawat left join satu_sehat_encounter on satu_sehat_encounter.no_rawat=reg_periksa.no_rawat "
                    + "where reg_periksa.no_rawat='" + norawat + "'");
            try {
                rs = ps.executeQuery();
                while (rs.next()) {
                    if ((!rs.getString("no_ktp").equals("")) && (!rs.getString("ktpdokter").equals("")) && rs.getString("id_encounter").equals("")) {
                        try {
                            iddokter = cekViaSatuSehat.tampilIDParktisi(rs.getString("ktpdokter"));
                            idpasien = cekViaSatuSehat.tampilIDPasien(rs.getString("no_ktp"));
                            try {
                                headers = new HttpHeaders();
                                headers.setContentType(MediaType.APPLICATION_JSON);
                                headers.add("Authorization", "Bearer " + api.TokenSatuSehat());
                                json = "{"
                                        + "\"resourceType\": \"Encounter\","
                                        + "\"status\": \"arrived\","
                                        + "\"class\": {"
                                        + "\"system\": \"http://terminology.hl7.org/CodeSystem/v3-ActCode\","
                                        + "\"code\": \"" + (rs.getString("status_lanjut").equals("Ralan") ? "AMB" : "IMP") + "\","
                                        + "\"display\": \"" + (rs.getString("status_lanjut").toString().equals("Ralan") ? "ambulatory" : "inpatient encounter") + "\""
                                        + "},"
                                        + "\"subject\": {"
                                        + "\"reference\": \"Patient/" + idpasien + "\","
                                        + "\"display\": \"" + rs.getString("nm_pasien") + "\""
                                        + "},"
                                        + "\"participant\": ["
                                        + "{"
                                        + "\"type\": ["
                                        + "{"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"system\": \"http://terminology.hl7.org/CodeSystem/v3-ParticipationType\","
                                        + "\"code\": \"ATND\","
                                        + "\"display\": \"attender\""
                                        + "}"
                                        + "]"
                                        + "}"
                                        + "],"
                                        + "\"individual\": {"
                                        + "\"reference\": \"Practitioner/" + iddokter + "\","
                                        + "\"display\": \"" + rs.getString("nama") + "\""
                                        + "}"
                                        + "}"
                                        + "],"
                                        + "\"period\": {"
                                        + "\"start\": \"" + rs.getString("tgl_registrasi") + "T" + rs.getString("jam_reg") + "+07:00\""
                                        + "},"
                                        + "\"location\": ["
                                        + "{"
                                        + "\"location\": {"
                                        + "\"reference\": \"Location/" + rs.getString("id_lokasi_satusehat") + "\","
                                        + "\"display\": \"" + rs.getString("nm_poli") + "\""
                                        + "}"
                                        + "}"
                                        + "],"
                                        + "\"statusHistory\": ["
                                        + "{"
                                        + "\"status\": \"arrived\","
                                        + "\"period\": {"
                                        + "\"start\": \"" + rs.getString("tgl_registrasi") + "T" + rs.getString("jam_reg") + "+07:00\""
                                        + "}"
                                        + "}"
                                        + "],"
                                        + "\"serviceProvider\": {"
                                        + "\"reference\": \"Organization/" + koneksiDB.IDSATUSEHAT() + "\""
                                        + "},"
                                        + "\"identifier\": ["
                                        + "{"
                                        + "\"system\": \"http://sys-ids.kemkes.go.id/encounter/" + koneksiDB.IDSATUSEHAT() + "\","
                                        + "\"value\": \"" + rs.getString("no_rawat") + "\""
                                        + "}"
                                        + "]"
                                        + "}";
                                System.out.println("URL : " + link + "/Encounter");
                                System.out.println("Request JSON : " + json);
                                requestEntity = new HttpEntity(json, headers);
                                json = api.getRest().exchange(link + "/Encounter", HttpMethod.POST, requestEntity, String.class).getBody();
                                System.out.println("Result JSON : " + json);
                                root = mapper.readTree(json);
                                response = root.path("id");
                                if (!response.asText().equals("")) {
                                    Sequel.menyimpan("satu_sehat_encounter", "?,?", "No.Rawat", 2, new String[]{
                                        norawat, response.asText()
                                    });

                                    TeksArea.append("\n" + norawat + ":" + response.asText());
                                }
                            } catch (HttpClientErrorException | HttpServerErrorException e) {
                                // Handle client and server errors
                                System.err.println("Error Response Status Code: " + e.getStatusCode());
//                            System.err.println("Error Response Body: " + e.getResponseBodyAsString());
                                // You can further parse the error response body if needed
                                ObjectMapper mapper = new ObjectMapper();
                                JsonNode errorResponse = mapper.readTree(e.getResponseBodyAsString());
                                ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
                                String prettyErrorResponse = writer.writeValueAsString(errorResponse);
                                System.err.println("Error Response JSON: \n" + prettyErrorResponse);
                            }
                        } catch (Exception ef) {
                            System.out.println("Notifikasi : " + ef);
                        }
                    }
                }
            } catch (Exception ex) {
                System.out.println("Notif : " + ex);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }

            ps = koneksi.prepareStatement(
                    "select reg_periksa.tgl_registrasi,reg_periksa.jam_reg,reg_periksa.no_rawat,pasien.nm_pasien,pasien.no_ktp,"
                    + "pegawai.nama,pegawai.no_ktp as ktpdokter,poliklinik.nm_poli,satu_sehat_mapping_lokasi_ralan.id_lokasi_satusehat,"
                    + "reg_periksa.status_lanjut,concat(nota_inap.tanggal,'T',nota_inap.jam,'+07:00') as pulang,ifnull(satu_sehat_encounter.id_encounter,'') as id_encounter "
                    + "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis inner join pegawai on pegawai.nik=reg_periksa.kd_dokter "
                    + "inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli inner join satu_sehat_mapping_lokasi_ralan on satu_sehat_mapping_lokasi_ralan.kd_poli=poliklinik.kd_poli "
                    + "left join nota_inap on nota_inap.no_rawat=reg_periksa.no_rawat left join satu_sehat_encounter on satu_sehat_encounter.no_rawat=reg_periksa.no_rawat "
                    + "where reg_periksa.no_rawat='" + norawat + "'");
            try {
                rs = ps.executeQuery();
                while (rs.next()) {
                    if ((!rs.getString("no_ktp").equals("")) && (!rs.getString("ktpdokter").equals("")) && rs.getString("id_encounter").equals("")) {
                        try {
                            iddokter = cekViaSatuSehat.tampilIDParktisi(rs.getString("ktpdokter"));
                            idpasien = cekViaSatuSehat.tampilIDPasien(rs.getString("no_ktp"));
                            try {
                                headers = new HttpHeaders();
                                headers.setContentType(MediaType.APPLICATION_JSON);
                                headers.add("Authorization", "Bearer " + api.TokenSatuSehat());
                                json = "{"
                                        + "\"resourceType\": \"Encounter\","
                                        + "\"status\": \"arrived\","
                                        + "\"class\": {"
                                        + "\"system\": \"http://terminology.hl7.org/CodeSystem/v3-ActCode\","
                                        + "\"code\": \"" + (rs.getString("status_lanjut").equals("Ralan") ? "AMB" : "IMP") + "\","
                                        + "\"display\": \"" + (rs.getString("status_lanjut").toString().equals("Ralan") ? "ambulatory" : "inpatient encounter") + "\""
                                        + "},"
                                        + "\"subject\": {"
                                        + "\"reference\": \"Patient/" + idpasien + "\","
                                        + "\"display\": \"" + rs.getString("nm_pasien") + "\""
                                        + "},"
                                        + "\"participant\": ["
                                        + "{"
                                        + "\"type\": ["
                                        + "{"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"system\": \"http://terminology.hl7.org/CodeSystem/v3-ParticipationType\","
                                        + "\"code\": \"ATND\","
                                        + "\"display\": \"attender\""
                                        + "}"
                                        + "]"
                                        + "}"
                                        + "],"
                                        + "\"individual\": {"
                                        + "\"reference\": \"Practitioner/" + iddokter + "\","
                                        + "\"display\": \"" + rs.getString("nama") + "\""
                                        + "}"
                                        + "}"
                                        + "],"
                                        + "\"period\": {"
                                        + "\"start\": \"" + rs.getString("tgl_registrasi") + "T" + rs.getString("jam_reg") + "+07:00\""
                                        + "},"
                                        + "\"location\": ["
                                        + "{"
                                        + "\"location\": {"
                                        + "\"reference\": \"Location/" + rs.getString("id_lokasi_satusehat") + "\","
                                        + "\"display\": \"" + rs.getString("nm_poli") + "\""
                                        + "}"
                                        + "}"
                                        + "],"
                                        + "\"statusHistory\": ["
                                        + "{"
                                        + "\"status\": \"arrived\","
                                        + "\"period\": {"
                                        + "\"start\": \"" + rs.getString("tgl_registrasi") + "T" + rs.getString("jam_reg") + "+07:00\""
                                        + "}"
                                        + "}"
                                        + "],"
                                        + "\"serviceProvider\": {"
                                        + "\"reference\": \"Organization/" + koneksiDB.IDSATUSEHAT() + "\""
                                        + "},"
                                        + "\"identifier\": ["
                                        + "{"
                                        + "\"system\": \"http://sys-ids.kemkes.go.id/encounter/" + koneksiDB.IDSATUSEHAT() + "\","
                                        + "\"value\": \"" + rs.getString("no_rawat") + "\""
                                        + "}"
                                        + "]"
                                        + "}";
                                System.out.println("URL : " + link + "/Encounter");
                                System.out.println("Request JSON : " + json);
                                requestEntity = new HttpEntity(json, headers);
                                json = api.getRest().exchange(link + "/Encounter", HttpMethod.POST, requestEntity, String.class).getBody();
                                System.out.println("Result JSON : " + json);
                                root = mapper.readTree(json);
                                response = root.path("id");
                                if (!response.asText().equals("")) {
                                    Sequel.menyimpan("satu_sehat_encounter", "?,?", "No.Rawat", 2, new String[]{
                                        norawat, response.asText()
                                    });
                                    TeksArea.append("\n" + norawat + ":" + response.asText());
                                }
                            } catch (HttpClientErrorException | HttpServerErrorException e) {
                                // Handle client and server errors
                                System.err.println("Error Response Status Code: " + e.getStatusCode());
//                            System.err.println("Error Response Body: " + e.getResponseBodyAsString());
                                // You can further parse the error response body if needed
                                ObjectMapper mapper = new ObjectMapper();
                                JsonNode errorResponse = mapper.readTree(e.getResponseBodyAsString());
                                ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
                                String prettyErrorResponse = writer.writeValueAsString(errorResponse);
                                System.err.println("Error Response JSON: \n" + prettyErrorResponse);
                            }
                        } catch (Exception ef) {
                            System.out.println("Notifikasi : " + ef);
                        }
                    }
                }
            } catch (Exception ex) {
                System.out.println("Notif : " + ex);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception ez) {
            System.out.println("Notifikasi : " + ez);
        }
    }

    private void hapusEncounter(String norawat) {

        //kirim encounter
        try {
            ps = koneksi.prepareStatement(
                    "select reg_periksa.tgl_registrasi,reg_periksa.jam_reg,reg_periksa.no_rawat,pasien.nm_pasien,pasien.no_ktp,"
                    + "pegawai.nama,pegawai.no_ktp as ktpdokter,poliklinik.nm_poli,satu_sehat_mapping_lokasi_ralan.id_lokasi_satusehat,"
                    + "reg_periksa.status_lanjut,concat(nota_jalan.tanggal,'T',nota_jalan.jam,'+07:00') as pulang,ifnull(satu_sehat_encounter.id_encounter,'') as id_encounter "
                    + "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis inner join pegawai on pegawai.nik=reg_periksa.kd_dokter "
                    + "inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli inner join satu_sehat_mapping_lokasi_ralan on satu_sehat_mapping_lokasi_ralan.kd_poli=poliklinik.kd_poli "
                    + "left join nota_jalan on nota_jalan.no_rawat=reg_periksa.no_rawat left join satu_sehat_encounter on satu_sehat_encounter.no_rawat=reg_periksa.no_rawat "
                    + "where reg_periksa.no_rawat='" + norawat + "'");
            try {
                rs = ps.executeQuery();
                while (rs.next()) {
                    if ((!rs.getString("no_ktp").equals("")) && (!rs.getString("ktpdokter").equals("")) && rs.getString("id_encounter").equals("")) {
                        try {
                            iddokter = cekViaSatuSehat.tampilIDParktisi(rs.getString("ktpdokter"));
                            idpasien = cekViaSatuSehat.tampilIDPasien(rs.getString("no_ktp"));
                            try {
                                headers = new HttpHeaders();
                                headers.setContentType(MediaType.APPLICATION_JSON);
                                headers.add("Authorization", "Bearer " + api.TokenSatuSehat());
                                json = "{"
                                        + "\"resourceType\": \"Encounter\","
                                        + "\"status\": \"arrived\","
                                        + "\"class\": {"
                                        + "\"system\": \"http://terminology.hl7.org/CodeSystem/v3-ActCode\","
                                        + "\"code\": \"" + (rs.getString("status_lanjut").equals("Ralan") ? "AMB" : "IMP") + "\","
                                        + "\"display\": \"" + (rs.getString("status_lanjut").toString().equals("Ralan") ? "ambulatory" : "inpatient encounter") + "\""
                                        + "},"
                                        + "\"subject\": {"
                                        + "\"reference\": \"Patient/" + idpasien + "\","
                                        + "\"display\": \"" + rs.getString("nm_pasien") + "\""
                                        + "},"
                                        + "\"participant\": ["
                                        + "{"
                                        + "\"type\": ["
                                        + "{"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"system\": \"http://terminology.hl7.org/CodeSystem/v3-ParticipationType\","
                                        + "\"code\": \"ATND\","
                                        + "\"display\": \"attender\""
                                        + "}"
                                        + "]"
                                        + "}"
                                        + "],"
                                        + "\"individual\": {"
                                        + "\"reference\": \"Practitioner/" + iddokter + "\","
                                        + "\"display\": \"" + rs.getString("nama") + "\""
                                        + "}"
                                        + "}"
                                        + "],"
                                        + "\"period\": {"
                                        + "\"start\": \"" + rs.getString("tgl_registrasi") + "T" + rs.getString("jam_reg") + "+07:00\""
                                        + "},"
                                        + "\"location\": ["
                                        + "{"
                                        + "\"location\": {"
                                        + "\"reference\": \"Location/" + rs.getString("id_lokasi_satusehat") + "\","
                                        + "\"display\": \"" + rs.getString("nm_poli") + "\""
                                        + "}"
                                        + "}"
                                        + "],"
                                        + "\"statusHistory\": ["
                                        + "{"
                                        + "\"status\": \"arrived\","
                                        + "\"period\": {"
                                        + "\"start\": \"" + rs.getString("tgl_registrasi") + "T" + rs.getString("jam_reg") + "+07:00\""
                                        + "}"
                                        + "}"
                                        + "],"
                                        + "\"serviceProvider\": {"
                                        + "\"reference\": \"Organization/" + koneksiDB.IDSATUSEHAT() + "\""
                                        + "},"
                                        + "\"identifier\": ["
                                        + "{"
                                        + "\"system\": \"http://sys-ids.kemkes.go.id/encounter/" + koneksiDB.IDSATUSEHAT() + "\","
                                        + "\"value\": \"" + rs.getString("no_rawat") + "\""
                                        + "}"
                                        + "]"
                                        + "}";
                                System.out.println("URL : " + link + "/Encounter");
                                System.out.println("Request JSON : " + json);
                                requestEntity = new HttpEntity(json, headers);
                                json = api.getRest().exchange(link + "/Encounter", HttpMethod.POST, requestEntity, String.class).getBody();
                                System.out.println("Result JSON : " + json);
                                root = mapper.readTree(json);
                                response = root.path("id");
                                if (!response.asText().equals("")) {
                                    Sequel.menyimpan("satu_sehat_encounter", "?,?", "No.Rawat", 2, new String[]{
                                        norawat, response.asText()
                                    });

                                    TeksArea.append("\n" + norawat + ":" + response.asText());
                                }
                            } catch (HttpClientErrorException | HttpServerErrorException e) {
                                // Handle client and server errors
                                System.err.println("Error Response Status Code: " + e.getStatusCode());
//                            System.err.println("Error Response Body: " + e.getResponseBodyAsString());
                                // You can further parse the error response body if needed
                                ObjectMapper mapper = new ObjectMapper();
                                JsonNode errorResponse = mapper.readTree(e.getResponseBodyAsString());
                                ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
                                String prettyErrorResponse = writer.writeValueAsString(errorResponse);
                                System.err.println("Error Response JSON: \n" + prettyErrorResponse);
                            }
                        } catch (Exception ef) {
                            System.out.println("Notifikasi : " + ef);
                        }
                    }
                }
            } catch (Exception ex) {
                System.out.println("Notif : " + ex);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }

            ps = koneksi.prepareStatement(
                    "select reg_periksa.tgl_registrasi,reg_periksa.jam_reg,reg_periksa.no_rawat,pasien.nm_pasien,pasien.no_ktp,"
                    + "pegawai.nama,pegawai.no_ktp as ktpdokter,poliklinik.nm_poli,satu_sehat_mapping_lokasi_ralan.id_lokasi_satusehat,"
                    + "reg_periksa.status_lanjut,concat(nota_inap.tanggal,'T',nota_inap.jam,'+07:00') as pulang,ifnull(satu_sehat_encounter.id_encounter,'') as id_encounter "
                    + "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis inner join pegawai on pegawai.nik=reg_periksa.kd_dokter "
                    + "inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli inner join satu_sehat_mapping_lokasi_ralan on satu_sehat_mapping_lokasi_ralan.kd_poli=poliklinik.kd_poli "
                    + "left join nota_inap on nota_inap.no_rawat=reg_periksa.no_rawat left join satu_sehat_encounter on satu_sehat_encounter.no_rawat=reg_periksa.no_rawat "
                    + "where reg_periksa.no_rawat='" + norawat + "'");
            try {
                rs = ps.executeQuery();
                while (rs.next()) {
                    if ((!rs.getString("no_ktp").equals("")) && (!rs.getString("ktpdokter").equals("")) && rs.getString("id_encounter").equals("")) {
                        try {
                            iddokter = cekViaSatuSehat.tampilIDParktisi(rs.getString("ktpdokter"));
                            idpasien = cekViaSatuSehat.tampilIDPasien(rs.getString("no_ktp"));
                            try {
                                headers = new HttpHeaders();
                                headers.setContentType(MediaType.APPLICATION_JSON);
                                headers.add("Authorization", "Bearer " + api.TokenSatuSehat());
                                json = "{"
                                        + "\"resourceType\": \"Encounter\","
                                        + "\"status\": \"arrived\","
                                        + "\"class\": {"
                                        + "\"system\": \"http://terminology.hl7.org/CodeSystem/v3-ActCode\","
                                        + "\"code\": \"" + (rs.getString("status_lanjut").equals("Ralan") ? "AMB" : "IMP") + "\","
                                        + "\"display\": \"" + (rs.getString("status_lanjut").toString().equals("Ralan") ? "ambulatory" : "inpatient encounter") + "\""
                                        + "},"
                                        + "\"subject\": {"
                                        + "\"reference\": \"Patient/" + idpasien + "\","
                                        + "\"display\": \"" + rs.getString("nm_pasien") + "\""
                                        + "},"
                                        + "\"participant\": ["
                                        + "{"
                                        + "\"type\": ["
                                        + "{"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"system\": \"http://terminology.hl7.org/CodeSystem/v3-ParticipationType\","
                                        + "\"code\": \"ATND\","
                                        + "\"display\": \"attender\""
                                        + "}"
                                        + "]"
                                        + "}"
                                        + "],"
                                        + "\"individual\": {"
                                        + "\"reference\": \"Practitioner/" + iddokter + "\","
                                        + "\"display\": \"" + rs.getString("nama") + "\""
                                        + "}"
                                        + "}"
                                        + "],"
                                        + "\"period\": {"
                                        + "\"start\": \"" + rs.getString("tgl_registrasi") + "T" + rs.getString("jam_reg") + "+07:00\""
                                        + "},"
                                        + "\"location\": ["
                                        + "{"
                                        + "\"location\": {"
                                        + "\"reference\": \"Location/" + rs.getString("id_lokasi_satusehat") + "\","
                                        + "\"display\": \"" + rs.getString("nm_poli") + "\""
                                        + "}"
                                        + "}"
                                        + "],"
                                        + "\"statusHistory\": ["
                                        + "{"
                                        + "\"status\": \"arrived\","
                                        + "\"period\": {"
                                        + "\"start\": \"" + rs.getString("tgl_registrasi") + "T" + rs.getString("jam_reg") + "+07:00\""
                                        + "}"
                                        + "}"
                                        + "],"
                                        + "\"serviceProvider\": {"
                                        + "\"reference\": \"Organization/" + koneksiDB.IDSATUSEHAT() + "\""
                                        + "},"
                                        + "\"identifier\": ["
                                        + "{"
                                        + "\"system\": \"http://sys-ids.kemkes.go.id/encounter/" + koneksiDB.IDSATUSEHAT() + "\","
                                        + "\"value\": \"" + rs.getString("no_rawat") + "\""
                                        + "}"
                                        + "]"
                                        + "}";
                                System.out.println("URL : " + link + "/Encounter");
                                System.out.println("Request JSON : " + json);
                                requestEntity = new HttpEntity(json, headers);
                                json = api.getRest().exchange(link + "/Encounter", HttpMethod.POST, requestEntity, String.class).getBody();
                                System.out.println("Result JSON : " + json);
                                root = mapper.readTree(json);
                                response = root.path("id");
                                if (!response.asText().equals("")) {
                                    Sequel.menyimpan("satu_sehat_encounter", "?,?", "No.Rawat", 2, new String[]{
                                        norawat, response.asText()
                                    });
                                    TeksArea.append("\n" + norawat + ":" + response.asText());
                                }
                            } catch (HttpClientErrorException | HttpServerErrorException e) {
                                // Handle client and server errors
                                System.err.println("Error Response Status Code: " + e.getStatusCode());
//                            System.err.println("Error Response Body: " + e.getResponseBodyAsString());
                                // You can further parse the error response body if needed
                                ObjectMapper mapper = new ObjectMapper();
                                JsonNode errorResponse = mapper.readTree(e.getResponseBodyAsString());
                                ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
                                String prettyErrorResponse = writer.writeValueAsString(errorResponse);
                                System.err.println("Error Response JSON: \n" + prettyErrorResponse);
                            }
                        } catch (Exception ef) {
                            System.out.println("Notifikasi : " + ef);
                        }
                    }
                }
            } catch (Exception ex) {
                System.out.println("Notif : " + ex);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception ez) {
            System.out.println("Notifikasi : " + ez);
        }
    }

    private void observationTTV() {
        //kirim TTV Suhu
        try {
            ps = koneksi.prepareStatement(
                    "select reg_periksa.no_rawat,pasien.nm_pasien,pasien.no_ktp,satu_sehat_encounter.id_encounter,pegawai.no_ktp as ktppraktisi,pemeriksaan_ralan.tgl_perawatan,"
                    + "pemeriksaan_ralan.jam_rawat,pemeriksaan_ralan.suhu_tubuh,ifnull(satu_sehat_observationttvsuhu.id_observation,'') as satu_sehat_observationttvsuhu "
                    + "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis inner join nota_jalan on nota_jalan.no_rawat=reg_periksa.no_rawat "
                    + "inner join satu_sehat_encounter on satu_sehat_encounter.no_rawat=reg_periksa.no_rawat inner join pemeriksaan_ralan on pemeriksaan_ralan.no_rawat=reg_periksa.no_rawat "
                    + "inner join pegawai on pemeriksaan_ralan.nip=pegawai.nik left join satu_sehat_observationttvsuhu on satu_sehat_observationttvsuhu.no_rawat=pemeriksaan_ralan.no_rawat "
                    + "and satu_sehat_observationttvsuhu.tgl_perawatan=pemeriksaan_ralan.tgl_perawatan and satu_sehat_observationttvsuhu.jam_rawat=pemeriksaan_ralan.jam_rawat "
                    + "and satu_sehat_observationttvsuhu.status='Ralan' where pemeriksaan_ralan.suhu_tubuh<>'' and nota_jalan.tanggal between ? and ?");
            try {
                ps.setString(1, Tanggal1.getText());
                ps.setString(2, Tanggal2.getText());
                rs = ps.executeQuery();
                while (rs.next()) {
                    if ((!rs.getString("no_ktp").equals("")) && (!rs.getString("ktppraktisi").equals("")) && rs.getString("satu_sehat_observationttvsuhu").equals("")) {
                        try {
                            iddokter = cekViaSatuSehat.tampilIDParktisi(rs.getString("ktppraktisi"));
                            idpasien = cekViaSatuSehat.tampilIDPasien(rs.getString("no_ktp"));
                            try {
                                headers = new HttpHeaders();
                                headers.setContentType(MediaType.APPLICATION_JSON);
                                headers.add("Authorization", "Bearer " + api.TokenSatuSehat());
                                json = "{"
                                        + "\"resourceType\": \"Observation\","
                                        + "\"status\": \"final\","
                                        + "\"category\": ["
                                        + "{"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"system\": \"http://terminology.hl7.org/CodeSystem/observation-category\","
                                        + "\"code\": \"vital-signs\","
                                        + "\"display\": \"Vital Signs\""
                                        + "}"
                                        + "]"
                                        + "}"
                                        + "],"
                                        + "\"code\": {"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"system\": \"http://loinc.org\","
                                        + "\"code\": \"8310-5\","
                                        + "\"display\": \"Body temperature\""
                                        + "}"
                                        + "]"
                                        + "},"
                                        + "\"subject\": {"
                                        + "\"reference\": \"Patient/" + idpasien + "\""
                                        + "},"
                                        + "\"performer\": ["
                                        + "{"
                                        + "\"reference\": \"Practitioner/" + iddokter + "\""
                                        + "}"
                                        + "],"
                                        + "\"encounter\": {"
                                        + "\"reference\": \"Encounter/" + rs.getString("id_encounter") + "\","
                                        + "\"display\": \"Pemeriksaan Fisik Suhu Badan di Rawat Jalan/IGD, Pasien " + rs.getString("nm_pasien") + " Pada Tanggal " + rs.getString("tgl_perawatan") + " Jam " + rs.getString("jam_rawat") + "\""
                                        + "},"
                                        + "\"effectiveDateTime\": \"" + rs.getString("tgl_perawatan") + "T" + rs.getString("jam_rawat") + "+07:00\","
                                        + "\"valueQuantity\": {"
                                        + "\"value\": " + rs.getString("suhu_tubuh").replaceAll(",", ".") + ","
                                        + "\"unit\": \"degree Celsius\","
                                        + "\"system\": \"http://unitsofmeasure.org\","
                                        + "\"code\": \"Cel\""
                                        + "}"
                                        + "}";
                                TeksArea.append("URL : " + link + "/Observation" + "\n");
                                TeksArea.append("Request JSON : " + json + "\n");
                                requestEntity = new HttpEntity(json, headers);
                                json = api.getRest().exchange(link + "/Observation", HttpMethod.POST, requestEntity, String.class).getBody();
                                TeksArea.append("Result JSON : " + json + "\n");
                                root = mapper.readTree(json);
                                response = root.path("id");
                                if (!response.asText().equals("")) {
                                    Sequel.menyimpan2("satu_sehat_observationttvsuhu", "?,?,?,?,?", "Observation Suhu", 5, new String[]{
                                        rs.getString("no_rawat"), rs.getString("tgl_perawatan"), rs.getString("jam_rawat"), "Ralan", response.asText()
                                    });
                                }
                            } catch (Exception eg) {
                                System.out.println("Notifikasi Bridging : " + eg);
                            }
                        } catch (Exception ed) {
                            System.out.println("Notifikasi : " + ed);
                        }
                    }
                }
            } catch (Exception ez) {
                System.out.println("Notif : " + ez);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }

            ps = koneksi.prepareStatement(
                    "select reg_periksa.no_rawat,pasien.nm_pasien,pasien.no_ktp,satu_sehat_encounter.id_encounter,pegawai.no_ktp as ktppraktisi,pemeriksaan_ranap.tgl_perawatan,"
                    + "pemeriksaan_ranap.jam_rawat,pemeriksaan_ranap.suhu_tubuh,ifnull(satu_sehat_observationttvsuhu.id_observation,'') as satu_sehat_observationttvsuhu "
                    + "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis inner join nota_inap on nota_inap.no_rawat=reg_periksa.no_rawat "
                    + "inner join satu_sehat_encounter on satu_sehat_encounter.no_rawat=reg_periksa.no_rawat inner join pemeriksaan_ranap on pemeriksaan_ranap.no_rawat=reg_periksa.no_rawat "
                    + "inner join pegawai on pemeriksaan_ranap.nip=pegawai.nik left join satu_sehat_observationttvsuhu on satu_sehat_observationttvsuhu.no_rawat=pemeriksaan_ranap.no_rawat "
                    + "and satu_sehat_observationttvsuhu.tgl_perawatan=pemeriksaan_ranap.tgl_perawatan and satu_sehat_observationttvsuhu.jam_rawat=pemeriksaan_ranap.jam_rawat "
                    + "and satu_sehat_observationttvsuhu.status='Ranap' where pemeriksaan_ranap.suhu_tubuh<>'' and nota_inap.tanggal between ? and ? ");
            try {
                ps.setString(1, Tanggal1.getText());
                ps.setString(2, Tanggal2.getText());
                rs = ps.executeQuery();
                while (rs.next()) {
                    if ((!rs.getString("no_ktp").equals("")) && (!rs.getString("ktppraktisi").equals("")) && rs.getString("satu_sehat_observationttvsuhu").equals("")) {
                        try {
                            iddokter = cekViaSatuSehat.tampilIDParktisi(rs.getString("ktppraktisi"));
                            idpasien = cekViaSatuSehat.tampilIDPasien(rs.getString("no_ktp"));
                            try {
                                headers = new HttpHeaders();
                                headers.setContentType(MediaType.APPLICATION_JSON);
                                headers.add("Authorization", "Bearer " + api.TokenSatuSehat());
                                json = "{"
                                        + "\"resourceType\": \"Observation\","
                                        + "\"status\": \"final\","
                                        + "\"category\": ["
                                        + "{"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"system\": \"http://terminology.hl7.org/CodeSystem/observation-category\","
                                        + "\"code\": \"vital-signs\","
                                        + "\"display\": \"Vital Signs\""
                                        + "}"
                                        + "]"
                                        + "}"
                                        + "],"
                                        + "\"code\": {"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"system\": \"http://loinc.org\","
                                        + "\"code\": \"8310-5\","
                                        + "\"display\": \"Body temperature\""
                                        + "}"
                                        + "]"
                                        + "},"
                                        + "\"subject\": {"
                                        + "\"reference\": \"Patient/" + idpasien + "\""
                                        + "},"
                                        + "\"performer\": ["
                                        + "{"
                                        + "\"reference\": \"Practitioner/" + iddokter + "\""
                                        + "}"
                                        + "],"
                                        + "\"encounter\": {"
                                        + "\"reference\": \"Encounter/" + rs.getString("id_encounter") + "\","
                                        + "\"display\": \"Pemeriksaan Fisik Suhu Badan di Rawat Inap, Pasien " + rs.getString("nm_pasien") + " Pada Tanggal " + rs.getString("tgl_perawatan") + " Jam " + rs.getString("jam_rawat") + "\""
                                        + "},"
                                        + "\"effectiveDateTime\": \"" + rs.getString("tgl_perawatan") + "T" + rs.getString("jam_rawat") + "+07:00\","
                                        + "\"valueQuantity\": {"
                                        + "\"value\": " + rs.getString("suhu_tubuh").replaceAll(",", ".") + ","
                                        + "\"unit\": \"degree Celsius\","
                                        + "\"system\": \"http://unitsofmeasure.org\","
                                        + "\"code\": \"Cel\""
                                        + "}"
                                        + "}";
                                TeksArea.append("URL : " + link + "/Observation" + "\n");
                                TeksArea.append("Request JSON : " + json + "\n");
                                requestEntity = new HttpEntity(json, headers);
                                json = api.getRest().exchange(link + "/Observation", HttpMethod.POST, requestEntity, String.class).getBody();
                                TeksArea.append("Result JSON : " + json + "\n");
                                root = mapper.readTree(json);
                                response = root.path("id");
                                if (!response.asText().equals("")) {
                                    Sequel.menyimpan2("satu_sehat_observationttvsuhu", "?,?,?,?,?", "Observation Suhu", 5, new String[]{
                                        rs.getString("no_rawat"), rs.getString("tgl_perawatan"), rs.getString("jam_rawat"), "Ranap", response.asText()
                                    });
                                }
                            } catch (Exception eg) {
                                System.out.println("Notifikasi Bridging : " + eg);
                            }
                        } catch (Exception ed) {
                            System.out.println("Notifikasi : " + ed);
                        }
                    }
                }
            } catch (Exception ez) {
                System.out.println("Notif : " + ez);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception ef) {
            System.out.println("Notifikasi : " + ef);
        }

        //kirim TTV respirasi
        try {
            ps = koneksi.prepareStatement(
                    "select reg_periksa.no_rawat,pasien.nm_pasien,pasien.no_ktp,satu_sehat_encounter.id_encounter,pegawai.no_ktp as ktppraktisi,pemeriksaan_ralan.tgl_perawatan,"
                    + "pemeriksaan_ralan.jam_rawat,pemeriksaan_ralan.respirasi,ifnull(satu_sehat_observationttvrespirasi.id_observation,'') as satu_sehat_observationttvrespirasi "
                    + "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis inner join nota_jalan on nota_jalan.no_rawat=reg_periksa.no_rawat "
                    + "inner join satu_sehat_encounter on satu_sehat_encounter.no_rawat=reg_periksa.no_rawat inner join pemeriksaan_ralan on pemeriksaan_ralan.no_rawat=reg_periksa.no_rawat "
                    + "inner join pegawai on pemeriksaan_ralan.nip=pegawai.nik left join satu_sehat_observationttvrespirasi on satu_sehat_observationttvrespirasi.no_rawat=pemeriksaan_ralan.no_rawat "
                    + "and satu_sehat_observationttvrespirasi.tgl_perawatan=pemeriksaan_ralan.tgl_perawatan and satu_sehat_observationttvrespirasi.jam_rawat=pemeriksaan_ralan.jam_rawat "
                    + "and satu_sehat_observationttvrespirasi.status='Ralan' where pemeriksaan_ralan.respirasi<>'' and nota_jalan.tanggal between ? and ? ");
            try {
                ps.setString(1, Tanggal1.getText());
                ps.setString(2, Tanggal2.getText());
                rs = ps.executeQuery();
                while (rs.next()) {
                    if ((!rs.getString("no_ktp").equals("")) && (!rs.getString("ktppraktisi").equals("")) && rs.getString("satu_sehat_observationttvrespirasi").equals("")) {
                        try {
                            iddokter = cekViaSatuSehat.tampilIDParktisi(rs.getString("ktppraktisi"));
                            idpasien = cekViaSatuSehat.tampilIDPasien(rs.getString("no_ktp"));
                            try {
                                headers = new HttpHeaders();
                                headers.setContentType(MediaType.APPLICATION_JSON);
                                headers.add("Authorization", "Bearer " + api.TokenSatuSehat());
                                json = "{"
                                        + "\"resourceType\": \"Observation\","
                                        + "\"status\": \"final\","
                                        + "\"category\": ["
                                        + "{"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"system\": \"http://terminology.hl7.org/CodeSystem/observation-category\","
                                        + "\"code\": \"vital-signs\","
                                        + "\"display\": \"Vital Signs\""
                                        + "}"
                                        + "]"
                                        + "}"
                                        + "],"
                                        + "\"code\": {"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"system\": \"http://loinc.org\","
                                        + "\"code\": \"9279-1\","
                                        + "\"display\": \"Respiratory rate\""
                                        + "}"
                                        + "]"
                                        + "},"
                                        + "\"subject\": {"
                                        + "\"reference\": \"Patient/" + idpasien + "\""
                                        + "},"
                                        + "\"performer\": ["
                                        + "{"
                                        + "\"reference\": \"Practitioner/" + iddokter + "\""
                                        + "}"
                                        + "],"
                                        + "\"encounter\": {"
                                        + "\"reference\": \"Encounter/" + rs.getString("id_encounter") + "\","
                                        + "\"display\": \"Pemeriksaan Fisik Respirasi di Rawat Jalan/IGD, Pasien " + rs.getString("nm_pasien") + " Pada Tanggal " + rs.getString("tgl_perawatan") + " Jam " + rs.getString("jam_rawat") + "\""
                                        + "},"
                                        + "\"effectiveDateTime\": \"" + rs.getString("tgl_perawatan") + "T" + rs.getString("jam_rawat") + "+07:00\","
                                        + "\"valueQuantity\": {"
                                        + "\"value\": " + rs.getString("respirasi") + ","
                                        + "\"unit\": \"breaths/minute\","
                                        + "\"system\": \"http://unitsofmeasure.org\","
                                        + "\"code\": \"/min\""
                                        + "}"
                                        + "}";
                                TeksArea.append("URL : " + link + "/Observation" + "\n");
                                TeksArea.append("Request JSON : " + json + "\n");
                                requestEntity = new HttpEntity(json, headers);
                                json = api.getRest().exchange(link + "/Observation", HttpMethod.POST, requestEntity, String.class).getBody();
                                TeksArea.append("Result JSON : " + json + "\n");
                                root = mapper.readTree(json);
                                response = root.path("id");
                                if (!response.asText().equals("")) {
                                    Sequel.menyimpan2("satu_sehat_observationttvrespirasi", "?,?,?,?,?", "Observation Respirasi", 5, new String[]{
                                        rs.getString("no_rawat"), rs.getString("tgl_perawatan"), rs.getString("jam_rawat"), "Ralan", response.asText()
                                    });
                                }
                            } catch (Exception ef) {
                                System.out.println("Notifikasi Bridging : " + ef);
                            }
                        } catch (Exception eg) {
                            System.out.println("Notifikasi : " + eg);
                        }
                    }
                }
            } catch (Exception ez) {
                System.out.println("Notif : " + ez);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }

            ps = koneksi.prepareStatement(
                    "select reg_periksa.no_rawat,pasien.nm_pasien,pasien.no_ktp,satu_sehat_encounter.id_encounter,pegawai.no_ktp as ktppraktisi,pemeriksaan_ranap.tgl_perawatan,"
                    + "pemeriksaan_ranap.jam_rawat,pemeriksaan_ranap.respirasi,ifnull(satu_sehat_observationttvrespirasi.id_observation,'') as satu_sehat_observationttvrespirasi "
                    + "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis inner join nota_inap on nota_inap.no_rawat=reg_periksa.no_rawat "
                    + "inner join satu_sehat_encounter on satu_sehat_encounter.no_rawat=reg_periksa.no_rawat inner join pemeriksaan_ranap on pemeriksaan_ranap.no_rawat=reg_periksa.no_rawat "
                    + "inner join pegawai on pemeriksaan_ranap.nip=pegawai.nik left join satu_sehat_observationttvrespirasi on satu_sehat_observationttvrespirasi.no_rawat=pemeriksaan_ranap.no_rawat "
                    + "and satu_sehat_observationttvrespirasi.tgl_perawatan=pemeriksaan_ranap.tgl_perawatan and satu_sehat_observationttvrespirasi.jam_rawat=pemeriksaan_ranap.jam_rawat "
                    + "and satu_sehat_observationttvrespirasi.status='Ranap' where pemeriksaan_ranap.respirasi<>'' and nota_inap.tanggal between ? and ? ");
            try {
                ps.setString(1, Tanggal1.getText());
                ps.setString(2, Tanggal2.getText());
                rs = ps.executeQuery();
                while (rs.next()) {
                    if ((!rs.getString("no_ktp").equals("")) && (!rs.getString("ktppraktisi").equals("")) && rs.getString("satu_sehat_observationttvrespirasi").equals("")) {
                        try {
                            iddokter = cekViaSatuSehat.tampilIDParktisi(rs.getString("ktppraktisi"));
                            idpasien = cekViaSatuSehat.tampilIDPasien(rs.getString("no_ktp"));
                            try {
                                headers = new HttpHeaders();
                                headers.setContentType(MediaType.APPLICATION_JSON);
                                headers.add("Authorization", "Bearer " + api.TokenSatuSehat());
                                json = "{"
                                        + "\"resourceType\": \"Observation\","
                                        + "\"status\": \"final\","
                                        + "\"category\": ["
                                        + "{"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"system\": \"http://terminology.hl7.org/CodeSystem/observation-category\","
                                        + "\"code\": \"vital-signs\","
                                        + "\"display\": \"Vital Signs\""
                                        + "}"
                                        + "]"
                                        + "}"
                                        + "],"
                                        + "\"code\": {"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"system\": \"http://loinc.org\","
                                        + "\"code\": \"9279-1\","
                                        + "\"display\": \"Respiratory rate\""
                                        + "}"
                                        + "]"
                                        + "},"
                                        + "\"subject\": {"
                                        + "\"reference\": \"Patient/" + idpasien + "\""
                                        + "},"
                                        + "\"performer\": ["
                                        + "{"
                                        + "\"reference\": \"Practitioner/" + iddokter + "\""
                                        + "}"
                                        + "],"
                                        + "\"encounter\": {"
                                        + "\"reference\": \"Encounter/" + rs.getString("id_encounter") + "\","
                                        + "\"display\": \"Pemeriksaan Fisik Respirasi di Rawat Inap, Pasien " + rs.getString("nm_pasien") + " Pada Tanggal " + rs.getString("tgl_perawatan") + " Jam " + rs.getString("jam_rawat") + "\""
                                        + "},"
                                        + "\"effectiveDateTime\": \"" + rs.getString("tgl_perawatan") + "T" + rs.getString("jam_rawat") + "+07:00\","
                                        + "\"valueQuantity\": {"
                                        + "\"value\": " + rs.getString("respirasi") + ","
                                        + "\"unit\": \"breaths/minute\","
                                        + "\"system\": \"http://unitsofmeasure.org\","
                                        + "\"code\": \"/min\""
                                        + "}"
                                        + "}";
                                TeksArea.append("URL : " + link + "/Observation" + "\n");
                                TeksArea.append("Request JSON : " + json + "\n");
                                requestEntity = new HttpEntity(json, headers);
                                json = api.getRest().exchange(link + "/Observation", HttpMethod.POST, requestEntity, String.class).getBody();
                                TeksArea.append("Result JSON : " + json + "\n");
                                root = mapper.readTree(json);
                                response = root.path("id");
                                if (!response.asText().equals("")) {
                                    Sequel.menyimpan2("satu_sehat_observationttvrespirasi", "?,?,?,?,?", "Observation Respirasi", 5, new String[]{
                                        rs.getString("no_rawat"), rs.getString("tgl_perawatan"), rs.getString("jam_rawat"), "Ranap", response.asText()
                                    });
                                }
                            } catch (Exception ef) {
                                System.out.println("Notifikasi Bridging : " + ef);
                            }
                        } catch (Exception eg) {
                            System.out.println("Notifikasi : " + eg);
                        }
                    }
                }
            } catch (Exception ez) {
                System.out.println("Notif : " + ez);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception ex) {
            System.out.println("Notifikasi : " + ex);
        }

        //kirim TTV nadi
        try {
            ps = koneksi.prepareStatement(
                    "select reg_periksa.no_rawat,pasien.nm_pasien,pasien.no_ktp,satu_sehat_encounter.id_encounter,pegawai.no_ktp as ktppraktisi,pemeriksaan_ralan.tgl_perawatan,"
                    + "pemeriksaan_ralan.jam_rawat,pemeriksaan_ralan.nadi,ifnull(satu_sehat_observationttvnadi.id_observation,'') as satu_sehat_observationttvnadi "
                    + "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis inner join nota_jalan on nota_jalan.no_rawat=reg_periksa.no_rawat "
                    + "inner join satu_sehat_encounter on satu_sehat_encounter.no_rawat=reg_periksa.no_rawat inner join pemeriksaan_ralan on pemeriksaan_ralan.no_rawat=reg_periksa.no_rawat "
                    + "inner join pegawai on pemeriksaan_ralan.nip=pegawai.nik left join satu_sehat_observationttvnadi on satu_sehat_observationttvnadi.no_rawat=pemeriksaan_ralan.no_rawat "
                    + "and satu_sehat_observationttvnadi.tgl_perawatan=pemeriksaan_ralan.tgl_perawatan and satu_sehat_observationttvnadi.jam_rawat=pemeriksaan_ralan.jam_rawat "
                    + "and satu_sehat_observationttvnadi.status='Ralan' where pemeriksaan_ralan.nadi<>'' and nota_jalan.tanggal between ? and ? ");
            try {
                ps.setString(1, Tanggal1.getText());
                ps.setString(2, Tanggal2.getText());
                rs = ps.executeQuery();
                while (rs.next()) {
                    if ((!rs.getString("no_ktp").equals("")) && (!rs.getString("ktppraktisi").equals("")) && rs.getString("satu_sehat_observationttvnadi").equals("")) {
                        try {
                            iddokter = cekViaSatuSehat.tampilIDParktisi(rs.getString("ktppraktisi"));
                            idpasien = cekViaSatuSehat.tampilIDPasien(rs.getString("no_ktp"));
                            try {
                                headers = new HttpHeaders();
                                headers.setContentType(MediaType.APPLICATION_JSON);
                                headers.add("Authorization", "Bearer " + api.TokenSatuSehat());
                                json = "{"
                                        + "\"resourceType\": \"Observation\","
                                        + "\"status\": \"final\","
                                        + "\"category\": ["
                                        + "{"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"system\": \"http://terminology.hl7.org/CodeSystem/observation-category\","
                                        + "\"code\": \"vital-signs\","
                                        + "\"display\": \"Vital Signs\""
                                        + "}"
                                        + "]"
                                        + "}"
                                        + "],"
                                        + "\"code\": {"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"system\": \"http://loinc.org\","
                                        + "\"code\": \"8867-4\","
                                        + "\"display\": \"Heart rate\""
                                        + "}"
                                        + "]"
                                        + "},"
                                        + "\"subject\": {"
                                        + "\"reference\": \"Patient/" + idpasien + "\""
                                        + "},"
                                        + "\"performer\": ["
                                        + "{"
                                        + "\"reference\": \"Practitioner/" + iddokter + "\""
                                        + "}"
                                        + "],"
                                        + "\"encounter\": {"
                                        + "\"reference\": \"Encounter/" + rs.getString("id_encounter") + "\","
                                        + "\"display\": \"Pemeriksaan Fisik Nadi di Rawat Jalan/IGD, Pasien " + rs.getString("nm_pasien") + " Pada Tanggal " + rs.getString("tgl_perawatan") + " Jam " + rs.getString("jam_rawat") + "\""
                                        + "},"
                                        + "\"effectiveDateTime\": \"" + rs.getString("tgl_perawatan") + "T" + rs.getString("jam_rawat") + "+07:00\","
                                        + "\"valueQuantity\": {"
                                        + "\"value\": " + rs.getString("nadi") + ","
                                        + "\"unit\": \"breaths/minute\","
                                        + "\"system\": \"http://unitsofmeasure.org\","
                                        + "\"code\": \"/min\""
                                        + "}"
                                        + "}";
                                TeksArea.append("URL : " + link + "/Observation" + "\n");
                                TeksArea.append("Request JSON : " + json + "\n");
                                requestEntity = new HttpEntity(json, headers);
                                json = api.getRest().exchange(link + "/Observation", HttpMethod.POST, requestEntity, String.class).getBody();
                                TeksArea.append("Result JSON : " + json + "\n");
                                root = mapper.readTree(json);
                                response = root.path("id");
                                if (!response.asText().equals("")) {
                                    Sequel.menyimpan2("satu_sehat_observationttvnadi", "?,?,?,?,?", "Observation Nadi", 5, new String[]{
                                        rs.getString("no_rawat"), rs.getString("tgl_perawatan"), rs.getString("jam_rawat"), "Ralan", response.asText()
                                    });
                                }
                            } catch (Exception ea) {
                                System.out.println("Notifikasi Bridging : " + ea);
                            }
                        } catch (Exception es) {
                            System.out.println("Notifikasi : " + es);
                        }
                    }
                }
            } catch (Exception ez) {
                System.out.println("Notif : " + ez);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }

            ps = koneksi.prepareStatement(
                    "select reg_periksa.no_rawat,pasien.nm_pasien,pasien.no_ktp,satu_sehat_encounter.id_encounter,pegawai.no_ktp as ktppraktisi,pemeriksaan_ranap.tgl_perawatan,"
                    + "pemeriksaan_ranap.jam_rawat,pemeriksaan_ranap.nadi,ifnull(satu_sehat_observationttvnadi.id_observation,'') as satu_sehat_observationttvnadi "
                    + "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis inner join nota_inap on nota_inap.no_rawat=reg_periksa.no_rawat "
                    + "inner join satu_sehat_encounter on satu_sehat_encounter.no_rawat=reg_periksa.no_rawat inner join pemeriksaan_ranap on pemeriksaan_ranap.no_rawat=reg_periksa.no_rawat "
                    + "inner join pegawai on pemeriksaan_ranap.nip=pegawai.nik left join satu_sehat_observationttvnadi on satu_sehat_observationttvnadi.no_rawat=pemeriksaan_ranap.no_rawat "
                    + "and satu_sehat_observationttvnadi.tgl_perawatan=pemeriksaan_ranap.tgl_perawatan and satu_sehat_observationttvnadi.jam_rawat=pemeriksaan_ranap.jam_rawat "
                    + "and satu_sehat_observationttvnadi.status='Ranap' where pemeriksaan_ranap.nadi<>'' and nota_inap.tanggal between ? and ? ");
            try {
                ps.setString(1, Tanggal1.getText());
                ps.setString(2, Tanggal2.getText());
                rs = ps.executeQuery();
                while (rs.next()) {
                    if ((!rs.getString("no_ktp").equals("")) && (!rs.getString("ktppraktisi").equals("")) && rs.getString("satu_sehat_observationttvnadi").equals("")) {
                        try {
                            iddokter = cekViaSatuSehat.tampilIDParktisi(rs.getString("ktppraktisi"));
                            idpasien = cekViaSatuSehat.tampilIDPasien(rs.getString("no_ktp"));
                            try {
                                headers = new HttpHeaders();
                                headers.setContentType(MediaType.APPLICATION_JSON);
                                headers.add("Authorization", "Bearer " + api.TokenSatuSehat());
                                json = "{"
                                        + "\"resourceType\": \"Observation\","
                                        + "\"status\": \"final\","
                                        + "\"category\": ["
                                        + "{"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"system\": \"http://terminology.hl7.org/CodeSystem/observation-category\","
                                        + "\"code\": \"vital-signs\","
                                        + "\"display\": \"Vital Signs\""
                                        + "}"
                                        + "]"
                                        + "}"
                                        + "],"
                                        + "\"code\": {"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"system\": \"http://loinc.org\","
                                        + "\"code\": \"8867-4\","
                                        + "\"display\": \"Heart rate\""
                                        + "}"
                                        + "]"
                                        + "},"
                                        + "\"subject\": {"
                                        + "\"reference\": \"Patient/" + idpasien + "\""
                                        + "},"
                                        + "\"performer\": ["
                                        + "{"
                                        + "\"reference\": \"Practitioner/" + iddokter + "\""
                                        + "}"
                                        + "],"
                                        + "\"encounter\": {"
                                        + "\"reference\": \"Encounter/" + rs.getString("id_encounter") + "\","
                                        + "\"display\": \"Pemeriksaan Fisik Nadi di Rawat Inap, Pasien " + rs.getString("nm_pasien") + " Pada Tanggal " + rs.getString("tgl_perawatan") + " Jam " + rs.getString("jam_rawat") + "\""
                                        + "},"
                                        + "\"effectiveDateTime\": \"" + rs.getString("tgl_perawatan") + "T" + rs.getString("jam_rawat") + "+07:00\","
                                        + "\"valueQuantity\": {"
                                        + "\"value\": " + rs.getString("nadi") + ","
                                        + "\"unit\": \"breaths/minute\","
                                        + "\"system\": \"http://unitsofmeasure.org\","
                                        + "\"code\": \"/min\""
                                        + "}"
                                        + "}";
                                TeksArea.append("URL : " + link + "/Observation" + "\n");
                                TeksArea.append("Request JSON : " + json + "\n");
                                requestEntity = new HttpEntity(json, headers);
                                json = api.getRest().exchange(link + "/Observation", HttpMethod.POST, requestEntity, String.class).getBody();
                                TeksArea.append("Result JSON : " + json + "\n");
                                root = mapper.readTree(json);
                                response = root.path("id");
                                if (!response.asText().equals("")) {
                                    Sequel.menyimpan2("satu_sehat_observationttvnadi", "?,?,?,?,?", "Observation Nadi", 5, new String[]{
                                        rs.getString("no_rawat"), rs.getString("tgl_perawatan"), rs.getString("jam_rawat"), "Ranap", response.asText()
                                    });
                                }
                            } catch (Exception ea) {
                                System.out.println("Notifikasi Bridging : " + ea);
                            }
                        } catch (Exception es) {
                            System.out.println("Notifikasi : " + es);
                        }
                    }
                }
            } catch (Exception ez) {
                System.out.println("Notif : " + ez);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception ex) {
            System.out.println("Notifikasi : " + ex);
        }

        //kirim TTV SPO2
        try {
            ps = koneksi.prepareStatement(
                    "select reg_periksa.no_rawat,pasien.nm_pasien,pasien.no_ktp,satu_sehat_encounter.id_encounter,pegawai.no_ktp as ktppraktisi,pemeriksaan_ralan.tgl_perawatan,"
                    + "pemeriksaan_ralan.jam_rawat,pemeriksaan_ralan.spo2,ifnull(satu_sehat_observationttvspo2.id_observation,'') as satu_sehat_observationttvspo2 "
                    + "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis inner join nota_jalan on nota_jalan.no_rawat=reg_periksa.no_rawat "
                    + "inner join satu_sehat_encounter on satu_sehat_encounter.no_rawat=reg_periksa.no_rawat inner join pemeriksaan_ralan on pemeriksaan_ralan.no_rawat=reg_periksa.no_rawat "
                    + "inner join pegawai on pemeriksaan_ralan.nip=pegawai.nik left join satu_sehat_observationttvspo2 on satu_sehat_observationttvspo2.no_rawat=pemeriksaan_ralan.no_rawat "
                    + "and satu_sehat_observationttvspo2.tgl_perawatan=pemeriksaan_ralan.tgl_perawatan and satu_sehat_observationttvspo2.jam_rawat=pemeriksaan_ralan.jam_rawat "
                    + "and satu_sehat_observationttvspo2.status='Ralan' where pemeriksaan_ralan.spo2<>'' and nota_jalan.tanggal between ? and ? ");
            try {
                ps.setString(1, Tanggal1.getText());
                ps.setString(2, Tanggal2.getText());
                rs = ps.executeQuery();
                while (rs.next()) {
                    if ((!rs.getString("no_ktp").equals("")) && (!rs.getString("ktppraktisi").equals("")) && rs.getString("satu_sehat_observationttvspo2").equals("")) {
                        try {
                            iddokter = cekViaSatuSehat.tampilIDParktisi(rs.getString("ktppraktisi"));
                            idpasien = cekViaSatuSehat.tampilIDPasien(rs.getString("no_ktp"));
                            try {
                                headers = new HttpHeaders();
                                headers.setContentType(MediaType.APPLICATION_JSON);
                                headers.add("Authorization", "Bearer " + api.TokenSatuSehat());
                                json = "{"
                                        + "\"resourceType\": \"Observation\","
                                        + "\"status\": \"final\","
                                        + "\"category\": ["
                                        + "{"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"system\": \"http://terminology.hl7.org/CodeSystem/observation-category\","
                                        + "\"code\": \"vital-signs\","
                                        + "\"display\": \"Vital Signs\""
                                        + "}"
                                        + "]"
                                        + "}"
                                        + "],"
                                        + "\"code\": {"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"system\": \"http://loinc.org\","
                                        + "\"code\": \"59408-5\","
                                        + "\"display\": \"Oxygen saturation\""
                                        + "}"
                                        + "]"
                                        + "},"
                                        + "\"subject\": {"
                                        + "\"reference\": \"Patient/" + idpasien + "\""
                                        + "},"
                                        + "\"performer\": ["
                                        + "{"
                                        + "\"reference\": \"Practitioner/" + iddokter + "\""
                                        + "}"
                                        + "],"
                                        + "\"encounter\": {"
                                        + "\"reference\": \"Encounter/" + rs.getString("id_encounter") + "\","
                                        + "\"display\": \"Pemeriksaan Fisik SpO2  di Rawat Jalan/IGD, Pasien " + rs.getString("nm_pasien") + " Pada Tanggal " + rs.getString("tgl_perawatan") + " Jam " + rs.getString("jam_rawat") + "\""
                                        + "},"
                                        + "\"effectiveDateTime\": \"" + rs.getString("tgl_perawatan") + "T" + rs.getString("jam_rawat") + "+07:00\","
                                        + "\"valueQuantity\": {"
                                        + "\"value\": " + rs.getString("spo2") + ","
                                        + "\"unit\": \"percent saturation\","
                                        + "\"system\": \"http://unitsofmeasure.org\","
                                        + "\"code\": \"%\""
                                        + "}"
                                        + "}";
                                TeksArea.append("URL : " + link + "/Observation" + "\n");
                                TeksArea.append("Request JSON : " + json + "\n");
                                requestEntity = new HttpEntity(json, headers);
                                json = api.getRest().exchange(link + "/Observation", HttpMethod.POST, requestEntity, String.class).getBody();
                                TeksArea.append("Result JSON : " + json + "\n");
                                root = mapper.readTree(json);
                                response = root.path("id");
                                if (!response.asText().equals("")) {
                                    Sequel.menyimpan2("satu_sehat_observationttvspo2", "?,?,?,?,?", "Observation SpO2", 5, new String[]{
                                        rs.getString("no_rawat"), rs.getString("tgl_perawatan"), rs.getString("jam_rawat"), "Ralan", response.asText()
                                    });
                                }
                            } catch (Exception ef) {
                                System.out.println("Notifikasi Bridging : " + ef);
                            }
                        } catch (Exception ex) {
                            System.out.println("Notifikasi : " + ex);
                        }
                    }
                }
            } catch (Exception ez) {
                System.out.println("Notif : " + ez);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }

            ps = koneksi.prepareStatement(
                    "select reg_periksa.no_rawat,pasien.nm_pasien,pasien.no_ktp,satu_sehat_encounter.id_encounter,pegawai.no_ktp as ktppraktisi,pemeriksaan_ranap.tgl_perawatan,"
                    + "pemeriksaan_ranap.jam_rawat,pemeriksaan_ranap.spo2,ifnull(satu_sehat_observationttvspo2.id_observation,'') as satu_sehat_observationttvspo2 "
                    + "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis inner join nota_inap on nota_inap.no_rawat=reg_periksa.no_rawat "
                    + "inner join satu_sehat_encounter on satu_sehat_encounter.no_rawat=reg_periksa.no_rawat inner join pemeriksaan_ranap on pemeriksaan_ranap.no_rawat=reg_periksa.no_rawat "
                    + "inner join pegawai on pemeriksaan_ranap.nip=pegawai.nik left join satu_sehat_observationttvspo2 on satu_sehat_observationttvspo2.no_rawat=pemeriksaan_ranap.no_rawat "
                    + "and satu_sehat_observationttvspo2.tgl_perawatan=pemeriksaan_ranap.tgl_perawatan and satu_sehat_observationttvspo2.jam_rawat=pemeriksaan_ranap.jam_rawat "
                    + "and satu_sehat_observationttvspo2.status='Ranap' where pemeriksaan_ranap.spo2<>'' and nota_inap.tanggal between ? and ? ");
            try {
                ps.setString(1, Tanggal1.getText());
                ps.setString(2, Tanggal2.getText());
                rs = ps.executeQuery();
                while (rs.next()) {
                    if ((!rs.getString("no_ktp").equals("")) && (!rs.getString("ktppraktisi").equals("")) && rs.getString("satu_sehat_observationttvspo2").equals("")) {
                        try {
                            iddokter = cekViaSatuSehat.tampilIDParktisi(rs.getString("ktppraktisi"));
                            idpasien = cekViaSatuSehat.tampilIDPasien(rs.getString("no_ktp"));
                            try {
                                headers = new HttpHeaders();
                                headers.setContentType(MediaType.APPLICATION_JSON);
                                headers.add("Authorization", "Bearer " + api.TokenSatuSehat());
                                json = "{"
                                        + "\"resourceType\": \"Observation\","
                                        + "\"status\": \"final\","
                                        + "\"category\": ["
                                        + "{"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"system\": \"http://terminology.hl7.org/CodeSystem/observation-category\","
                                        + "\"code\": \"vital-signs\","
                                        + "\"display\": \"Vital Signs\""
                                        + "}"
                                        + "]"
                                        + "}"
                                        + "],"
                                        + "\"code\": {"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"system\": \"http://loinc.org\","
                                        + "\"code\": \"59408-5\","
                                        + "\"display\": \"Oxygen saturation\""
                                        + "}"
                                        + "]"
                                        + "},"
                                        + "\"subject\": {"
                                        + "\"reference\": \"Patient/" + idpasien + "\""
                                        + "},"
                                        + "\"performer\": ["
                                        + "{"
                                        + "\"reference\": \"Practitioner/" + iddokter + "\""
                                        + "}"
                                        + "],"
                                        + "\"encounter\": {"
                                        + "\"reference\": \"Encounter/" + rs.getString("id_encounter") + "\","
                                        + "\"display\": \"Pemeriksaan Fisik SpO2  di Rawat Jalan/IGD, Pasien " + rs.getString("nm_pasien") + " Pada Tanggal " + rs.getString("tgl_perawatan") + " Jam " + rs.getString("jam_rawat") + "\""
                                        + "},"
                                        + "\"effectiveDateTime\": \"" + rs.getString("tgl_perawatan") + "T" + rs.getString("jam_rawat") + "+07:00\","
                                        + "\"valueQuantity\": {"
                                        + "\"value\": " + rs.getString("spo2") + ","
                                        + "\"unit\": \"percent saturation\","
                                        + "\"system\": \"http://unitsofmeasure.org\","
                                        + "\"code\": \"%\""
                                        + "}"
                                        + "}";
                                TeksArea.append("URL : " + link + "/Observation" + "\n");
                                TeksArea.append("Request JSON : " + json + "\n");
                                requestEntity = new HttpEntity(json, headers);
                                json = api.getRest().exchange(link + "/Observation", HttpMethod.POST, requestEntity, String.class).getBody();
                                TeksArea.append("Result JSON : " + json + "\n");
                                root = mapper.readTree(json);
                                response = root.path("id");
                                if (!response.asText().equals("")) {
                                    Sequel.menyimpan2("satu_sehat_observationttvspo2", "?,?,?,?,?", "Observation SpO2", 5, new String[]{
                                        rs.getString("no_rawat"), rs.getString("tgl_perawatan"), rs.getString("jam_rawat"), "Ranap", response.asText()
                                    });
                                }
                            } catch (Exception ef) {
                                System.out.println("Notifikasi Bridging : " + ef);
                            }
                        } catch (Exception ex) {
                            System.out.println("Notifikasi : " + ex);
                        }
                    }
                }
            } catch (Exception ez) {
                System.out.println("Notif : " + ez);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception ex) {
            System.out.println("Notifikasi : " + ex);
        }

        //kirim TTV GCS
        try {
            ps = koneksi.prepareStatement(
                    "select reg_periksa.no_rawat,pasien.nm_pasien,pasien.no_ktp,satu_sehat_encounter.id_encounter,pegawai.no_ktp as ktppraktisi,pemeriksaan_ralan.tgl_perawatan,"
                    + "pemeriksaan_ralan.jam_rawat,pemeriksaan_ralan.gcs,ifnull(satu_sehat_observationttvgcs.id_observation,'') as satu_sehat_observationttvgcs "
                    + "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis inner join nota_jalan on nota_jalan.no_rawat=reg_periksa.no_rawat "
                    + "inner join satu_sehat_encounter on satu_sehat_encounter.no_rawat=reg_periksa.no_rawat inner join pemeriksaan_ralan on pemeriksaan_ralan.no_rawat=reg_periksa.no_rawat "
                    + "inner join pegawai on pemeriksaan_ralan.nip=pegawai.nik left join satu_sehat_observationttvgcs on satu_sehat_observationttvgcs.no_rawat=pemeriksaan_ralan.no_rawat "
                    + "and satu_sehat_observationttvgcs.tgl_perawatan=pemeriksaan_ralan.tgl_perawatan and satu_sehat_observationttvgcs.jam_rawat=pemeriksaan_ralan.jam_rawat "
                    + "and satu_sehat_observationttvgcs.status='Ralan' where pemeriksaan_ralan.gcs<>'' and nota_jalan.tanggal between ? and ? ");
            try {
                ps.setString(1, Tanggal1.getText());
                ps.setString(2, Tanggal2.getText());
                rs = ps.executeQuery();
                while (rs.next()) {
                    if ((!rs.getString("no_ktp").equals("")) && (!rs.getString("ktppraktisi").equals("")) && rs.getString("satu_sehat_observationttvgcs").equals("")) {
                        try {
                            iddokter = cekViaSatuSehat.tampilIDParktisi(rs.getString("ktppraktisi"));
                            idpasien = cekViaSatuSehat.tampilIDPasien(rs.getString("no_ktp"));
                            try {
                                headers = new HttpHeaders();
                                headers.setContentType(MediaType.APPLICATION_JSON);
                                headers.add("Authorization", "Bearer " + api.TokenSatuSehat());
                                json = "{"
                                        + "\"resourceType\": \"Observation\","
                                        + "\"status\": \"final\","
                                        + "\"category\": ["
                                        + "{"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"system\": \"http://terminology.hl7.org/CodeSystem/observation-category\","
                                        + "\"code\": \"vital-signs\","
                                        + "\"display\": \"Vital Signs\""
                                        + "}"
                                        + "]"
                                        + "}"
                                        + "],"
                                        + "\"code\": {"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"system\": \"http://loinc.org\","
                                        + "\"code\": \"9269-2\","
                                        + "\"display\": \"Glasgow coma score total\""
                                        + "}"
                                        + "]"
                                        + "},"
                                        + "\"subject\": {"
                                        + "\"reference\": \"Patient/" + idpasien + "\""
                                        + "},"
                                        + "\"performer\": ["
                                        + "{"
                                        + "\"reference\": \"Practitioner/" + iddokter + "\""
                                        + "}"
                                        + "],"
                                        + "\"encounter\": {"
                                        + "\"reference\": \"Encounter/" + rs.getString("id_encounter") + "\","
                                        + "\"display\": \"Pemeriksaan Fisik GCS di Rawat Jalan/IGD, Pasien " + rs.getString("nm_pasien") + " Pada Tanggal " + rs.getString("tgl_perawatan") + " Jam " + rs.getString("jam_rawat") + "\""
                                        + "},"
                                        + "\"effectiveDateTime\": \"" + rs.getString("tgl_perawatan") + "T" + rs.getString("jam_rawat") + "+07:00\","
                                        + "\"valueQuantity\": {"
                                        + "\"value\": " + rs.getString("gcs") + ","
                                        + "\"system\": \"http://unitsofmeasure.org\","
                                        + "\"code\": \"{score}\""
                                        + "}"
                                        + "}";
                                TeksArea.append("URL : " + link + "/Observation" + "\n");
                                TeksArea.append("Request JSON : " + json + "\n");
                                requestEntity = new HttpEntity(json, headers);
                                json = api.getRest().exchange(link + "/Observation", HttpMethod.POST, requestEntity, String.class).getBody();
                                TeksArea.append("Result JSON : " + json + "\n");
                                root = mapper.readTree(json);
                                response = root.path("id");
                                if (!response.asText().equals("")) {
                                    Sequel.menyimpan2("satu_sehat_observationttvgcs", "?,?,?,?,?", "Observation GCS", 5, new String[]{
                                        rs.getString("no_rawat"), rs.getString("tgl_perawatan"), rs.getString("jam_rawat"), "Ralan", response.asText()
                                    });
                                }
                            } catch (Exception es) {
                                System.out.println("Notifikasi Bridging : " + es);
                            }
                        } catch (Exception ea) {
                            System.out.println("Notifikasi : " + ea);
                        }
                    }
                }
            } catch (Exception ez) {
                System.out.println("Notif : " + ez);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }

            ps = koneksi.prepareStatement(
                    "select reg_periksa.no_rawat,pasien.nm_pasien,pasien.no_ktp,satu_sehat_encounter.id_encounter,pegawai.no_ktp as ktppraktisi,pemeriksaan_ranap.tgl_perawatan,"
                    + "pemeriksaan_ranap.jam_rawat,pemeriksaan_ranap.gcs,ifnull(satu_sehat_observationttvgcs.id_observation,'') as satu_sehat_observationttvgcs "
                    + "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis inner join nota_inap on nota_inap.no_rawat=reg_periksa.no_rawat "
                    + "inner join satu_sehat_encounter on satu_sehat_encounter.no_rawat=reg_periksa.no_rawat inner join pemeriksaan_ranap on pemeriksaan_ranap.no_rawat=reg_periksa.no_rawat "
                    + "inner join pegawai on pemeriksaan_ranap.nip=pegawai.nik left join satu_sehat_observationttvgcs on satu_sehat_observationttvgcs.no_rawat=pemeriksaan_ranap.no_rawat "
                    + "and satu_sehat_observationttvgcs.tgl_perawatan=pemeriksaan_ranap.tgl_perawatan and satu_sehat_observationttvgcs.jam_rawat=pemeriksaan_ranap.jam_rawat "
                    + "and satu_sehat_observationttvgcs.status='Ranap' where pemeriksaan_ranap.gcs<>'' and nota_inap.tanggal between ? and ? ");
            try {
                ps.setString(1, Tanggal1.getText());
                ps.setString(2, Tanggal2.getText());
                rs = ps.executeQuery();
                while (rs.next()) {
                    if ((!rs.getString("no_ktp").equals("")) && (!rs.getString("ktppraktisi").equals("")) && rs.getString("satu_sehat_observationttvgcs").equals("")) {
                        try {
                            iddokter = cekViaSatuSehat.tampilIDParktisi(rs.getString("ktppraktisi"));
                            idpasien = cekViaSatuSehat.tampilIDPasien(rs.getString("no_ktp"));
                            try {
                                headers = new HttpHeaders();
                                headers.setContentType(MediaType.APPLICATION_JSON);
                                headers.add("Authorization", "Bearer " + api.TokenSatuSehat());
                                json = "{"
                                        + "\"resourceType\": \"Observation\","
                                        + "\"status\": \"final\","
                                        + "\"category\": ["
                                        + "{"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"system\": \"http://terminology.hl7.org/CodeSystem/observation-category\","
                                        + "\"code\": \"vital-signs\","
                                        + "\"display\": \"Vital Signs\""
                                        + "}"
                                        + "]"
                                        + "}"
                                        + "],"
                                        + "\"code\": {"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"system\": \"http://loinc.org\","
                                        + "\"code\": \"9269-2\","
                                        + "\"display\": \"Glasgow coma score total\""
                                        + "}"
                                        + "]"
                                        + "},"
                                        + "\"subject\": {"
                                        + "\"reference\": \"Patient/" + idpasien + "\""
                                        + "},"
                                        + "\"performer\": ["
                                        + "{"
                                        + "\"reference\": \"Practitioner/" + iddokter + "\""
                                        + "}"
                                        + "],"
                                        + "\"encounter\": {"
                                        + "\"reference\": \"Encounter/" + rs.getString("id_encounter") + "\","
                                        + "\"display\": \"Pemeriksaan Fisik GCS di Rawat Inap, Pasien " + rs.getString("nm_pasien") + " Pada Tanggal " + rs.getString("tgl_perawatan") + " Jam " + rs.getString("jam_rawat") + "\""
                                        + "},"
                                        + "\"effectiveDateTime\": \"" + rs.getString("tgl_perawatan") + "T" + rs.getString("jam_rawat") + "+07:00\","
                                        + "\"valueQuantity\": {"
                                        + "\"value\": " + rs.getString("gcs") + ","
                                        + "\"system\": \"http://unitsofmeasure.org\","
                                        + "\"code\": \"{score}\""
                                        + "}"
                                        + "}";
                                TeksArea.append("URL : " + link + "/Observation" + "\n");
                                TeksArea.append("Request JSON : " + json + "\n");
                                requestEntity = new HttpEntity(json, headers);
                                json = api.getRest().exchange(link + "/Observation", HttpMethod.POST, requestEntity, String.class).getBody();
                                TeksArea.append("Result JSON : " + json + "\n");
                                root = mapper.readTree(json);
                                response = root.path("id");
                                if (!response.asText().equals("")) {
                                    Sequel.menyimpan2("satu_sehat_observationttvgcs", "?,?,?,?,?", "Observation GCS", 5, new String[]{
                                        rs.getString("no_rawat"), rs.getString("tgl_perawatan"), rs.getString("jam_rawat"), "Ranap", response.asText()
                                    });
                                }
                            } catch (Exception es) {
                                System.out.println("Notifikasi Bridging : " + es);
                            }
                        } catch (Exception ea) {
                            System.out.println("Notifikasi : " + ea);
                        }
                    }
                }
            } catch (Exception ez) {
                System.out.println("Notif : " + ez);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception ex) {
            System.out.println("Notifikasi : " + ex);
        }

        //kirim TTV Kesadaran
        try {
            ps = koneksi.prepareStatement(
                    "select reg_periksa.no_rawat,pasien.nm_pasien,pasien.no_ktp,satu_sehat_encounter.id_encounter,pegawai.no_ktp as ktppraktisi,pemeriksaan_ralan.tgl_perawatan,"
                    + "pemeriksaan_ralan.jam_rawat,pemeriksaan_ralan.kesadaran,ifnull(satu_sehat_observationttvkesadaran.id_observation,'') as satu_sehat_observationttvkesadaran "
                    + "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis inner join nota_jalan on nota_jalan.no_rawat=reg_periksa.no_rawat "
                    + "inner join satu_sehat_encounter on satu_sehat_encounter.no_rawat=reg_periksa.no_rawat inner join pemeriksaan_ralan on pemeriksaan_ralan.no_rawat=reg_periksa.no_rawat "
                    + "inner join pegawai on pemeriksaan_ralan.nip=pegawai.nik left join satu_sehat_observationttvkesadaran on satu_sehat_observationttvkesadaran.no_rawat=pemeriksaan_ralan.no_rawat "
                    + "and satu_sehat_observationttvkesadaran.tgl_perawatan=pemeriksaan_ralan.tgl_perawatan and satu_sehat_observationttvkesadaran.jam_rawat=pemeriksaan_ralan.jam_rawat "
                    + "and satu_sehat_observationttvkesadaran.status='Ralan' where pemeriksaan_ralan.kesadaran<>'' and nota_jalan.tanggal between ? and ? ");
            try {
                ps.setString(1, Tanggal1.getText());
                ps.setString(2, Tanggal2.getText());
                rs = ps.executeQuery();
                while (rs.next()) {
                    if ((!rs.getString("no_ktp").equals("")) && (!rs.getString("ktppraktisi").equals("")) && rs.getString("satu_sehat_observationttvkesadaran").equals("")) {
                        try {
                            iddokter = cekViaSatuSehat.tampilIDParktisi(rs.getString("ktppraktisi"));
                            idpasien = cekViaSatuSehat.tampilIDPasien(rs.getString("no_ktp"));
                            try {
                                headers = new HttpHeaders();
                                headers.setContentType(MediaType.APPLICATION_JSON);
                                headers.add("Authorization", "Bearer " + api.TokenSatuSehat());
                                json = "{"
                                        + "\"resourceType\": \"Observation\","
                                        + "\"status\": \"final\","
                                        + "\"category\": ["
                                        + "{"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"system\": \"http://terminology.hl7.org/CodeSystem/observation-category\","
                                        + "\"code\": \"exam\","
                                        + "\"display\": \"Exam\""
                                        + "}"
                                        + "]"
                                        + "}"
                                        + "],"
                                        + "\"code\": {"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"system\": \"http://snomed.info/sct\","
                                        + "\"code\": \"1104441000000107\","
                                        + "\"display\": \"ACVPU (Alert Confusion Voice Pain Unresponsive) scale score\""
                                        + "}"
                                        + "]"
                                        + "},"
                                        + "\"subject\": {"
                                        + "\"reference\": \"Patient/" + idpasien + "\""
                                        + "},"
                                        + "\"performer\": ["
                                        + "{"
                                        + "\"reference\": \"Practitioner/" + iddokter + "\""
                                        + "}"
                                        + "],"
                                        + "\"encounter\": {"
                                        + "\"reference\": \"Encounter/" + rs.getString("id_encounter") + "\","
                                        + "\"display\": \"Pemeriksaan Fisik Kesadaran di Rawat Jalan/IGD, Pasien " + rs.getString("nm_pasien") + " Pada Tanggal " + rs.getString("tgl_perawatan") + " Jam " + rs.getString("jam_rawat") + "\""
                                        + "},"
                                        + "\"effectiveDateTime\": \"" + rs.getString("tgl_perawatan") + "T" + rs.getString("jam_rawat") + "+07:00\","
                                        + "\"valueCodeableConcept\": {"
                                        + "\"text\": \"" + rs.getString("kesadaran").replaceAll("Compos Mentis", "Alert").replaceAll("Somnolence", "Voice").replaceAll("Sopor", "Pain").replaceAll("Coma", "Unresponsive") + "\""
                                        + "}"
                                        + "}";
                                TeksArea.append("URL : " + link + "/Observation" + "\n");
                                TeksArea.append("Request JSON : " + json + "\n");
                                requestEntity = new HttpEntity(json, headers);
                                json = api.getRest().exchange(link + "/Observation", HttpMethod.POST, requestEntity, String.class).getBody();
                                TeksArea.append("Result JSON : " + json + "\n");
                                root = mapper.readTree(json);
                                response = root.path("id");
                                if (!response.asText().equals("")) {
                                    Sequel.menyimpan2("satu_sehat_observationttvkesadaran", "?,?,?,?,?", "Observation Kesadaran", 5, new String[]{
                                        rs.getString("no_rawat"), rs.getString("tgl_perawatan"), rs.getString("jam_rawat"), "Ralan", response.asText()
                                    });
                                }
                            } catch (Exception eg) {
                                System.out.println("Notifikasi Bridging : " + eg);
                            }
                        } catch (Exception ef) {
                            System.out.println("Notifikasi : " + ef);
                        }
                    }
                }
            } catch (Exception ez) {
                System.out.println("Notif : " + ez);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }

            ps = koneksi.prepareStatement(
                    "select reg_periksa.no_rawat,pasien.nm_pasien,pasien.no_ktp,satu_sehat_encounter.id_encounter,pegawai.nama,pegawai.no_ktp as ktppraktisi,pemeriksaan_ranap.tgl_perawatan,"
                    + "pemeriksaan_ranap.jam_rawat,pemeriksaan_ranap.kesadaran,ifnull(satu_sehat_observationttvkesadaran.id_observation,'') as satu_sehat_observationttvkesadaran "
                    + "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis inner join nota_inap on nota_inap.no_rawat=reg_periksa.no_rawat "
                    + "inner join satu_sehat_encounter on satu_sehat_encounter.no_rawat=reg_periksa.no_rawat inner join pemeriksaan_ranap on pemeriksaan_ranap.no_rawat=reg_periksa.no_rawat "
                    + "inner join pegawai on pemeriksaan_ranap.nip=pegawai.nik left join satu_sehat_observationttvkesadaran on satu_sehat_observationttvkesadaran.no_rawat=pemeriksaan_ranap.no_rawat "
                    + "and satu_sehat_observationttvkesadaran.tgl_perawatan=pemeriksaan_ranap.tgl_perawatan and satu_sehat_observationttvkesadaran.jam_rawat=pemeriksaan_ranap.jam_rawat "
                    + "and satu_sehat_observationttvkesadaran.status='Ranap' where pemeriksaan_ranap.kesadaran<>'' and nota_inap.tanggal between ? and ? ");
            try {
                ps.setString(1, Tanggal1.getText());
                ps.setString(2, Tanggal2.getText());
                rs = ps.executeQuery();
                while (rs.next()) {
                    if ((!rs.getString("no_ktp").equals("")) && (!rs.getString("ktppraktisi").equals("")) && rs.getString("satu_sehat_observationttvkesadaran").equals("")) {
                        try {
                            iddokter = cekViaSatuSehat.tampilIDParktisi(rs.getString("ktppraktisi"));
                            idpasien = cekViaSatuSehat.tampilIDPasien(rs.getString("no_ktp"));
                            try {
                                headers = new HttpHeaders();
                                headers.setContentType(MediaType.APPLICATION_JSON);
                                headers.add("Authorization", "Bearer " + api.TokenSatuSehat());
                                json = "{"
                                        + "\"resourceType\": \"Observation\","
                                        + "\"status\": \"final\","
                                        + "\"category\": ["
                                        + "{"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"system\": \"http://terminology.hl7.org/CodeSystem/observation-category\","
                                        + "\"code\": \"exam\","
                                        + "\"display\": \"Exam\""
                                        + "}"
                                        + "]"
                                        + "}"
                                        + "],"
                                        + "\"code\": {"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"system\": \"http://snomed.info/sct\","
                                        + "\"code\": \"1104441000000107\","
                                        + "\"display\": \"ACVPU (Alert Confusion Voice Pain Unresponsive) scale score\""
                                        + "}"
                                        + "]"
                                        + "},"
                                        + "\"subject\": {"
                                        + "\"reference\": \"Patient/" + idpasien + "\""
                                        + "},"
                                        + "\"performer\": ["
                                        + "{"
                                        + "\"reference\": \"Practitioner/" + iddokter + "\""
                                        + "}"
                                        + "],"
                                        + "\"encounter\": {"
                                        + "\"reference\": \"Encounter/" + rs.getString("id_encounter") + "\","
                                        + "\"display\": \"Pemeriksaan Fisik Kesadaran di Rawat Inap, Pasien " + rs.getString("nm_pasien") + " Pada Tanggal " + rs.getString("tgl_perawatan") + " Jam " + rs.getString("jam_rawat") + "\""
                                        + "},"
                                        + "\"effectiveDateTime\": \"" + rs.getString("tgl_perawatan") + "T" + rs.getString("jam_rawat") + "+07:00\","
                                        + "\"valueCodeableConcept\": {"
                                        + "\"text\": \"" + rs.getString("kesadaran").replaceAll("Compos Mentis", "Alert").replaceAll("Somnolence", "Voice").replaceAll("Sopor", "Pain").replaceAll("Coma", "Unresponsive") + "\""
                                        + "}"
                                        + "}";
                                TeksArea.append("URL : " + link + "/Observation" + "\n");
                                TeksArea.append("Request JSON : " + json + "\n");
                                requestEntity = new HttpEntity(json, headers);
                                json = api.getRest().exchange(link + "/Observation", HttpMethod.POST, requestEntity, String.class).getBody();
                                TeksArea.append("Result JSON : " + json + "\n");
                                root = mapper.readTree(json);
                                response = root.path("id");
                                if (!response.asText().equals("")) {
                                    Sequel.menyimpan2("satu_sehat_observationttvkesadaran", "?,?,?,?,?", "Observation Kesadaran", 5, new String[]{
                                        rs.getString("no_rawat"), rs.getString("tgl_perawatan"), rs.getString("jam_rawat"), "Ranap", response.asText()
                                    });
                                }
                            } catch (Exception eg) {
                                System.out.println("Notifikasi Bridging : " + eg);
                            }
                        } catch (Exception ef) {
                            System.out.println("Notifikasi : " + ef);
                        }
                    }
                }
            } catch (Exception ez) {
                System.out.println("Notif : " + ez);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception ex) {
            System.out.println("Notifikasi : " + ex);
        }

        //kirim TTV Tensi
        try {
            ps = koneksi.prepareStatement(
                    "select reg_periksa.no_rawat,pasien.nm_pasien,pasien.no_ktp,satu_sehat_encounter.id_encounter,pegawai.no_ktp as ktppraktisi,pemeriksaan_ralan.tgl_perawatan,"
                    + "pemeriksaan_ralan.jam_rawat,pemeriksaan_ralan.tensi,ifnull(satu_sehat_observationttvtensi.id_observation,'') as satu_sehat_observationttvtensi "
                    + "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis inner join nota_jalan on nota_jalan.no_rawat=reg_periksa.no_rawat "
                    + "inner join satu_sehat_encounter on satu_sehat_encounter.no_rawat=reg_periksa.no_rawat inner join pemeriksaan_ralan on pemeriksaan_ralan.no_rawat=reg_periksa.no_rawat "
                    + "inner join pegawai on pemeriksaan_ralan.nip=pegawai.nik left join satu_sehat_observationttvtensi on satu_sehat_observationttvtensi.no_rawat=pemeriksaan_ralan.no_rawat "
                    + "and satu_sehat_observationttvtensi.tgl_perawatan=pemeriksaan_ralan.tgl_perawatan and satu_sehat_observationttvtensi.jam_rawat=pemeriksaan_ralan.jam_rawat "
                    + "and satu_sehat_observationttvtensi.status='Ralan' where pemeriksaan_ralan.tensi<>'' and nota_jalan.tanggal between ? and ? ");
            try {
                ps.setString(1, Tanggal1.getText());
                ps.setString(2, Tanggal2.getText());
                rs = ps.executeQuery();
                while (rs.next()) {
                    if ((!rs.getString("no_ktp").equals("")) && (!rs.getString("ktppraktisi").equals("")) && rs.getString("satu_sehat_observationttvtensi").equals("")) {
                        try {
                            iddokter = cekViaSatuSehat.tampilIDParktisi(rs.getString("ktppraktisi"));
                            idpasien = cekViaSatuSehat.tampilIDPasien(rs.getString("no_ktp"));
                            arrSplit = rs.getString("tensi").split("/");
                            sistole = "0";
                            try {
                                if (!arrSplit[0].equals("")) {
                                    sistole = arrSplit[0];
                                }
                            } catch (Exception ef) {
                                sistole = "0";
                            }
                            diastole = "0";
                            try {
                                if (!arrSplit[1].equals("")) {
                                    diastole = arrSplit[1];
                                }
                            } catch (Exception eg) {
                                diastole = "0";
                            }
                            try {
                                headers = new HttpHeaders();
                                headers.setContentType(MediaType.APPLICATION_JSON);
                                headers.add("Authorization", "Bearer " + api.TokenSatuSehat());
                                json = "{"
                                        + "\"resourceType\": \"Observation\","
                                        + "\"status\": \"final\","
                                        + "\"category\": ["
                                        + "{"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"system\": \"http://terminology.hl7.org/CodeSystem/observation-category\","
                                        + "\"code\": \"vital-signs\","
                                        + "\"display\": \"Vital Signs\""
                                        + "}"
                                        + "]"
                                        + "}"
                                        + "],"
                                        + "\"code\": {"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"system\": \"http://loinc.org\","
                                        + "\"code\": \"35094-2\","
                                        + "\"display\": \"Blood pressure panel\""
                                        + "}"
                                        + "],"
                                        + "\"text\": \"Blood pressure systolic & diastolic\""
                                        + "},"
                                        + "\"subject\": {"
                                        + "\"reference\": \"Patient/" + idpasien + "\""
                                        + "},"
                                        + "\"performer\": ["
                                        + "{"
                                        + "\"reference\": \"Practitioner/" + iddokter + "\""
                                        + "}"
                                        + "],"
                                        + "\"encounter\": {"
                                        + "\"reference\": \"Encounter/" + rs.getString("id_encounter") + "\","
                                        + "\"display\": \"Pemeriksaan Fisik Tensi di Rawat Jalan/IGD, Pasien " + rs.getString("nm_pasien") + " Pada Tanggal " + rs.getString("tgl_perawatan") + " Jam " + rs.getString("jam_rawat") + "\""
                                        + "},"
                                        + "\"effectiveDateTime\": \"" + rs.getString("tgl_perawatan") + "T" + rs.getString("jam_rawat") + "+07:00\","
                                        + "\"component\" : ["
                                        + "{"
                                        + "\"code\" : {"
                                        + "\"coding\" : ["
                                        + "{"
                                        + "\"system\" : \"http://loinc.org\","
                                        + "\"code\" : \"8480-6\","
                                        + "\"display\" : \"Systolic blood pressure\""
                                        + "}"
                                        + "]"
                                        + "},"
                                        + "\"valueQuantity\" : {"
                                        + "\"value\" : " + sistole + ","
                                        + "\"unit\" : \"mmHg\","
                                        + "\"system\" : \"http://unitsofmeasure.org\","
                                        + "\"code\" : \"mm[Hg]\""
                                        + "}"
                                        + "},"
                                        + "{"
                                        + "\"code\" : {"
                                        + "\"coding\" : ["
                                        + "{"
                                        + "\"system\" : \"http://loinc.org\","
                                        + "\"code\" : \"8462-4\","
                                        + "\"display\" : \"Diastolic blood pressure\""
                                        + "}"
                                        + "]"
                                        + "},"
                                        + "\"valueQuantity\" : {"
                                        + "\"value\" : " + diastole + ","
                                        + "\"unit\" : \"mmHg\","
                                        + "\"system\" : \"http://unitsofmeasure.org\","
                                        + "\"code\" : \"mm[Hg]\""
                                        + "}"
                                        + "}"
                                        + "]"
                                        + "}";
                                TeksArea.append("URL : " + link + "/Observation" + "\n");
                                TeksArea.append("Request JSON : " + json + "\n");
                                requestEntity = new HttpEntity(json, headers);
                                json = api.getRest().exchange(link + "/Observation", HttpMethod.POST, requestEntity, String.class).getBody();
                                TeksArea.append("Result JSON : " + json + "\n");
                                root = mapper.readTree(json);
                                response = root.path("id");
                                if (!response.asText().equals("")) {
                                    Sequel.menyimpan2("satu_sehat_observationttvtensi", "?,?,?,?,?", "Observation Tensi", 5, new String[]{
                                        rs.getString("no_rawat"), rs.getString("tgl_perawatan"), rs.getString("jam_rawat"), "Ralan", response.asText()
                                    });
                                }
                            } catch (Exception eg) {
                                System.out.println("Notifikasi Bridging : " + eg);
                            }
                        } catch (Exception ef) {
                            System.out.println("Notifikasi : " + ef);
                        }
                    }
                }
            } catch (Exception ez) {
                System.out.println("Notif : " + ez);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }

            ps = koneksi.prepareStatement(
                    "select reg_periksa.no_rawat,pasien.nm_pasien,pasien.no_ktp,satu_sehat_encounter.id_encounter,pegawai.nama,pegawai.no_ktp as ktppraktisi,pemeriksaan_ranap.tgl_perawatan,"
                    + "pemeriksaan_ranap.jam_rawat,pemeriksaan_ranap.tensi,ifnull(satu_sehat_observationttvtensi.id_observation,'') as satu_sehat_observationttvtensi "
                    + "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis inner join nota_inap on nota_inap.no_rawat=reg_periksa.no_rawat "
                    + "inner join satu_sehat_encounter on satu_sehat_encounter.no_rawat=reg_periksa.no_rawat inner join pemeriksaan_ranap on pemeriksaan_ranap.no_rawat=reg_periksa.no_rawat "
                    + "inner join pegawai on pemeriksaan_ranap.nip=pegawai.nik left join satu_sehat_observationttvtensi on satu_sehat_observationttvtensi.no_rawat=pemeriksaan_ranap.no_rawat "
                    + "and satu_sehat_observationttvtensi.tgl_perawatan=pemeriksaan_ranap.tgl_perawatan and satu_sehat_observationttvtensi.jam_rawat=pemeriksaan_ranap.jam_rawat "
                    + "and satu_sehat_observationttvtensi.status='Ranap' where pemeriksaan_ranap.tensi<>'' and nota_inap.tanggal between ? and ?");
            try {
                ps.setString(1, Tanggal1.getText());
                ps.setString(2, Tanggal2.getText());
                rs = ps.executeQuery();
                while (rs.next()) {
                    if ((!rs.getString("no_ktp").equals("")) && (!rs.getString("ktppraktisi").equals("")) && rs.getString("satu_sehat_observationttvtensi").equals("")) {
                        try {
                            iddokter = cekViaSatuSehat.tampilIDParktisi(rs.getString("ktppraktisi"));
                            idpasien = cekViaSatuSehat.tampilIDPasien(rs.getString("no_ktp"));
                            arrSplit = rs.getString("tensi").split("/");
                            sistole = "0";
                            try {
                                if (!arrSplit[0].equals("")) {
                                    sistole = arrSplit[0];
                                }
                            } catch (Exception ef) {
                                sistole = "0";
                            }
                            diastole = "0";
                            try {
                                if (!arrSplit[1].equals("")) {
                                    diastole = arrSplit[1];
                                }
                            } catch (Exception eg) {
                                diastole = "0";
                            }
                            try {
                                headers = new HttpHeaders();
                                headers.setContentType(MediaType.APPLICATION_JSON);
                                headers.add("Authorization", "Bearer " + api.TokenSatuSehat());
                                json = "{"
                                        + "\"resourceType\": \"Observation\","
                                        + "\"status\": \"final\","
                                        + "\"category\": ["
                                        + "{"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"system\": \"http://terminology.hl7.org/CodeSystem/observation-category\","
                                        + "\"code\": \"vital-signs\","
                                        + "\"display\": \"Vital Signs\""
                                        + "}"
                                        + "]"
                                        + "}"
                                        + "],"
                                        + "\"code\": {"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"system\": \"http://loinc.org\","
                                        + "\"code\": \"35094-2\","
                                        + "\"display\": \"Blood pressure panel\""
                                        + "}"
                                        + "],"
                                        + "\"text\": \"Blood pressure systolic & diastolic\""
                                        + "},"
                                        + "\"subject\": {"
                                        + "\"reference\": \"Patient/" + idpasien + "\""
                                        + "},"
                                        + "\"performer\": ["
                                        + "{"
                                        + "\"reference\": \"Practitioner/" + iddokter + "\""
                                        + "}"
                                        + "],"
                                        + "\"encounter\": {"
                                        + "\"reference\": \"Encounter/" + rs.getString("id_encounter") + "\","
                                        + "\"display\": \"Pemeriksaan Fisik Tensi di Rawat Inap, Pasien " + rs.getString("nm_pasien") + " Pada Tanggal " + rs.getString("tgl_perawatan") + " Jam " + rs.getString("jam_rawat") + "\""
                                        + "},"
                                        + "\"effectiveDateTime\": \"" + rs.getString("tgl_perawatan") + "T" + rs.getString("jam_rawat") + "+07:00\","
                                        + "\"component\" : ["
                                        + "{"
                                        + "\"code\" : {"
                                        + "\"coding\" : ["
                                        + "{"
                                        + "\"system\" : \"http://loinc.org\","
                                        + "\"code\" : \"8480-6\","
                                        + "\"display\" : \"Systolic blood pressure\""
                                        + "}"
                                        + "]"
                                        + "},"
                                        + "\"valueQuantity\" : {"
                                        + "\"value\" : " + sistole + ","
                                        + "\"unit\" : \"mmHg\","
                                        + "\"system\" : \"http://unitsofmeasure.org\","
                                        + "\"code\" : \"mm[Hg]\""
                                        + "}"
                                        + "},"
                                        + "{"
                                        + "\"code\" : {"
                                        + "\"coding\" : ["
                                        + "{"
                                        + "\"system\" : \"http://loinc.org\","
                                        + "\"code\" : \"8462-4\","
                                        + "\"display\" : \"Diastolic blood pressure\""
                                        + "}"
                                        + "]"
                                        + "},"
                                        + "\"valueQuantity\" : {"
                                        + "\"value\" : " + diastole + ","
                                        + "\"unit\" : \"mmHg\","
                                        + "\"system\" : \"http://unitsofmeasure.org\","
                                        + "\"code\" : \"mm[Hg]\""
                                        + "}"
                                        + "}"
                                        + "]"
                                        + "}";
                                TeksArea.append("URL : " + link + "/Observation" + "\n");
                                TeksArea.append("Request JSON : " + json + "\n");
                                requestEntity = new HttpEntity(json, headers);
                                json = api.getRest().exchange(link + "/Observation", HttpMethod.POST, requestEntity, String.class).getBody();
                                TeksArea.append("Result JSON : " + json + "\n");
                                root = mapper.readTree(json);
                                response = root.path("id");
                                if (!response.asText().equals("")) {
                                    Sequel.menyimpan2("satu_sehat_observationttvtensi", "?,?,?,?,?", "Observation Tensi", 5, new String[]{
                                        rs.getString("no_rawat"), rs.getString("tgl_perawatan"), rs.getString("jam_rawat"), "Ranap", response.asText()
                                    });
                                }
                            } catch (Exception eg) {
                                System.out.println("Notifikasi Bridging : " + eg);
                            }
                        } catch (Exception ef) {
                            System.out.println("Notifikasi : " + ef);
                        }
                    }
                }
            } catch (Exception ez) {
                System.out.println("Notif : " + ez);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception ex) {
            System.out.println("Notifikasi : " + ex);
        }

        //kirim TTV Tinggi Badan
        try {
            ps = koneksi.prepareStatement(
                    "select reg_periksa.no_rawat,pasien.nm_pasien,pasien.no_ktp,satu_sehat_encounter.id_encounter,pegawai.nama,pegawai.no_ktp as ktppraktisi,pemeriksaan_ralan.tgl_perawatan,"
                    + "pemeriksaan_ralan.jam_rawat,pemeriksaan_ralan.tinggi,ifnull(satu_sehat_observationttvtb.id_observation,'') as satu_sehat_observationttvtb "
                    + "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis inner join nota_jalan on nota_jalan.no_rawat=reg_periksa.no_rawat "
                    + "inner join satu_sehat_encounter on satu_sehat_encounter.no_rawat=reg_periksa.no_rawat inner join pemeriksaan_ralan on pemeriksaan_ralan.no_rawat=reg_periksa.no_rawat "
                    + "inner join pegawai on pemeriksaan_ralan.nip=pegawai.nik left join satu_sehat_observationttvtb on satu_sehat_observationttvtb.no_rawat=pemeriksaan_ralan.no_rawat "
                    + "and satu_sehat_observationttvtb.tgl_perawatan=pemeriksaan_ralan.tgl_perawatan and satu_sehat_observationttvtb.jam_rawat=pemeriksaan_ralan.jam_rawat "
                    + "and satu_sehat_observationttvtb.status='Ralan' where pemeriksaan_ralan.tinggi<>'' and nota_jalan.tanggal between ? and ?");
            try {
                ps.setString(1, Tanggal1.getText());
                ps.setString(2, Tanggal2.getText());
                rs = ps.executeQuery();
                while (rs.next()) {
                    if ((!rs.getString("no_ktp").equals("")) && (!rs.getString("ktppraktisi").equals("")) && rs.getString("satu_sehat_observationttvtb").equals("")) {
                        try {
                            iddokter = cekViaSatuSehat.tampilIDParktisi(rs.getString("ktppraktisi"));
                            idpasien = cekViaSatuSehat.tampilIDPasien(rs.getString("no_ktp"));
                            try {
                                headers = new HttpHeaders();
                                headers.setContentType(MediaType.APPLICATION_JSON);
                                headers.add("Authorization", "Bearer " + api.TokenSatuSehat());
                                json = "{"
                                        + "\"resourceType\": \"Observation\","
                                        + "\"status\": \"final\","
                                        + "\"category\": ["
                                        + "{"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"system\": \"http://terminology.hl7.org/CodeSystem/observation-category\","
                                        + "\"code\": \"vital-signs\","
                                        + "\"display\": \"Vital Signs\""
                                        + "}"
                                        + "]"
                                        + "}"
                                        + "],"
                                        + "\"code\": {"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"system\": \"http://loinc.org\","
                                        + "\"code\": \"8302-2\","
                                        + "\"display\": \"Body height\""
                                        + "}"
                                        + "]"
                                        + "},"
                                        + "\"subject\": {"
                                        + "\"reference\": \"Patient/" + idpasien + "\""
                                        + "},"
                                        + "\"performer\": ["
                                        + "{"
                                        + "\"reference\": \"Practitioner/" + iddokter + "\""
                                        + "}"
                                        + "],"
                                        + "\"encounter\": {"
                                        + "\"reference\": \"Encounter/" + rs.getString("id_encounter") + "\","
                                        + "\"display\": \"Pemeriksaan Fisik Tinggi Badan di Rawat Jalan/IGD, Pasien " + rs.getString("nm_pasien") + " Pada Tanggal " + rs.getString("tgl_perawatan") + " Jam " + rs.getString("jam_rawat") + "\""
                                        + "},"
                                        + "\"effectiveDateTime\": \"" + rs.getString("tgl_perawatan") + "T" + rs.getString("jam_rawat") + "+07:00\","
                                        + "\"valueQuantity\": {"
                                        + "\"value\": " + rs.getString("tinggi").replaceAll(",", ".") + ","
                                        + "\"unit\": \"centimeter\","
                                        + "\"system\": \"http://unitsofmeasure.org\","
                                        + "\"code\": \"cm\""
                                        + "}"
                                        + "}";
                                TeksArea.append("URL : " + link + "/Observation" + "\n");
                                TeksArea.append("Request JSON : " + json + "\n");
                                requestEntity = new HttpEntity(json, headers);
                                json = api.getRest().exchange(link + "/Observation", HttpMethod.POST, requestEntity, String.class).getBody();
                                TeksArea.append("Result JSON : " + json + "\n");
                                root = mapper.readTree(json);
                                response = root.path("id");
                                if (!response.asText().equals("")) {
                                    Sequel.menyimpan2("satu_sehat_observationttvtb", "?,?,?,?,?", "Observation TB", 5, new String[]{
                                        rs.getString("no_rawat"), rs.getString("tgl_perawatan"), rs.getString("jam_rawat"), "Ralan", response.asText()
                                    });
                                }
                            } catch (Exception eg) {
                                System.out.println("Notifikasi Bridging : " + eg);
                            }
                        } catch (Exception ef) {
                            System.out.println("Notifikasi : " + ef);
                        }
                    }
                }
            } catch (Exception ef) {
                System.out.println("Notif : " + ef);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }

            ps = koneksi.prepareStatement(
                    "select reg_periksa.no_rawat,pasien.nm_pasien,pasien.no_ktp,satu_sehat_encounter.id_encounter,pegawai.nama,pegawai.no_ktp as ktppraktisi,pemeriksaan_ranap.tgl_perawatan,"
                    + "pemeriksaan_ranap.jam_rawat,pemeriksaan_ranap.tinggi,ifnull(satu_sehat_observationttvtb.id_observation,'') as satu_sehat_observationttvtb "
                    + "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis inner join nota_inap on nota_inap.no_rawat=reg_periksa.no_rawat "
                    + "inner join satu_sehat_encounter on satu_sehat_encounter.no_rawat=reg_periksa.no_rawat inner join pemeriksaan_ranap on pemeriksaan_ranap.no_rawat=reg_periksa.no_rawat "
                    + "inner join pegawai on pemeriksaan_ranap.nip=pegawai.nik left join satu_sehat_observationttvtb on satu_sehat_observationttvtb.no_rawat=pemeriksaan_ranap.no_rawat "
                    + "and satu_sehat_observationttvtb.tgl_perawatan=pemeriksaan_ranap.tgl_perawatan and satu_sehat_observationttvtb.jam_rawat=pemeriksaan_ranap.jam_rawat "
                    + "and satu_sehat_observationttvtb.status='Ranap' where pemeriksaan_ranap.tinggi<>'' and nota_inap.tanggal between ? and ? ");
            try {
                ps.setString(1, Tanggal1.getText());
                ps.setString(2, Tanggal2.getText());
                rs = ps.executeQuery();
                while (rs.next()) {
                    if ((!rs.getString("no_ktp").equals("")) && (!rs.getString("ktppraktisi").equals("")) && rs.getString("satu_sehat_observationttvtb").equals("")) {
                        try {
                            iddokter = cekViaSatuSehat.tampilIDParktisi(rs.getString("ktppraktisi"));
                            idpasien = cekViaSatuSehat.tampilIDPasien(rs.getString("no_ktp"));
                            try {
                                headers = new HttpHeaders();
                                headers.setContentType(MediaType.APPLICATION_JSON);
                                headers.add("Authorization", "Bearer " + api.TokenSatuSehat());
                                json = "{"
                                        + "\"resourceType\": \"Observation\","
                                        + "\"status\": \"final\","
                                        + "\"category\": ["
                                        + "{"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"system\": \"http://terminology.hl7.org/CodeSystem/observation-category\","
                                        + "\"code\": \"vital-signs\","
                                        + "\"display\": \"Vital Signs\""
                                        + "}"
                                        + "]"
                                        + "}"
                                        + "],"
                                        + "\"code\": {"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"system\": \"http://loinc.org\","
                                        + "\"code\": \"8302-2\","
                                        + "\"display\": \"Body height\""
                                        + "}"
                                        + "]"
                                        + "},"
                                        + "\"subject\": {"
                                        + "\"reference\": \"Patient/" + idpasien + "\""
                                        + "},"
                                        + "\"performer\": ["
                                        + "{"
                                        + "\"reference\": \"Practitioner/" + iddokter + "\""
                                        + "}"
                                        + "],"
                                        + "\"encounter\": {"
                                        + "\"reference\": \"Encounter/" + rs.getString("id_encounter") + "\","
                                        + "\"display\": \"Pemeriksaan Fisik Tinggi Badan di Rawat Inap, Pasien " + rs.getString("nm_pasien") + " Pada Tanggal " + rs.getString("tgl_perawatan") + " Jam " + rs.getString("jam_rawat") + "\""
                                        + "},"
                                        + "\"effectiveDateTime\": \"" + rs.getString("tgl_perawatan") + "T" + rs.getString("jam_rawat") + "+07:00\","
                                        + "\"valueQuantity\": {"
                                        + "\"value\": " + rs.getString("tinggi").replaceAll(",", ".") + ","
                                        + "\"unit\": \"centimeter\","
                                        + "\"system\": \"http://unitsofmeasure.org\","
                                        + "\"code\": \"cm\""
                                        + "}"
                                        + "}";
                                TeksArea.append("URL : " + link + "/Observation" + "\n");
                                TeksArea.append("Request JSON : " + json + "\n");
                                requestEntity = new HttpEntity(json, headers);
                                json = api.getRest().exchange(link + "/Observation", HttpMethod.POST, requestEntity, String.class).getBody();
                                TeksArea.append("Result JSON : " + json + "\n");
                                root = mapper.readTree(json);
                                response = root.path("id");
                                if (!response.asText().equals("")) {
                                    Sequel.menyimpan2("satu_sehat_observationttvtb", "?,?,?,?,?", "Observation TB", 5, new String[]{
                                        rs.getString("no_rawat"), rs.getString("tgl_perawatan"), rs.getString("jam_rawat"), "Ranap", response.asText()
                                    });
                                }
                            } catch (Exception eg) {
                                System.out.println("Notifikasi Bridging : " + eg);
                            }
                        } catch (Exception ef) {
                            System.out.println("Notifikasi : " + ef);
                        }
                    }
                }
            } catch (Exception ef) {
                System.out.println("Notif : " + ef);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception ex) {
            System.out.println("Notifikasi : " + ex);
        }

        //kirim TTV Berat Badan
        try {
            ps = koneksi.prepareStatement(
                    "select reg_periksa.no_rawat,pasien.nm_pasien,pasien.no_ktp,satu_sehat_encounter.id_encounter,pegawai.no_ktp as ktppraktisi,pemeriksaan_ralan.tgl_perawatan,"
                    + "pemeriksaan_ralan.jam_rawat,pemeriksaan_ralan.berat,ifnull(satu_sehat_observationttvbb.id_observation,'') as satu_sehat_observationttvbb "
                    + "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis inner join nota_jalan on nota_jalan.no_rawat=reg_periksa.no_rawat "
                    + "inner join satu_sehat_encounter on satu_sehat_encounter.no_rawat=reg_periksa.no_rawat inner join pemeriksaan_ralan on pemeriksaan_ralan.no_rawat=reg_periksa.no_rawat "
                    + "inner join pegawai on pemeriksaan_ralan.nip=pegawai.nik left join satu_sehat_observationttvbb on satu_sehat_observationttvbb.no_rawat=pemeriksaan_ralan.no_rawat "
                    + "and satu_sehat_observationttvbb.tgl_perawatan=pemeriksaan_ralan.tgl_perawatan and satu_sehat_observationttvbb.jam_rawat=pemeriksaan_ralan.jam_rawat "
                    + "and satu_sehat_observationttvbb.status='Ralan' where pemeriksaan_ralan.berat<>'' and nota_jalan.tanggal between ? and ? ");
            try {
                ps.setString(1, Tanggal1.getText());
                ps.setString(2, Tanggal2.getText());
                rs = ps.executeQuery();
                while (rs.next()) {
                    if ((!rs.getString("no_ktp").equals("")) && (!rs.getString("ktppraktisi").equals("")) && rs.getString("satu_sehat_observationttvbb").equals("")) {
                        try {
                            iddokter = cekViaSatuSehat.tampilIDParktisi(rs.getString("ktppraktisi"));
                            idpasien = cekViaSatuSehat.tampilIDPasien(rs.getString("no_ktp"));
                            try {
                                headers = new HttpHeaders();
                                headers.setContentType(MediaType.APPLICATION_JSON);
                                headers.add("Authorization", "Bearer " + api.TokenSatuSehat());
                                json = "{"
                                        + "\"resourceType\": \"Observation\","
                                        + "\"status\": \"final\","
                                        + "\"category\": ["
                                        + "{"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"system\": \"http://terminology.hl7.org/CodeSystem/observation-category\","
                                        + "\"code\": \"vital-signs\","
                                        + "\"display\": \"Vital Signs\""
                                        + "}"
                                        + "]"
                                        + "}"
                                        + "],"
                                        + "\"code\": {"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"system\": \"http://loinc.org\","
                                        + "\"code\": \"29463-7\","
                                        + "\"display\": \"Body Weight\""
                                        + "}"
                                        + "]"
                                        + "},"
                                        + "\"subject\": {"
                                        + "\"reference\": \"Patient/" + idpasien + "\""
                                        + "},"
                                        + "\"performer\": ["
                                        + "{"
                                        + "\"reference\": \"Practitioner/" + iddokter + "\""
                                        + "}"
                                        + "],"
                                        + "\"encounter\": {"
                                        + "\"reference\": \"Encounter/" + rs.getString("id_encounter") + "\","
                                        + "\"display\": \"Pemeriksaan Fisik Berat Badan di Rawat Jalan/IGD, Pasien " + rs.getString("nm_pasien") + " Pada Tanggal " + rs.getString("tgl_perawatan") + " Jam " + rs.getString("jam_rawat") + "\""
                                        + "},"
                                        + "\"effectiveDateTime\": \"" + rs.getString("tgl_perawatan") + "T" + rs.getString("jam_rawat") + "+07:00\","
                                        + "\"valueQuantity\": {"
                                        + "\"value\": " + rs.getString("berat").replaceAll(",", ".") + ","
                                        + "\"unit\": \"kilogram\","
                                        + "\"system\": \"http://unitsofmeasure.org\","
                                        + "\"code\": \"kg\""
                                        + "}"
                                        + "}";
                                TeksArea.append("URL : " + link + "/Observation" + "\n");
                                TeksArea.append("Request JSON : " + json + "\n");
                                requestEntity = new HttpEntity(json, headers);
                                json = api.getRest().exchange(link + "/Observation", HttpMethod.POST, requestEntity, String.class).getBody();
                                TeksArea.append("Result JSON : " + json + "\n");
                                root = mapper.readTree(json);
                                response = root.path("id");
                                if (!response.asText().equals("")) {
                                    Sequel.menyimpan2("satu_sehat_observationttvbb", "?,?,?,?,?", "Observation BB", 5, new String[]{
                                        rs.getString("no_rawat"), rs.getString("tgl_perawatan"), rs.getString("jam_rawat"), "Ralan", response.asText()
                                    });
                                }
                            } catch (Exception e) {
                                System.out.println("Notifikasi Bridging : " + e);
                            }
                        } catch (Exception e) {
                            System.out.println("Notifikasi : " + e);
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Notif : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }

            ps = koneksi.prepareStatement(
                    "select reg_periksa.no_rawat,pasien.nm_pasien,pasien.no_ktp,satu_sehat_encounter.id_encounter,pegawai.no_ktp as ktppraktisi,pemeriksaan_ranap.tgl_perawatan,"
                    + "pemeriksaan_ranap.jam_rawat,pemeriksaan_ranap.berat,ifnull(satu_sehat_observationttvbb.id_observation,'') as satu_sehat_observationttvbb "
                    + "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis inner join nota_inap on nota_inap.no_rawat=reg_periksa.no_rawat "
                    + "inner join satu_sehat_encounter on satu_sehat_encounter.no_rawat=reg_periksa.no_rawat inner join pemeriksaan_ranap on pemeriksaan_ranap.no_rawat=reg_periksa.no_rawat "
                    + "inner join pegawai on pemeriksaan_ranap.nip=pegawai.nik left join satu_sehat_observationttvbb on satu_sehat_observationttvbb.no_rawat=pemeriksaan_ranap.no_rawat "
                    + "and satu_sehat_observationttvbb.tgl_perawatan=pemeriksaan_ranap.tgl_perawatan and satu_sehat_observationttvbb.jam_rawat=pemeriksaan_ranap.jam_rawat "
                    + "and satu_sehat_observationttvbb.status='Ranap' where pemeriksaan_ranap.berat<>'' and nota_inap.tanggal between ? and ? ");
            try {
                ps.setString(1, Tanggal1.getText());
                ps.setString(2, Tanggal2.getText());
                rs = ps.executeQuery();
                while (rs.next()) {
                    if ((!rs.getString("no_ktp").equals("")) && (!rs.getString("ktppraktisi").equals("")) && rs.getString("satu_sehat_observationttvbb").equals("")) {
                        try {
                            iddokter = cekViaSatuSehat.tampilIDParktisi(rs.getString("ktppraktisi"));
                            idpasien = cekViaSatuSehat.tampilIDPasien(rs.getString("no_ktp"));
                            try {
                                headers = new HttpHeaders();
                                headers.setContentType(MediaType.APPLICATION_JSON);
                                headers.add("Authorization", "Bearer " + api.TokenSatuSehat());
                                json = "{"
                                        + "\"resourceType\": \"Observation\","
                                        + "\"status\": \"final\","
                                        + "\"category\": ["
                                        + "{"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"system\": \"http://terminology.hl7.org/CodeSystem/observation-category\","
                                        + "\"code\": \"vital-signs\","
                                        + "\"display\": \"Vital Signs\""
                                        + "}"
                                        + "]"
                                        + "}"
                                        + "],"
                                        + "\"code\": {"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"system\": \"http://loinc.org\","
                                        + "\"code\": \"29463-7\","
                                        + "\"display\": \"Body Weight\""
                                        + "}"
                                        + "]"
                                        + "},"
                                        + "\"subject\": {"
                                        + "\"reference\": \"Patient/" + idpasien + "\""
                                        + "},"
                                        + "\"performer\": ["
                                        + "{"
                                        + "\"reference\": \"Practitioner/" + iddokter + "\""
                                        + "}"
                                        + "],"
                                        + "\"encounter\": {"
                                        + "\"reference\": \"Encounter/" + rs.getString("id_encounter") + "\","
                                        + "\"display\": \"Pemeriksaan Fisik Berat Badan di Rawat Inap, Pasien " + rs.getString("nm_pasien") + " Pada Tanggal " + rs.getString("tgl_perawatan") + " Jam " + rs.getString("jam_rawat") + "\""
                                        + "},"
                                        + "\"effectiveDateTime\": \"" + rs.getString("tgl_perawatan") + "T" + rs.getString("jam_rawat") + "+07:00\","
                                        + "\"valueQuantity\": {"
                                        + "\"value\": " + rs.getString("berat").replaceAll(",", ".") + ","
                                        + "\"unit\": \"kilogram\","
                                        + "\"system\": \"http://unitsofmeasure.org\","
                                        + "\"code\": \"kg\""
                                        + "}"
                                        + "}";
                                TeksArea.append("URL : " + link + "/Observation" + "\n");
                                TeksArea.append("Request JSON : " + json + "\n");
                                requestEntity = new HttpEntity(json, headers);
                                json = api.getRest().exchange(link + "/Observation", HttpMethod.POST, requestEntity, String.class).getBody();
                                TeksArea.append("Result JSON : " + json + "\n");
                                root = mapper.readTree(json);
                                response = root.path("id");
                                if (!response.asText().equals("")) {
                                    Sequel.menyimpan2("satu_sehat_observationttvbb", "?,?,?,?,?", "Observation BB", 5, new String[]{
                                        rs.getString("no_rawat"), rs.getString("tgl_perawatan"), rs.getString("jam_rawat"), "Ranap", response.asText()
                                    });
                                }
                            } catch (Exception e) {
                                System.out.println("Notifikasi Bridging : " + e);
                            }
                        } catch (Exception e) {
                            System.out.println("Notifikasi : " + e);
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Notif : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }

        //kirim TTV Lingkar Perut
        try {
            ps = koneksi.prepareStatement(
                    "select reg_periksa.no_rawat,pasien.nm_pasien,pasien.no_ktp,satu_sehat_encounter.id_encounter,pegawai.no_ktp as ktppraktisi,pemeriksaan_ralan.tgl_perawatan,"
                    + "pemeriksaan_ralan.jam_rawat,pemeriksaan_ralan.lingkar_perut,ifnull(satu_sehat_observationttvlp.id_observation,'') as satu_sehat_observationttvlp "
                    + "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis inner join nota_jalan on nota_jalan.no_rawat=reg_periksa.no_rawat "
                    + "inner join satu_sehat_encounter on satu_sehat_encounter.no_rawat=reg_periksa.no_rawat inner join pemeriksaan_ralan on pemeriksaan_ralan.no_rawat=reg_periksa.no_rawat "
                    + "inner join pegawai on pemeriksaan_ralan.nip=pegawai.nik left join satu_sehat_observationttvlp on satu_sehat_observationttvlp.no_rawat=pemeriksaan_ralan.no_rawat "
                    + "and satu_sehat_observationttvlp.tgl_perawatan=pemeriksaan_ralan.tgl_perawatan and satu_sehat_observationttvlp.jam_rawat=pemeriksaan_ralan.jam_rawat "
                    + "and satu_sehat_observationttvlp.status='Ralan' where pemeriksaan_ralan.lingkar_perut<>'' and nota_jalan.tanggal between ? and ? ");
            try {
                ps.setString(1, Tanggal1.getText());
                ps.setString(2, Tanggal2.getText());
                rs = ps.executeQuery();
                while (rs.next()) {
                    if ((!rs.getString("no_ktp").equals("")) && (!rs.getString("ktppraktisi").equals("")) && rs.getString("satu_sehat_observationttvlp").equals("")) {
                        try {
                            iddokter = cekViaSatuSehat.tampilIDParktisi(rs.getString("ktppraktisi"));
                            idpasien = cekViaSatuSehat.tampilIDPasien(rs.getString("no_ktp"));
                            try {
                                headers = new HttpHeaders();
                                headers.setContentType(MediaType.APPLICATION_JSON);
                                headers.add("Authorization", "Bearer " + api.TokenSatuSehat());
                                json = "{"
                                        + "\"resourceType\": \"Observation\","
                                        + "\"status\": \"final\","
                                        + "\"category\": ["
                                        + "{"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"system\": \"http://terminology.hl7.org/CodeSystem/observation-category\","
                                        + "\"code\": \"vital-signs\","
                                        + "\"display\": \"Vital Signs\""
                                        + "}"
                                        + "]"
                                        + "}"
                                        + "],"
                                        + "\"code\": {"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"system\": \"http://loinc.org\","
                                        + "\"code\": \"8280-0\","
                                        + "\"display\": \"Waist Circumference at umbilicus by Tape measure\""
                                        + "}"
                                        + "]"
                                        + "},"
                                        + "\"subject\": {"
                                        + "\"reference\": \"Patient/" + idpasien + "\""
                                        + "},"
                                        + "\"performer\": ["
                                        + "{"
                                        + "\"reference\": \"Practitioner/" + iddokter + "\""
                                        + "}"
                                        + "],"
                                        + "\"encounter\": {"
                                        + "\"reference\": \"Encounter/" + rs.getString("id_encounter") + "\","
                                        + "\"display\": \"Pemeriksaan Fisik Lingkar Perut di Rawat Jalan/IGD, Pasien " + rs.getString("nm_pasien") + " Pada Tanggal " + rs.getString("tgl_perawatan") + " Jam " + rs.getString("jam_rawat") + "\""
                                        + "},"
                                        + "\"effectiveDateTime\": \"" + rs.getString("tgl_perawatan") + "T" + rs.getString("jam_rawat") + "+07:00\","
                                        + "\"valueQuantity\": {"
                                        + "\"value\": " + rs.getString("lingkar_perut").replaceAll(",", ".") + ","
                                        + "\"unit\": \"centimeter\","
                                        + "\"system\": \"http://unitsofmeasure.org\","
                                        + "\"code\": \"cm\""
                                        + "}"
                                        + "}";
                                TeksArea.append("URL : " + link + "/Observation" + "\n");
                                TeksArea.append("Request JSON : " + json + "\n");
                                requestEntity = new HttpEntity(json, headers);
                                json = api.getRest().exchange(link + "/Observation", HttpMethod.POST, requestEntity, String.class).getBody();
                                TeksArea.append("Result JSON : " + json + "\n");
                                root = mapper.readTree(json);
                                response = root.path("id");
                                if (!response.asText().equals("")) {
                                    Sequel.menyimpan2("satu_sehat_observationttvlp", "?,?,?,?,?", "Observation LP", 5, new String[]{
                                        rs.getString("no_rawat"), rs.getString("tgl_perawatan"), rs.getString("jam_rawat"), "Ralan", response.asText()
                                    });
                                }
                            } catch (Exception e) {
                                System.out.println("Notifikasi Bridging : " + e);
                            }
                        } catch (Exception e) {
                            System.out.println("Notifikasi : " + e);
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Notif : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
    }

    public void clinicalimpression(String norawat) {
        try {
            ps = koneksi.prepareStatement(
                    "select reg_periksa.tgl_registrasi,reg_periksa.jam_reg,reg_periksa.no_rawat,reg_periksa.no_rkm_medis,"
                    + "pasien.nm_pasien,pasien.no_ktp,reg_periksa.stts,concat(nota_jalan.tanggal,' ',nota_jalan.jam) "
                    + "as pulang,satu_sehat_encounter.id_encounter,pegawai.nama,pegawai.no_ktp as ktppraktisi,"
                    + "pemeriksaan_ralan.tgl_perawatan,pemeriksaan_ralan.jam_rawat,pemeriksaan_ralan.penilaian,"
                    + "pemeriksaan_ralan.keluhan,pemeriksaan_ralan.pemeriksaan,satu_sehat_condition.kd_penyakit,"
                    + "penyakit.nm_penyakit,satu_sehat_condition.id_condition,"
                    + "ifnull(satu_sehat_clinicalimpression.id_clinicalimpression,'') as satu_sehat_clinicalimpression "
                    + "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "
                    + "left join nota_jalan on nota_jalan.no_rawat=reg_periksa.no_rawat "
                    + "inner join satu_sehat_encounter on satu_sehat_encounter.no_rawat=reg_periksa.no_rawat "
                    + "inner join satu_sehat_condition on satu_sehat_condition.no_rawat=reg_periksa.no_rawat and satu_sehat_condition.status='Ralan' "
                    + "inner join penyakit on penyakit.kd_penyakit=satu_sehat_condition.kd_penyakit "
                    + "inner join pemeriksaan_ralan on pemeriksaan_ralan.no_rawat=reg_periksa.no_rawat "
                    + "inner join pegawai on pemeriksaan_ralan.nip=pegawai.nik "
                    + "left join satu_sehat_clinicalimpression on satu_sehat_clinicalimpression.no_rawat=pemeriksaan_ralan.no_rawat "
                    + "and satu_sehat_clinicalimpression.tgl_perawatan=pemeriksaan_ralan.tgl_perawatan "
                    + "and satu_sehat_clinicalimpression.jam_rawat=pemeriksaan_ralan.jam_rawat "
                    + "and satu_sehat_clinicalimpression.status='Ralan' where reg_periksa.no_rawat = ?");
            try {
                ps.setString(1, norawat);

                rs = ps.executeQuery();
                while (rs.next()) {
                    try {
                        iddokter = cekViaSatuSehat.tampilIDParktisi(rs.getString("ktppraktisi"));
                        idpasien = cekViaSatuSehat.tampilIDPasien(rs.getString("no_ktp"));
                        try {
                            headers = new HttpHeaders();
                            headers.setContentType(MediaType.APPLICATION_JSON);
                            headers.add("Authorization", "Bearer " + api.TokenSatuSehat());
                            json = "{"
                                    + "\"resourceType\": \"ClinicalImpression\","
                                    + "\"status\": \"completed\","
                                    + "\"description\" : \"" + rs.getString("keluhan") + "\","
                                    + "\"subject\" : {"
                                    + "\"reference\" : \"Patient/" + idpasien + "\","
                                    + "\"display\" : \"" + rs.getString("nm_pasien") + "\""
                                    + "},"
                                    + "\"encounter\" : { "
                                    + "\"reference\" : \"Encounter/" + rs.getString("id_encounter") + "\","
                                    + "\"display\" : \"Kunjungan " + rs.getString("nm_pasien") + " pada tanggal " + rs.getString("tgl_registrasi") + ":" + rs.getString("jam_reg") + " dengan nomor kunjungan " + rs.getString("no_rawat") + "\""
                                    + "},"
                                    + "\"effectiveDateTime\": \"" + rs.getString("tgl_perawatan") + "T" + rs.getString("jam_rawat") + "+07:00\","
                                    + "\"date\": \"" + rs.getString("tgl_perawatan") + "T" + rs.getString("jam_rawat") + "+07:00\","
                                    + "\"assessor\" : {"
                                    + "\"reference\" : \"Practitioner/" + iddokter + "\""
                                    + "},"
                                    + "\"summary\" : \"" + rs.getString("penilaian").replaceAll("\n", "") + "\","
                                    + "\"finding\": ["
                                    + "{"
                                    + "\"itemCodeableConcept\": {"
                                    + "\"coding\": ["
                                    + "{"
                                    + "\"system\": \"http://hl7.org/fhir/sid/icd-10\","
                                    + "\"code\": \"" + rs.getString("kd_penyakit") + "\","
                                    + "\"display\": \"" + rs.getString("nm_penyakit") + "\""
                                    + "}"
                                    + "]"
                                    + "},"
                                    + "\"itemReference\": {"
                                    + "\"reference\": \"Condition/" + rs.getString("id_condition") + "\""
                                    + "}"
                                    + "}"
                                    + "],"
                                    + "\"prognosisCodeableConcept\": ["
                                    + "{"
                                    + "\"coding\": ["
                                    + "{"
                                    + "\"system\": \"http://terminology.kemkes.go.id/CodeSystem/clinical-term\","
                                    + "\"code\": \"PR000001\","
                                    + "\"display\": \"Prognosis\""
                                    + "}"
                                    + "]"
                                    + "}"
                                    + "]"
                                    + "}";
                            System.out.println("URL : " + link + "/ClinicalImpression");
                            System.out.println("Request JSON : " + json);
                            requestEntity = new HttpEntity(json, headers);
                            json = api.getRest().exchange(link + "/ClinicalImpression", HttpMethod.POST, requestEntity, String.class).getBody();
                            System.out.println("Result JSON : " + json);
                            root = mapper.readTree(json);
                            response = root.path("id");
                            if (!response.asText().equals("")) {
                                Sequel.menyimpan("satu_sehat_clinicalimpression", "?,?,?,?,?", "Clinical Impression", 5, new String[]{
                                    rs.getString("no_rawat"), rs.getString("tgl_perawatan"), rs.getString("jam_rawat"), "Ralan", response.asText()
                                });
                            }
                        } catch (HttpClientErrorException | HttpServerErrorException e) {
                            // Handle client and server errors
                            System.err.println("Error Response Status Code: " + e.getStatusCode());
//                            System.err.println("Error Response Body: " + e.getResponseBodyAsString());

                            // You can further parse the error response body if needed
                            ObjectMapper mapper = new ObjectMapper();
                            JsonNode errorResponse = mapper.readTree(e.getResponseBodyAsString());
                            ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
                            String prettyErrorResponse = writer.writeValueAsString(errorResponse);
                            System.err.println("Error Response JSON: \n" + prettyErrorResponse);
                        }
                    } catch (Exception e) {
                        System.out.println("Notifikasi : " + e);
                    }

                }
            } catch (Exception e) {
                System.out.println("Notif : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }

            ps = koneksi.prepareStatement(
                    "select reg_periksa.tgl_registrasi,reg_periksa.jam_reg,reg_periksa.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien,pasien.no_ktp,"
                    + "reg_periksa.stts,concat(nota_inap.tanggal,' ',nota_inap.jam) as pulang,satu_sehat_encounter.id_encounter,"
                    + "pegawai.nama,pegawai.no_ktp as ktppraktisi,pemeriksaan_ranap.tgl_perawatan,pemeriksaan_ranap.jam_rawat,pemeriksaan_ranap.penilaian,"
                    + "pemeriksaan_ranap.keluhan,pemeriksaan_ranap.pemeriksaan,satu_sehat_condition.kd_penyakit,"
                    + "penyakit.nm_penyakit,satu_sehat_condition.id_condition,"
                    + "ifnull(satu_sehat_clinicalimpression.id_clinicalimpression,'') as satu_sehat_clinicalimpression "
                    + "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "
                    + "left join nota_inap on nota_inap.no_rawat=reg_periksa.no_rawat "
                    + "inner join satu_sehat_encounter on satu_sehat_encounter.no_rawat=reg_periksa.no_rawat "
                    + "inner join satu_sehat_condition on satu_sehat_condition.no_rawat=reg_periksa.no_rawat and satu_sehat_condition.status='Ranap' "
                    + "inner join penyakit on penyakit.kd_penyakit=satu_sehat_condition.kd_penyakit "
                    + "inner join pemeriksaan_ranap on pemeriksaan_ranap.no_rawat=reg_periksa.no_rawat "
                    + "inner join pegawai on pemeriksaan_ranap.nip=pegawai.nik "
                    + "left join satu_sehat_clinicalimpression on satu_sehat_clinicalimpression.no_rawat=pemeriksaan_ranap.no_rawat "
                    + "and satu_sehat_clinicalimpression.tgl_perawatan=pemeriksaan_ranap.tgl_perawatan and satu_sehat_clinicalimpression.jam_rawat=pemeriksaan_ranap.jam_rawat "
                    + "and satu_sehat_clinicalimpression.status='Ranap' where reg_periksa.no_rawat= ?");
            try {
                ps.setString(1, norawat);

                rs = ps.executeQuery();
                while (rs.next()) {
                    try {
                        iddokter = cekViaSatuSehat.tampilIDParktisi(rs.getString("ktppraktisi"));
                        idpasien = cekViaSatuSehat.tampilIDPasien(rs.getString("no_ktp"));
                        try {
                            headers = new HttpHeaders();
                            headers.setContentType(MediaType.APPLICATION_JSON);
                            headers.add("Authorization", "Bearer " + api.TokenSatuSehat());
                            json = "{"
                                    + "\"resourceType\": \"ClinicalImpression\","
                                    + "\"status\": \"completed\","
                                    + "\"description\" : \"" + rs.getString("keluhan") + "\","
                                    + "\"subject\" : {"
                                    + "\"reference\" : \"Patient/" + idpasien + "\","
                                    + "\"display\" : \"" + rs.getString("nm_pasien") + "\""
                                    + "},"
                                    + "\"encounter\" : { "
                                    + "\"reference\" : \"Encounter/" + rs.getString("id_encounter") + "\","
                                    + "\"display\" : \"Kunjungan " + rs.getString("nm_pasien") + " pada tanggal " + rs.getString("tgl_registrasi") + ":" + rs.getString("jam_reg") + " dengan nomor kunjungan " + rs.getString("no_rawat") + "\""
                                    + "},"
                                    + "\"effectiveDateTime\": \"" + rs.getString("tgl_perawatan") + "T" + rs.getString("jam_rawat") + "+07:00\","
                                    + "\"date\": \"" + rs.getString("tgl_perawatan") + "T" + rs.getString("jam_rawat") + "+07:00\","
                                    + "\"assessor\" : {"
                                    + "\"reference\" : \"Practitioner/" + iddokter + "\""
                                    + "},"
                                    + "\"summary\" : \"" + rs.getString("penilaian").replaceAll("\n", "") + "\","
                                    + "\"finding\": ["
                                    + "{"
                                    + "\"itemCodeableConcept\": {"
                                    + "\"coding\": ["
                                    + "{"
                                    + "\"system\": \"http://hl7.org/fhir/sid/icd-10\","
                                    + "\"code\": \"" + rs.getString("kd_penyakit") + "\","
                                    + "\"display\": \"" + rs.getString("nm_penyakit") + "\""
                                    + "}"
                                    + "]"
                                    + "},"
                                    + "\"itemReference\": {"
                                    + "\"reference\": \"Condition/" + rs.getString("id_condition") + "\""
                                    + "}"
                                    + "}"
                                    + "],"
                                    + "\"prognosisCodeableConcept\": ["
                                    + "{"
                                    + "\"coding\": ["
                                    + "{"
                                    + "\"system\": \"http://terminology.kemkes.go.id/CodeSystem/clinical-term\","
                                    + "\"code\": \"PR000001\","
                                    + "\"display\": \"Prognosis\""
                                    + "}"
                                    + "]"
                                    + "}"
                                    + "]"
                                    + "}";
                            System.out.println("URL : " + link + "/ClinicalImpression");
                            System.out.println("Request JSON : " + json);
                            requestEntity = new HttpEntity(json, headers);
                            json = api.getRest().exchange(link + "/ClinicalImpression", HttpMethod.POST, requestEntity, String.class).getBody();
                            System.out.println("Result JSON : " + json);
                            root = mapper.readTree(json);
                            response = root.path("id");
                            if (!response.asText().equals("")) {
                                Sequel.menyimpan("satu_sehat_clinicalimpression", "?,?,?,?,?", "Clinical Impression", 5, new String[]{
                                    rs.getString("no_rawat"), rs.getString("tgl_perawatan"), rs.getString("jam_rawat"), "Ranap", response.asText()
                                });
                            }
                        } catch (HttpClientErrorException | HttpServerErrorException e) {
                            // Handle client and server errors
                            System.err.println("Error Response Status Code: " + e.getStatusCode());
//                            System.err.println("Error Response Body: " + e.getResponseBodyAsString());

                            // You can further parse the error response body if needed
                            ObjectMapper mapper = new ObjectMapper();
                            JsonNode errorResponse = mapper.readTree(e.getResponseBodyAsString());
                            ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
                            String prettyErrorResponse = writer.writeValueAsString(errorResponse);
                            System.err.println("Error Response JSON: \n" + prettyErrorResponse);
                        }
                    } catch (Exception e) {
                        System.out.println("Notifikasi : " + e);
                    }
                }
            } catch (Exception e) {
                System.out.println("Notif : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
    }

    private void vaksin() {
        try {
            ps = koneksi.prepareStatement(
                    "select reg_periksa.no_rawat,pasien.no_ktp,satu_sehat_encounter.id_encounter,satu_sehat_mapping_vaksin.vaksin_code,satu_sehat_mapping_vaksin.vaksin_system,"
                    + "satu_sehat_mapping_vaksin.kode_brng,satu_sehat_mapping_vaksin.vaksin_display,satu_sehat_mapping_vaksin.route_code,satu_sehat_mapping_vaksin.route_system,"
                    + "satu_sehat_mapping_vaksin.route_display,satu_sehat_mapping_vaksin.dose_quantity_code,satu_sehat_mapping_vaksin.dose_quantity_system,"
                    + "satu_sehat_mapping_vaksin.dose_quantity_unit,detail_pemberian_obat.no_batch,detail_pemberian_obat.tgl_perawatan,detail_pemberian_obat.jam,"
                    + "detail_pemberian_obat.jml,aturan_pakai.aturan,satu_sehat_mapping_lokasi_ralan.id_lokasi_satusehat,poliklinik.nm_poli,pegawai.no_ktp as ktppraktisi,"
                    + "ifnull(satu_sehat_immunization.id_immunization,'') as id_immunization,detail_pemberian_obat.no_faktur from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "
                    + "inner join satu_sehat_encounter on satu_sehat_encounter.no_rawat=reg_periksa.no_rawat "
                    + "inner join detail_pemberian_obat on detail_pemberian_obat.no_rawat=reg_periksa.no_rawat "
                    + "inner join satu_sehat_mapping_vaksin on satu_sehat_mapping_vaksin.kode_brng=detail_pemberian_obat.kode_brng "
                    + "inner join aturan_pakai on aturan_pakai.tgl_perawatan=detail_pemberian_obat.tgl_perawatan and aturan_pakai.jam=detail_pemberian_obat.jam and "
                    + "aturan_pakai.no_rawat=detail_pemberian_obat.no_rawat and aturan_pakai.kode_brng=detail_pemberian_obat.kode_brng "
                    + "inner join satu_sehat_mapping_lokasi_ralan on satu_sehat_mapping_lokasi_ralan.kd_poli=reg_periksa.kd_poli "
                    + "inner join poliklinik on poliklinik.kd_poli=satu_sehat_mapping_lokasi_ralan.kd_poli "
                    + "inner join pegawai on reg_periksa.kd_dokter=pegawai.nik "
                    + "inner join nota_jalan on nota_jalan.no_rawat=reg_periksa.no_rawat "
                    + "left join satu_sehat_immunization on satu_sehat_immunization.no_rawat=detail_pemberian_obat.no_rawat and satu_sehat_immunization.tgl_perawatan=detail_pemberian_obat.tgl_perawatan and "
                    + "satu_sehat_immunization.jam=detail_pemberian_obat.jam and satu_sehat_immunization.kode_brng=detail_pemberian_obat.kode_brng and "
                    + "satu_sehat_immunization.no_batch=detail_pemberian_obat.no_batch and satu_sehat_immunization.no_faktur=detail_pemberian_obat.no_faktur "
                    + "where detail_pemberian_obat.no_batch<>'' and nota_jalan.tanggal between ? and ?");
            try {
                ps.setString(1, Tanggal1.getText());
                ps.setString(2, Tanggal2.getText());
                rs = ps.executeQuery();
                while (rs.next()) {
                    if ((!rs.getString("no_ktp").equals("")) && (!rs.getString("ktppraktisi").equals("")) && rs.getString("id_immunization").equals("")) {
                        try {
                            idpasien = cekViaSatuSehat.tampilIDPasien(rs.getString("no_ktp"));
                            iddokter = cekViaSatuSehat.tampilIDParktisi(rs.getString("ktppraktisi"));
                            try {
                                headers = new HttpHeaders();
                                headers.setContentType(MediaType.APPLICATION_JSON);
                                headers.add("Authorization", "Bearer " + api.TokenSatuSehat());
                                json = "{"
                                        + "\"resourceType\": \"Immunization\","
                                        + "\"status\": \"completed\","
                                        + "\"vaccineCode\": {"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"system\": \"" + rs.getString("vaksin_system") + "\","
                                        + "\"code\": \"" + rs.getString("vaksin_code") + "\","
                                        + "\"display\": \"" + rs.getString("vaksin_display") + "\""
                                        + "}"
                                        + "]"
                                        + "},"
                                        + "\"patient\": {"
                                        + "\"reference\": \"Patient/" + idpasien + "\""
                                        + "},"
                                        + "\"encounter\": {"
                                        + "\"reference\": \"Encounter/" + rs.getString("id_encounter") + "\""
                                        + "},"
                                        + "\"occurrenceDateTime\": \"" + rs.getString("tgl_perawatan") + "T" + rs.getString("jam") + "+07:00" + "\","
                                        + "\"recorded\": \"" + rs.getString("tgl_perawatan") + "T" + rs.getString("jam") + "+07:00" + "\","
                                        + "\"primarySource\": true,"
                                        + "\"location\": {"
                                        + "\"reference\": \"Location/" + rs.getString("id_lokasi_satusehat") + "\","
                                        + "\"display\": \"" + rs.getString("nm_poli") + "\""
                                        + "},"
                                        + "\"lotNumber\": \"" + rs.getString("no_batch") + "\","
                                        + "\"route\": {"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"system\": \"" + rs.getString("route_system") + "\","
                                        + "\"code\": \"" + rs.getString("route_code") + "\","
                                        + "\"display\": \"" + rs.getString("route_display") + "\""
                                        + "}"
                                        + "]"
                                        + "},"
                                        + "\"doseQuantity\": {"
                                        + "\"value\": " + rs.getString("jml") + ","
                                        + "\"unit\": \"" + rs.getString("dose_quantity_unit") + "\","
                                        + "\"system\": \"" + rs.getString("dose_quantity_system") + "\","
                                        + "\"code\": \"" + rs.getString("dose_quantity_code") + "\""
                                        + "},"
                                        + "\"performer\": ["
                                        + "{"
                                        + "\"function\": {"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"system\": \"http://terminology.hl7.org/CodeSystem/v2-0443\","
                                        + "\"code\": \"AP\","
                                        + "\"display\": \"Administering Provider\""
                                        + "}"
                                        + "]"
                                        + "},"
                                        + "\"actor\": {"
                                        + "\"reference\": \"Practitioner/" + iddokter + "\""
                                        + "}"
                                        + "}"
                                        + "],"
                                        + "\"reasonCode\": ["
                                        + "{"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"system\": \"https://terminology.kemkes.go.id/CodeSystem/immunization-reason\","
                                        + "\"code\": \"IM-Program\","
                                        + "\"display\" : \"Imunisasi Program\""
                                        + "}"
                                        + "]"
                                        + "}"
                                        + "],"
                                        + "\"protocolApplied\" : ["
                                        + "{"
                                        + "\"doseNumberPositiveInt\" : " + rs.getString("aturan").toLowerCase().replaceAll("dosis", "").replaceAll(" ", "")
                                        + "}"
                                        + "]"
                                        + "}";
                                TeksArea.append("URL : " + link + "/Immunization" + "\n");
                                TeksArea.append("Request JSON : " + json + "\n");
                                requestEntity = new HttpEntity(json, headers);
                                json = api.getRest().exchange(link + "/Immunization", HttpMethod.POST, requestEntity, String.class).getBody();
                                TeksArea.append("Result JSON : " + json + "\n");
                                root = mapper.readTree(json);
                                response = root.path("id");
                                if (!response.asText().equals("")) {
                                    Sequel.menyimpan2("satu_sehat_immunization", "?,?,?,?,?,?,?", "Imunisasi/Vaksin", 7, new String[]{
                                        rs.getString("no_rawat"), rs.getString("tgl_perawatan"), rs.getString("jam"), rs.getString("kode_brng"), rs.getString("no_batch"), rs.getString("no_faktur"), response.asText()
                                    });
                                }
                            } catch (Exception e) {
                                System.out.println("Notifikasi Bridging : " + e);
                            }
                        } catch (Exception e) {
                            System.out.println("Notifikasi : " + e);
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Notif : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }

            ps = koneksi.prepareStatement(
                    "select reg_periksa.no_rawat,pasien.no_ktp,satu_sehat_encounter.id_encounter,satu_sehat_mapping_vaksin.vaksin_code,satu_sehat_mapping_vaksin.vaksin_system,"
                    + "satu_sehat_mapping_vaksin.kode_brng,satu_sehat_mapping_vaksin.vaksin_display,satu_sehat_mapping_vaksin.route_code,satu_sehat_mapping_vaksin.route_system,"
                    + "satu_sehat_mapping_vaksin.route_display,satu_sehat_mapping_vaksin.dose_quantity_code,satu_sehat_mapping_vaksin.dose_quantity_system,"
                    + "satu_sehat_mapping_vaksin.dose_quantity_unit,detail_pemberian_obat.no_batch,detail_pemberian_obat.tgl_perawatan,detail_pemberian_obat.jam,"
                    + "detail_pemberian_obat.jml,aturan_pakai.aturan,satu_sehat_mapping_lokasi_ralan.id_lokasi_satusehat,poliklinik.nm_poli,pegawai.no_ktp as ktppraktisi,"
                    + "ifnull(satu_sehat_immunization.id_immunization,'') as id_immunization,detail_pemberian_obat.no_faktur from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "
                    + "inner join satu_sehat_encounter on satu_sehat_encounter.no_rawat=reg_periksa.no_rawat "
                    + "inner join detail_pemberian_obat on detail_pemberian_obat.no_rawat=reg_periksa.no_rawat "
                    + "inner join satu_sehat_mapping_vaksin on satu_sehat_mapping_vaksin.kode_brng=detail_pemberian_obat.kode_brng "
                    + "inner join aturan_pakai on aturan_pakai.tgl_perawatan=detail_pemberian_obat.tgl_perawatan and aturan_pakai.jam=detail_pemberian_obat.jam and "
                    + "aturan_pakai.no_rawat=detail_pemberian_obat.no_rawat and aturan_pakai.kode_brng=detail_pemberian_obat.kode_brng "
                    + "inner join satu_sehat_mapping_lokasi_ralan on satu_sehat_mapping_lokasi_ralan.kd_poli=reg_periksa.kd_poli "
                    + "inner join poliklinik on poliklinik.kd_poli=satu_sehat_mapping_lokasi_ralan.kd_poli "
                    + "inner join pegawai on reg_periksa.kd_dokter=pegawai.nik "
                    + "inner join nota_inap on nota_inap.no_rawat=reg_periksa.no_rawat "
                    + "left join satu_sehat_immunization on satu_sehat_immunization.no_rawat=detail_pemberian_obat.no_rawat and satu_sehat_immunization.tgl_perawatan=detail_pemberian_obat.tgl_perawatan and "
                    + "satu_sehat_immunization.jam=detail_pemberian_obat.jam and satu_sehat_immunization.kode_brng=detail_pemberian_obat.kode_brng and "
                    + "satu_sehat_immunization.no_batch=detail_pemberian_obat.no_batch and satu_sehat_immunization.no_faktur=detail_pemberian_obat.no_faktur "
                    + "where detail_pemberian_obat.no_batch<>'' and nota_inap.tanggal between ? and ? ");
            try {
                ps.setString(1, Tanggal1.getText());
                ps.setString(2, Tanggal2.getText());
                rs = ps.executeQuery();
                while (rs.next()) {
                    if ((!rs.getString("no_ktp").equals("")) && (!rs.getString("ktppraktisi").equals("")) && rs.getString("id_immunization").equals("")) {
                        try {
                            idpasien = cekViaSatuSehat.tampilIDPasien(rs.getString("no_ktp"));
                            iddokter = cekViaSatuSehat.tampilIDParktisi(rs.getString("ktppraktisi"));
                            try {
                                headers = new HttpHeaders();
                                headers.setContentType(MediaType.APPLICATION_JSON);
                                headers.add("Authorization", "Bearer " + api.TokenSatuSehat());
                                json = "{"
                                        + "\"resourceType\": \"Immunization\","
                                        + "\"status\": \"completed\","
                                        + "\"vaccineCode\": {"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"system\": \"" + rs.getString("vaksin_system") + "\","
                                        + "\"code\": \"" + rs.getString("vaksin_code") + "\","
                                        + "\"display\": \"" + rs.getString("vaksin_display") + "\""
                                        + "}"
                                        + "]"
                                        + "},"
                                        + "\"patient\": {"
                                        + "\"reference\": \"Patient/" + idpasien + "\""
                                        + "},"
                                        + "\"encounter\": {"
                                        + "\"reference\": \"Encounter/" + rs.getString("id_encounter") + "\""
                                        + "},"
                                        + "\"occurrenceDateTime\": \"" + rs.getString("tgl_perawatan") + "T" + rs.getString("jam") + "+07:00" + "\","
                                        + "\"recorded\": \"" + rs.getString("tgl_perawatan") + "T" + rs.getString("jam") + "+07:00" + "\","
                                        + "\"primarySource\": true,"
                                        + "\"location\": {"
                                        + "\"reference\": \"Location/" + rs.getString("id_lokasi_satusehat") + "\","
                                        + "\"display\": \"" + rs.getString("nm_poli") + "\""
                                        + "},"
                                        + "\"lotNumber\": \"" + rs.getString("no_batch") + "\","
                                        + "\"route\": {"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"system\": \"" + rs.getString("route_system") + "\","
                                        + "\"code\": \"" + rs.getString("route_code") + "\","
                                        + "\"display\": \"" + rs.getString("route_display") + "\""
                                        + "}"
                                        + "]"
                                        + "},"
                                        + "\"doseQuantity\": {"
                                        + "\"value\": " + rs.getString("jml") + ","
                                        + "\"unit\": \"" + rs.getString("dose_quantity_unit") + "\","
                                        + "\"system\": \"" + rs.getString("dose_quantity_system") + "\","
                                        + "\"code\": \"" + rs.getString("dose_quantity_code") + "\""
                                        + "},"
                                        + "\"performer\": ["
                                        + "{"
                                        + "\"function\": {"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"system\": \"http://terminology.hl7.org/CodeSystem/v2-0443\","
                                        + "\"code\": \"AP\","
                                        + "\"display\": \"Administering Provider\""
                                        + "}"
                                        + "]"
                                        + "},"
                                        + "\"actor\": {"
                                        + "\"reference\": \"Practitioner/" + iddokter + "\""
                                        + "}"
                                        + "}"
                                        + "],"
                                        + "\"reasonCode\": ["
                                        + "{"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"system\": \"https://terminology.kemkes.go.id/CodeSystem/immunization-reason\","
                                        + "\"code\": \"IM-Program\","
                                        + "\"display\" : \"Imunisasi Program\""
                                        + "}"
                                        + "]"
                                        + "}"
                                        + "],"
                                        + "\"protocolApplied\" : ["
                                        + "{"
                                        + "\"doseNumberPositiveInt\" : " + rs.getString("aturan").toLowerCase().replaceAll("dosis", "").replaceAll(" ", "")
                                        + "}"
                                        + "]"
                                        + "}";
                                TeksArea.append("URL : " + link + "/Immunization" + "\n");
                                TeksArea.append("Request JSON : " + json + "\n");
                                requestEntity = new HttpEntity(json, headers);
                                json = api.getRest().exchange(link + "/Immunization", HttpMethod.POST, requestEntity, String.class).getBody();
                                TeksArea.append("Result JSON : " + json + "\n");
                                root = mapper.readTree(json);
                                response = root.path("id");
                                if (!response.asText().equals("")) {
                                    Sequel.menyimpan2("satu_sehat_immunization", "?,?,?,?,?,?,?", "Imunisasi/Vaksin", 7, new String[]{
                                        rs.getString("no_rawat"), rs.getString("tgl_perawatan"), rs.getString("jam"), rs.getString("kode_brng"), rs.getString("no_batch"), rs.getString("no_faktur"), response.asText()
                                    });
                                }
                            } catch (Exception e) {
                                System.out.println("Notifikasi Bridging : " + e);
                            }
                        } catch (Exception e) {
                            System.out.println("Notifikasi : " + e);
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Notif : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
    }

    public void prosedur() {
        try {
            ps = koneksi.prepareStatement(
                    "select reg_periksa.tgl_registrasi,reg_periksa.jam_reg,reg_periksa.no_rawat,pasien.nm_pasien,pasien.no_ktp,reg_periksa.status_lanjut,"
                    + "concat(nota_jalan.tanggal,'T',nota_jalan.jam,'+07:00') as pulang,satu_sehat_encounter.id_encounter,prosedur_pasien.kode,icd9.deskripsi_panjang,"
                    + "ifnull(satu_sehat_procedure.id_procedure,'') as id_procedure from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "
                    + "inner join nota_jalan on nota_jalan.no_rawat=reg_periksa.no_rawat inner join satu_sehat_encounter on satu_sehat_encounter.no_rawat=reg_periksa.no_rawat "
                    + "inner join prosedur_pasien on prosedur_pasien.no_rawat=reg_periksa.no_rawat inner join icd9 on prosedur_pasien.kode=icd9.kode "
                    + "left join satu_sehat_procedure on satu_sehat_procedure.no_rawat=prosedur_pasien.no_rawat and satu_sehat_procedure.kode=prosedur_pasien.kode "
                    + "and satu_sehat_procedure.status=prosedur_pasien.status where nota_jalan.tanggal between ? and ? ");
            try {
                ps.setString(1, Tanggal1.getText());
                ps.setString(2, Tanggal2.getText());
                rs = ps.executeQuery();
                while (rs.next()) {
                    if ((!rs.getString("no_ktp").equals("")) && rs.getString("id_procedure").equals("")) {
                        try {
                            idpasien = cekViaSatuSehat.tampilIDPasien(rs.getString("no_ktp"));
                            try {
                                headers = new HttpHeaders();
                                headers.setContentType(MediaType.APPLICATION_JSON);
                                headers.add("Authorization", "Bearer " + api.TokenSatuSehat());
                                json = "{"
                                        + "\"resourceType\": \"Procedure\","
                                        + "\"status\": \"completed\","
                                        + "\"category\": {"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"system\": \"http://snomed.info/sct\","
                                        + "\"code\": \"103693007\","
                                        + "\"display\": \"Diagnostic procedure\""
                                        + "}"
                                        + "],"
                                        + "\"text\":\"Diagnostic procedure\""
                                        + "},"
                                        + "\"code\": {"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"system\": \"http://hl7.org/fhir/sid/icd-9-cm\","
                                        + "\"code\": \"" + rs.getString("kode") + "\","
                                        + "\"display\": \"" + rs.getString("deskripsi_panjang") + "\""
                                        + "}"
                                        + "]"
                                        + "},"
                                        + "\"subject\": {"
                                        + "\"reference\": \"Patient/" + idpasien + "\","
                                        + "\"display\": \"" + rs.getString("nm_pasien") + "\""
                                        + "},"
                                        + "\"encounter\": {"
                                        + "\"reference\": \"Encounter/" + rs.getString("id_encounter") + "\","
                                        + "\"display\": \"Prosedur kepada " + rs.getString("nm_pasien") + " selama kunjungan/dirawat dari tanggal " + rs.getString("tgl_registrasi") + "T" + rs.getString("jam_reg") + "+07:00" + " sampai " + rs.getString("pulang") + "\""
                                        + "},"
                                        + "\"performedPeriod\": {"
                                        + "\"start\": \"" + rs.getString("tgl_registrasi") + "T" + rs.getString("jam_reg") + "+07:00" + "\","
                                        + "\"end\": \"" + rs.getString("pulang") + "\""
                                        + "}"
                                        + "}";
                                TeksArea.append("URL : " + link + "/Procedure" + "\n");
                                TeksArea.append("Request JSON : " + json + "\n");
                                requestEntity = new HttpEntity(json, headers);
                                json = api.getRest().exchange(link + "/Procedure", HttpMethod.POST, requestEntity, String.class).getBody();
                                TeksArea.append("Result JSON : " + json + "\n");
                                root = mapper.readTree(json);
                                response = root.path("id");
                                if (!response.asText().equals("")) {
                                    Sequel.menyimpan2("satu_sehat_procedure", "?,?,?,?", "Prosedur", 4, new String[]{
                                        rs.getString("no_rawat"), rs.getString("kode"), rs.getString("status_lanjut"), response.asText()
                                    });
                                }
                            } catch (Exception e) {
                                System.out.println("Notifikasi Bridging : " + e);
                            }
                        } catch (Exception e) {
                            System.out.println("Notifikasi : " + e);
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Notif : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }

            ps = koneksi.prepareStatement(
                    "select reg_periksa.tgl_registrasi,reg_periksa.jam_reg,reg_periksa.no_rawat,pasien.nm_pasien,pasien.no_ktp,reg_periksa.status_lanjut,"
                    + "concat(nota_inap.tanggal,'T',nota_inap.jam,'+07:00') as pulang,satu_sehat_encounter.id_encounter,prosedur_pasien.kode,icd9.deskripsi_panjang,"
                    + "ifnull(satu_sehat_procedure.id_procedure,'') as id_procedure from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "
                    + "inner join nota_inap on nota_inap.no_rawat=reg_periksa.no_rawat inner join satu_sehat_encounter on satu_sehat_encounter.no_rawat=reg_periksa.no_rawat "
                    + "inner join prosedur_pasien on prosedur_pasien.no_rawat=reg_periksa.no_rawat inner join icd9 on prosedur_pasien.kode=icd9.kode "
                    + "left join satu_sehat_procedure on satu_sehat_procedure.no_rawat=prosedur_pasien.no_rawat and satu_sehat_procedure.kode=prosedur_pasien.kode "
                    + "and satu_sehat_procedure.status=prosedur_pasien.status where nota_inap.tanggal between ? and ? ");
            try {
                ps.setString(1, Tanggal1.getText());
                ps.setString(2, Tanggal2.getText());
                rs = ps.executeQuery();
                while (rs.next()) {
                    if ((!rs.getString("no_ktp").equals("")) && rs.getString("id_procedure").equals("")) {
                        try {
                            idpasien = cekViaSatuSehat.tampilIDPasien(rs.getString("no_ktp"));
                            try {
                                headers = new HttpHeaders();
                                headers.setContentType(MediaType.APPLICATION_JSON);
                                headers.add("Authorization", "Bearer " + api.TokenSatuSehat());
                                json = "{"
                                        + "\"resourceType\": \"Procedure\","
                                        + "\"status\": \"completed\","
                                        + "\"category\": {"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"system\": \"http://snomed.info/sct\","
                                        + "\"code\": \"103693007\","
                                        + "\"display\": \"Diagnostic procedure\""
                                        + "}"
                                        + "],"
                                        + "\"text\":\"Diagnostic procedure\""
                                        + "},"
                                        + "\"code\": {"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"system\": \"http://hl7.org/fhir/sid/icd-9-cm\","
                                        + "\"code\": \"" + rs.getString("kode") + "\","
                                        + "\"display\": \"" + rs.getString("deskripsi_panjang") + "\""
                                        + "}"
                                        + "]"
                                        + "},"
                                        + "\"subject\": {"
                                        + "\"reference\": \"Patient/" + idpasien + "\","
                                        + "\"display\": \"" + rs.getString("nm_pasien") + "\""
                                        + "},"
                                        + "\"encounter\": {"
                                        + "\"reference\": \"Encounter/" + rs.getString("id_encounter") + "\","
                                        + "\"display\": \"Prosedur kepada " + rs.getString("nm_pasien") + " selama kunjungan/dirawat dari tanggal " + rs.getString("tgl_registrasi") + "T" + rs.getString("jam_reg") + "+07:00" + " sampai " + rs.getString("pulang") + "\""
                                        + "},"
                                        + "\"performedPeriod\": {"
                                        + "\"start\": \"" + rs.getString("tgl_registrasi") + "T" + rs.getString("jam_reg") + "+07:00" + "\","
                                        + "\"end\": \"" + rs.getString("pulang") + "\""
                                        + "}"
                                        + "}";
                                TeksArea.append("URL : " + link + "/Procedure" + "\n");
                                TeksArea.append("Request JSON : " + json + "\n");
                                requestEntity = new HttpEntity(json, headers);
                                json = api.getRest().exchange(link + "/Procedure", HttpMethod.POST, requestEntity, String.class).getBody();
                                TeksArea.append("Result JSON : " + json + "\n");
                                root = mapper.readTree(json);
                                response = root.path("id");
                                if (!response.asText().equals("")) {
                                    Sequel.menyimpan2("satu_sehat_procedure", "?,?,?,?", "Prosedur", 4, new String[]{
                                        rs.getString("no_rawat"), rs.getString("kode"), rs.getString("status_lanjut"), response.asText()
                                    });
                                }
                            } catch (Exception e) {
                                System.out.println("Notifikasi Bridging : " + e);
                            }
                        } catch (Exception e) {
                            System.out.println("Notifikasi : " + e);
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Notif : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
    }

    private void condition(String norawat) {
        try {
            ps = koneksi.prepareStatement(
                    "select reg_periksa.tgl_registrasi,reg_periksa.jam_reg,reg_periksa.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien,pasien.no_ktp,"
                    + "reg_periksa.stts,reg_periksa.status_lanjut,concat(nota_jalan.tanggal,'T',nota_jalan.jam) as pulang,satu_sehat_encounter.id_encounter, "
                    + "diagnosa_pasien.kd_penyakit,diagnosa_pasien.prioritas,penyakit.nm_penyakit,ifnull(satu_sehat_condition.id_condition,'') as id_condition "
                    + "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis left join nota_jalan on nota_jalan.no_rawat=reg_periksa.no_rawat "
                    + "inner join satu_sehat_encounter on satu_sehat_encounter.no_rawat=reg_periksa.no_rawat inner join diagnosa_pasien on diagnosa_pasien.no_rawat=reg_periksa.no_rawat "
                    + "inner join penyakit on diagnosa_pasien.kd_penyakit=penyakit.kd_penyakit left join satu_sehat_condition on satu_sehat_condition.no_rawat=diagnosa_pasien.no_rawat "
                    + "and satu_sehat_condition.kd_penyakit=diagnosa_pasien.kd_penyakit and satu_sehat_condition.status=diagnosa_pasien.status "
                    + "where reg_periksa.no_rawat = ?");
            try {
                ps.setString(1, norawat);

                rs = ps.executeQuery();
                while (rs.next()) {
                    try {
                        idpasien = cekViaSatuSehat.tampilIDPasien(rs.getString("no_ktp"));
                        try {

                            headers = new HttpHeaders();
                            headers.setContentType(MediaType.APPLICATION_JSON);
                            headers.add("Authorization", "Bearer " + api.TokenSatuSehat());
                            json = "{"
                                    + "\"resourceType\": \"Condition\","
                                    + "\"clinicalStatus\": {"
                                    + "\"coding\": ["
                                    + "{"
                                    + "\"system\": \"http://terminology.hl7.org/CodeSystem/condition-clinical\","
                                    + "\"code\": \"active\","
                                    + "\"display\": \"Active\""
                                    + "}"
                                    + "]"
                                    + "},"
                                    + "\"category\": ["
                                    + "{"
                                    + "\"coding\": ["
                                    + "{"
                                    + "\"system\": \"http://terminology.hl7.org/CodeSystem/condition-category\","
                                    + "\"code\": \"encounter-diagnosis\","
                                    + "\"display\": \"Encounter Diagnosis\""
                                    + "}"
                                    + "]"
                                    + "}"
                                    + "],"
                                    + "\"code\": {"
                                    + "\"coding\": ["
                                    + "{"
                                    + "\"system\": \"http://hl7.org/fhir/sid/icd-10\","
                                    + "\"code\": \"" + rs.getString("kd_penyakit") + "\","
                                    + "\"display\": \"" + rs.getString("nm_penyakit") + "\""
                                    + "}"
                                    + "]"
                                    + "},"
                                    + "\"subject\": {"
                                    + "\"reference\": \"Patient/" + idpasien + "\","
                                    + "\"display\": \"" + rs.getString("nm_pasien") + "\""
                                    + "},"
                                    + "\"encounter\": {"
                                    + "\"reference\": \"Encounter/" + rs.getString("id_encounter") + "\","
                                    + "\"display\": \"Diagnosa " + rs.getString("nm_pasien") + " selama kunjungan/dirawat dari tanggal " + rs.getString("tgl_registrasi") + ":" + rs.getString("jam_reg") + " sampai " + rs.getString("pulang") + "\""
                                    + "}"
                                    + "}";
                            System.out.println("URL : " + link + "/Condition");
                            System.out.println("Request JSON : " + json);
                            requestEntity = new HttpEntity(json, headers);
                            json = api.getRest().exchange(link + "/Condition", HttpMethod.POST, requestEntity, String.class).getBody();
                            System.out.println("Result JSON : " + json);
                            root = mapper.readTree(json);
                            response = root.path("id");
                            if (!response.asText().equals("")) {
                                Sequel.menyimpan("satu_sehat_condition", "?,?,?,?", "Diagnosa", 4, new String[]{
                                    rs.getString("no_rawat"), rs.getString("kd_penyakit"), rs.getString("status_lanjut"), response.asText()
                                });

                                String kodedokter = Sequel.cariIsi("select kd_dokter from reg_periksa where no_rawat='" + rs.getString("no_rawat") + "'");
                                String kodepoString = Sequel.cariIsi("select kd_poli from reg_periksa where no_rawat='" + rs.getString("no_rawat") + "'");
                                String idpoliSatusehat = Sequel.cariIsi("select id_lokasi_satusehat from satu_sehat_mapping_lokasi_ralan where kd_poli='" + kodepoString + "'");
                                String namapoli = Sequel.cariIsi("select nm_poli from poliklinik where kd_poli='" + kodepoString + "'");
                                String nikdokter = Sequel.cariIsi("select no_ktp from pegawai where nik='" + kodedokter + "'");
                                String nama_dokter = Sequel.cariIsi("select nama from pegawai where nik='" + kodedokter + "'");
                                iddokter = cekViaSatuSehat.tampilIDParktisi(nikdokter);

                                try {
                                    //UPDATE ENCOPUNTER TO FINISHED
                                    headers = new HttpHeaders();
                                    headers.setContentType(MediaType.APPLICATION_JSON);
                                    headers.add("Authorization", "Bearer " + api.TokenSatuSehat());
                                    json = "{\n"
                                            + "  \"resourceType\": \"Encounter\",\n"
                                            + "  \"id\": \"" + rs.getString("id_encounter") + "\",\n"
                                            + "  \"identifier\": [\n"
                                            + "    {\n"
                                            + "      \"system\": \"http://sys-ids.kemkes.go.id/encounter/" + koneksiDB.IDSATUSEHAT() + "\",\n"
                                            + "      \"value\": \"" + rs.getString("no_rawat").replaceAll("/", "") + "\"\n"
                                            + "    }\n"
                                            + "  ],\n"
                                            + "  \"status\": \"finished\",\n"
                                            + "  \"class\": {\n"
                                            + "    \"system\": \"http://terminology.hl7.org/CodeSystem/v3-ActCode\",\n"
                                            + "    \"code\": \"AMB\",\n"
                                            + "    \"display\": \"ambulatory\"\n"
                                            + "  },\n"
                                            + "  \"subject\": {\n"
                                            + "    \"reference\": \"Patient/" + idpasien + "\",\n"
                                            + "    \"display\": \"" + rs.getString("nm_pasien") + "\"\n"
                                            + "  },\n"
                                            + "  \"participant\": [\n"
                                            + "    {\n"
                                            + "      \"type\": [\n"
                                            + "        {\n"
                                            + "          \"coding\": [\n"
                                            + "            {\n"
                                            + "              \"system\": \"http://terminology.hl7.org/CodeSystem/v3-ParticipationType\",\n"
                                            + "              \"code\": \"ATND\",\n"
                                            + "              \"display\": \"attender\"\n"
                                            + "            }\n"
                                            + "          ]\n"
                                            + "        }\n"
                                            + "      ],\n"
                                            + "      \"individual\": {\n"
                                            + "        \"reference\": \"Practitioner/" + iddokter + "\",\n"
                                            + "        \"display\": \"" + nama_dokter + "\"\n"
                                            + "      }\n"
                                            + "    }\n"
                                            + "  ],\n"
                                            + "  \"period\": {\n"
                                            + "    \"start\": \"" + rs.getString("tgl_registrasi") + "T" + rs.getString("jam_reg") + "+07:00\",\n"
                                            + "    \"end\": \"" + (rs.getString("pulang") == null ? Sequel.cariIsi("select current_date()") + "T" + Sequel.cariIsi("select current_time()") : rs.getString("pulang")) + "+07:00\"\n"
                                            + "  },\n"
                                            + "  \"location\": [\n"
                                            + "    {\n"
                                            + "      \"location\": {\n"
                                            + "        \"reference\": \"Location/" + idpoliSatusehat + "\",\n"
                                            + "        \"display\": \"" + namapoli + "\"\n"
                                            + "      }\n"
                                            + "    }\n"
                                            + "  ],\n"
                                            + "  \"diagnosis\": [\n"
                                            + "    {\n"
                                            + "      \"condition\": {\n"
                                            + "        \"reference\": \"Condition/" + response.asText() + "\",\n"
                                            + "        \"display\": \"" + rs.getString("nm_penyakit") + "\"\n"
                                            + "      },\n"
                                            + "      \"use\": {\n"
                                            + "        \"coding\": [\n"
                                            + "          {\n"
                                            + "            \"system\": \"http://terminology.hl7.org/CodeSystem/diagnosis-role\",\n"
                                            + "            \"code\": \"DD\",\n"
                                            + "            \"display\": \"Discharge diagnosis\"\n"
                                            + "          }\n"
                                            + "        ]\n"
                                            + "      },\n"
                                            + "      \"rank\": " + rs.getString("prioritas") + "\n"
                                            + "    }\n"
                                            + "  ],\n"
                                            + "  \"statusHistory\": [\n"
                                            + "    {\n"
                                            + "      \"status\": \"finished\",\n"
                                            + "      \"period\": {\n"
                                            + "                 \"start\": \"" + rs.getString("tgl_registrasi") + "T" + rs.getString("jam_reg") + "+07:00\",\n"
                                            + "                 \"end\": \"" + (rs.getString("pulang") == null ? Sequel.cariIsi("select current_date()") + "T" + Sequel.cariIsi("select current_time()") : rs.getString("pulang")) + "+07:00\"\n"
                                            + "      }\n"
                                            + "    }\n"
                                            + "  ],\n"
                                            + "  \"serviceProvider\": {\n"
                                            + "    \"reference\":\"Organization/" + koneksiDB.IDSATUSEHAT() + "\"\n"
                                            + "  }\n"
                                            + "}";
                                    System.out.println("URL : " + link + "/Encounter");
                                    System.out.println("Request JSON : " + json);
                                    requestEntity = new HttpEntity(json, headers);
                                    System.out.println(requestEntity.toString());
                                    json = api.getRest().exchange(link + "/Encounter/" + rs.getString("id_encounter"), HttpMethod.PUT, requestEntity, String.class).getBody();
                                    root = mapper.readTree(json);
                                    System.out.println(root.asText());
                                    System.out.println("Result UPDATE ENCOUNTER : " + json);

                                } catch (HttpClientErrorException | HttpServerErrorException e) {
                                    // Handle client and server errors
                                    System.err.println("Error Response Status Code: " + e.getStatusCode());
//                            System.err.println("Error Response Body: " + e.getResponseBodyAsString());

                                    // You can further parse the error response body if needed
                                    ObjectMapper mapper = new ObjectMapper();
                                    JsonNode errorResponse = mapper.readTree(e.getResponseBodyAsString());
                                    ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
                                    String prettyErrorResponse = writer.writeValueAsString(errorResponse);
                                    System.err.println("Error Response JSON: \n" + prettyErrorResponse);
                                }

                            }
                        } catch (HttpClientErrorException | HttpServerErrorException e) {
                            // Handle client and server errors
                            System.err.println("Error Response Status Code: " + e.getStatusCode());
//                            System.err.println("Error Response Body: " + e.getResponseBodyAsString());

                            // You can further parse the error response body if needed
                            ObjectMapper mapper = new ObjectMapper();
                            JsonNode errorResponse = mapper.readTree(e.getResponseBodyAsString());
                            ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
                            String prettyErrorResponse = writer.writeValueAsString(errorResponse);
                            System.err.println("Error Response JSON: \n" + prettyErrorResponse);
                        }
                    } catch (Exception e) {
                        System.out.println("Notifikasi : " + e);
                    }
                }
            } catch (Exception e) {
                System.out.println("Notif : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }

            ps = koneksi.prepareStatement(
                    "select reg_periksa.tgl_registrasi,reg_periksa.jam_reg,reg_periksa.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien,pasien.no_ktp,"
                    + "reg_periksa.stts,reg_periksa.status_lanjut,concat(nota_inap.tanggal,'T',nota_inap.jam) as pulang,satu_sehat_encounter.id_encounter, "
                    + "diagnosa_pasien.kd_penyakit,diagnosa_pasien.prioritas,penyakit.nm_penyakit,ifnull(satu_sehat_condition.id_condition,'') as id_condition "
                    + "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis left join nota_inap on nota_inap.no_rawat=reg_periksa.no_rawat "
                    + "inner join satu_sehat_encounter on satu_sehat_encounter.no_rawat=reg_periksa.no_rawat inner join diagnosa_pasien on diagnosa_pasien.no_rawat=reg_periksa.no_rawat "
                    + "inner join penyakit on diagnosa_pasien.kd_penyakit=penyakit.kd_penyakit left join satu_sehat_condition on satu_sehat_condition.no_rawat=diagnosa_pasien.no_rawat "
                    + "and satu_sehat_condition.kd_penyakit=diagnosa_pasien.kd_penyakit and satu_sehat_condition.status=diagnosa_pasien.status "
                    + "where reg_periksa.no_rawat = ?");
            try {
                ps.setString(1, norawat);

                rs = ps.executeQuery();
                while (rs.next()) {
                    try {
                        idpasien = cekViaSatuSehat.tampilIDPasien(rs.getString("no_ktp"));
                        try {

                            headers = new HttpHeaders();
                            headers.setContentType(MediaType.APPLICATION_JSON);
                            headers.add("Authorization", "Bearer " + api.TokenSatuSehat());
                            json = "{"
                                    + "\"resourceType\": \"Condition\","
                                    + "\"clinicalStatus\": {"
                                    + "\"coding\": ["
                                    + "{"
                                    + "\"system\": \"http://terminology.hl7.org/CodeSystem/condition-clinical\","
                                    + "\"code\": \"active\","
                                    + "\"display\": \"Active\""
                                    + "}"
                                    + "]"
                                    + "},"
                                    + "\"category\": ["
                                    + "{"
                                    + "\"coding\": ["
                                    + "{"
                                    + "\"system\": \"http://terminology.hl7.org/CodeSystem/condition-category\","
                                    + "\"code\": \"encounter-diagnosis\","
                                    + "\"display\": \"Encounter Diagnosis\""
                                    + "}"
                                    + "]"
                                    + "}"
                                    + "],"
                                    + "\"code\": {"
                                    + "\"coding\": ["
                                    + "{"
                                    + "\"system\": \"http://hl7.org/fhir/sid/icd-10\","
                                    + "\"code\": \"" + rs.getString("kd_penyakit") + "\","
                                    + "\"display\": \"" + rs.getString("nm_penyakit") + "\""
                                    + "}"
                                    + "]"
                                    + "},"
                                    + "\"subject\": {"
                                    + "\"reference\": \"Patient/" + idpasien + "\","
                                    + "\"display\": \"" + rs.getString("nm_pasien") + "\""
                                    + "},"
                                    + "\"encounter\": {"
                                    + "\"reference\": \"Encounter/" + rs.getString("id_encounter") + "\","
                                    + "\"display\": \"Diagnosa " + rs.getString("nm_pasien") + " selama kunjungan/dirawat dari tanggal " + rs.getString("tgl_registrasi") + ":" + rs.getString("jam_reg") + " sampai " + rs.getString("pulang") + "\""
                                    + "}"
                                    + "}";
                            System.out.println("URL : " + link + "/Condition");
                            System.out.println("Request JSON : " + json);
                            requestEntity = new HttpEntity(json, headers);
                            json = api.getRest().exchange(link + "/Condition", HttpMethod.POST, requestEntity, String.class).getBody();
                            System.out.println("Result JSON : " + json);
                            root = mapper.readTree(json);
                            response = root.path("id");
                            if (!response.asText().equals("")) {
                                Sequel.menyimpan("satu_sehat_condition", "?,?,?,?", "Diagnosa", 4, new String[]{
                                    rs.getString("no_rawat"), rs.getString("kd_penyakit"), rs.getString("status_lanjut"), response.asText()
                                });

                                String kodedokter = Sequel.cariIsi("select kd_dokter from reg_periksa where no_rawat='" + rs.getString("no_rawat") + "'");
                                String kodepoString = Sequel.cariIsi("select kd_poli from reg_periksa where no_rawat='" + rs.getString("no_rawat") + "'");
                                String idpoliSatusehat = Sequel.cariIsi("select id_lokasi_satusehat from satu_sehat_mapping_lokasi_ralan where kd_poli='" + kodepoString + "'");
                                String namapoli = Sequel.cariIsi("select nm_poli from poliklinik where kd_poli='" + kodepoString + "'");
                                String nikdokter = Sequel.cariIsi("select no_ktp from pegawai where nik='" + kodedokter + "'");
                                String nama_dokter = Sequel.cariIsi("select nama from pegawai where nik='" + kodedokter + "'");
                                iddokter = cekViaSatuSehat.tampilIDParktisi(nikdokter);

                                try {
                                    //UPDATE ENCOPUNTER TO FINISHED
                                    headers = new HttpHeaders();
                                    headers.setContentType(MediaType.APPLICATION_JSON);
                                    headers.add("Authorization", "Bearer " + api.TokenSatuSehat());
                                    json = "{\n"
                                            + "  \"resourceType\": \"Encounter\",\n"
                                            + "  \"id\": \"" + rs.getString("id_encounter") + "\",\n"
                                            + "  \"identifier\": [\n"
                                            + "    {\n"
                                            + "      \"system\": \"http://sys-ids.kemkes.go.id/encounter/" + koneksiDB.IDSATUSEHAT() + "\",\n"
                                            + "      \"value\": \"" + rs.getString("no_rawat").replaceAll("/", "") + "\"\n"
                                            + "    }\n"
                                            + "  ],\n"
                                            + "  \"status\": \"finished\",\n"
                                            + "  \"class\": {\n"
                                            + "    \"system\": \"http://terminology.hl7.org/CodeSystem/v3-ActCode\",\n"
                                            + "    \"code\": \"AMB\",\n"
                                            + "    \"display\": \"ambulatory\"\n"
                                            + "  },\n"
                                            + "  \"subject\": {\n"
                                            + "    \"reference\": \"Patient/" + idpasien + "\",\n"
                                            + "    \"display\": \"" + rs.getString("nm_pasien") + "\"\n"
                                            + "  },\n"
                                            + "  \"participant\": [\n"
                                            + "    {\n"
                                            + "      \"type\": [\n"
                                            + "        {\n"
                                            + "          \"coding\": [\n"
                                            + "            {\n"
                                            + "              \"system\": \"http://terminology.hl7.org/CodeSystem/v3-ParticipationType\",\n"
                                            + "              \"code\": \"ATND\",\n"
                                            + "              \"display\": \"attender\"\n"
                                            + "            }\n"
                                            + "          ]\n"
                                            + "        }\n"
                                            + "      ],\n"
                                            + "      \"individual\": {\n"
                                            + "        \"reference\": \"Practitioner/" + iddokter + "\",\n"
                                            + "        \"display\": \"" + nama_dokter + "\"\n"
                                            + "      }\n"
                                            + "    }\n"
                                            + "  ],\n"
                                            + "  \"period\": {\n"
                                            + "    \"start\": \"" + rs.getString("tgl_registrasi") + "T" + rs.getString("jam_reg") + "+07:00\",\n"
                                            + "    \"end\": \"" + (rs.getString("pulang") == null ? Sequel.cariIsi("select current_date()") + "T" + Sequel.cariIsi("select current_time()") : rs.getString("pulang")) + "+07:00\"\n"
                                            + "  },\n"
                                            + "  \"location\": [\n"
                                            + "    {\n"
                                            + "      \"location\": {\n"
                                            + "        \"reference\": \"Location/" + idpoliSatusehat + "\",\n"
                                            + "        \"display\": \"" + namapoli + "\"\n"
                                            + "      }\n"
                                            + "    }\n"
                                            + "  ],\n"
                                            + "  \"diagnosis\": [\n"
                                            + "    {\n"
                                            + "      \"condition\": {\n"
                                            + "        \"reference\": \"Condition/" + response.asText() + "\",\n"
                                            + "        \"display\": \"" + rs.getString("nm_penyakit") + "\"\n"
                                            + "      },\n"
                                            + "      \"use\": {\n"
                                            + "        \"coding\": [\n"
                                            + "          {\n"
                                            + "            \"system\": \"http://terminology.hl7.org/CodeSystem/diagnosis-role\",\n"
                                            + "            \"code\": \"DD\",\n"
                                            + "            \"display\": \"Discharge diagnosis\"\n"
                                            + "          }\n"
                                            + "        ]\n"
                                            + "      },\n"
                                            + "      \"rank\": " + rs.getString("prioritas") + "\n"
                                            + "    }\n"
                                            + "  ],\n"
                                            + "  \"statusHistory\": [\n"
                                            + "    {\n"
                                            + "      \"status\": \"finished\",\n"
                                            + "      \"period\": {\n"
                                            + "                 \"start\": \"" + rs.getString("tgl_registrasi") + "T" + rs.getString("jam_reg") + "+07:00\",\n"
                                            + "                 \"end\": \"" + (rs.getString("pulang") == null ? Sequel.cariIsi("select current_date()") + "T" + Sequel.cariIsi("select current_time()") : rs.getString("pulang")) + "+07:00\"\n"
                                            + "      }\n"
                                            + "    }\n"
                                            + "  ],\n"
                                            + "  \"serviceProvider\": {\n"
                                            + "    \"reference\":\"Organization/" + koneksiDB.IDSATUSEHAT() + "\"\n"
                                            + "  }\n"
                                            + "}";
                                    System.out.println("URL : " + link + "/Encounter");
                                    System.out.println("Request JSON : " + json);
                                    requestEntity = new HttpEntity(json, headers);
                                    System.out.println(requestEntity.toString());
                                    json = api.getRest().exchange(link + "/Encounter/" + rs.getString("id_encounter"), HttpMethod.PUT, requestEntity, String.class).getBody();
                                    root = mapper.readTree(json);
                                    System.out.println(root.asText());
                                    System.out.println("Result UPDATE ENCOUNTER : " + json);

                                } catch (HttpClientErrorException | HttpServerErrorException e) {
                                    // Handle client and server errors
                                    System.err.println("Error Response Status Code: " + e.getStatusCode());
//                            System.err.println("Error Response Body: " + e.getResponseBodyAsString());

                                    // You can further parse the error response body if needed
                                    ObjectMapper mapper = new ObjectMapper();
                                    JsonNode errorResponse = mapper.readTree(e.getResponseBodyAsString());
                                    ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
                                    String prettyErrorResponse = writer.writeValueAsString(errorResponse);
                                    System.err.println("Error Response JSON: \n" + prettyErrorResponse);
                                }

                            }
                        } catch (HttpClientErrorException | HttpServerErrorException e) {
                            // Handle client and server errors
                            System.err.println("Error Response Status Code: " + e.getStatusCode());
//                            System.err.println("Error Response Body: " + e.getResponseBodyAsString());

                            // You can further parse the error response body if needed
                            ObjectMapper mapper = new ObjectMapper();
                            JsonNode errorResponse = mapper.readTree(e.getResponseBodyAsString());
                            ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
                            String prettyErrorResponse = writer.writeValueAsString(errorResponse);
                            System.err.println("Error Response JSON: \n" + prettyErrorResponse);
                        }
                    } catch (Exception e) {
                        System.out.println("Notifikasi : " + e);
                    }
                }
            } catch (Exception e) {
                System.out.println("Notif : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
    }

    private void dietgizi(String norawat) {
        try {
            ps = koneksi.prepareStatement(
                    "select reg_periksa.tgl_registrasi,reg_periksa.jam_reg,reg_periksa.no_rawat,reg_periksa.no_rkm_medis,"
                    + "pasien.nm_pasien,pasien.no_ktp,satu_sehat_encounter.id_encounter,catatan_adime_gizi.instruksi,"
                    + "pegawai.nama,pegawai.no_ktp as ktppraktisi,CONCAT(DATE_FORMAT(catatan_adime_gizi.tanggal,'%Y-%m-%d'),'T',DATE_FORMAT(catatan_adime_gizi.tanggal,'%T'),'+07:00') AS tanggal,"
                    + "ifnull(satu_sehat_diet.id_diet,'') as satu_sehat_diet "
                    + "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "
                    + "left join nota_jalan on nota_jalan.no_rawat=reg_periksa.no_rawat "
                    + "inner join satu_sehat_encounter on satu_sehat_encounter.no_rawat=reg_periksa.no_rawat "
                    + "inner join catatan_adime_gizi on catatan_adime_gizi.no_rawat=reg_periksa.no_rawat "
                    + "inner join pegawai on catatan_adime_gizi.nip=pegawai.nik "
                    + "left join satu_sehat_diet on satu_sehat_diet.no_rawat=catatan_adime_gizi.no_rawat "
                    + "and satu_sehat_diet.tanggal=catatan_adime_gizi.tanggal "
                    + "where reg_periksa.no_rawat=?");
            try {
                ps.setString(1, norawat);

                rs = ps.executeQuery();
                while (rs.next()) {
                    try {
                        // Parse the input string into an OffsetDateTime
                        OffsetDateTime offsetDateTime = OffsetDateTime.parse(rs.getString("tanggal"));

                        // Define the desired output format
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                        // Format the OffsetDateTime using the specified pattern
                        String formattedDateTime = offsetDateTime.format(formatter);
                        iddokter = cekViaSatuSehat.tampilIDParktisi(rs.getString("ktppraktisi"));
                        idpasien = cekViaSatuSehat.tampilIDPasien(rs.getString("no_ktp"));
                        try {
                            headers = new HttpHeaders();
                            headers.setContentType(MediaType.APPLICATION_JSON);
                            headers.add("Authorization", "Bearer " + api.TokenSatuSehat());
                            json = "{"
                                    + "\"resourceType\" : \"Composition\" ,"
                                    + "\"identifier\" : {"
                                    + "\"system\" : \"http://sys-ids.kemkes.go.id/composition/" + koneksiDB.IDSATUSEHAT() + "\","
                                    + "\"value\" : \"" + rs.getString("no_rawat").replaceAll("/", "") + "\""
                                    + "},"
                                    + "\"status\" : \"final\","
                                    + "\"type\" : {"
                                    + "\"coding\" : ["
                                    + "{"
                                    + "\"system\" : \"http://loinc.org\" ,"
                                    + "\"code\" : \"18842-5\" ,"
                                    + "\"display\" : \"Discharge summary\""
                                    + "}"
                                    + "]"
                                    + "},"
                                    + "\"category\" : ["
                                    + "{"
                                    + "\"coding\" : ["
                                    + "{"
                                    + "\"system\" : \"http://loinc.org\" ,"
                                    + "\"code\" : \"LP173421-1\" ,"
                                    + "\"display\" : \"Report\""
                                    + "}"
                                    + "]"
                                    + "}"
                                    + "],"
                                    + "\"subject\" : {"
                                    + "\"reference\" : \"Patient/" + idpasien + "\" ,"
                                    + "\"display\" : \"" + rs.getString("nm_pasien") + "\""
                                    + "},"
                                    + "\"encounter\" : {"
                                    + "\"reference\" : \"Encounter/" + rs.getString("id_encounter") + "\","
                                    + "\"display\" : \"Kunjungan " + rs.getString("nm_pasien") + " pada tanggal " + rs.getString("tanggal") + " dengan nomor kunjungan " + rs.getString("no_rawat") + "\""
                                    + "},"
                                    + "\"date\" : \"" + rs.getString("tanggal") + "\" ,"
                                    + "\"author\" : ["
                                    + "{"
                                    + "\"reference\" : \"Practitioner/" + iddokter + "\" ,"
                                    + "\"display\" : \"" + rs.getString("nama") + "\""
                                    + "}"
                                    + "],"
                                    + "\"title\" : \"Modul Gizi\" ,"
                                    + "\"custodian\" : {"
                                    + "\"reference\" : \"Organization/" + koneksiDB.IDSATUSEHAT() + "\""
                                    + "},"
                                    + "\"section\" : ["
                                    + "{"
                                    + "\"code\" : {"
                                    + "\"coding\" : ["
                                    + "{"
                                    + "\"system\" : \"http://loinc.org\" ,"
                                    + "\"code\" : \"42344-2\" ,"
                                    + "\"display\" : \"Discharge diet (narrative)\""
                                    + "}"
                                    + "]"
                                    + "},"
                                    + "\"text\" : {"
                                    + "\"status\" : \"additional\" ,"
                                    + "\"div\" : \"" + rs.getString("instruksi").replaceAll("\n", " ") + "\""
                                    + "}"
                                    + "}"
                                    + "]"
                                    + "}";
                            System.out.println("URL : " + link + "/Composition");
                            System.out.println("Request JSON : " + json);
                            requestEntity = new HttpEntity(json, headers);
                            json = api.getRest().exchange(link + "/Composition", HttpMethod.POST, requestEntity, String.class).getBody();
                            System.out.println("Result JSON : " + json);
                            root = mapper.readTree(json);
                            response = root.path("id");
                            if (!response.asText().equals("")) {
                                Sequel.menyimpan("satu_sehat_diet", "?,?,?", "Diet/Gizi", 3, new String[]{
                                    rs.getString("no_rawat"), formattedDateTime, response.asText()
                                });
                            }
                        } catch (HttpClientErrorException | HttpServerErrorException e) {
                            // Handle client and server errors
                            System.err.println("Error Response Status Code: " + e.getStatusCode());
//                            System.err.println("Error Response Body: " + e.getResponseBodyAsString());
                            // You can further parse the error response body if needed
                            ObjectMapper mapper = new ObjectMapper();
                            JsonNode errorResponse = mapper.readTree(e.getResponseBodyAsString());
                            ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
                            String prettyErrorResponse = writer.writeValueAsString(errorResponse);
                            System.err.println("Error Response JSON: \n" + prettyErrorResponse);
                        }
                    } catch (Exception e) {
                        System.out.println("Notifikasi : " + e);
                    }
                }
            } catch (Exception e) {
                System.out.println("Notif : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }

            ps = koneksi.prepareStatement(
                    "select reg_periksa.tgl_registrasi,reg_periksa.jam_reg,reg_periksa.no_rawat,reg_periksa.no_rkm_medis,"
                    + "pasien.nm_pasien,pasien.no_ktp,satu_sehat_encounter.id_encounter,catatan_adime_gizi.instruksi,"
                    + "pegawai.nama,pegawai.no_ktp as ktppraktisi,CONCAT(DATE_FORMAT(catatan_adime_gizi.tanggal,'%Y-%m-%d'),'T',DATE_FORMAT(catatan_adime_gizi.tanggal,'%T'),'+07:00') AS tanggal,"
                    + "ifnull(satu_sehat_diet.id_diet,'') as satu_sehat_diet "
                    + "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "
                    + "left join nota_inap on nota_inap.no_rawat=reg_periksa.no_rawat "
                    + "inner join satu_sehat_encounter on satu_sehat_encounter.no_rawat=reg_periksa.no_rawat "
                    + "inner join catatan_adime_gizi on catatan_adime_gizi.no_rawat=reg_periksa.no_rawat "
                    + "inner join pegawai on catatan_adime_gizi.nip=pegawai.nik "
                    + "left join satu_sehat_diet on satu_sehat_diet.no_rawat=catatan_adime_gizi.no_rawat "
                    + "and satu_sehat_diet.tanggal=catatan_adime_gizi.tanggal "
                    + "where reg_periksa.no_rawat = ?");
            try {
                ps.setString(1, norawat);

                rs = ps.executeQuery();
                while (rs.next()) {
                    try {
                        // Parse the input string into an OffsetDateTime
                        OffsetDateTime offsetDateTime = OffsetDateTime.parse(rs.getString("tanggal"));

                        // Define the desired output format
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                        // Format the OffsetDateTime using the specified pattern
                        String formattedDateTime = offsetDateTime.format(formatter);
                        iddokter = cekViaSatuSehat.tampilIDParktisi(rs.getString("ktppraktisi"));
                        idpasien = cekViaSatuSehat.tampilIDPasien(rs.getString("no_ktp"));
                        try {
                            headers = new HttpHeaders();
                            headers.setContentType(MediaType.APPLICATION_JSON);
                            headers.add("Authorization", "Bearer " + api.TokenSatuSehat());
                            json = "{"
                                    + "\"resourceType\" : \"Composition\" ,"
                                    + "\"identifier\" : {"
                                    + "\"system\" : \"http://sys-ids.kemkes.go.id/composition/" + koneksiDB.IDSATUSEHAT() + "\","
                                    + "\"value\" : \"" + rs.getString("no_rawat").replaceAll("/", "") + "\""
                                    + "},"
                                    + "\"status\" : \"final\","
                                    + "\"type\" : {"
                                    + "\"coding\" : ["
                                    + "{"
                                    + "\"system\" : \"http://loinc.org\" ,"
                                    + "\"code\" : \"18842-5\" ,"
                                    + "\"display\" : \"Discharge summary\""
                                    + "}"
                                    + "]"
                                    + "},"
                                    + "\"category\" : ["
                                    + "{"
                                    + "\"coding\" : ["
                                    + "{"
                                    + "\"system\" : \"http://loinc.org\" ,"
                                    + "\"code\" : \"LP173421-1\" ,"
                                    + "\"display\" : \"Report\""
                                    + "}"
                                    + "]"
                                    + "}"
                                    + "],"
                                    + "\"subject\" : {"
                                    + "\"reference\" : \"Patient/" + idpasien + "\" ,"
                                    + "\"display\" : \"" + rs.getString("nm_pasien") + "\""
                                    + "},"
                                    + "\"encounter\" : {"
                                    + "\"reference\" : \"Encounter/" + rs.getString("id_encounter") + "\","
                                    + "\"display\" : \"Kunjungan " + rs.getString("nm_pasien") + " pada tanggal " + rs.getString("tanggal") + " dengan nomor kunjungan " + rs.getString("no_rawat") + "\""
                                    + "},"
                                    + "\"date\" : \"" + rs.getString("tanggal") + "\" ,"
                                    + "\"author\" : ["
                                    + "{"
                                    + "\"reference\" : \"Practitioner/" + iddokter + "\" ,"
                                    + "\"display\" : \"" + rs.getString("nama") + "\""
                                    + "}"
                                    + "],"
                                    + "\"title\" : \"Modul Gizi\" ,"
                                    + "\"custodian\" : {"
                                    + "\"reference\" : \"Organization/" + koneksiDB.IDSATUSEHAT() + "\""
                                    + "},"
                                    + "\"section\" : ["
                                    + "{"
                                    + "\"code\" : {"
                                    + "\"coding\" : ["
                                    + "{"
                                    + "\"system\" : \"http://loinc.org\" ,"
                                    + "\"code\" : \"42344-2\" ,"
                                    + "\"display\" : \"Discharge diet (narrative)\""
                                    + "}"
                                    + "]"
                                    + "},"
                                    + "\"text\" : {"
                                    + "\"status\" : \"additional\" ,"
                                    + "\"div\" : \"" + rs.getString("instruksi").replaceAll("\n", " ") + "\""
                                    + "}"
                                    + "}"
                                    + "]"
                                    + "}";
                            System.out.println("URL : " + link + "/Composition");
                            System.out.println("Request JSON : " + json);
                            requestEntity = new HttpEntity(json, headers);
                            json = api.getRest().exchange(link + "/Composition", HttpMethod.POST, requestEntity, String.class).getBody();
                            System.out.println("Result JSON : " + json);
                            root = mapper.readTree(json);
                            response = root.path("id");
                            if (!response.asText().equals("")) {
                                Sequel.menyimpan("satu_sehat_diet", "?,?,?", "Diet/Gizi", 3, new String[]{
                                    rs.getString("no_rawat"), formattedDateTime, response.asText()
                                });
                            }
                        } catch (HttpClientErrorException | HttpServerErrorException e) {
                            // Handle client and server errors
                            System.err.println("Error Response Status Code: " + e.getStatusCode());
//                            System.err.println("Error Response Body: " + e.getResponseBodyAsString());
                            // You can further parse the error response body if needed
                            ObjectMapper mapper = new ObjectMapper();
                            JsonNode errorResponse = mapper.readTree(e.getResponseBodyAsString());
                            ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
                            String prettyErrorResponse = writer.writeValueAsString(errorResponse);
                            System.err.println("Error Response JSON: \n" + prettyErrorResponse);
                        }
                    } catch (Exception e) {
                        System.out.println("Notifikasi : " + e);
                    }
                }
            } catch (Exception e) {
                System.out.println("Notif : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
    }

    private void medicationrequest(String norawat) {
        try {
            ps = koneksi.prepareStatement(
                    "SELECT\n"
                    + "	reg_periksa.tgl_registrasi,\n"
                    + "	reg_periksa.jam_reg,\n"
                    + "	reg_periksa.no_rawat,\n"
                    + "	reg_periksa.no_rkm_medis,\n"
                    + "	pasien.nm_pasien,\n"
                    + "	pasien.no_ktp,\n"
                    + "	pegawai.nama,\n"
                    + "	pegawai.no_ktp AS ktppraktisi,\n"
                    + "	satu_sehat_encounter.id_encounter,\n"
                    + "	satu_sehat_mapping_kfa.kfa_code,\n"
                    + "	resep_dokter.kode_brng,\n"
                    + "	satu_sehat_mapping_kfa.kfa_name,\n"
                    + "	satu_sehat_kfa_master_detail.dosage_form_code AS formcode,\n"
                    + "	satu_sehat_kfa_master_detail.dosage_form_name AS formdisplay,\n"
                    + "	satu_sehat_kfa_master_detail.rute_pemberian_code as routecode,\n"
                    + "	'http://www.whocc.no/atc' as keterangan,\n"
                    + "	satu_sehat_kfa_master_detail.rute_pemberian_name AS routedisplay,\n"
                    + "	resep_obat.tgl_peresepan,\n"
                    + "	resep_obat.jam_peresepan,\n"
                    + "	resep_dokter.jml,\n"
                    + "	satu_sehat_medication.id_medication,\n"
                    + "	resep_dokter.aturan_pakai,\n"
                    + "	resep_dokter.no_resep,\n"
                    + "	ifnull( satu_sehat_medicationrequest.id_medicationrequest, '' ) AS id_medicationrequest,"
                    + "	kodesatuan.kodefhir \n"
                    + "FROM\n"
                    + "	reg_periksa\n"
                    + "	INNER JOIN pasien ON reg_periksa.no_rkm_medis = pasien.no_rkm_medis\n"
                    + "	INNER JOIN resep_obat ON reg_periksa.no_rawat = resep_obat.no_rawat\n"
                    + "	INNER JOIN pegawai ON resep_obat.kd_dokter = pegawai.nik\n"
                    + "	INNER JOIN satu_sehat_encounter ON satu_sehat_encounter.no_rawat = reg_periksa.no_rawat\n"
                    + "	INNER JOIN resep_dokter ON resep_dokter.no_resep = resep_obat.no_resep\n"
                    + "	INNER JOIN satu_sehat_mapping_kfa ON satu_sehat_mapping_kfa.kode_brng = resep_dokter.kode_brng\n"
                    + "	INNER JOIN databarang ON satu_sehat_mapping_kfa.kode_brng = databarang.kode_brng\n"
                    + "	INNER JOIN satu_sehat_medication ON satu_sehat_medication.kode_brng = satu_sehat_mapping_kfa.kode_brng\n"
                    + "	LEFT JOIN nota_jalan ON nota_jalan.no_rawat = reg_periksa.no_rawat\n"
                    + "	INNER JOIN satu_sehat_kfa_master_detail ON satu_sehat_mapping_kfa.kfa_code = satu_sehat_kfa_master_detail.kfa_code"
                    + " INNER JOIN kodesatuan ON databarang.kode_sat = kodesatuan.kode_sat\n"
                    + "	LEFT JOIN satu_sehat_medicationrequest ON satu_sehat_medicationrequest.no_resep = resep_dokter.no_resep \n"
                    + "	AND satu_sehat_medicationrequest.kode_brng = resep_dokter.kode_brng "
                    + "WHERE reg_periksa.no_rawat=?");
            try {
                ps.setString(1, norawat);

                rs = ps.executeQuery();
                while (rs.next()) {

                    try {
                        idpasien = cekViaSatuSehat.tampilIDPasien(rs.getString("no_ktp"));
                        iddokter = cekViaSatuSehat.tampilIDParktisi(rs.getString("ktppraktisi"));
                        arrSplit = rs.getString("aturan_pakai").toString().toLowerCase().split("x");
                        signa1 = "1";
                        try {
                            if (!arrSplit[0].replaceAll("[^0-9.]+", "").equals("")) {
                                signa1 = arrSplit[0].replaceAll("[^0-9.]+", "");
                            }
                        } catch (Exception e) {
                            signa1 = "1";
                        }
                        signa2 = "1";
                        try {
                            if (!arrSplit[1].replaceAll("[^0-9.]+", "").equals("")) {
                                signa2 = arrSplit[1].replaceAll("[^0-9.]+", "");
                            }
                        } catch (Exception e) {
                            signa2 = "1";
                        }
                        try {
                            headers = new HttpHeaders();
                            headers.setContentType(MediaType.APPLICATION_JSON);
                            headers.add("Authorization", "Bearer " + api.TokenSatuSehat());
                            json = "{\n"
                                    + "    \"resourceType\": \"MedicationRequest\",\n"
                                    + "    \"identifier\": [\n"
                                    + "        {\n"
                                    + "\"system\": \"http://sys-ids.kemkes.go.id/prescription/" + koneksiDB.IDSATUSEHAT() + "\","
                                    + "            \"use\": \"official\",\n"
                                    + "            \"value\": \"" + rs.getString("no_resep").toString() + "\""
                                    + "        },\n"
                                    + "        {\n"
                                    + "            \"system\": \"http://sys-ids.kemkes.go.id/prescription-item/" + koneksiDB.IDSATUSEHAT() + "\",\n"
                                    + "            \"use\": \"official\",\n"
                                    + "            \"value\": \"" + rs.getString("kode_brng") + "\"\n"
                                    + "        }\n"
                                    + "    ],\n"
                                    + "    \"status\": \"completed\",\n"
                                    + "    \"intent\": \"order\",\n"
                                    + "    \"category\": [\n"
                                    + "        {\n"
                                    + "            \"coding\": [\n"
                                    + "                {\n"
                                    + "                    \"system\": \"http://terminology.hl7.org/CodeSystem/medicationrequest-category\",\n"
                                    + "                    \"code\": \"" + "Ralan".replaceAll("Ralan", "outpatient").replaceAll("Ranap", "inpatient") + "\",\n"
                                    + "                    \"display\": \"" + "Ralan".replaceAll("Ralan", "Outpatient").replaceAll("Ranap", "Inpatient") + "\"\n"
                                    + "                }\n"
                                    + "            ]\n"
                                    + "        }\n"
                                    + "    ],\n"
                                    + "    \"medicationReference\": {\n"
                                    + "        \"reference\": \"Medication/" + rs.getString("id_medication") + "\",\n"
                                    + "        \"display\": \"" + rs.getString("kfa_name") + "\"\n"
                                    + "    },\n"
                                    + "    \"subject\": {\n"
                                    + "        \"reference\": \"Patient/" + idpasien + "\",\n"
                                    + "        \"display\": \"" + rs.getString("nm_pasien") + "\"\n"
                                    + "    },\n"
                                    + "    \"encounter\": {\n"
                                    + "        \"reference\": \"Encounter/" + rs.getString("id_encounter") + "\"\n"
                                    + "    },\n"
                                    + "    \"authoredOn\": \"" + rs.getString("tgl_peresepan") + "T" + rs.getString("jam_peresepan") + "+07:00\",\n"
                                    + "    \"requester\": {\n"
                                    + "        \"reference\": \"Practitioner/" + iddokter + "\",\n"
                                    + "        \"display\": \"" + rs.getString("nama") + "\"\n"
                                    + "    },\n"
                                    + "    \"dosageInstruction\": [\n"
                                    + "        {\n"
                                    + "            \"sequence\": 1,\n"
                                    + "            \"patientInstruction\": \"" + rs.getString("aturan_pakai") + "\",\n"
                                    + "            \"timing\": {\n"
                                    + "                \"repeat\": {\n"
                                    + "                    \"frequency\": " + signa2 + ",\n"
                                    + "                    \"period\": 1,\n"
                                    + "                    \"periodUnit\": \"d\"\n"
                                    + "                }\n"
                                    + "            },\n"
                                    + "            \"route\": {\n"
                                    + "                \"coding\": [\n"
                                    + "                    {\n"
                                    + "                        \"system\": \"http://www.whocc.no/atc\",\n"
                                    + "                        \"code\": \"" + rs.getString("routecode") + "\",\n"
                                    + "                        \"display\": \"" + rs.getString("routedisplay") + "\"\n"
                                    + "                    }\n"
                                    + "                ]\n"
                                    + "            },\n"
                                    + "            \"doseAndRate\": [\n"
                                    + "                {\n"
                                    + "                    \"doseQuantity\": {\n"
                                    + "                        \"value\": " + Sequel.cariInteger("SELECT IF(kapasitas < 1, 1, kapasitas) AS kapasitas from databarang where kode_brng='" + rs.getString("kode_brng") + "'") + ",\n"
                                    + "                        \"unit\": \"" + rs.getString("kodefhir") + "\",\n"
                                    + "                        \"system\": \"http://terminology.hl7.org/CodeSystem/v3-orderableDrugForm\",\n"
                                    + "                        \"code\": \"" + rs.getString("kodefhir") + "\"\n"
                                    + "                    }\n"
                                    + "                }\n"
                                    + "            ]\n"
                                    + "        }\n"
                                    + "    ],\n"
                                    + "    \"dispenseRequest\": {\n"
                                    + "        \"quantity\": {\n"
                                    + "            \"value\": " + rs.getString("jml") + ",\n"
                                    + "            \"unit\": \"" + rs.getString("kodefhir") + "\",\n"
                                    + "            \"system\": \"http://terminology.hl7.org/CodeSystem/v3-orderableDrugForm\",\n"
                                    + "            \"code\": \"" + rs.getString("kodefhir") + "\"\n"
                                    + "        },\n"
                                    + "        \"performer\": {\n"
                                    + "            \"reference\":  \"Organization/" + koneksiDB.IDSATUSEHAT() + "\"\n"
                                    + "        }\n"
                                    + "    }\n"
                                    + "}";

                            System.out.println("URL : " + link + "/MedicationRequest");
                            System.out.println("Request JSON : " + json);
                            requestEntity = new HttpEntity(json, headers);
                            json = api.getRest().exchange(link + "/MedicationRequest", HttpMethod.POST, requestEntity, String.class).getBody();
                            System.out.println("Result JSON : " + json);
                            root = mapper.readTree(json);
                            response = root.path("id");
                            if (!response.asText().equals("")) {
                                if (Sequel.menyimpantf2("satu_sehat_medicationrequest", "?,?,?", "Obat/Alkes", 3, new String[]{
                                    rs.getString("no_resep").toString(), rs.getString("kode_brng").toString(), response.asText()
                                }) == true) {
                                }
                            }

                        } catch (HttpClientErrorException | HttpServerErrorException e) {
                            // Handle client and server errors
                            System.err.println("Error Response Status Code: " + e.getStatusCode());
//                            System.err.println("Error Response Body: " + e.getResponseBodyAsString());

                            // You can further parse the error response body if needed
                            ObjectMapper mapper = new ObjectMapper();
                            JsonNode errorResponse = mapper.readTree(e.getResponseBodyAsString());
                            ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
                            String prettyErrorResponse = writer.writeValueAsString(errorResponse);
                            System.err.println("Error Response JSON: \n" + prettyErrorResponse);
                        }
                    } catch (Exception e) {
                        System.out.println("Notifikasi : " + e);
                    }

                }
            } catch (Exception e) {
                System.out.println("Notif : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }

            ps = koneksi.prepareStatement(
                    "SELECT\n"
                    + "	reg_periksa.tgl_registrasi,\n"
                    + "	reg_periksa.jam_reg,\n"
                    + "	reg_periksa.no_rawat,\n"
                    + "	reg_periksa.no_rkm_medis,\n"
                    + "	pasien.nm_pasien,\n"
                    + "	pasien.no_ktp,\n"
                    + "	pegawai.nama,\n"
                    + "	pegawai.no_ktp AS ktppraktisi,\n"
                    + "	satu_sehat_encounter.id_encounter,\n"
                    + "	satu_sehat_mapping_kfa.kfa_code,\n"
                    + "	resep_dokter.kode_brng,\n"
                    + "	satu_sehat_mapping_kfa.kfa_name,\n"
                    + "	satu_sehat_kfa_master_detail.dosage_form_code AS formcode,\n"
                    + "	satu_sehat_kfa_master_detail.dosage_form_name AS formdisplay,\n"
                    + "	satu_sehat_kfa_master_detail.rute_pemberian_code as routecode,\n"
                    + "	'http://www.whocc.no/atc' as keterangan,\n"
                    + "	satu_sehat_kfa_master_detail.rute_pemberian_name AS routedisplay,\n"
                    + "	resep_obat.tgl_peresepan,\n"
                    + "	resep_obat.jam_peresepan,\n"
                    + "	resep_dokter.jml,\n"
                    + "	satu_sehat_medication.id_medication,\n"
                    + "	resep_dokter.aturan_pakai,\n"
                    + "	resep_dokter.no_resep,\n"
                    + "	ifnull( satu_sehat_medicationrequest.id_medicationrequest, '' ) AS id_medicationrequest,"
                    + "	kodesatuan.kodefhir \n"
                    + "FROM\n"
                    + "	reg_periksa\n"
                    + "	INNER JOIN pasien ON reg_periksa.no_rkm_medis = pasien.no_rkm_medis\n"
                    + "	INNER JOIN resep_obat ON reg_periksa.no_rawat = resep_obat.no_rawat\n"
                    + "	INNER JOIN pegawai ON resep_obat.kd_dokter = pegawai.nik\n"
                    + "	INNER JOIN satu_sehat_encounter ON satu_sehat_encounter.no_rawat = reg_periksa.no_rawat\n"
                    + "	INNER JOIN resep_dokter ON resep_dokter.no_resep = resep_obat.no_resep\n"
                    + "	INNER JOIN satu_sehat_mapping_kfa ON satu_sehat_mapping_kfa.kode_brng = resep_dokter.kode_brng\n"
                    + "	INNER JOIN databarang ON satu_sehat_mapping_kfa.kode_brng = databarang.kode_brng\n"
                    + "	INNER JOIN satu_sehat_medication ON satu_sehat_medication.kode_brng = satu_sehat_mapping_kfa.kode_brng\n"
                    + "	LEFT JOIN nota_inap ON nota_inap.no_rawat = reg_periksa.no_rawat\n"
                    + "	INNER JOIN satu_sehat_kfa_master_detail ON satu_sehat_mapping_kfa.kfa_code = satu_sehat_kfa_master_detail.kfa_code\n"
                    + " INNER JOIN kodesatuan ON databarang.kode_sat = kodesatuan.kode_sat\n"
                    + "	LEFT JOIN satu_sehat_medicationrequest ON satu_sehat_medicationrequest.no_resep = resep_dokter.no_resep \n"
                    + "	AND satu_sehat_medicationrequest.kode_brng = resep_dokter.kode_brng \n"
                    + "WHERE reg_periksa.no_rawat=?");
            try {
                ps.setString(1, norawat);

                rs = ps.executeQuery();
                while (rs.next()) {

                }
            } catch (Exception e) {
                System.out.println("Notif : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }

        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
    }

    private void medicationdispense() {
        try {
            ps = koneksi.prepareStatement(
                    "select reg_periksa.tgl_registrasi,reg_periksa.jam_reg,reg_periksa.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien,pasien.no_ktp,"
                    + "pegawai.nama,pegawai.no_ktp as ktppraktisi,satu_sehat_encounter.id_encounter,satu_sehat_mapping_obat.obat_code,satu_sehat_mapping_obat.obat_system,"
                    + "detail_pemberian_obat.kode_brng,satu_sehat_mapping_obat.obat_display,satu_sehat_mapping_obat.form_code,satu_sehat_mapping_obat.form_system,satu_sehat_mapping_obat.form_display,"
                    + "satu_sehat_mapping_obat.route_code,satu_sehat_mapping_obat.route_system,satu_sehat_mapping_obat.route_display,satu_sehat_mapping_obat.denominator_code,"
                    + "satu_sehat_mapping_obat.denominator_system,resep_obat.tgl_peresepan,resep_obat.jam_peresepan,detail_pemberian_obat.jml,satu_sehat_medication.id_medication,"
                    + "aturan_pakai.aturan,resep_obat.no_resep,ifnull(satu_sehat_medicationdispense.id_medicationdispanse,'') as id_medicationdispanse,detail_pemberian_obat.no_batch,"
                    + "detail_pemberian_obat.no_faktur,detail_pemberian_obat.tgl_perawatan,detail_pemberian_obat.jam,satu_sehat_mapping_lokasi_depo_farmasi.id_lokasi_satusehat,"
                    + "bangsal.nm_bangsal from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "
                    + "inner join resep_obat on reg_periksa.no_rawat=resep_obat.no_rawat "
                    + "inner join pegawai on resep_obat.kd_dokter=pegawai.nik "
                    + "inner join satu_sehat_encounter on satu_sehat_encounter.no_rawat=reg_periksa.no_rawat "
                    + "inner join detail_pemberian_obat on detail_pemberian_obat.no_rawat=resep_obat.no_rawat and "
                    + "detail_pemberian_obat.tgl_perawatan=resep_obat.tgl_perawatan and detail_pemberian_obat.jam=resep_obat.jam "
                    + "inner join aturan_pakai on detail_pemberian_obat.no_rawat=aturan_pakai.no_rawat and "
                    + "detail_pemberian_obat.tgl_perawatan=aturan_pakai.tgl_perawatan and detail_pemberian_obat.jam=aturan_pakai.jam and "
                    + "detail_pemberian_obat.kode_brng=aturan_pakai.kode_brng "
                    + "inner join satu_sehat_mapping_obat on satu_sehat_mapping_obat.kode_brng=detail_pemberian_obat.kode_brng "
                    + "inner join bangsal on bangsal.kd_bangsal=detail_pemberian_obat.kd_bangsal "
                    + "inner join satu_sehat_mapping_lokasi_depo_farmasi on satu_sehat_mapping_lokasi_depo_farmasi.kd_bangsal=bangsal.kd_bangsal "
                    + "inner join satu_sehat_medication on satu_sehat_medication.kode_brng=satu_sehat_mapping_obat.kode_brng "
                    + "inner join nota_jalan on nota_jalan.no_rawat=reg_periksa.no_rawat "
                    + "left join satu_sehat_medicationdispense on satu_sehat_medicationdispense.no_rawat=detail_pemberian_obat.no_rawat and "
                    + "satu_sehat_medicationdispense.tgl_perawatan=detail_pemberian_obat.tgl_perawatan and "
                    + "satu_sehat_medicationdispense.jam=detail_pemberian_obat.jam and "
                    + "satu_sehat_medicationdispense.kode_brng=detail_pemberian_obat.kode_brng and "
                    + "satu_sehat_medicationdispense.no_batch=detail_pemberian_obat.no_batch and "
                    + "satu_sehat_medicationdispense.no_faktur=detail_pemberian_obat.no_faktur "
                    + "where nota_jalan.tanggal between ? and ?");
            try {
                ps.setString(1, Tanggal1.getText());
                ps.setString(2, Tanggal2.getText());
                rs = ps.executeQuery();
                while (rs.next()) {
                    if ((!rs.getString("no_ktp").equals("")) && (!rs.getString("ktppraktisi").equals("")) && rs.getString("id_medicationdispanse").equals("")) {
                        try {
                            idpasien = cekViaSatuSehat.tampilIDPasien(rs.getString("no_ktp"));
                            iddokter = cekViaSatuSehat.tampilIDParktisi(rs.getString("ktppraktisi"));
                            arrSplit = rs.getString("aturan").toLowerCase().split("x");
                            signa1 = "1";
                            try {
                                if (!arrSplit[0].replaceAll("[^0-9.]+", "").equals("")) {
                                    signa1 = arrSplit[0].replaceAll("[^0-9.]+", "");
                                }
                            } catch (Exception e) {
                                signa1 = "1";
                            }
                            signa2 = "1";
                            try {
                                if (!arrSplit[1].replaceAll("[^0-9.]+", "").equals("")) {
                                    signa2 = arrSplit[1].replaceAll("[^0-9.]+", "");
                                }
                            } catch (Exception e) {
                                signa2 = "1";
                            }
                            try {
                                headers = new HttpHeaders();
                                headers.setContentType(MediaType.APPLICATION_JSON);
                                headers.add("Authorization", "Bearer " + api.TokenSatuSehat());
                                json = "{"
                                        + "\"resourceType\": \"MedicationDispense\","
                                        + "\"identifier\": ["
                                        + "{"
                                        + "\"system\": \"http://sys-ids.kemkes.go.id/prescription/" + koneksiDB.IDSATUSEHAT() + "\","
                                        + "\"use\": \"official\","
                                        + "\"value\": \"" + rs.getString("no_resep") + "\""
                                        + "},"
                                        + "{"
                                        + "\"system\": \"http://sys-ids.kemkes.go.id/prescription-item/" + koneksiDB.IDSATUSEHAT() + "\","
                                        + "\"use\": \"official\","
                                        + "\"value\": \"" + rs.getString("kode_brng") + "\""
                                        + "}"
                                        + "],"
                                        + "\"status\": \"completed\","
                                        + "\"category\": {"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"system\": \"http://terminology.hl7.org/fhir/CodeSystem/medicationdispense-category\","
                                        + "\"code\": \"outpatient\","
                                        + "\"display\": \"Outpatient\""
                                        + "}"
                                        + "]"
                                        + "},"
                                        + "\"medicationReference\": {"
                                        + "\"reference\": \"Medication/" + rs.getString("id_medicationrequest") + "\","
                                        + "\"display\": \"" + rs.getString("obat_display") + "\""
                                        + "},"
                                        + "\"subject\": {"
                                        + "\"reference\": \"Patient/" + idpasien + "\","
                                        + "\"display\": \"" + rs.getString("nm_pasien") + "\""
                                        + "},"
                                        + "\"context\": {"
                                        + "\"reference\": \"Encounter/" + rs.getString("id_encounter") + "\""
                                        + "},"
                                        + "\"performer\": ["
                                        + "{"
                                        + "\"actor\": {"
                                        + "\"reference\": \"Practitioner/" + iddokter + "\","
                                        + "\"display\": \"" + rs.getString("nama") + "\""
                                        + "}"
                                        + "}"
                                        + "],"
                                        + "\"location\": {"
                                        + "\"reference\": \"Location/" + rs.getString("id_lokasi_satusehat") + "\","
                                        + "\"display\": \"" + rs.getString("nm_bangsal") + "\""
                                        + "},"
                                        + "\"quantity\": {"
                                        + "\"system\": \"" + rs.getString("denominator_system") + "\","
                                        + "\"code\": \"" + rs.getString("denominator_code") + "\","
                                        + "\"value\": " + rs.getString("jml")
                                        + "},"
                                        + "\"whenPrepared\": \"" + rs.getString("tgl_peresepan") + "T" + rs.getString("jam_peresepan") + "Z\","
                                        + "\"whenHandedOver\": \"" + rs.getString("tgl_perawatan") + "T" + rs.getString("jam") + "Z\","
                                        + "\"dosageInstruction\": ["
                                        + "{"
                                        + "\"sequence\": 1,"
                                        + "\"text\": \"" + rs.getString("aturan") + "\","
                                        + "\"timing\": {"
                                        + "\"repeat\": {"
                                        + "\"frequency\": " + signa2 + ","
                                        + "\"period\": 1,"
                                        + "\"periodUnit\": \"d\""
                                        + "}"
                                        + "},"
                                        + "\"route\": {"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"system\": \"" + rs.getString("route_system") + "\","
                                        + "\"code\": \"" + rs.getString("route_code") + "\","
                                        + "\"display\": \"" + rs.getString("route_display") + "\""
                                        + "}"
                                        + "]"
                                        + "},"
                                        + "\"doseAndRate\": ["
                                        + "{"
                                        + "\"doseQuantity\": {"
                                        + "\"value\": " + signa1 + ","
                                        + "\"unit\": \"" + rs.getString("denominator_code") + "\","
                                        + "\"system\": \"" + rs.getString("denominator_system") + "\","
                                        + "\"code\": \"" + rs.getString("denominator_code") + "\""
                                        + "}"
                                        + "}"
                                        + "]"
                                        + "}"
                                        + "]"
                                        + "}";
                                TeksArea.append("URL : " + link + "/MedicationDispense");
                                TeksArea.append("Request JSON : " + json);
                                requestEntity = new HttpEntity(json, headers);
                                json = api.getRest().exchange(link + "/MedicationDispense", HttpMethod.POST, requestEntity, String.class).getBody();
                                TeksArea.append("Result JSON : " + json);
                                root = mapper.readTree(json);
                                response = root.path("id");
                                if (!response.asText().equals("")) {
                                    Sequel.menyimpan2("satu_sehat_medicationdispense", "?,?,?,?,?,?,?", "Obat/Alkes", 7, new String[]{
                                        rs.getString("no_rawat"), rs.getString("tgl_perawatan"), rs.getString("jam"), rs.getString("kode_brng"), rs.getString("no_batch"), rs.getString("no_faktur"), response.asText()
                                    });
                                }
                            } catch (Exception e) {
                                System.out.println("Notifikasi Bridging : " + e);
                            }
                        } catch (Exception e) {
                            System.out.println("Notifikasi : " + e);
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Notif : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }

            ps = koneksi.prepareStatement(
                    "select reg_periksa.tgl_registrasi,reg_periksa.jam_reg,reg_periksa.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien,pasien.no_ktp,"
                    + "pegawai.nama,pegawai.no_ktp as ktppraktisi,satu_sehat_encounter.id_encounter,satu_sehat_mapping_obat.obat_code,satu_sehat_mapping_obat.obat_system,"
                    + "detail_pemberian_obat.kode_brng,satu_sehat_mapping_obat.obat_display,satu_sehat_mapping_obat.form_code,satu_sehat_mapping_obat.form_system,satu_sehat_mapping_obat.form_display,"
                    + "satu_sehat_mapping_obat.route_code,satu_sehat_mapping_obat.route_system,satu_sehat_mapping_obat.route_display,satu_sehat_mapping_obat.denominator_code,"
                    + "satu_sehat_mapping_obat.denominator_system,resep_obat.tgl_peresepan,resep_obat.jam_peresepan,detail_pemberian_obat.jml,satu_sehat_medication.id_medication,"
                    + "aturan_pakai.aturan,resep_obat.no_resep,ifnull(satu_sehat_medicationdispense.id_medicationdispanse,'') as id_medicationdispanse,detail_pemberian_obat.no_batch,"
                    + "detail_pemberian_obat.no_faktur,detail_pemberian_obat.tgl_perawatan,detail_pemberian_obat.jam,satu_sehat_mapping_lokasi_depo_farmasi.id_lokasi_satusehat,"
                    + "bangsal.nm_bangsal from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "
                    + "inner join resep_obat on reg_periksa.no_rawat=resep_obat.no_rawat "
                    + "inner join pegawai on resep_obat.kd_dokter=pegawai.nik "
                    + "inner join satu_sehat_encounter on satu_sehat_encounter.no_rawat=reg_periksa.no_rawat "
                    + "inner join detail_pemberian_obat on detail_pemberian_obat.no_rawat=resep_obat.no_rawat and "
                    + "detail_pemberian_obat.tgl_perawatan=resep_obat.tgl_perawatan and detail_pemberian_obat.jam=resep_obat.jam "
                    + "inner join aturan_pakai on detail_pemberian_obat.no_rawat=aturan_pakai.no_rawat and "
                    + "detail_pemberian_obat.tgl_perawatan=aturan_pakai.tgl_perawatan and detail_pemberian_obat.jam=aturan_pakai.jam and "
                    + "detail_pemberian_obat.kode_brng=aturan_pakai.kode_brng "
                    + "inner join satu_sehat_mapping_obat on satu_sehat_mapping_obat.kode_brng=detail_pemberian_obat.kode_brng "
                    + "inner join bangsal on bangsal.kd_bangsal=detail_pemberian_obat.kd_bangsal "
                    + "inner join satu_sehat_mapping_lokasi_depo_farmasi on satu_sehat_mapping_lokasi_depo_farmasi.kd_bangsal=bangsal.kd_bangsal "
                    + "inner join satu_sehat_medication on satu_sehat_medication.kode_brng=satu_sehat_mapping_obat.kode_brng "
                    + "inner join nota_inap on nota_inap.no_rawat=reg_periksa.no_rawat "
                    + "left join satu_sehat_medicationdispense on satu_sehat_medicationdispense.no_rawat=detail_pemberian_obat.no_rawat and "
                    + "satu_sehat_medicationdispense.tgl_perawatan=detail_pemberian_obat.tgl_perawatan and "
                    + "satu_sehat_medicationdispense.jam=detail_pemberian_obat.jam and "
                    + "satu_sehat_medicationdispense.kode_brng=detail_pemberian_obat.kode_brng and "
                    + "satu_sehat_medicationdispense.no_batch=detail_pemberian_obat.no_batch and "
                    + "satu_sehat_medicationdispense.no_faktur=detail_pemberian_obat.no_faktur "
                    + "where nota_inap.tanggal between ? and ?");
            try {
                ps.setString(1, Tanggal1.getText());
                ps.setString(2, Tanggal2.getText());
                rs = ps.executeQuery();
                while (rs.next()) {
                    if ((!rs.getString("no_ktp").equals("")) && (!rs.getString("ktppraktisi").equals("")) && rs.getString("id_medicationdispanse").equals("")) {
                        try {
                            idpasien = cekViaSatuSehat.tampilIDPasien(rs.getString("no_ktp"));
                            iddokter = cekViaSatuSehat.tampilIDParktisi(rs.getString("ktppraktisi"));
                            arrSplit = rs.getString("aturan").toLowerCase().split("x");
                            signa1 = "1";
                            try {
                                if (!arrSplit[0].replaceAll("[^0-9.]+", "").equals("")) {
                                    signa1 = arrSplit[0].replaceAll("[^0-9.]+", "");
                                }
                            } catch (Exception e) {
                                signa1 = "1";
                            }
                            signa2 = "1";
                            try {
                                if (!arrSplit[1].replaceAll("[^0-9.]+", "").equals("")) {
                                    signa2 = arrSplit[1].replaceAll("[^0-9.]+", "");
                                }
                            } catch (Exception e) {
                                signa2 = "1";
                            }
                            try {
                                headers = new HttpHeaders();
                                headers.setContentType(MediaType.APPLICATION_JSON);
                                headers.add("Authorization", "Bearer " + api.TokenSatuSehat());
                                json = "{"
                                        + "\"resourceType\": \"MedicationDispense\","
                                        + "\"identifier\": ["
                                        + "{"
                                        + "\"system\": \"http://sys-ids.kemkes.go.id/prescription/" + koneksiDB.IDSATUSEHAT() + "\","
                                        + "\"use\": \"official\","
                                        + "\"value\": \"" + rs.getString("no_resep") + "\""
                                        + "},"
                                        + "{"
                                        + "\"system\": \"http://sys-ids.kemkes.go.id/prescription-item/" + koneksiDB.IDSATUSEHAT() + "\","
                                        + "\"use\": \"official\","
                                        + "\"value\": \"" + rs.getString("kode_brng") + "\""
                                        + "}"
                                        + "],"
                                        + "\"status\": \"completed\","
                                        + "\"category\": {"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"system\": \"http://terminology.hl7.org/fhir/CodeSystem/medicationdispense-category\","
                                        + "\"code\": \"inpatient\","
                                        + "\"display\": \"Inpatient\""
                                        + "}"
                                        + "]"
                                        + "},"
                                        + "\"medicationReference\": {"
                                        + "\"reference\": \"Medication/" + rs.getString("id_medicationrequest") + "\","
                                        + "\"display\": \"" + rs.getString("obat_display") + "\""
                                        + "},"
                                        + "\"subject\": {"
                                        + "\"reference\": \"Patient/" + idpasien + "\","
                                        + "\"display\": \"" + rs.getString("nm_pasien") + "\""
                                        + "},"
                                        + "\"context\": {"
                                        + "\"reference\": \"Encounter/" + rs.getString("id_encounter") + "\""
                                        + "},"
                                        + "\"performer\": ["
                                        + "{"
                                        + "\"actor\": {"
                                        + "\"reference\": \"Practitioner/" + iddokter + "\","
                                        + "\"display\": \"" + rs.getString("nama") + "\""
                                        + "}"
                                        + "}"
                                        + "],"
                                        + "\"location\": {"
                                        + "\"reference\": \"Location/" + rs.getString("id_lokasi_satusehat") + "\","
                                        + "\"display\": \"" + rs.getString("nm_bangsal") + "\""
                                        + "},"
                                        + "\"quantity\": {"
                                        + "\"system\": \"" + rs.getString("denominator_system") + "\","
                                        + "\"code\": \"" + rs.getString("denominator_code") + "\","
                                        + "\"value\": " + rs.getString("jml")
                                        + "},"
                                        + "\"whenPrepared\": \"" + rs.getString("tgl_peresepan") + "T" + rs.getString("jam_peresepan") + "Z\","
                                        + "\"whenHandedOver\": \"" + rs.getString("tgl_perawatan") + "T" + rs.getString("jam") + "Z\","
                                        + "\"dosageInstruction\": ["
                                        + "{"
                                        + "\"sequence\": 1,"
                                        + "\"text\": \"" + rs.getString("aturan") + "\","
                                        + "\"timing\": {"
                                        + "\"repeat\": {"
                                        + "\"frequency\": " + signa2 + ","
                                        + "\"period\": 1,"
                                        + "\"periodUnit\": \"d\""
                                        + "}"
                                        + "},"
                                        + "\"route\": {"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"system\": \"" + rs.getString("route_system") + "\","
                                        + "\"code\": \"" + rs.getString("route_code") + "\","
                                        + "\"display\": \"" + rs.getString("route_display") + "\""
                                        + "}"
                                        + "]"
                                        + "},"
                                        + "\"doseAndRate\": ["
                                        + "{"
                                        + "\"doseQuantity\": {"
                                        + "\"value\": " + signa1 + ","
                                        + "\"unit\": \"" + rs.getString("denominator_code") + "\","
                                        + "\"system\": \"" + rs.getString("denominator_system") + "\","
                                        + "\"code\": \"" + rs.getString("denominator_code") + "\""
                                        + "}"
                                        + "}"
                                        + "]"
                                        + "}"
                                        + "]"
                                        + "}";
                                TeksArea.append("URL : " + link + "/MedicationDispense");
                                TeksArea.append("Request JSON : " + json);
                                requestEntity = new HttpEntity(json, headers);
                                json = api.getRest().exchange(link + "/MedicationDispense", HttpMethod.POST, requestEntity, String.class).getBody();
                                TeksArea.append("Result JSON : " + json);
                                root = mapper.readTree(json);
                                response = root.path("id");
                                if (!response.asText().equals("")) {
                                    Sequel.menyimpan2("satu_sehat_medicationdispense", "?,?,?,?,?,?,?", "Obat/Alkes", 7, new String[]{
                                        rs.getString("no_rawat"), rs.getString("tgl_perawatan"), rs.getString("jam"), rs.getString("kode_brng"), rs.getString("no_batch"), rs.getString("no_faktur"), response.asText()
                                    });
                                }
                            } catch (Exception e) {
                                System.out.println("Notifikasi Bridging : " + e);
                            }
                        } catch (Exception e) {
                            System.out.println("Notifikasi : " + e);
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Notif : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
    }

    private void servicerequestradiologi() {
        try {
            ps = koneksi.prepareStatement(
                    "select reg_periksa.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien,pasien.no_ktp,pegawai.no_ktp as ktpdokter,"
                    + "satu_sehat_encounter.id_encounter,permintaan_radiologi.noorder,permintaan_radiologi.tgl_permintaan,permintaan_radiologi.jam_permintaan,permintaan_radiologi.diagnosa_klinis,"
                    + "jns_perawatan_radiologi.nm_perawatan,satu_sehat_mapping_radiologi.code,satu_sehat_mapping_radiologi.system,satu_sehat_mapping_radiologi.display,"
                    + "ifnull(satu_sehat_servicerequest_radiologi.id_servicerequest,'') as id_servicerequest,permintaan_pemeriksaan_radiologi.kd_jenis_prw "
                    + "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis inner join pegawai on pegawai.nik=reg_periksa.kd_dokter "
                    + "inner join satu_sehat_encounter on satu_sehat_encounter.no_rawat=reg_periksa.no_rawat inner join permintaan_radiologi on permintaan_radiologi.no_rawat=reg_periksa.no_rawat "
                    + "inner join permintaan_pemeriksaan_radiologi on permintaan_pemeriksaan_radiologi.noorder=permintaan_radiologi.noorder "
                    + "inner join jns_perawatan_radiologi on jns_perawatan_radiologi.kd_jenis_prw=permintaan_pemeriksaan_radiologi.kd_jenis_prw "
                    + "inner join satu_sehat_mapping_radiologi on satu_sehat_mapping_radiologi.kd_jenis_prw=jns_perawatan_radiologi.kd_jenis_prw "
                    + "left join satu_sehat_servicerequest_radiologi on satu_sehat_servicerequest_radiologi.noorder=permintaan_pemeriksaan_radiologi.noorder "
                    + "and satu_sehat_servicerequest_radiologi.kd_jenis_prw=permintaan_pemeriksaan_radiologi.kd_jenis_prw "
                    + "inner join nota_jalan on nota_jalan.no_rawat=reg_periksa.no_rawat "
                    + "where nota_jalan.tanggal between ? and ? ");
            try {
                ps.setString(1, Tanggal1.getText());
                ps.setString(2, Tanggal2.getText());
                rs = ps.executeQuery();
                while (rs.next()) {
                    if ((!rs.getString("no_ktp").equals("")) && (!rs.getString("ktpdokter").equals("")) && rs.getString("id_servicerequest").equals("")) {
                        try {
                            iddokter = cekViaSatuSehat.tampilIDParktisi(rs.getString("ktpdokter"));
                            idpasien = cekViaSatuSehat.tampilIDPasien(rs.getString("no_ktp"));
                            try {
                                headers = new HttpHeaders();
                                headers.setContentType(MediaType.APPLICATION_JSON);
                                headers.add("Authorization", "Bearer " + api.TokenSatuSehat());
                                json = "{"
                                        + "\"resourceType\": \"ServiceRequest\","
                                        + "\"identifier\": ["
                                        + "{"
                                        + "\"system\": \"http://sys-ids.kemkes.go.id/servicerequest/" + koneksiDB.IDSATUSEHAT() + "\","
                                        + "\"value\": \"" + rs.getString("noorder") + "\""
                                        + "}"
                                        + "],"
                                        + "\"status\": \"active\","
                                        + "\"intent\": \"order\","
                                        + "\"code\": {"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"system\": \"" + rs.getString("system") + "\","
                                        + "\"code\": \"" + rs.getString("code") + "\","
                                        + "\"display\": \"" + rs.getString("display") + "\""
                                        + "}"
                                        + "],"
                                        + "\"text\": \"" + rs.getString("nm_perawatan") + "\""
                                        + "},"
                                        + "\"subject\": {"
                                        + "\"reference\": \"Patient/" + idpasien + "\""
                                        + "},"
                                        + "\"encounter\": {"
                                        + "\"reference\": \"Encounter/" + rs.getString("id_encounter") + "\","
                                        + "\"display\": \"Permintaan " + rs.getString("nm_perawatan") + " atas nama pasien " + rs.getString("nm_pasien") + " No.RM " + rs.getString("no_rkm_medis") + " No.Rawat " + rs.getString("no_rawat") + ", pada tanggal " + rs.getString("tgl_registrasi") + " " + rs.getString("jam_reg") + "\""
                                        + "},"
                                        + "\"authoredOn\" : \"" + rs.getString("tgl_permintaan") + "T" + rs.getString("jam_permintaan") + "+07:00\","
                                        + "\"requester\": {"
                                        + "\"reference\": \"Practitioner/" + iddokter + "\","
                                        + "\"display\": \"" + rs.getString("nama") + "\""
                                        + "},"
                                        + "\"performer\": [{"
                                        + "\"reference\": \"Organization/" + koneksiDB.IDSATUSEHAT() + "\","
                                        + "\"display\": \"Ruang Radiologi/Petugas Radiologi\""
                                        + "}],"
                                        + "\"reasonCode\": ["
                                        + "{"
                                        + "\"text\": \"" + rs.getString("diagnosa_klinis") + "\""
                                        + "}"
                                        + "]"
                                        + "}";
                                TeksArea.append("URL : " + link + "/ServiceRequest");
                                TeksArea.append("Request JSON : " + json);
                                requestEntity = new HttpEntity(json, headers);
                                json = api.getRest().exchange(link + "/ServiceRequest", HttpMethod.POST, requestEntity, String.class).getBody();
                                TeksArea.append("Result JSON : " + json);
                                root = mapper.readTree(json);
                                response = root.path("id");
                                if (!response.asText().equals("")) {
                                    Sequel.menyimpan2("satu_sehat_servicerequest_radiologi", "?,?,?", "No.Rawat", 3, new String[]{
                                        rs.getString("noorder"), rs.getString("kd_jenis_prw"), response.asText()
                                    });
                                }
                            } catch (Exception ea) {
                                System.out.println("Notifikasi Bridging : " + ea);
                            }
                        } catch (Exception ef) {
                            System.out.println("Notifikasi : " + ef);
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Notif : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }

        try {
            ps = koneksi.prepareStatement(
                    "select reg_periksa.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien,pasien.no_ktp,pegawai.no_ktp as ktpdokter,"
                    + "satu_sehat_encounter.id_encounter,permintaan_radiologi.noorder,permintaan_radiologi.tgl_permintaan,permintaan_radiologi.jam_permintaan,permintaan_radiologi.diagnosa_klinis,"
                    + "jns_perawatan_radiologi.nm_perawatan,satu_sehat_mapping_radiologi.code,satu_sehat_mapping_radiologi.system,satu_sehat_mapping_radiologi.display,"
                    + "ifnull(satu_sehat_servicerequest_radiologi.id_servicerequest,'') as id_servicerequest,permintaan_pemeriksaan_radiologi.kd_jenis_prw "
                    + "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis inner join pegawai on pegawai.nik=reg_periksa.kd_dokter "
                    + "inner join satu_sehat_encounter on satu_sehat_encounter.no_rawat=reg_periksa.no_rawat inner join permintaan_radiologi on permintaan_radiologi.no_rawat=reg_periksa.no_rawat "
                    + "inner join permintaan_pemeriksaan_radiologi on permintaan_pemeriksaan_radiologi.noorder=permintaan_radiologi.noorder "
                    + "inner join jns_perawatan_radiologi on jns_perawatan_radiologi.kd_jenis_prw=permintaan_pemeriksaan_radiologi.kd_jenis_prw "
                    + "inner join satu_sehat_mapping_radiologi on satu_sehat_mapping_radiologi.kd_jenis_prw=jns_perawatan_radiologi.kd_jenis_prw "
                    + "left join satu_sehat_servicerequest_radiologi on satu_sehat_servicerequest_radiologi.noorder=permintaan_pemeriksaan_radiologi.noorder "
                    + "and satu_sehat_servicerequest_radiologi.kd_jenis_prw=permintaan_pemeriksaan_radiologi.kd_jenis_prw "
                    + "inner join nota_inap on nota_inap.no_rawat=reg_periksa.no_rawat "
                    + "where nota_inap.tanggal between ? and ? ");
            try {
                ps.setString(1, Tanggal1.getText());
                ps.setString(2, Tanggal2.getText());
                rs = ps.executeQuery();
                while (rs.next()) {
                    if ((!rs.getString("no_ktp").equals("")) && (!rs.getString("ktpdokter").equals("")) && rs.getString("id_servicerequest").equals("")) {
                        try {
                            iddokter = cekViaSatuSehat.tampilIDParktisi(rs.getString("ktpdokter"));
                            idpasien = cekViaSatuSehat.tampilIDPasien(rs.getString("no_ktp"));
                            try {
                                headers = new HttpHeaders();
                                headers.setContentType(MediaType.APPLICATION_JSON);
                                headers.add("Authorization", "Bearer " + api.TokenSatuSehat());
                                json = "{"
                                        + "\"resourceType\": \"ServiceRequest\","
                                        + "\"identifier\": ["
                                        + "{"
                                        + "\"system\": \"http://sys-ids.kemkes.go.id/servicerequest/" + koneksiDB.IDSATUSEHAT() + "\","
                                        + "\"value\": \"" + rs.getString("noorder") + "\""
                                        + "}"
                                        + "],"
                                        + "\"status\": \"active\","
                                        + "\"intent\": \"order\","
                                        + "\"code\": {"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"system\": \"" + rs.getString("system") + "\","
                                        + "\"code\": \"" + rs.getString("code") + "\","
                                        + "\"display\": \"" + rs.getString("display") + "\""
                                        + "}"
                                        + "],"
                                        + "\"text\": \"" + rs.getString("nm_perawatan") + "\""
                                        + "},"
                                        + "\"subject\": {"
                                        + "\"reference\": \"Patient/" + idpasien + "\""
                                        + "},"
                                        + "\"encounter\": {"
                                        + "\"reference\": \"Encounter/" + rs.getString("id_encounter") + "\","
                                        + "\"display\": \"Permintaan " + rs.getString("nm_perawatan") + " atas nama pasien " + rs.getString("nm_pasien") + " No.RM " + rs.getString("no_rkm_medis") + " No.Rawat " + rs.getString("no_rawat") + ", pada tanggal " + rs.getString("tgl_registrasi") + " " + rs.getString("jam_reg") + "\""
                                        + "},"
                                        + "\"authoredOn\" : \"" + rs.getString("tgl_permintaan") + "T" + rs.getString("jam_permintaan") + "+07:00\","
                                        + "\"requester\": {"
                                        + "\"reference\": \"Practitioner/" + iddokter + "\","
                                        + "\"display\": \"" + rs.getString("nama") + "\""
                                        + "},"
                                        + "\"performer\": [{"
                                        + "\"reference\": \"Organization/" + koneksiDB.IDSATUSEHAT() + "\","
                                        + "\"display\": \"Ruang Radiologi/Petugas Radiologi\""
                                        + "}],"
                                        + "\"reasonCode\": ["
                                        + "{"
                                        + "\"text\": \"" + rs.getString("diagnosa_klinis") + "\""
                                        + "}"
                                        + "]"
                                        + "}";
                                TeksArea.append("URL : " + link + "/ServiceRequest");
                                TeksArea.append("Request JSON : " + json);
                                requestEntity = new HttpEntity(json, headers);
                                json = api.getRest().exchange(link + "/ServiceRequest", HttpMethod.POST, requestEntity, String.class).getBody();
                                TeksArea.append("Result JSON : " + json);
                                root = mapper.readTree(json);
                                response = root.path("id");
                                if (!response.asText().equals("")) {
                                    Sequel.menyimpan2("satu_sehat_servicerequest_radiologi", "?,?,?", "No.Rawat", 3, new String[]{
                                        rs.getString("noorder"), rs.getString("kd_jenis_prw"), response.asText()
                                    });
                                }
                            } catch (Exception ea) {
                                System.out.println("Notifikasi Bridging : " + ea);
                            }
                        } catch (Exception ef) {
                            System.out.println("Notifikasi : " + ef);
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Notif : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
    }

    private void specimenradiologi() {
        try {
            ps = koneksi.prepareStatement(
                    "select pasien.nm_pasien,pasien.no_ktp,permintaan_radiologi.noorder,permintaan_radiologi.tgl_sampel,permintaan_radiologi.jam_sampel,"
                    + "satu_sehat_mapping_radiologi.sampel_code,satu_sehat_mapping_radiologi.sampel_system,satu_sehat_mapping_radiologi.sampel_display,satu_sehat_servicerequest_radiologi.id_servicerequest,"
                    + "permintaan_pemeriksaan_radiologi.kd_jenis_prw,ifnull(satu_sehat_specimen_radiologi.id_specimen,'') as id_specimen "
                    + "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis inner join permintaan_radiologi on permintaan_radiologi.no_rawat=reg_periksa.no_rawat "
                    + "inner join permintaan_pemeriksaan_radiologi on permintaan_pemeriksaan_radiologi.noorder=permintaan_radiologi.noorder "
                    + "inner join jns_perawatan_radiologi on jns_perawatan_radiologi.kd_jenis_prw=permintaan_pemeriksaan_radiologi.kd_jenis_prw "
                    + "inner join satu_sehat_mapping_radiologi on satu_sehat_mapping_radiologi.kd_jenis_prw=jns_perawatan_radiologi.kd_jenis_prw "
                    + "inner join satu_sehat_servicerequest_radiologi on satu_sehat_servicerequest_radiologi.noorder=permintaan_pemeriksaan_radiologi.noorder "
                    + "and satu_sehat_servicerequest_radiologi.kd_jenis_prw=permintaan_pemeriksaan_radiologi.kd_jenis_prw "
                    + "left join satu_sehat_specimen_radiologi on satu_sehat_servicerequest_radiologi.noorder=satu_sehat_specimen_radiologi.noorder "
                    + "and satu_sehat_servicerequest_radiologi.kd_jenis_prw=satu_sehat_specimen_radiologi.kd_jenis_prw "
                    + "inner join nota_jalan on nota_jalan.no_rawat=reg_periksa.no_rawat "
                    + "where nota_jalan.tanggal between ? and ? ");
            try {
                ps.setString(1, Tanggal1.getText());
                ps.setString(2, Tanggal2.getText());
                rs = ps.executeQuery();
                while (rs.next()) {
                    if ((!rs.getString("no_ktp").equals("")) && rs.getString("id_specimen").equals("")) {
                        try {
                            idpasien = cekViaSatuSehat.tampilIDPasien(rs.getString("no_ktp"));
                            try {
                                headers = new HttpHeaders();
                                headers.setContentType(MediaType.APPLICATION_JSON);
                                headers.add("Authorization", "Bearer " + api.TokenSatuSehat());
                                json = "{"
                                        + "\"resourceType\": \"Specimen\","
                                        + "\"identifier\": ["
                                        + "{"
                                        + "\"system\": \"http://sys-ids.kemkes.go.id/specimen/" + koneksiDB.IDSATUSEHAT() + "\","
                                        + "\"value\": \"" + rs.getString("noorder") + "\""
                                        + "}"
                                        + "],"
                                        + "\"status\": \"available\","
                                        + "\"type\": {"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"system\": \"" + rs.getString("sampel_system") + "\","
                                        + "\"code\": \"" + rs.getString("sampel_code") + "\","
                                        + "\"display\": \"" + rs.getString("sampel_display") + "\""
                                        + "}"
                                        + "]"
                                        + "},"
                                        + "\"subject\": {"
                                        + "\"reference\": \"Patient/" + idpasien + "\","
                                        + "\"display\": \"" + rs.getString("nm_pasien") + "\""
                                        + "},"
                                        + "\"request\": ["
                                        + "{"
                                        + "\"reference\": \"ServiceRequest/" + rs.getString("id_servicerequest") + "\""
                                        + "}"
                                        + "],"
                                        + "\"receivedTime\": \"" + rs.getString("tgl_sampel") + "T" + rs.getString("jam_sampel") + "+07:00\""
                                        + "}";
                                TeksArea.append("URL : " + link + "/Specimen");
                                TeksArea.append("Request JSON : " + json);
                                requestEntity = new HttpEntity(json, headers);
                                json = api.getRest().exchange(link + "/Specimen", HttpMethod.POST, requestEntity, String.class).getBody();
                                TeksArea.append("Result JSON : " + json);
                                root = mapper.readTree(json);
                                response = root.path("id");
                                if (!response.asText().equals("")) {
                                    Sequel.menyimpan2("satu_sehat_specimen_radiologi", "?,?,?", "No.Rawat", 3, new String[]{
                                        rs.getString("noorder"), rs.getString("kd_jenis_prw"), response.asText()
                                    });
                                }
                            } catch (Exception ea) {
                                System.out.println("Notifikasi Bridging : " + ea);
                            }
                        } catch (Exception ef) {
                            System.out.println("Notifikasi : " + ef);
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Notif : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }

        try {
            ps = koneksi.prepareStatement(
                    "select pasien.nm_pasien,pasien.no_ktp,permintaan_radiologi.noorder,permintaan_radiologi.tgl_sampel,permintaan_radiologi.jam_sampel,"
                    + "satu_sehat_mapping_radiologi.sampel_code,satu_sehat_mapping_radiologi.sampel_system,satu_sehat_mapping_radiologi.sampel_display,satu_sehat_servicerequest_radiologi.id_servicerequest,"
                    + "permintaan_pemeriksaan_radiologi.kd_jenis_prw,ifnull(satu_sehat_specimen_radiologi.id_specimen,'') as id_specimen "
                    + "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis inner join permintaan_radiologi on permintaan_radiologi.no_rawat=reg_periksa.no_rawat "
                    + "inner join permintaan_pemeriksaan_radiologi on permintaan_pemeriksaan_radiologi.noorder=permintaan_radiologi.noorder "
                    + "inner join jns_perawatan_radiologi on jns_perawatan_radiologi.kd_jenis_prw=permintaan_pemeriksaan_radiologi.kd_jenis_prw "
                    + "inner join satu_sehat_mapping_radiologi on satu_sehat_mapping_radiologi.kd_jenis_prw=jns_perawatan_radiologi.kd_jenis_prw "
                    + "inner join satu_sehat_servicerequest_radiologi on satu_sehat_servicerequest_radiologi.noorder=permintaan_pemeriksaan_radiologi.noorder "
                    + "and satu_sehat_servicerequest_radiologi.kd_jenis_prw=permintaan_pemeriksaan_radiologi.kd_jenis_prw "
                    + "left join satu_sehat_specimen_radiologi on satu_sehat_servicerequest_radiologi.noorder=satu_sehat_specimen_radiologi.noorder "
                    + "and satu_sehat_servicerequest_radiologi.kd_jenis_prw=satu_sehat_specimen_radiologi.kd_jenis_prw "
                    + "inner join nota_inap on nota_inap.no_rawat=reg_periksa.no_rawat "
                    + "where nota_inap.tanggal between ? and ? ");
            try {
                ps.setString(1, Tanggal1.getText());
                ps.setString(2, Tanggal2.getText());
                rs = ps.executeQuery();
                while (rs.next()) {
                    if ((!rs.getString("no_ktp").equals("")) && rs.getString("id_specimen").equals("")) {
                        try {
                            idpasien = cekViaSatuSehat.tampilIDPasien(rs.getString("no_ktp"));
                            try {
                                headers = new HttpHeaders();
                                headers.setContentType(MediaType.APPLICATION_JSON);
                                headers.add("Authorization", "Bearer " + api.TokenSatuSehat());
                                json = "{"
                                        + "\"resourceType\": \"Specimen\","
                                        + "\"identifier\": ["
                                        + "{"
                                        + "\"system\": \"http://sys-ids.kemkes.go.id/specimen/" + koneksiDB.IDSATUSEHAT() + "\","
                                        + "\"value\": \"" + rs.getString("noorder") + "\""
                                        + "}"
                                        + "],"
                                        + "\"status\": \"available\","
                                        + "\"type\": {"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"system\": \"" + rs.getString("sampel_system") + "\","
                                        + "\"code\": \"" + rs.getString("sampel_code") + "\","
                                        + "\"display\": \"" + rs.getString("sampel_display") + "\""
                                        + "}"
                                        + "]"
                                        + "},"
                                        + "\"subject\": {"
                                        + "\"reference\": \"Patient/" + idpasien + "\","
                                        + "\"display\": \"" + rs.getString("nm_pasien") + "\""
                                        + "},"
                                        + "\"request\": ["
                                        + "{"
                                        + "\"reference\": \"ServiceRequest/" + rs.getString("id_servicerequest") + "\""
                                        + "}"
                                        + "],"
                                        + "\"receivedTime\": \"" + rs.getString("tgl_sampel") + "T" + rs.getString("jam_sampel") + "+07:00\""
                                        + "}";
                                TeksArea.append("URL : " + link + "/Specimen");
                                TeksArea.append("Request JSON : " + json);
                                requestEntity = new HttpEntity(json, headers);
                                json = api.getRest().exchange(link + "/Specimen", HttpMethod.POST, requestEntity, String.class).getBody();
                                TeksArea.append("Result JSON : " + json);
                                root = mapper.readTree(json);
                                response = root.path("id");
                                if (!response.asText().equals("")) {
                                    Sequel.menyimpan2("satu_sehat_specimen_radiologi", "?,?,?", "No.Rawat", 3, new String[]{
                                        rs.getString("noorder"), rs.getString("kd_jenis_prw"), response.asText()
                                    });
                                }
                            } catch (Exception ea) {
                                System.out.println("Notifikasi Bridging : " + ea);
                            }
                        } catch (Exception ef) {
                            System.out.println("Notifikasi : " + ef);
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Notif : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
    }

    private void observationradiologi() {
        try {
            ps = koneksi.prepareStatement(
                    "select reg_periksa.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien,pasien.no_ktp,permintaan_radiologi.noorder,"
                    + "permintaan_radiologi.tgl_hasil,permintaan_radiologi.jam_hasil,jns_perawatan_radiologi.nm_perawatan,satu_sehat_mapping_radiologi.code,"
                    + "satu_sehat_mapping_radiologi.system,satu_sehat_mapping_radiologi.display,hasil_radiologi.hasil,permintaan_pemeriksaan_radiologi.kd_jenis_prw,"
                    + "satu_sehat_specimen_radiologi.id_specimen,pegawai.no_ktp as ktppraktisi,satu_sehat_encounter.id_encounter,"
                    + "ifnull(satu_sehat_observation_radiologi.id_observation,'') as id_observation "
                    + "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis inner join permintaan_radiologi on permintaan_radiologi.no_rawat=reg_periksa.no_rawat "
                    + "inner join permintaan_pemeriksaan_radiologi on permintaan_pemeriksaan_radiologi.noorder=permintaan_radiologi.noorder "
                    + "inner join jns_perawatan_radiologi on jns_perawatan_radiologi.kd_jenis_prw=permintaan_pemeriksaan_radiologi.kd_jenis_prw "
                    + "inner join satu_sehat_mapping_radiologi on satu_sehat_mapping_radiologi.kd_jenis_prw=jns_perawatan_radiologi.kd_jenis_prw "
                    + "inner join satu_sehat_specimen_radiologi on satu_sehat_specimen_radiologi.noorder=permintaan_pemeriksaan_radiologi.noorder "
                    + "and satu_sehat_specimen_radiologi.kd_jenis_prw=permintaan_pemeriksaan_radiologi.kd_jenis_prw "
                    + "inner join periksa_radiologi on periksa_radiologi.no_rawat=permintaan_radiologi.no_rawat and periksa_radiologi.tgl_periksa=permintaan_radiologi.tgl_hasil "
                    + "and periksa_radiologi.jam=permintaan_radiologi.jam_hasil and periksa_radiologi.dokter_perujuk=permintaan_radiologi.dokter_perujuk "
                    + "inner join hasil_radiologi on periksa_radiologi.no_rawat=hasil_radiologi.no_rawat and periksa_radiologi.tgl_periksa=hasil_radiologi.tgl_periksa "
                    + "and periksa_radiologi.jam=hasil_radiologi.jam "
                    + "left join satu_sehat_observation_radiologi on satu_sehat_specimen_radiologi.noorder=satu_sehat_observation_radiologi.noorder "
                    + "and satu_sehat_specimen_radiologi.kd_jenis_prw=satu_sehat_observation_radiologi.kd_jenis_prw "
                    + "inner join nota_jalan on nota_jalan.no_rawat=reg_periksa.no_rawat inner join satu_sehat_encounter on satu_sehat_encounter.no_rawat=reg_periksa.no_rawat "
                    + "inner join pegawai on periksa_radiologi.kd_dokter=pegawai.nik "
                    + "where nota_jalan.tanggal between ? and ? ");
            try {
                ps.setString(1, Tanggal1.getText());
                ps.setString(2, Tanggal2.getText());
                rs = ps.executeQuery();
                while (rs.next()) {
                    if ((!rs.getString("no_ktp").equals("")) && (!rs.getString("ktppraktisi").equals("")) && rs.getString("id_observation").equals("")) {
                        try {
                            iddokter = cekViaSatuSehat.tampilIDParktisi(rs.getString("ktppraktisi"));
                            idpasien = cekViaSatuSehat.tampilIDPasien(rs.getString("no_ktp"));
                            try {
                                headers = new HttpHeaders();
                                headers.setContentType(MediaType.APPLICATION_JSON);
                                headers.add("Authorization", "Bearer " + api.TokenSatuSehat());
                                json = "{"
                                        + "\"resourceType\": \"Observation\","
                                        + "\"identifier\": ["
                                        + "{"
                                        + "\"system\": \"http://sys-ids.kemkes.go.id/servicerequest/" + koneksiDB.IDSATUSEHAT() + "\","
                                        + "\"value\": \"" + rs.getString("noorder") + "\""
                                        + "}"
                                        + "],"
                                        + "\"status\": \"final\","
                                        + "\"category\": ["
                                        + "{"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"system\": \"http://terminology.hl7.org/CodeSystem/observation-category\","
                                        + "\"code\": \"imaging\","
                                        + "\"display\": \"Imaging\""
                                        + "}"
                                        + "]"
                                        + "}"
                                        + "],"
                                        + "\"code\": {"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"system\": \"" + rs.getString("system") + "\","
                                        + "\"code\": \"" + rs.getString("code") + "\","
                                        + "\"display\": \"" + rs.getString("display") + "\""
                                        + "}"
                                        + "]"
                                        + "},"
                                        + "\"subject\": {"
                                        + "\"reference\": \"Patient/" + idpasien + "\""
                                        + "},"
                                        + "\"performer\": ["
                                        + "{"
                                        + "\"reference\": \"Practitioner/" + iddokter + "\""
                                        + "}"
                                        + "],"
                                        + "\"encounter\": {"
                                        + "\"reference\": \"Encounter/" + rs.getString("id_encounter") + "\","
                                        + "\"display\": \"Hasil Pemeriksaan Radiologi " + rs.getString("nm_perawatan") + " No.Rawat " + rs.getString("no_rawat") + ", Atas Nama Pasien " + rs.getString("nm_pasien") + ", No.RM " + rs.getString("no_rkm_medis") + ", Pada Tanggal " + rs.getString("tgl_hasil") + " " + rs.getString("jam_hasil") + "\""
                                        + "},"
                                        + "\"specimen\": {"
                                        + "\"reference\": \"Specimen/" + rs.getString("id_specimen") + "\""
                                        + "},"
                                        + "\"effectiveDateTime\": \"" + rs.getString("tgl_hasil") + "T" + rs.getString("jam_hasil") + "+07:00\","
                                        + "\"valueString\": \"" + rs.getString("hasil") + "\""
                                        + "}";
                                TeksArea.append("URL : " + link + "/Observation");
                                TeksArea.append("Request JSON : " + json);
                                requestEntity = new HttpEntity(json, headers);
                                json = api.getRest().exchange(link + "/Observation", HttpMethod.POST, requestEntity, String.class).getBody();
                                TeksArea.append("Result JSON : " + json);
                                root = mapper.readTree(json);
                                response = root.path("id");
                                if (!response.asText().equals("")) {
                                    Sequel.menyimpan2("satu_sehat_observation_radiologi", "?,?,?", "No.Order", 3, new String[]{
                                        rs.getString("noorder"), rs.getString("kd_jenis_prw"), response.asText()
                                    });
                                }
                            } catch (Exception ea) {
                                System.out.println("Notifikasi Bridging : " + ea);
                            }
                        } catch (Exception ef) {
                            System.out.println("Notifikasi : " + ef);
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Notif : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }

        try {
            ps = koneksi.prepareStatement(
                    "select reg_periksa.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien,pasien.no_ktp,permintaan_radiologi.noorder,"
                    + "permintaan_radiologi.tgl_hasil,permintaan_radiologi.jam_hasil,jns_perawatan_radiologi.nm_perawatan,satu_sehat_mapping_radiologi.code,"
                    + "satu_sehat_mapping_radiologi.system,satu_sehat_mapping_radiologi.display,hasil_radiologi.hasil,permintaan_pemeriksaan_radiologi.kd_jenis_prw,"
                    + "satu_sehat_specimen_radiologi.id_specimen,pegawai.no_ktp as ktppraktisi,satu_sehat_encounter.id_encounter,"
                    + "ifnull(satu_sehat_observation_radiologi.id_observation,'') as id_observation "
                    + "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis inner join permintaan_radiologi on permintaan_radiologi.no_rawat=reg_periksa.no_rawat "
                    + "inner join permintaan_pemeriksaan_radiologi on permintaan_pemeriksaan_radiologi.noorder=permintaan_radiologi.noorder "
                    + "inner join jns_perawatan_radiologi on jns_perawatan_radiologi.kd_jenis_prw=permintaan_pemeriksaan_radiologi.kd_jenis_prw "
                    + "inner join satu_sehat_mapping_radiologi on satu_sehat_mapping_radiologi.kd_jenis_prw=jns_perawatan_radiologi.kd_jenis_prw "
                    + "inner join satu_sehat_specimen_radiologi on satu_sehat_specimen_radiologi.noorder=permintaan_pemeriksaan_radiologi.noorder "
                    + "and satu_sehat_specimen_radiologi.kd_jenis_prw=permintaan_pemeriksaan_radiologi.kd_jenis_prw "
                    + "inner join periksa_radiologi on periksa_radiologi.no_rawat=permintaan_radiologi.no_rawat and periksa_radiologi.tgl_periksa=permintaan_radiologi.tgl_hasil "
                    + "and periksa_radiologi.jam=permintaan_radiologi.jam_hasil and periksa_radiologi.dokter_perujuk=permintaan_radiologi.dokter_perujuk "
                    + "inner join hasil_radiologi on periksa_radiologi.no_rawat=hasil_radiologi.no_rawat and periksa_radiologi.tgl_periksa=hasil_radiologi.tgl_periksa "
                    + "and periksa_radiologi.jam=hasil_radiologi.jam "
                    + "left join satu_sehat_observation_radiologi on satu_sehat_specimen_radiologi.noorder=satu_sehat_observation_radiologi.noorder "
                    + "and satu_sehat_specimen_radiologi.kd_jenis_prw=satu_sehat_observation_radiologi.kd_jenis_prw "
                    + "inner join nota_inap on nota_inap.no_rawat=reg_periksa.no_rawat inner join satu_sehat_encounter on satu_sehat_encounter.no_rawat=reg_periksa.no_rawat "
                    + "inner join pegawai on periksa_radiologi.kd_dokter=pegawai.nik "
                    + "where nota_inap.tanggal between ? and ? ");
            try {
                ps.setString(1, Tanggal1.getText());
                ps.setString(2, Tanggal2.getText());
                rs = ps.executeQuery();
                while (rs.next()) {
                    if ((!rs.getString("no_ktp").equals("")) && (!rs.getString("ktppraktisi").equals("")) && rs.getString("id_observation").equals("")) {
                        try {
                            iddokter = cekViaSatuSehat.tampilIDParktisi(rs.getString("ktppraktisi"));
                            idpasien = cekViaSatuSehat.tampilIDPasien(rs.getString("no_ktp"));
                            try {
                                headers = new HttpHeaders();
                                headers.setContentType(MediaType.APPLICATION_JSON);
                                headers.add("Authorization", "Bearer " + api.TokenSatuSehat());
                                json = "{"
                                        + "\"resourceType\": \"Observation\","
                                        + "\"identifier\": ["
                                        + "{"
                                        + "\"system\": \"http://sys-ids.kemkes.go.id/servicerequest/" + koneksiDB.IDSATUSEHAT() + "\","
                                        + "\"value\": \"" + rs.getString("noorder") + "\""
                                        + "}"
                                        + "],"
                                        + "\"status\": \"final\","
                                        + "\"category\": ["
                                        + "{"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"system\": \"http://terminology.hl7.org/CodeSystem/observation-category\","
                                        + "\"code\": \"imaging\","
                                        + "\"display\": \"Imaging\""
                                        + "}"
                                        + "]"
                                        + "}"
                                        + "],"
                                        + "\"code\": {"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"system\": \"" + rs.getString("system") + "\","
                                        + "\"code\": \"" + rs.getString("code") + "\","
                                        + "\"display\": \"" + rs.getString("display") + "\""
                                        + "}"
                                        + "]"
                                        + "},"
                                        + "\"subject\": {"
                                        + "\"reference\": \"Patient/" + idpasien + "\""
                                        + "},"
                                        + "\"performer\": ["
                                        + "{"
                                        + "\"reference\": \"Practitioner/" + iddokter + "\""
                                        + "}"
                                        + "],"
                                        + "\"encounter\": {"
                                        + "\"reference\": \"Encounter/" + rs.getString("id_encounter") + "\","
                                        + "\"display\": \"Hasil Pemeriksaan Radiologi " + rs.getString("nm_perawatan") + " No.Rawat " + rs.getString("no_rawat") + ", Atas Nama Pasien " + rs.getString("nm_pasien") + ", No.RM " + rs.getString("no_rkm_medis") + ", Pada Tanggal " + rs.getString("tgl_hasil") + " " + rs.getString("jam_hasil") + "\""
                                        + "},"
                                        + "\"specimen\": {"
                                        + "\"reference\": \"Specimen/" + rs.getString("id_specimen") + "\""
                                        + "},"
                                        + "\"effectiveDateTime\": \"" + rs.getString("tgl_hasil") + "T" + rs.getString("jam_hasil") + "+07:00\","
                                        + "\"valueString\": \"" + rs.getString("hasil") + "\""
                                        + "}";
                                TeksArea.append("URL : " + link + "/Observation");
                                TeksArea.append("Request JSON : " + json);
                                requestEntity = new HttpEntity(json, headers);
                                json = api.getRest().exchange(link + "/Observation", HttpMethod.POST, requestEntity, String.class).getBody();
                                TeksArea.append("Result JSON : " + json);
                                root = mapper.readTree(json);
                                response = root.path("id");
                                if (!response.asText().equals("")) {
                                    Sequel.menyimpan2("satu_sehat_observation_radiologi", "?,?,?", "No.Order", 3, new String[]{
                                        rs.getString("noorder"), rs.getString("kd_jenis_prw"), response.asText()
                                    });
                                }
                            } catch (Exception ea) {
                                System.out.println("Notifikasi Bridging : " + ea);
                            }
                        } catch (Exception ef) {
                            System.out.println("Notifikasi : " + ef);
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Notif : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
    }

    private void diagnosticreportradiologi() {
        try {
            ps = koneksi.prepareStatement(
                    "select reg_periksa.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien,pasien.no_ktp,periksa_radiologi.kd_dokter,pegawai.nama,pegawai.no_ktp as ktpdokter,"
                    + "satu_sehat_encounter.id_encounter,permintaan_radiologi.noorder,permintaan_radiologi.tgl_hasil,permintaan_radiologi.jam_hasil,permintaan_radiologi.diagnosa_klinis,"
                    + "jns_perawatan_radiologi.nm_perawatan,satu_sehat_mapping_radiologi.code,satu_sehat_mapping_radiologi.system,satu_sehat_mapping_radiologi.display,"
                    + "satu_sehat_servicerequest_radiologi.id_servicerequest,permintaan_pemeriksaan_radiologi.kd_jenis_prw,satu_sehat_specimen_radiologi.id_specimen,"
                    + "satu_sehat_observation_radiologi.id_observation,ifnull(satu_sehat_diagnosticreport_radiologi.id_diagnosticreport,'') as id_diagnosticreport,hasil_radiologi.hasil "
                    + "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "
                    + "inner join satu_sehat_encounter on satu_sehat_encounter.no_rawat=reg_periksa.no_rawat inner join permintaan_radiologi on permintaan_radiologi.no_rawat=reg_periksa.no_rawat "
                    + "inner join permintaan_pemeriksaan_radiologi on permintaan_pemeriksaan_radiologi.noorder=permintaan_radiologi.noorder "
                    + "inner join jns_perawatan_radiologi on jns_perawatan_radiologi.kd_jenis_prw=permintaan_pemeriksaan_radiologi.kd_jenis_prw "
                    + "inner join satu_sehat_mapping_radiologi on satu_sehat_mapping_radiologi.kd_jenis_prw=jns_perawatan_radiologi.kd_jenis_prw "
                    + "inner join satu_sehat_servicerequest_radiologi on satu_sehat_servicerequest_radiologi.noorder=permintaan_pemeriksaan_radiologi.noorder "
                    + "and satu_sehat_servicerequest_radiologi.kd_jenis_prw=permintaan_pemeriksaan_radiologi.kd_jenis_prw "
                    + "inner join satu_sehat_specimen_radiologi on satu_sehat_servicerequest_radiologi.noorder=satu_sehat_specimen_radiologi.noorder "
                    + "and satu_sehat_servicerequest_radiologi.kd_jenis_prw=satu_sehat_specimen_radiologi.kd_jenis_prw "
                    + "inner join periksa_radiologi on periksa_radiologi.no_rawat=permintaan_radiologi.no_rawat and periksa_radiologi.tgl_periksa=permintaan_radiologi.tgl_hasil "
                    + "and periksa_radiologi.jam=permintaan_radiologi.jam_hasil and periksa_radiologi.dokter_perujuk=permintaan_radiologi.dokter_perujuk "
                    + "inner join hasil_radiologi on periksa_radiologi.no_rawat=hasil_radiologi.no_rawat and periksa_radiologi.tgl_periksa=hasil_radiologi.tgl_periksa "
                    + "and periksa_radiologi.jam=hasil_radiologi.jam "
                    + "inner join satu_sehat_observation_radiologi on satu_sehat_specimen_radiologi.noorder=satu_sehat_observation_radiologi.noorder "
                    + "and satu_sehat_specimen_radiologi.kd_jenis_prw=satu_sehat_observation_radiologi.kd_jenis_prw "
                    + "left join satu_sehat_diagnosticreport_radiologi on satu_sehat_servicerequest_radiologi.noorder=satu_sehat_diagnosticreport_radiologi.noorder "
                    + "and satu_sehat_servicerequest_radiologi.kd_jenis_prw=satu_sehat_diagnosticreport_radiologi.kd_jenis_prw "
                    + "inner join nota_jalan on nota_jalan.no_rawat=reg_periksa.no_rawat "
                    + "inner join pegawai on periksa_radiologi.kd_dokter=pegawai.nik "
                    + "where nota_jalan.tanggal between ? and ? ");
            try {
                ps.setString(1, Tanggal1.getText());
                ps.setString(2, Tanggal2.getText());
                rs = ps.executeQuery();
                while (rs.next()) {
                    if ((!rs.getString("no_ktp").equals("")) && (!rs.getString("ktpdokter").equals("")) && rs.getString("id_diagnosticreport").equals("")) {
                        try {
                            iddokter = cekViaSatuSehat.tampilIDParktisi(rs.getString("ktpdokter"));
                            idpasien = cekViaSatuSehat.tampilIDPasien(rs.getString("no_ktp"));
                            try {
                                headers = new HttpHeaders();
                                headers.setContentType(MediaType.APPLICATION_JSON);
                                headers.add("Authorization", "Bearer " + api.TokenSatuSehat());
                                json = "{"
                                        + "\"resourceType\": \"DiagnosticReport\","
                                        + "\"identifier\": ["
                                        + "{"
                                        + "\"system\": \"http://sys-ids.kemkes.go.id/diagnostic/" + koneksiDB.IDSATUSEHAT() + "/rad\","
                                        + "\"use\": \"official\","
                                        + "\"value\": \"" + rs.getString("noorder") + "\""
                                        + "}"
                                        + "],"
                                        + "\"status\": \"final\","
                                        + "\"category\": ["
                                        + "{"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"system\": \"http://terminology.hl7.org/CodeSystem/v2-0074\","
                                        + "\"code\": \"RAD\","
                                        + "\"display\": \"Radiology\""
                                        + "}"
                                        + "]"
                                        + "}"
                                        + "],"
                                        + "\"code\": {"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"code\": \"" + rs.getString("code") + "\","
                                        + "\"display\": \"" + rs.getString("display") + "\","
                                        + "\"system\": \"" + rs.getString("system") + "\""
                                        + "}"
                                        + "]"
                                        + "},"
                                        + "\"subject\": {"
                                        + "\"reference\": \"Patient/" + idpasien + "\""
                                        + "},"
                                        + "\"encounter\": {"
                                        + "\"reference\": \"Encounter/" + rs.getString("id_encounter") + "\""
                                        + "},"
                                        + "\"effectiveDateTime\": \"" + rs.getString("tgl_hasil") + "T" + rs.getString("jam_hasil") + "+07:00\","
                                        + "\"issued\": \"" + rs.getString("tgl_hasil") + "T" + rs.getString("jam_hasil") + "+07:00\","
                                        + "\"performer\": ["
                                        + "{"
                                        + "\"reference\": \"Practitioner/" + iddokter + "\""
                                        + "}"
                                        + "],"
                                        + "\"specimen\": [{"
                                        + "\"reference\": \"Specimen/" + rs.getString("id_specimen") + "\""
                                        + "}],"
                                        + "\"result\": ["
                                        + "{"
                                        + "\"reference\": \"Observation/" + rs.getString("id_observation") + "\""
                                        + "}"
                                        + "],"
                                        + "\"basedOn\": ["
                                        + "{"
                                        + "\"reference\": \"ServiceRequest/" + rs.getString("id_servicerequest") + "\""
                                        + "}"
                                        + "],"
                                        + "\"conclusion\": \"" + rs.getString("hasil") + "\""
                                        + "}";
                                TeksArea.append("URL : " + link + "/DiagnosticReport");
                                TeksArea.append("Request JSON : " + json);
                                requestEntity = new HttpEntity(json, headers);
                                json = api.getRest().exchange(link + "/DiagnosticReport", HttpMethod.POST, requestEntity, String.class).getBody();
                                TeksArea.append("Result JSON : " + json);
                                root = mapper.readTree(json);
                                response = root.path("id");
                                if (!response.asText().equals("")) {
                                    Sequel.menyimpan2("satu_sehat_diagnosticreport_radiologi", "?,?,?", "No.Order", 3, new String[]{
                                        rs.getString("noorder"), rs.getString("kd_jenis_prw"), response.asText()
                                    });
                                }
                            } catch (Exception ea) {
                                System.out.println("Notifikasi Bridging : " + ea);
                            }
                        } catch (Exception ef) {
                            System.out.println("Notifikasi : " + ef);
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Notif : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }

        try {
            ps = koneksi.prepareStatement(
                    "select reg_periksa.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien,pasien.no_ktp,periksa_radiologi.kd_dokter,pegawai.nama,pegawai.no_ktp as ktpdokter,"
                    + "satu_sehat_encounter.id_encounter,permintaan_radiologi.noorder,permintaan_radiologi.tgl_hasil,permintaan_radiologi.jam_hasil,permintaan_radiologi.diagnosa_klinis,"
                    + "jns_perawatan_radiologi.nm_perawatan,satu_sehat_mapping_radiologi.code,satu_sehat_mapping_radiologi.system,satu_sehat_mapping_radiologi.display,"
                    + "satu_sehat_servicerequest_radiologi.id_servicerequest,permintaan_pemeriksaan_radiologi.kd_jenis_prw,satu_sehat_specimen_radiologi.id_specimen,"
                    + "satu_sehat_observation_radiologi.id_observation,ifnull(satu_sehat_diagnosticreport_radiologi.id_diagnosticreport,'') as id_diagnosticreport,hasil_radiologi.hasil "
                    + "from reg_periksa inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "
                    + "inner join satu_sehat_encounter on satu_sehat_encounter.no_rawat=reg_periksa.no_rawat inner join permintaan_radiologi on permintaan_radiologi.no_rawat=reg_periksa.no_rawat "
                    + "inner join permintaan_pemeriksaan_radiologi on permintaan_pemeriksaan_radiologi.noorder=permintaan_radiologi.noorder "
                    + "inner join jns_perawatan_radiologi on jns_perawatan_radiologi.kd_jenis_prw=permintaan_pemeriksaan_radiologi.kd_jenis_prw "
                    + "inner join satu_sehat_mapping_radiologi on satu_sehat_mapping_radiologi.kd_jenis_prw=jns_perawatan_radiologi.kd_jenis_prw "
                    + "inner join satu_sehat_servicerequest_radiologi on satu_sehat_servicerequest_radiologi.noorder=permintaan_pemeriksaan_radiologi.noorder "
                    + "and satu_sehat_servicerequest_radiologi.kd_jenis_prw=permintaan_pemeriksaan_radiologi.kd_jenis_prw "
                    + "inner join satu_sehat_specimen_radiologi on satu_sehat_servicerequest_radiologi.noorder=satu_sehat_specimen_radiologi.noorder "
                    + "and satu_sehat_servicerequest_radiologi.kd_jenis_prw=satu_sehat_specimen_radiologi.kd_jenis_prw "
                    + "inner join periksa_radiologi on periksa_radiologi.no_rawat=permintaan_radiologi.no_rawat and periksa_radiologi.tgl_periksa=permintaan_radiologi.tgl_hasil "
                    + "and periksa_radiologi.jam=permintaan_radiologi.jam_hasil and periksa_radiologi.dokter_perujuk=permintaan_radiologi.dokter_perujuk "
                    + "inner join hasil_radiologi on periksa_radiologi.no_rawat=hasil_radiologi.no_rawat and periksa_radiologi.tgl_periksa=hasil_radiologi.tgl_periksa "
                    + "and periksa_radiologi.jam=hasil_radiologi.jam "
                    + "inner join satu_sehat_observation_radiologi on satu_sehat_specimen_radiologi.noorder=satu_sehat_observation_radiologi.noorder "
                    + "and satu_sehat_specimen_radiologi.kd_jenis_prw=satu_sehat_observation_radiologi.kd_jenis_prw "
                    + "left join satu_sehat_diagnosticreport_radiologi on satu_sehat_servicerequest_radiologi.noorder=satu_sehat_diagnosticreport_radiologi.noorder "
                    + "and satu_sehat_servicerequest_radiologi.kd_jenis_prw=satu_sehat_diagnosticreport_radiologi.kd_jenis_prw "
                    + "inner join nota_inap on nota_inap.no_rawat=reg_periksa.no_rawat "
                    + "inner join pegawai on periksa_radiologi.kd_dokter=pegawai.nik "
                    + "where nota_inap.tanggal between ? and ? ");
            try {
                ps.setString(1, Tanggal1.getText());
                ps.setString(2, Tanggal2.getText());
                rs = ps.executeQuery();
                while (rs.next()) {
                    if ((!rs.getString("no_ktp").equals("")) && (!rs.getString("ktpdokter").equals("")) && rs.getString("id_diagnosticreport").equals("")) {
                        try {
                            iddokter = cekViaSatuSehat.tampilIDParktisi(rs.getString("ktpdokter"));
                            idpasien = cekViaSatuSehat.tampilIDPasien(rs.getString("no_ktp"));
                            try {
                                headers = new HttpHeaders();
                                headers.setContentType(MediaType.APPLICATION_JSON);
                                headers.add("Authorization", "Bearer " + api.TokenSatuSehat());
                                json = "{"
                                        + "\"resourceType\": \"DiagnosticReport\","
                                        + "\"identifier\": ["
                                        + "{"
                                        + "\"system\": \"http://sys-ids.kemkes.go.id/diagnostic/" + koneksiDB.IDSATUSEHAT() + "/rad\","
                                        + "\"use\": \"official\","
                                        + "\"value\": \"" + rs.getString("noorder") + "\""
                                        + "}"
                                        + "],"
                                        + "\"status\": \"final\","
                                        + "\"category\": ["
                                        + "{"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"system\": \"http://terminology.hl7.org/CodeSystem/v2-0074\","
                                        + "\"code\": \"RAD\","
                                        + "\"display\": \"Radiology\""
                                        + "}"
                                        + "]"
                                        + "}"
                                        + "],"
                                        + "\"code\": {"
                                        + "\"coding\": ["
                                        + "{"
                                        + "\"code\": \"" + rs.getString("code") + "\","
                                        + "\"display\": \"" + rs.getString("display") + "\","
                                        + "\"system\": \"" + rs.getString("system") + "\""
                                        + "}"
                                        + "]"
                                        + "},"
                                        + "\"subject\": {"
                                        + "\"reference\": \"Patient/" + idpasien + "\""
                                        + "},"
                                        + "\"encounter\": {"
                                        + "\"reference\": \"Encounter/" + rs.getString("id_encounter") + "\""
                                        + "},"
                                        + "\"effectiveDateTime\": \"" + rs.getString("tgl_hasil") + "T" + rs.getString("jam_hasil") + "+07:00\","
                                        + "\"issued\": \"" + rs.getString("tgl_hasil") + "T" + rs.getString("jam_hasil") + "+07:00\","
                                        + "\"performer\": ["
                                        + "{"
                                        + "\"reference\": \"Practitioner/" + iddokter + "\""
                                        + "}"
                                        + "],"
                                        + "\"specimen\": [{"
                                        + "\"reference\": \"Specimen/" + rs.getString("id_specimen") + "\""
                                        + "}],"
                                        + "\"result\": ["
                                        + "{"
                                        + "\"reference\": \"Observation/" + rs.getString("id_observation") + "\""
                                        + "}"
                                        + "],"
                                        + "\"basedOn\": ["
                                        + "{"
                                        + "\"reference\": \"ServiceRequest/" + rs.getString("id_servicerequest") + "\""
                                        + "}"
                                        + "],"
                                        + "\"conclusion\": \"" + rs.getString("hasil") + "\""
                                        + "}";
                                TeksArea.append("URL : " + link + "/DiagnosticReport");
                                TeksArea.append("Request JSON : " + json);
                                requestEntity = new HttpEntity(json, headers);
                                json = api.getRest().exchange(link + "/DiagnosticReport", HttpMethod.POST, requestEntity, String.class).getBody();
                                TeksArea.append("Result JSON : " + json);
                                root = mapper.readTree(json);
                                response = root.path("id");
                                if (!response.asText().equals("")) {
                                    Sequel.menyimpan2("satu_sehat_diagnosticreport_radiologi", "?,?,?", "No.Order", 3, new String[]{
                                        rs.getString("noorder"), rs.getString("kd_jenis_prw"), response.asText()
                                    });
                                }
                            } catch (Exception ea) {
                                System.out.println("Notifikasi Bridging : " + ea);
                            }
                        } catch (Exception ef) {
                            System.out.println("Notifikasi : " + ef);
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Notif : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
    }

    public void AllergyIntolerance(String norawat) {
        try {
            ps = koneksi.prepareStatement(
                    "SELECT\n"
                    + "	reg_periksa.no_rawat, \n"
                    + "	pasien.nm_pasien, \n"
                    + "	pasien.no_ktp, \n"
                    + "	pegawai.nama, \n"
                    + "	pegawai.no_ktp AS ktpdokter, \n"
                    + "	poliklinik.nm_poli, \n"
                    + "	satu_sehat_mapping_lokasi_ralan.id_lokasi_satusehat, \n"
                    + "	reg_periksa.stts, \n"
                    + "	reg_periksa.status_lanjut, \n"
                    + "	reg_periksa.kd_dokter,"
                    + " reg_periksa.kd_poli, \n"
                    + "	reg_periksa.no_rkm_medis, \n"
                    + "	satu_sehat_encounter.id_encounter, \n"
                    + "	alergi_pasien.allergy_code, \n"
                    + "	satu_sehat_ref_allergy.system, \n"
                    + "	satu_sehat_ref_allergy.display, \n"
                    + "	alergi_pasien.note, \n"
                    + "	DATE_FORMAT(alergi_pasien.tgl_perawatan, '%Y-%m-%dT%H:%i:%s+00:00') AS tgl_perawatan, \n"
                    + "	alergi_pasien.nippetugas, \n"
                    + "	petugas.nama AS petugasallergy, \n"
                    + "	alergi_pasien.category,"
                    + " satu_sehat_allergy.id_allergy \n"
                    + "FROM\n"
                    + "	reg_periksa\n"
                    + "	INNER JOIN\n"
                    + "	pasien\n"
                    + "	ON \n"
                    + "		reg_periksa.no_rkm_medis = pasien.no_rkm_medis\n"
                    + "	INNER JOIN\n"
                    + "	pegawai\n"
                    + "	ON \n"
                    + "		pegawai.nik = reg_periksa.kd_dokter\n"
                    + "	INNER JOIN\n"
                    + "	poliklinik\n"
                    + "	ON \n"
                    + "		reg_periksa.kd_poli = poliklinik.kd_poli\n"
                    + "	INNER JOIN\n"
                    + "	satu_sehat_mapping_lokasi_ralan\n"
                    + "	ON \n"
                    + "		satu_sehat_mapping_lokasi_ralan.kd_poli = poliklinik.kd_poli\n"
                    + "	INNER JOIN\n"
                    + "	satu_sehat_encounter\n"
                    + "	ON \n"
                    + "		satu_sehat_encounter.no_rawat = reg_periksa.no_rawat\n"
                    + "	INNER JOIN\n"
                    + "	alergi_pasien\n"
                    + "	ON \n"
                    + "		reg_periksa.no_rawat = alergi_pasien.no_rawat\n"
                    + "	INNER JOIN\n"
                    + "	satu_sehat_ref_allergy\n"
                    + "	ON \n"
                    + "		alergi_pasien.allergy_code = satu_sehat_ref_allergy.kode\n"
                    + "	INNER JOIN\n"
                    + "	satu_sehat_ref_allergy_reaction\n"
                    + "	ON \n"
                    + "		alergi_pasien.reactioncode = satu_sehat_ref_allergy_reaction.kode\n"
                    + "	LEFT JOIN\n"
                    + "	satu_sehat_allergy\n"
                    + "	ON \n"
                    + "		reg_periksa.no_rawat = satu_sehat_allergy.no_rawat\n"
                    + "	INNER JOIN\n"
                    + "	pegawai AS petugas\n"
                    + "	ON \n"
                    + "		petugas.nik = alergi_pasien.nippetugas  "
                    + "where reg_periksa.no_rawat= ?");
            try {
                ps.setString(1, norawat);

                rs = ps.executeQuery();
                while (rs.next()) {
                    try {
//                    true, rs.getString("no_rawat"), rs.getString("no_rkm_medis"), rs.getString("nm_pasien"),
//                                rs.getString("no_ktp"), rs.getString("kd_dokter"), rs.getString("nama"), rs.getString("ktpdokter"), rs.getString("kd_poli"), rs.getString("nm_poli"),
//                                rs.getString("id_lokasi_satusehat"), rs.getString("stts"), rs.getString("status_lanjut"), rs.getString("tgl_perawatan"), rs.getString("id_encounter"), rs.getString("category"),
//                                rs.getString("allergy_code"), rs.getString("system"), rs.getString("display"), rs.getString("note"), rs.getString("id_allergy")
                        iddokter = cekViaSatuSehat.tampilIDParktisi(rs.getString("ktpdokter"));
                        idpasien = cekViaSatuSehat.tampilIDPasien(rs.getString("no_ktp"));
                        try {
                            headers = new HttpHeaders();
                            headers.setContentType(MediaType.APPLICATION_JSON);
                            headers.add("Authorization", "Bearer " + api.TokenSatuSehat());

                            json = "{\n"
                                    + "    \"resourceType\": \"AllergyIntolerance\",\n"
                                    + "    \"identifier\": [\n"
                                    + "        {\n"
                                    + "            \"system\": \"http://sys-ids.kemkes.go.id/allergy/" + koneksiDB.IDSATUSEHAT() + "\",\n"
                                    + "            \"use\": \"official\",\n"
                                    + "            \"value\": \"" + rs.getString("no_rawat").replaceAll("/", "") + "\"\n"
                                    + "        }\n"
                                    + "    ],\n"
                                    + "    \"clinicalStatus\": {\n"
                                    + "        \"coding\": [\n"
                                    + "            {\n"
                                    + "                \"system\": \"http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical\",\n"
                                    + "                \"code\": \"active\",\n"
                                    + "                \"display\": \"Active\"\n"
                                    + "            }\n"
                                    + "        ]\n"
                                    + "    },\n"
                                    + "    \"verificationStatus\": {\n"
                                    + "        \"coding\": [\n"
                                    + "            {\n"
                                    + "                \"system\": \"http://terminology.hl7.org/CodeSystem/allergyintolerance-verification\",\n"
                                    + "                \"code\": \"confirmed\",\n"
                                    + "                \"display\": \"Confirmed\"\n"
                                    + "            }\n"
                                    + "        ]\n"
                                    + "    },\n"
                                    + "    \"category\": [\n"
                                    + "        \"" + rs.getString("category").toString().replaceAll("Makanan", "food").replaceAll("Medication", "medication").replaceAll("Lingkungan", "environment").replaceAll("Biologis", "biologic") + "\"\n"
                                    + "    ],\n"
                                    + "    \"code\": {\n"
                                    + "        \"coding\": [\n"
                                    + "            {\n"
                                    + "                \"system\": \"" + rs.getString("system") + "\",\n"
                                    + "                \"code\": \"" + rs.getString("allergy_code") + "\",\n"
                                    + "                \"display\": \"" + rs.getString("display") + "\"\n"
                                    + "            }\n"
                                    + "        ],\n"
                                    + "        \"text\": \"" + rs.getString("note") + "\"\n"
                                    + "    },\n"
                                    + "    \"patient\": {\n"
                                    + "        \"reference\": \"Patient/" + idpasien + "\",\n"
                                    + "        \"display\": \"" + rs.getString("nm_pasien") + "\"\n"
                                    + "    },\n"
                                    + "    \"encounter\": {\n"
                                    + "        \"reference\": \"Encounter/" + rs.getString("id_encounter") + "\",\n"
                                    + "        \"display\": \"Kunjungan " + rs.getString("nm_poli") + "\"\n"
                                    + "    },\n"
                                    + "    \"recordedDate\": \"" + rs.getString("tgl_perawatan") + "\",\n"
                                    + "    \"recorder\": {\n"
                                    + "        \"reference\": \"Practitioner/" + iddokter + "\"\n"
                                    + "    }\n"
                                    + "}";
                            System.out.println("URL : " + link + "/AllergyIntolerance");
                            System.out.println("Request JSON : " + json);
                            requestEntity = new HttpEntity(json, headers);
                            json = api.getRest().exchange(link + "/AllergyIntolerance", HttpMethod.POST, requestEntity, String.class).getBody();
                            System.out.println("Result JSON : " + json);
                            root = mapper.readTree(json);
                            response = root.path("id");
                            if (!response.asText().equals("")) {
                                Sequel.menyimpan("satu_sehat_allergy", "?,?", "No.Rawat", 2, new String[]{
                                    rs.getString("no_rawat"), response.asText()
                                });
                            }

                        } catch (HttpClientErrorException | HttpServerErrorException e) {
                            // Handle client and server errors
                            System.err.println("Error Response Status Code: " + e.getStatusCode());
//                            System.err.println("Error Response Body: " + e.getResponseBodyAsString());

                            // You can further parse the error response body if needed
                            ObjectMapper mapper = new ObjectMapper();
                            JsonNode errorResponse = mapper.readTree(e.getResponseBodyAsString());
                            ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
                            String prettyErrorResponse = writer.writeValueAsString(errorResponse);
                            System.err.println("Error Response JSON: \n" + prettyErrorResponse);
                        }
                    } catch (Exception e) {
                        System.out.println("Notifikasi : " + e);
                    }
                }
            } catch (Exception e) {
                System.out.println("Notif : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
    }

    private void taskIDBPJS(String task, String waktu, String norawat) {
        System.out.println("Update taskid : " + task + " dengan waktu : " + waktu);

        try {
            datajam = waktu;
            if (!datajam.equals("") || datajam != null) {
                parsedDate = dateFormat.parse(datajam);
                try {
                    TeksArea.append("Menjalankan WS taskid :" + task + "\n");
                    headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                    utc = String.valueOf(apiBPJS.GetUTCdatetimeAsString());
                    headers.add("x-timestamp", utc);
                    headers.add("x-signature", apiBPJS.getHmac(utc));
                    headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());
                    requestJson = "{"
                            + "\"kodebooking\": \"" + norawat + "\","
                            + "\"taskid\": \"" + task + "\","
                            + "\"waktu\": \"" + parsedDate.getTime() + "\""
                            + "}";
                    TeksArea.append("JSON : " + requestJson + "\n");
                    requestEntity = new HttpEntity(requestJson, headers);
                    URL = linkbpjs + "/antrean/updatewaktu";
                    System.out.println("URL : " + URL);
                    root = mapper.readTree(apiBPJS.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                    nameNode = root.path("metadata");
                    if (!nameNode.path("code").asText().equals("200")) {
                        Sequel.queryu2("delete from referensi_mobilejkn_bpjs_taskid where taskid='2' and no_rawat='" + norawat + "'");
                    }
                    if (nameNode.path("code").asText().equals("200")) {
                        Sequel.mengedit("referensi_mobilejkn_bpjs_taskid_status200", "no_rawat=?", "taskid" + task + "=?", 2, new String[]{datajam, norawat});
                    }
                    TeksArea.append("respon WS BPJS : " + nameNode.path("code").asText() + " " + nameNode.path("message").asText() + "\n");
                } catch (Exception ex) {
                    System.out.println("Notifikasi Bridging Task ID: " + ex);
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi Task ID : " + e);
        }

    }

    // Dynamic handler for all events
    private void handleEvent(String event, JSONObject data) {
        // Extract individual fields from the JSON data
        if (koneksiDB.AKTIFKANREALTIMEKIRIM().equals("aktif")) {
            try {
                String taskID = data.has("TaskID") ? data.getString("TaskID") : "";
                String noRawat = data.has("NoRawat") ? data.getString("NoRawat") : "";
                String waktuTaskID = data.has("WaktuTaskID") ? data.getString("WaktuTaskID") : "";

                // Pass the extracted data to the corresponding function
                switch (event) {
                    case "kirimencounter":

                        if (ChkSatusehat.isSelected() == true) {
                            System.out.println("Triggered event: " + event);
                            kirimencounter(noRawat);  // Example: Pass only NoRawat
                        }
                        break;
                    case "kirimtaskid":

                        if (ChkTaskID.isSelected() == true) {
                            System.out.println("Triggered event: " + event);
                            taskIDBPJS(taskID, waktuTaskID, noRawat);  // Pass all extracted values
                        }

                        break;
                    case "hapusencounter":
                        System.out.println("Triggered event: " + event);
                        break;
                    case "updateencounter":
                        System.out.println("Triggered event: " + event);
                        break;
                    case "kirimcondition":

                        if (ChkSatusehat.isSelected() == true) {
                            System.out.println("Triggered event: " + event);
                            condition(noRawat);
                        }
                        break;
                    case "updatecondition":
                        System.out.println("Triggered event: " + event);

                        break;
                    case "deletecondition":
                        System.out.println("Triggered event: " + event);

                        break;
                    case "insertprocedure":
                        System.out.println("Triggered event: " + event);

                        break;
                    case "deleteprocedure":
                        System.out.println("Triggered event: " + event);

                        break;
                    case "updateprocedure":
                        System.out.println("Triggered event: " + event);

                        break;
                    default:
                        System.out.println("Triggered event: " + event);
                        break;
                }
            } catch (Exception e) {
                System.out.println("Error handling event: " + event + " - " + e);
            }
        } else {
            System.out.println("Service Realtime Status : " + koneksiDB.AKTIFKANREALTIMEKIRIM());
        }

    }

}
