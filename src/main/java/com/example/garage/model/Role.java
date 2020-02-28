package com.example.garage.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) //Inkrementowanie jest po stronie bazy danych
    @Column(name = "role_id")
    private int id;

    @NotNull
    private String role;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
