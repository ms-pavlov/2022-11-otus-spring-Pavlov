package ru.otus.config;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import ru.otus.annotations.ExpressionsComponent;
import ru.otus.expressions.ExpressionFactory;
import ru.otus.expressions.services.PackageService;


@Component
@AllArgsConstructor
public class ExpressionBeanPostProcessor implements BeanPostProcessor {

    private final PackageService packageService;

    @Override
    public Object postProcessBeforeInitialization(@NotNull Object bean, @Nullable String beanName) throws BeansException {
        if (bean.getClass().isAnnotationPresent(ExpressionsComponent.class)) {
            ExpressionsComponent component = bean.getClass().getAnnotation(ExpressionsComponent.class);
            if (null != component) {
                for (var scopePackage : component.scopePackages()) {
                    packageService.put(component.expression(), scopePackage, (ExpressionFactory) bean);
                }
            }
        }

        return bean;
    }
}
