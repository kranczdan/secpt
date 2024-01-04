package at.fhburgenland.product.base;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The BaseMapper helper class to map data between two classes.
 */

public abstract class BaseMapper {

    /**
     * An instance of ModelMapper.
     */
    protected ModelMapper modelMapper;

    /**
     * Constructor for the BaseMapper.
     */
    public BaseMapper() {
        this.modelMapper = new ModelMapper();
        this.modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
    }

    /**
     * <p>
     * Note: outClass object must have default constructor with no arguments
     * </p>
     *
     * @param <D>      type of result object.
     * @param <T>      type of source object to map from.
     * @param entity   entity that needs to be mapped.
     * @param outClass class of result object.
     * @return new object of <code>outClass</code> type.
     */
    public <D, T> D map(Optional<T> entity, Class<D> outClass) {
        if (entity.isPresent()) {
            return modelMapper.map(entity.get(), outClass);
        }
        return null;
    }

    /**
     * <p>
     * Note: outClass object must have default constructor with no arguments
     * </p>
     *
     * @param <D>      type of result object.
     * @param <T>      type of source object to map from.
     * @param entity   entity that needs to be mapped.
     * @param outClass class of result object.
     * @return new object of <code>outClass</code> type.
     */
    public <D, T> D map(T entity, Class<D> outClass) {
        return modelMapper.map(entity, outClass);
    }

    public <D, T> void map(T t, D d) {
        modelMapper.map(t, d);
    }

    /**
     * <p>
     * Note: outClass object must have default constructor with no arguments
     * </p>
     *
     * @param entityList list of entities that needs to be mapped
     * @param outCLass   class of result list element
     * @param <D>        type of objects in result list
     * @param <T>        type of entity in <code>entityList</code>
     * @return list of mapped object with <code><D></code> type.
     */
    public <D, T> List<D> mapAll(Collection<T> entityList, Class<D> outCLass) {
        return entityList.stream().map(entity -> map(entity, outCLass)).collect(Collectors.toList());
    }

    public <D, T> Set<D> mapAllSet(Collection<T> entityList, Class<D> outCLass) {
        return entityList.stream().map(entity -> map(entity, outCLass)).collect(Collectors.toSet());
    }

}