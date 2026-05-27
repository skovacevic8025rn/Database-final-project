package org.example.util;

import org.example.model.User;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WritingInFile {

    private static final String FILE_PATH = "registrations.txt";
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void log(User user) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_PATH, true))) {
            pw.printf("[%s] Ime: %s, Prezime: %s, Email: %s, Korisnicko ime: %s%n",
                    LocalDateTime.now().format(FMT),
                    user.getName(),
                    user.getSurname(),
                    user.getEmail(),
                    user.getUsername());
        } catch (IOException e) {
            System.err.println("Greska pri pisanju u log fajl: " + e.getMessage());
        }
    }
}