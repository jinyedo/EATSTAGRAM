package daelim.project.eatstagram.service.base;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.convention.NameTokenizers;
import org.modelmapper.spi.MatchingStrategy;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ModelMapperUtils {

    private static final Map<EModelMapperType , ModelMapper> modelMappers = new HashMap<>();

    private static ModelMapper getModelMapper() {
        return getModelMapper(EModelMapperType.STANDARD);
    }

    private static ModelMapper getModelMapper(EModelMapperType mapperType) {
        if(!modelMappers.containsKey(mapperType)) {
            modelMappers.put(mapperType, getInstance(mapperType.getMatchingStrategy()));
        }
        return modelMappers.get(mapperType);
    }

    private static ModelMapper getInstance(MatchingStrategy matchingStrategy) {
        var modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(matchingStrategy)
                .setSourceNameTokenizer(NameTokenizers.UNDERSCORE)
                .setDestinationNameTokenizer(NameTokenizers.UNDERSCORE)
                .setSkipNullEnabled(true);
        return modelMapper;
    }

    public static void map(Object source, Object dest) throws MappingException {
        if(source != null) {
            getModelMapper().map(source, dest);
        }
    }

    public static void map(Object source, Object dest, EModelMapperType mapperType) throws MappingException {
        if(source != null) {
            getModelMapper(mapperType).map(source, dest);
        }
    }

    public static <T> T map(Object source, Type type) throws MappingException {
        if(source != null) {
            return getModelMapper().map(source, type);
        }
        return null;
    }

    public static <T> T map(Object source, Type type, EModelMapperType mapperType) throws MappingException {
        if(source != null) {
            return getModelMapper(mapperType).map(source, type);
        }
        return null;
    }

    public static <T> T map(Object source, Class<T> destinationClass) throws MappingException {
        if(source != null) {
            return getModelMapper().map(source, destinationClass);
        }
        return null;
    }

    public static <T> T map(Object source, Class<T> destinationClass, EModelMapperType mapperType) throws MappingException {
        if(source != null) {
            return getModelMapper(mapperType).map(source, destinationClass);
        }
        return null;
    }

    public static <T> List<T> mapList(List<?> sources, Class<T> destinationClass) {
        if(sources != null) {
            return sources.stream().map(entity -> getModelMapper().map(entity, destinationClass))
                    .collect(Collectors.toList());
        }
        return Collections.<T>emptyList();
    }

    public static <T> List<T> mapList(List<?> sources, Class<T> destinationClass, EModelMapperType mapperType) {
        if(sources != null) {
            return sources.stream().map(entity -> getModelMapper(mapperType).map(entity, destinationClass))
                    .collect(Collectors.toList());
        }
        return Collections.<T>emptyList();
    }

    @RequiredArgsConstructor
    @Getter
    public enum EModelMapperType {
        LOOSE(MatchingStrategies.LOOSE), STANDARD(MatchingStrategies.STANDARD), STRICT(MatchingStrategies.STRICT);
        private final MatchingStrategy matchingStrategy;
    }
}