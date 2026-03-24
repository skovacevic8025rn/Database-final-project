package org.example.service;


import org.example.model.User;
import org.example.repository.UserRepository;

import java.sql.SQLException;
import java.util.Optional;

public class AuthService {

    private final UserRepository userRepository;

    public AuthService() {
        this.userRepository = new UserRepository();
    }

    /**
     * Prijavljuje korisnika.
     * Vraća User objekat ako su podaci ispravni, ili prazan Optional ako nisu.
     *
     * @param korisnickoIme korisničko ime
     * @param lozinka       lozinka
     * @return Optional<User>
     * @throws SQLException ako dođe do greške pri radu sa bazom
     */
    public Optional<User> login(String korisnickoIme, String lozinka) throws SQLException {
        if (korisnickoIme == null || korisnickoIme.isBlank()) {
            throw new IllegalArgumentException("Korisničko ime ne sme biti prazno.");
        }
        if (lozinka == null || lozinka.isBlank()) {
            throw new IllegalArgumentException("Lozinka ne sme biti prazna.");
        }
        System.out.println("Loguje se: " + korisnickoIme + " " + lozinka);
        return userRepository.findByKorisnickoImeILozinka(korisnickoIme, lozinka);
    }

    /**
     * Registruje novog korisnika.
     * Proverava da li korisničko ime ili email već postoje pre upisa.
     *
     * @param user User objekat sa svim podacima
     * @throws SQLException             ako dođe do greške pri radu sa bazom
     * @throws IllegalArgumentException ako korisničko ime ili email već postoje
     */
    public void register(User user) throws SQLException {
        if (user.getName() == null || user.getName().isBlank()) {
            throw new IllegalArgumentException("Ime je obavezno.");
        }
        if (user.getSurname() == null || user.getSurname().isBlank()) {
            throw new IllegalArgumentException("Prezime je obavezno.");
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email je obavezan.");
        }
        if (user.getUsername() == null || user.getUsername().length() < 3) {
            throw new IllegalArgumentException("Korisničko ime mora imati najmanje 3 karaktera.");
        }
        if (user.getPassword() == null || user.getPassword().length() < 8) {
            throw new IllegalArgumentException("Lozinka mora imati najmanje 8 karaktera.");
        }
        if (user.getRole() == null || user.getRole().isBlank()) {
            throw new IllegalArgumentException("Uloga je obavezna.");
        }
        if (userRepository.existsByKorisnickoIme(user.getUsername())) {
            throw new IllegalArgumentException("Korisničko ime '" + user.getUsername() + "' je već zauzeto.");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email '" + user.getEmail() + "' je već registrovan.");
        }

        boolean uspesno = userRepository.save(user);
        if (!uspesno) {
            throw new SQLException("Registracija nije uspela. Pokušajte ponovo.");
        }
    }
}
