package br.com.coletaverde.domain.address.entities;

import br.com.coletaverde.infrastructure.model.PersistenceEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "address")
@Table(name = "addresses")
@EqualsAndHashCode(callSuper = false)
public class Address extends PersistenceEntity implements Serializable {
    
    @Column(name = "public_place", nullable = false, length = 150)
    private String publicPlace;

    @Column(name = "street", nullable = false, length = 150)
    private String street;

    @Column(name = "number", nullable = false, length = 20)
    private String number;

    @Column(name = "complement", length = 100)
    private String complement;

    @Column(name = "city", nullable = false, length = 100)
    private String city;

    @Column(name = "uf", nullable = false, length = 2)
    private String uf;

    @Column(name = "zip_code", nullable = false, length = 10)
    private String zipCode;
}
