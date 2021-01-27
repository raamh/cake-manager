package com.waracle.cakemgr;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "cake")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cake {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false, length = 100)
    private String title;

    @Column(name = "description", nullable = false, length = 100)
    private String desc;

    @Column(name = "image_url", nullable = false, length = 300)
    private String image;
}