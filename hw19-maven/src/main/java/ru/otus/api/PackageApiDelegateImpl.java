package ru.otus.api;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.openapi.api.PackageApiDelegate;
import ru.otus.openapi.model.PackageResponse;
import ru.otus.securities.PackageService;

@Service
@AllArgsConstructor
public class PackageApiDelegateImpl implements PackageApiDelegate {

    private final PackageService packageService;

    @Override
    public Mono<ResponseEntity<Flux<PackageResponse>>> getPackages(ServerWebExchange exchange) {
        return Mono.just(
                ResponseEntity.ok(
                        packageService.getPackages()));
    }
}
