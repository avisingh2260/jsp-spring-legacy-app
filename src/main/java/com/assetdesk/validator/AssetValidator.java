package com.assetdesk.validator;

import com.assetdesk.model.Asset;
import com.assetdesk.service.AssetService;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class AssetValidator implements Validator {

    private AssetService assetService;

    public void setAssetService(AssetService assetService) {
        this.assetService = assetService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Asset.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Asset asset = (Asset) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "serialNumber", "required.serial", "Serial number is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "required.name", "Asset name is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "category", "required.category", "Category is required.");

        if (asset.getSerialNumber() != null && !asset.getSerialNumber().trim().isEmpty()) {
            if (!asset.getSerialNumber().trim().matches("^[A-Za-z]{3}-\\d{4}-\\d{3}$")) {
                errors.rejectValue("serialNumber", "format.serial", "Serial must match AAA-YYYY-NNN (e.g. MBP-2026-009).");
            } else if (assetService.serialExists(asset.getSerialNumber())) {
                errors.rejectValue("serialNumber", "duplicate.serial", "Serial number already exists.");
            }
        }
    }
}
