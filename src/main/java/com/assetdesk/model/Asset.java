package com.assetdesk.model;

import java.io.Serializable;
import java.util.Date;

public class Asset implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String serialNumber;
    private String name;
    private String category;
    private AssetStatus status;
    private String assigneeName;
    private String assigneeBadgeId;
    private Date checkedOutDate;

    public Asset() {
    }

    public Asset(Long id, String serialNumber, String name, String category,
                 AssetStatus status, String assigneeName, String assigneeBadgeId, Date checkedOutDate) {
        this.id = id;
        this.serialNumber = serialNumber;
        this.name = name;
        this.category = category;
        this.status = status;
        this.assigneeName = assigneeName;
        this.assigneeBadgeId = assigneeBadgeId;
        this.checkedOutDate = checkedOutDate;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSerialNumber() { return serialNumber; }
    public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public AssetStatus getStatus() { return status; }
    public void setStatus(AssetStatus status) { this.status = status; }

    public String getAssigneeName() { return assigneeName; }
    public void setAssigneeName(String assigneeName) { this.assigneeName = assigneeName; }

    public String getAssigneeBadgeId() { return assigneeBadgeId; }
    public void setAssigneeBadgeId(String assigneeBadgeId) { this.assigneeBadgeId = assigneeBadgeId; }

    public Date getCheckedOutDate() { return checkedOutDate; }
    public void setCheckedOutDate(Date checkedOutDate) { this.checkedOutDate = checkedOutDate; }
}
