package de.timmi6790.basemod.utilities;


import com.google.common.collect.Lists;
import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * List utilities.
 */
@UtilityClass
public class ListUtilities {
    public <T> List<String> toStringList(final T[] values, final Function<T, String> toStringFunction) {
        return toTypeList(Arrays.asList(values), toStringFunction);
    }

    /**
     * Converts the values into a string list
     *
     * @param <T>              type parameter of the values list
     * @param values           the values
     * @param toStringFunction values to string function
     * @return the string list
     */
    public <T> List<String> toStringList(final Collection<T> values, final Function<T, String> toStringFunction) {
        return toTypeList(values, toStringFunction);
    }

    /**
     * Convert the input collection to a type list
     *
     * @param <C>            type parameter of the input collection
     * @param <T>            type parameter of the output list
     * @param values         input values
     * @param toTypeFunction input type to output type function
     * @return the converted list
     */
    public <C, T> List<T> toTypeList(final Collection<C> values, final Function<C, T> toTypeFunction) {
        final List<T> convertedList = Lists.newArrayListWithExpectedSize(values.size());
        for (final C value : values) {
            convertedList.add(toTypeFunction.apply(value));
        }
        return convertedList;
    }
}
