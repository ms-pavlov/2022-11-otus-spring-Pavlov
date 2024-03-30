package ru.otus.expressions.services;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import ru.otus.annotations.ExpressionsComponent;
import ru.otus.expressions.ExpressionFactory;
import ru.otus.model.enums.Expressions;
import ru.otus.model.enums.ScopePackages;
import ru.otus.openapi.model.PackageResponse;
import ru.otus.openapi.model.PackagesGroupResponse;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PackageServiceImpl implements PackageService {

    private final Table<Expressions, ScopePackages, ExpressionFactory> packages;

    public PackageServiceImpl() {
        this.packages = HashBasedTable.create();
    }

    @Override
    public void put(ExpressionFactory expressionFactory) {
        ExpressionsComponent component = expressionFactory.getClass().getAnnotation(ExpressionsComponent.class);
        if (null != component) {
            packages.put(component.expression(), component.scopePackages(), expressionFactory);
        }
    }

    @Override
    public Flux<PackagesGroupResponse> getPackages() {
        return Flux.fromStream(
                packages.columnMap()
                        .entrySet()
                        .stream()
                        .collect(Collectors.groupingBy(entry -> entry.getKey().getPackageGroup()))
                        .entrySet()
                        .stream()
                        .map(
                                entry -> new PackagesGroupResponse()
                                        .name(entry.getKey().getGroupName())
                                        .description(entry.getKey().getGroupDescription())
                                        .packages(
                                                entry.getValue()
                                                        .stream()
                                                        .map(
                                                                item -> new PackageResponse()
                                                                        .name(item.getKey().getPackageName())
                                                                        .description(item.getKey().getPackageDescription())
                                                        )
                                                        .toList()
                                        )
                        ));
    }

    @Override
    public Map<Expressions, ExpressionFactory> getPackageExpressions(String packageName) {
        return packages.columnMap()
                .getOrDefault(ScopePackages.getByName(packageName), Map.of());
    }
}
