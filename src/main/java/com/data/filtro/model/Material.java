package com.data.filtro.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "vatlieu")
@Data
@AllArgsConstructor
@Component
@NoArgsConstructor
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mavatlieu")
    private Integer id;

    @Column(name = "tenvatlieu")
    private String materialName;

    @Column(name = "mota")
    private String description;

    @Column(name = "tinhtrang")
    private Integer status;

    @OneToMany(mappedBy = "material", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<Product> products;

}
