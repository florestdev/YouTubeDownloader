package ru.florestdev;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::createAndShowGui);
    }

    private static void createAndShowGui() {
        JFrame frame = new JFrame("YouTube Downloader");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        JTextField urlField = new JTextField(30);
        JTextField dirField = new JTextField(30);
        JButton browseButton = new JButton("Обзор...");
        JButton downloadButton = new JButton("Скачать");
        JTextArea logArea = new JTextArea(8, 40);
        JProgressBar progressBar = new JProgressBar();

        // --- Панель ввода ---
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        inputPanel.add(new JLabel("Ссылка на видео:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1;
        inputPanel.add(urlField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        inputPanel.add(new JLabel("Директория:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1;
        inputPanel.add(dirField, gbc);
        gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0;
        inputPanel.add(browseButton, gbc);

        frame.add(inputPanel, BorderLayout.NORTH);

        // --- Лог ---
        logArea.setEditable(false);
        logArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        frame.add(new JScrollPane(logArea), BorderLayout.CENTER);

        // --- Нижняя панель ---
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 0));
        progressBar.setStringPainted(true);
        bottomPanel.add(progressBar, BorderLayout.CENTER);
        bottomPanel.add(downloadButton, BorderLayout.EAST);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        // --- Слушатели ---
        browseButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                dirField.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        });

        downloadButton.addActionListener(e -> {
            String url = urlField.getText().trim();
            String dir = dirField.getText().trim();

            if (url.isEmpty() || dir.isEmpty()) {
                JOptionPane.showMessageDialog(frame,
                        "Заполните ссылку и директорию.", "Ошибка", JOptionPane.WARNING_MESSAGE);
                return;
            }

            downloadButton.setEnabled(false);
            progressBar.setIndeterminate(true);
            progressBar.setString("Скачивание...");
            logArea.append("Начинаю загрузку: " + url + "\n");

            SwingWorker<Void, Void> worker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() throws Exception {
                    Downloader.download(url, dir);
                    return null;
                }

                @Override
                protected void done() {
                    progressBar.setIndeterminate(false);
                    downloadButton.setEnabled(true);
                    try {
                        get();
                        progressBar.setValue(100);
                        progressBar.setString("Готово");
                        logArea.append("Загрузка завершена успешно.\n");
                    } catch (Exception ex) {
                        progressBar.setValue(0);
                        progressBar.setString("Ошибка");
                        logArea.append("Ошибка: " + ex.getCause() + "\n");
                        JOptionPane.showMessageDialog(frame,
                                "Ошибка загрузки: " + ex.getCause(),
                                "Ошибка", JOptionPane.ERROR_MESSAGE);
                    }
                }
            };
            worker.execute();
        });

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}