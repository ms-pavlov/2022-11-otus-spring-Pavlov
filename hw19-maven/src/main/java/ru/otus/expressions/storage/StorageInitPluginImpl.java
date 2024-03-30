package ru.otus.expressions.storage;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.expressions.services.PackageService;
import ru.otus.model.enums.ScopePackages;
import ru.otus.repositories.ScopeRepository;

@Component
@AllArgsConstructor
public class StorageInitPluginImpl implements StorageInitPlugin {

    private final PackageService packageService;
    private final ScopeRepository scopeRepository;

    @Override
    public void execute(ExpressionStorage storage) {
        var scope = scopeRepository.findByName(storage.getScope()).block();
        storage.put(
                packageService.getPackageExpressions(
                        ScopePackages.DEFAULT.getPackageName())
        );
        if (null != scope) {
            scope.getPackages()
                    .stream()
                    .map(packageService::getPackageExpressions)
                    .forEach(storage::put);
        }
    }
}
