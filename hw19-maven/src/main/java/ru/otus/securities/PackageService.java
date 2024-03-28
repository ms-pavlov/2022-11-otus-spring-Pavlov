package ru.otus.securities;

import reactor.core.publisher.Flux;
import ru.otus.openapi.model.PackageResponse;

public interface PackageService {

    Flux<PackageResponse> getPackages();
}
