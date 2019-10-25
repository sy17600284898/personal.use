package com.personal.use.generator.pojo;

import java.util.Date;

public class organization {
    private Integer id;

    private String name;

    private String logo;

    private String type;

    private Boolean visible;

    private String sourceId;

    private Date created;

    private Date updated;

    private Boolean deleted;

    public organization(Integer id, String name, String logo, String type, Boolean visible, String sourceId, Date created, Date updated, Boolean deleted) {
        this.id = id;
        this.name = name;
        this.logo = logo;
        this.type = type;
        this.visible = visible;
        this.sourceId = sourceId;
        this.created = created;
        this.updated = updated;
        this.deleted = deleted;
    }

    public organization() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo == null ? null : logo.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId == null ? null : sourceId.trim();
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}