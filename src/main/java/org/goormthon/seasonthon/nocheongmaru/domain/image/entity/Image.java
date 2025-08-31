package org.goormthon.seasonthon.nocheongmaru.domain.image.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.goormthon.seasonthon.nocheongmaru.domain.information.entity.Information;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "images")
@Entity
public class Image {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;
    
    @Lob
    @Column(name = "image_url", nullable = false)
    private String imageUrl;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "information_id")
    private Information information;
    
    @Builder
    private Image(String imageUrl, Information information) {
        this.imageUrl = imageUrl;
        this.information = information;
    }
    
}
