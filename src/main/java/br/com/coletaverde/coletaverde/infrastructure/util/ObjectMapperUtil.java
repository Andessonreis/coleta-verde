package br.com.coletaverde.coletaverde.infrastructure.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Utility class for mapping objects and handling JSON transformations.
 * Designed for internal service and DTO conversions.
 */
@Component
public class ObjectMapperUtil {

    private static final Logger LOGGER = Logger.getLogger(ObjectMapperUtil.class.getName());

    private static final ModelMapper MODEL_MAPPER = new ModelMapper();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        MODEL_MAPPER.getConfiguration()
                .setAmbiguityIgnored(true)
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);
    }

    /**
     * Maps one object to another existing target instance using ModelMapper.
     *
     * @param source the source object
     * @param target the target object to be populated
     * @param <S>    the source type
     * @param <T>    the target type
     * @return the updated target object
     */
    public <S, T> T map(final S source, final T target) {
        if (source == null || target == null) {
            LOGGER.warning("Mapping skipped due to null source or target.");
            return target;
        }

        try {
            MODEL_MAPPER.map(source, target);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Failed to map source to target: " +
                    source.getClass().getName() + " → " + target.getClass().getName(), ex);
        }

        return target;
    }

    /**
     * Maps a source object to a new instance of the target class.
     *
     * @param source      the source object
     * @param targetClass the class of the target type
     * @param <S>         the source type
     * @param <T>         the target type
     * @return a new mapped instance of the target type, or null if source is null
     */
    public <S, T> T map(final S source, final Class<T> targetClass) {
        if (source == null || targetClass == null) {
            LOGGER.warning("Mapping skipped due to null source or targetClass.");
            return null;
        }

        try {
            return MODEL_MAPPER.map(source, targetClass);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Failed to map source to target class: " +
                    source.getClass().getName() + " → " + targetClass.getName(), ex);
            return null;
        }
    }

    /**
     * Maps a collection of source objects to a list of target objects.
     *
     * @param sourceList  the list of source objects
     * @param targetClass the class of the target type
     * @param <D>         the target type
     * @param <T>         the source type
     * @return a list of mapped target objects; empty list if source is null/empty
     */
    public <D, T> List<D> mapAll(final Collection<T> sourceList, final Class<D> targetClass) {
        if (sourceList == null || sourceList.isEmpty()) {
            return Collections.emptyList();
        }

        return sourceList.stream()
                .map(source -> map(source, targetClass))
                .collect(Collectors.toList());
    }

    /**
     * Serializes an object to a JSON string using Jackson.
     *
     * @param obj the object to serialize
     * @return JSON string, or empty string if serialization fails
     */
    public String objectToJson(final Object obj) {
        if (obj == null) {
            LOGGER.warning("Serialization skipped: object is null.");
            return "";
        }

        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            LOGGER.log(Level.SEVERE, "Failed to serialize object to JSON: " + obj.getClass().getName(), e);
            return "";
        }
    }

    /**
     * Deserializes a JSON string into an object of the specified class.
     *
     * @param json        the JSON string
     * @param targetClass the target class
     * @param <T>         the type of the returned object
     * @return an instance of the target class, or null if deserialization fails
     */
    public <T> T jsonToObject(final String json, final Class<T> targetClass) {
        if (json == null || json.trim().isEmpty() || targetClass == null) {
            LOGGER.warning("Deserialization skipped due to null/empty input or target class.");
            return null;
        }

        try {
            return OBJECT_MAPPER.readValue(json, targetClass);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to deserialize JSON to object: " + targetClass.getName(), e);
            return null;
        }
    }
}
