package org.example.service;


import org.example.model.Istrazivac;
import org.example.model.User;
import org.example.repository.IstrazivacRepository;
import org.example.repository.UserRepository;
import org.example.util.WritingInFile;

import java.sql.SQLException;
import java.util.Optional;

public class AuthService {

    private final UserRepository userRepository;

    public AuthService() {
        this.userRepository = new UserRepository();
    }

    public Optional<Istrazivac> login(String korisnickoIme, String lozinka) throws SQLException {
        if (korisnickoIme == null || korisnickoIme.isBlank()) {
            throw new IllegalArgumentException("Korisničko ime ne sme biti prazno.");
        }
        if (lozinka == null || lozinka.isBlank()) {
            throw new IllegalArgumentException("Lozinka ne sme biti prazna.");
        }
        System.out.println("Loguje se: " + korisnickoIme);
        return userRepository.findByKorisnickoImeILozinka(korisnickoIme, lozinka);
    }

    public Istrazivac register(String kontakt, String korisnickoIme, String lozinka) throws SQLException {
        IstrazivacRepository istrazivacRepo = new IstrazivacRepository();
        Istrazivac ist = istrazivacRepo.findByKontakt(kontakt)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Email '" + kontakt + "' nije pronađen u registru istraživača."));

        if (userRepository.existsByKorisnickoIme(korisnickoIme)) {
            throw new IllegalArgumentException("Korisničko ime '" + korisnickoIme + "' je već zauzeto.");
        }
        if (userRepository.existsByEmail(kontakt)) {
            throw new IllegalArgumentException("Email '" + kontakt + "' je već registrovan.");
        }

        User user = new User(ist.getName(), ist.getSurname(), kontakt, korisnickoIme, lozinka);
        boolean uspesno = userRepository.save(user, ist.getIstrazivacId());
        if (!uspesno) {
            throw new SQLException("Registracija nije uspela. Pokušajte ponovo.");
        }
        WritingInFile.log(user);
        return ist;
    }
}
