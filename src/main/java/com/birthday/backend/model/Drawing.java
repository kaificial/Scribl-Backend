package com.birthday.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
public class Drawing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")
    @JsonIgnore
    private Card card;

    @Column(columnDefinition = "TEXT")
    private String imageData; // base64 string

    private String authorName;

    private double x;
    private double y;
    private String userId; // who made this
    private Double width;
    private Double height;
    private double rotation;

    @Column(columnDefinition = "TEXT")
    private String contentJson; // json string with all the editor bits
}
