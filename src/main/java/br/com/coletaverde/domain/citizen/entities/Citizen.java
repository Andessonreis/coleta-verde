package br.com.coletaverde.domain.citizen.entities;

import br.com.coletaverde.domain.address.entities.Address;
import br.com.coletaverde.domain.user.entities.User;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "citizen")
@Table(name = "citizens")
@EqualsAndHashCode(callSuper = false)
public class Citizen extends User {

    @OneToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @Column(name = "phone", nullable = false, length = 20)
    private String phone;
}
