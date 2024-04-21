package com.example.spring.authentication.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ObjectMapperUtils {
    public static ModelMapper mapper;

    static {
        mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    public static <D, T> D map(final T data, Class<D> targetClazz) {
        return mapper.map(data, targetClazz);
    }

    public static <D, T> List<D> mapAll(final Collection<T> collections, Class<D> targetClazz) {
        return collections.stream()
                .map(entity -> map(entity, targetClazz))
                .collect(Collectors.toList());
    }
}
