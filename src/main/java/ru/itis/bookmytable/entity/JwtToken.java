package ru.itis.bookmytable.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tokens")
public class JwtToken {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(unique = true)
    private String value;

    private boolean revoked;
}
