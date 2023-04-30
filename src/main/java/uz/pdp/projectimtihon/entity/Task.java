package uz.pdp.projectimtihon.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private Integer taskNumber;
    @Column(nullable = false)
    private String name;
    @ManyToOne
    @Column(nullable = false)
    private Section section;
    private String description;
    private LocalDate beginDate;
    private LocalDate endDate;
    private String assignee;
    private String dependency;
}
