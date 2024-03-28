package ru.otus.securities;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import ru.otus.annotations.ExpressionsComponent;
import ru.otus.expressions.ExpressionFactory;
import ru.otus.model.enums.Expressions;
import ru.otus.model.enums.ScopePackages;
import ru.otus.openapi.model.PackageResponse;

import java.util.Map;

@Service
public class PackageServiceImpl implements PackageService {

    private final Table<Expressions, ScopePackages, String> packages;


    public PackageServiceImpl(Map<String, ExpressionFactory> expressionSource) {
        this.packages = HashBasedTable.create();
        expressionSource.forEach(
                (name, expression) -> {
                    ExpressionsComponent component = expression.getClass().getAnnotation(ExpressionsComponent.class);
                    if (null != component) {
                        packages.put(component.expression(), component.scopePackages(), component.description());
                    }
                }
        );
    }

    @Override
    public Flux<PackageResponse> getPackages() {
        return Flux.fromStream(
                packages.columnMap()
                        .entrySet()
                        .stream()
                        .map(
                                entry -> new PackageResponse()
                                        .name(entry.getKey().getPackageName())
                                        .description(entry.getKey().getPackageDescription())
                                        .expressions(
                                                entry.getValue().values().stream().toList()
                                        )
                        ));
    }
}
