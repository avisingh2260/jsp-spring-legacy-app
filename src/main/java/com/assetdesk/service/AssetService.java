package com.assetdesk.service;

import com.assetdesk.model.Asset;
import com.assetdesk.model.AssetStatus;
import com.assetdesk.repository.InMemoryAssetRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AssetService {

    private static final String DATE_PATTERN = "dd/MM/yyyy";
    private static final SimpleDateFormat LEGACY_FMT = new SimpleDateFormat("dd/MM/yyyy");

    private InMemoryAssetRepository assetRepository;

    public void setAssetRepository(InMemoryAssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    public List<Asset> searchAssets(String query, String statusFilter) {
        List<Asset> result = new ArrayList<Asset>();
        for (Asset asset : assetRepository.findAll()) {
            if (statusFilter != null && !statusFilter.trim().isEmpty() && !"ALL".equals(statusFilter)) {
                if (!asset.getStatus().name().equals(statusFilter)) {
                    continue;
                }
            }
            if (query != null && !query.trim().isEmpty()) {
                String q = query.trim().toLowerCase();
                boolean matchesName = asset.getName() != null && asset.getName().toLowerCase().contains(q);
                boolean matchesSerial = asset.getSerialNumber() != null && asset.getSerialNumber().toLowerCase().contains(q);
                boolean matchesAssignee = asset.getAssigneeName() != null && asset.getAssigneeName().toLowerCase().contains(q);
                if (!matchesName && !matchesSerial && !matchesAssignee) {
                    continue;
                }
            }
            result.add(asset);
        }
        return result;
    }

    public Asset getById(Long id) {
        return assetRepository.findById(id);
    }

    public boolean serialExists(String serialNumber) {
        if (serialNumber == null) {
            return false;
        }
        for (Asset a : assetRepository.findAll()) {
            if (serialNumber.trim().equals(a.getSerialNumber())) {
                return true;
            }
        }
        return false;
    }

    public Asset createAsset(Asset asset) {
        asset.setSerialNumber(asset.getSerialNumber().trim().toUpperCase());
        asset.setStatus(AssetStatus.AVAILABLE);
        return assetRepository.save(asset);
    }

    public void checkoutAsset(Long id, String assigneeName, String badgeId) {
        Asset asset = assetRepository.findById(id);
        if (asset == null) {
            throw new IllegalArgumentException("Asset not found: " + id);
        }
        if (asset.getStatus() != AssetStatus.AVAILABLE) {
            throw new IllegalStateException("Only AVAILABLE assets can be checked out.");
        }
        if (assigneeName == null || assigneeName.trim().isEmpty()) {
            throw new IllegalArgumentException("Assignee name is required.");
        }
        if (badgeId == null || !badgeId.trim().matches("^EMP-\\d{4}-\\d{4}$")) {
            throw new IllegalArgumentException("Badge ID must match EMP-XXXX-XXXX.");
        }
        asset.setStatus(AssetStatus.CHECKED_OUT);
        asset.setAssigneeName(assigneeName.trim());
        asset.setAssigneeBadgeId(badgeId.trim());
        asset.setCheckedOutDate(new Date());
        assetRepository.save(asset);
    }

    public void returnAsset(Long id) {
        Asset asset = assetRepository.findById(id);
        if (asset == null) {
            throw new IllegalArgumentException("Asset not found: " + id);
        }
        if (asset.getStatus() != AssetStatus.CHECKED_OUT) {
            throw new IllegalStateException("Asset is not currently checked out.");
        }
        asset.setStatus(AssetStatus.AVAILABLE);
        asset.setAssigneeName(null);
        asset.setAssigneeBadgeId(null);
        asset.setCheckedOutDate(null);
        assetRepository.save(asset);
    }

    public String formatCheckoutDate(Date date) {
        return date == null ? "-" : LEGACY_FMT.format(date);
    }
}
