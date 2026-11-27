package com.assetdesk.repository;

import com.assetdesk.model.Asset;
import com.assetdesk.model.AssetStatus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * In-memory asset store initialized on startup.
 */
public class InMemoryAssetRepository {

    private final List<Asset> store = new ArrayList<Asset>();
    private long sequence = 100L;

    public void seedDefaults() {
        store.clear();
        store.add(new Asset(nextId(), "MBP-2024-001", "MacBook Pro 16 M3", "Laptop",
                AssetStatus.CHECKED_OUT, "Priya Nair", "EMP-9021-4821", daysAgo(21)));
        store.add(new Asset(nextId(), "MBP-2024-002", "MacBook Air 15 M3", "Laptop",
                AssetStatus.AVAILABLE, null, null, null));
        store.add(new Asset(nextId(), "MON-2023-014", "Dell UltraSharp 27 4K", "Monitor",
                AssetStatus.CHECKED_OUT, "Rohan Kulkarni", "EMP-7710-3319", daysAgo(4)));
        store.add(new Asset(nextId(), "MON-2023-019", "LG DualUp 28", "Monitor",
                AssetStatus.MAINTENANCE, null, null, null));
        store.add(new Asset(nextId(), "DEV-2025-007", "Pixel 9 Pro Test Device", "Mobile",
                AssetStatus.CHECKED_OUT, "Ananya Deshmukh", "EMP-6412-9045", daysAgo(16)));
        store.add(new Asset(nextId(), "ACC-2024-033", "YubiKey 5C NFC Kit", "Security",
                AssetStatus.AVAILABLE, null, null, null));
    }

    private Date daysAgo(int days) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -days);
        return cal.getTime();
    }

    private long nextId() {
        return ++sequence;
    }

    public List<Asset> findAll() {
        return new ArrayList<Asset>(store);
    }

    public Asset findById(Long id) {
        for (Asset a : store) {
            if (a.getId().equals(id)) {
                return a;
            }
        }
        return null;
    }

    public Asset save(Asset asset) {
        if (asset.getId() == null) {
            asset.setId(nextId());
            if (asset.getStatus() == null) {
                asset.setStatus(AssetStatus.AVAILABLE);
            }
            store.add(asset);
        }
        return asset;
    }
}
