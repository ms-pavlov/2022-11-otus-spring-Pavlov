package ru.otus.api;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.otus.expressions.services.PackageService;
import ru.otus.openapi.api.PackageApiDelegate;
import ru.otus.openapi.model.PackagesGroupResponse;

import java.util.List;

@Service
@AllArgsConstructor
public class PackageApiDelegateImpl implements PackageApiDelegate {

    private final PackageService packageService;

    @Override
    public ResponseEntity<List<PackagesGroupResponse>> getPackages() {
        return ResponseEntity.ok(
                packageService.getPackages());
    }
}
