package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Properties;

/**
 * The ExportFrame class represents a JFrame for exporting data.
 */
public class ExportFrame extends JFrame {

    private Toolbar toolbar;

    private JButton exportButton;
    private JTextField exportTarget;
    private JLabel exportLabel;
    private PDFGenerator pdfGenerator;

    /**
     * Creates a new ExportFrame instance with the specified title.
     *
     * @param title The title of the frame.
     */
    public ExportFrame(String title) {
        super(title);

        setLayout(null);

        setSize(800, 500);
        setResizable(true);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set custom Icon
        ImageIcon icon = new ImageIcon("assets/NOETOLogo.jpg");
        setIconImage(icon.getImage());

        initToolbar();
        initNoetoLabel();
        initExportButton();
        initExportTargetField();
    }

    private void initToolbar() {
        toolbar = new Toolbar(this);
        toolbar.setVisible(true);
        toolbar.setLocation(0, 0);

        add(toolbar);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                toolbar.setSize(getWidth(), 30);
            }
        });
    }

    private void initNoetoLabel() {
        ImageIcon icon = new ImageIcon("assets/NOETOLogo.jpg");
        Image img = icon.getImage();

        Image newImg = img.getScaledInstance(510, 150, Image.SCALE_SMOOTH);

        ImageIcon newIcon = new ImageIcon(newImg);
        JLabel label = new JLabel(newIcon);
        label.setBounds(getWidth() / 2 - 255, 100, newIcon.getIconWidth(), newIcon.getIconHeight());
        add(label);
    }

    private void initExportButton() {
        exportButton = new JButton("EXPORTIEREN");
        exportButton.setBounds(getWidth() / 2 - 80, 350, 160, 20);
        add(exportButton);

        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (CapacityTable.getData() != null && OccupancyFrame.getSelectedOccupancyData() != null) {
                        pdfGenerator = new PDFGenerator(CapacityTable.getData(), OccupancyFrame.getSelectedOccupancyData());
                    } else {
                        throw new IllegalArgumentException("BITTE WÄHLE ERST WERTE");
                    }
                } catch (IllegalArgumentException iae) {
                    iae.printStackTrace();
                }

                if (exportTarget.getText().contains("@")) {
                    sendMail();
                } else {
                    saveFile();
                }
            }
        });
    }

    private void saveFile() {
        String oldFileName = pdfGenerator.getFileName();
        String targetFolderName = exportTarget.getText();

        Path sourcePath = Paths.get(oldFileName);
        Path targetFolderPath = Paths.get(targetFolderName);

        // Create the target folder if it doesn't exist
        if (!Files.exists(targetFolderPath)) {
            try {
                Files.createDirectories(targetFolderPath);
            } catch (IOException e) {
                System.out.println("Failed to create the target folder: " + e.getMessage());
                return;
            }
        }

        Path targetFilePath = targetFolderPath.resolve(sourcePath.getFileName());

        try {
            Files.move(sourcePath, targetFilePath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File saved to: " + targetFilePath.toAbsolutePath());
        } catch (IOException e) {
            System.out.println("Failed to save the file to the target folder: " + e.getMessage());
        }
    }

    private void sendMail() {
        System.out.println("An email would have been sent if the process was still supported!\nSadly, Google " +
                "canceled our support!");

        /*
        String recipientEmail = exportTarget.getText();
        String subject = "NOE-TO Export";
        String body = "Sehr geehrte Damen und Herren! \nIm Anhang finden Sie die von Ihnen exportierte Datei. \n\n" +
                "Mit freundlichen Grüßen\nIhr NOE-TO Team";

        String senderEmail = "noeto.app@gmail.com";
        String senderPassword = "NOETOAPP";
        String smtpHost = "smtp.gmail.com";
        int smtpPort = 587;

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", smtpHost);
        properties.put("mail.smtp.port", smtpPort);

        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(subject);

            // Create a multipart message
            Multipart multipart = new MimeMultipart();

            // Create the message body part
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(body);

            // Add the message body part to the multipart
            multipart.addBodyPart(messageBodyPart);

            // Create the attachment body part
            MimeBodyPart attachmentBodyPart = new MimeBodyPart();

            String oldFileName = pdfGenerator.getFileName();
            Path sourcePath = Paths.get(oldFileName);

            DataSource source = new FileDataSource(sourcePath.toString());
            attachmentBodyPart.setDataHandler(new DataHandler(source));
            attachmentBodyPart.setFileName(pdfGenerator.getFileName());

            // Add the attachment body part to the multipart
            multipart.addBodyPart(attachmentBodyPart);

            // Set the multipart as the message's content
            message.setContent(multipart);

            Transport.send(message);

            System.out.println("Email sent successfully.");
        } catch (MessagingException e) {
            System.out.println("Failed to send email: " + e.getMessage());
        }
        */
    }

    private void initExportTargetField() {
        exportTarget = new JTextField();
        exportTarget.setBounds(getWidth() / 2 - 220, 325, 440, 20);
        add(exportTarget);

        exportLabel = new JLabel("GEBEN SIE DEN PFAD ZUM ZIELORDNER BITTE HIER EIN:");
        exportLabel.setBounds(getWidth() / 2 - 180, 300, 360, 20);
        add(exportLabel);
    }
}
