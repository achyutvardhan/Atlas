package com.productmodel.productmodel.model;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Images {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID imgId;
    private String ImgFileName;
    private String ImgFileType;
    private String ImgUrl;
    private int ImgFileSize;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
