package ru.otus.expressions.storage;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.expressions.services.PackageService;
import ru.otus.model.enums.ScopePackages;

@Component
@AllArgsConstructor
public class StorageInitPluginImpl implements StorageInitPlugin {

    private final PackageService packageService;

    @Override
    public void execute(ExpressionStorage storage) {
        storage.put(
                packageService.getPackageExpressions(
                        ScopePackages.DEFAULT.getPackageName())
        );
    }
}
