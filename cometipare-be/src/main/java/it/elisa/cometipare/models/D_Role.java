package it.elisa.cometipare.models;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class D_Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long id;

    @Column
    private ERole name;


    public D_Role() {
    }

    public D_Role(ERole role) {
        this.name = role;
    }
}
