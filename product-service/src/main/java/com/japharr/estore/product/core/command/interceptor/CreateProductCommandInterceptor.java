package com.japharr.estore.productservice.core.command.interceptor;

import com.japharr.estore.productservice.core.command.CreateProductCommand;
import com.japharr.estore.productservice.entity.ProductLookupEntity;
import com.japharr.estore.productservice.repository.ProductLookRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.BiFunction;

@Slf4j
@Component
public class CreateProductCommandInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {
    private final ProductLookRepository productLookRepository;

    public CreateProductCommandInterceptor(ProductLookRepository productLookRepository) {
        this.productLookRepository = productLookRepository;
    }

    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(List<? extends CommandMessage<?>> list) {
        return (index, command) -> {
            log.info("CreateProductCommandInterceptor: {}", command);
            if(CreateProductCommand.class.equals(command.getPayloadType())) {
                CreateProductCommand createProductCommand = (CreateProductCommand) command.getPayload();

                ProductLookupEntity entity = productLookRepository.findByProductIdOrTitle(createProductCommand.getProductId(),
                        createProductCommand.getTitle());
                if(entity != null) {
                    throw new IllegalArgumentException("Product with this id or title already exist");
                }
            }
            return command;
        };
    }
}
