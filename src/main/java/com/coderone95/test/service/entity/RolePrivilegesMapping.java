package com.coderone95.test.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "tbl_role_privileges")
public class RolePrivilegesMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "functionality_id")
    @NotNull(message = "Functionality id is mandatory")
    private Long functionalityId;

    @Column(name = "role_id")
    @NotNull(message = "Role id is mandatory")
    private Long roleId;

    @Column(name = "privileges")
    @NotNull(message = "Privilege is mandatory")
    private String privileges;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFunctionalityId() {
        return functionalityId;
    }

    public void setFunctionalityId(Long functionalityId) {
        this.functionalityId = functionalityId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getPrivileges() {
        return privileges;
    }

    public void setPrivileges(String privileges) {
        this.privileges = privileges;
    }

    public RolePrivilegesMapping(){}
    public RolePrivilegesMapping(Long roleId, Long functionalityId){
        this.roleId = roleId;
        this.functionalityId = functionalityId;
    }
}
