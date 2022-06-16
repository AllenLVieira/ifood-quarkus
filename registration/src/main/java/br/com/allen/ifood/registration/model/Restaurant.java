package br.com.allen.ifood.registration.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "restaurant")
public class Restaurant  extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String owner;

    public String cnpj;

    public String name;

    @ManyToOne
    public Localization Localization;

    @CreationTimestamp
    public Date creationDate;

    @UpdateTimestamp
    public Date updateDate;
}
