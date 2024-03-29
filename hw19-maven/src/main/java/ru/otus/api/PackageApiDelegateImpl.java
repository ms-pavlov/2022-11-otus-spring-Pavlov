package ru.otus.api;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.expressions.services.PackageService;
import ru.otus.openapi.api.PackageApiDelegate;
import ru.otus.openapi.model.PackagesGroupResponse;

@Service
@AllArgsConstructor
public class PackageApiDelegateImpl implements PackageApiDelegate {

    private final PackageService packageService;

    @Override
    public Mono<ResponseEntity<Flux<PackagesGroupResponse>>> getPackages(ServerWebExchange exchange) {
        return Mono.just(
                ResponseEntity.ok(
                        packageService.getPackages()));
    }
}
