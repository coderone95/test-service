package com.coderone95.test.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "tbl_functionalities_mst")
public class FunctionalityMst {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "functionality_id")
    private Long id;

    @Column(name = "functionality_code")
    @NotNull(message = "Functionality Code is mandatory")
    private String functionalityCode;

    @Column(name = "functionality_name")
    private String functionalityName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFunctionalityCode() {
        return functionalityCode;
    }

    public void setFunctionalityCode(String functionalityCode) {
        this.functionalityCode = functionalityCode;
    }

    public String getFunctionalityName() {
        return functionalityName;
    }

    public void setFunctionalityName(String functionalityName) {
        this.functionalityName = functionalityName;
    }
}
