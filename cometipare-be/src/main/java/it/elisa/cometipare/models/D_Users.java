package it.elisa.cometipare.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor //crea da se getter e setter, costruttore vuoto, costruttore con tutte le variabili
public class D_Users {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String username;

    private String password;

    private String nome;

    private String cognome;

    private String email;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Column
    private Set<D_Role> roles = new HashSet<D_Role>();

    public D_Users(String username, String email, String encode) {
        this.username = username;
        this.email = email;
        this.password = encode;
    }
}
