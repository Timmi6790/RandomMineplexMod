package de.timmi6790.mpmod.utilities;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

@UtilityClass
public class DataUtilities {
    private final Pattern INTEGER_PATTERN = Pattern.compile("^-?\\d+$");
    private final Pattern FLOAT_PATTERN = Pattern.compile("[+-]?(\\d+|\\d+\\.\\d+|\\.\\d+|\\d+\\.)([eE]\\d+)?");
    private final Pattern DOUBLE_PATTERN = Pattern.compile("[\\x00-\\x20]*[+-]?(((((\\p{Digit}+)(\\.)?((\\p{Digit}+)?)([eE][+-]?(\\p{Digit}+))?)|(\\.((\\p{Digit}+))([eE][+-]?(\\p{Digit}+))?)|(((0[xX](\\p{XDigit}+)(\\.)?)|(0[xX](\\p{XDigit}+)?(\\.)(\\p{XDigit}+)))[pP][+-]?(\\p{Digit}+)))[fFdD]?))[\\x00-\\x20]*");
    private final Pattern BOOLEAN_PATTERN = Pattern.compile("true|false", Pattern.CASE_INSENSITIVE);

    public boolean isInt(final Object value) {
        return INTEGER_PATTERN.matcher(String.valueOf(value)).matches();
    }

    public boolean isDouble(final Object value) {
        return DOUBLE_PATTERN.matcher(String.valueOf(value)).matches();
    }

    public boolean isFloat(final Object value) {
        return FLOAT_PATTERN.matcher(String.valueOf(value)).matches();
    }

    public boolean isBoolean(final Object value) {
        return BOOLEAN_PATTERN.matcher(String.valueOf(value)).matches();
    }

    public boolean hasEmptyArg(final String[] args, final int position) {
        return (args.length == position + 1 && args[position].length() == 0);
    }

    public List<String> getStartWithIgnoreCase(final Collection<String> options, final String start) {
        return getStartWithIgnoreCase(options.toArray(new String[0]), start);
    }

    public List<String> getStartWithIgnoreCase(final String[] options, final String start) {
        if (start.length() == 0 || (start.length() == 1 && start.charAt(0) == ' ')) {
            return new ArrayList<>(Arrays.asList(options));
        }

        final List<String> tabCompleteOptions = new ArrayList<>();
        final String startLower = start.toLowerCase();
        for (final String option : options) {
            if (option.toLowerCase().startsWith(startLower) && !option.equalsIgnoreCase(start)) {
                tabCompleteOptions.add(option);
            }
        }

        return tabCompleteOptions;
    }

    public List<String> addToListIfStartWith(final List<String> list, final String condition, final String toCheck) {
        if (toCheck.length() == 0 || (toCheck.length() == 1 && toCheck.charAt(0) == ' ')
                || condition.toLowerCase().startsWith(toCheck.toLowerCase())) {
            list.add(condition);
        }

        return list;
    }

    public void shuffleArray(final int[] array) {
        for (int index = array.length - 1; index > 0; index--) {
            final int switchIndex = ThreadLocalRandom.current().nextInt(index + 1);
            final int temp = array[switchIndex];
            array[switchIndex] = array[index];
            array[index] = temp;
        }
    }

    public List<String> combine(final String prefix, final Collection<String> values) {
        final List<String> combinedValues = new ArrayList<>();
        for (final String value : values) {
            combinedValues.add(prefix + value);
        }

        return combinedValues;
    }

    public boolean containsIgnoreCase(final Collection<String> values, final String search) {
        for (final String value : values) {
            if (value.equalsIgnoreCase(search)) {
                return true;
            }
        }

        return false;
    }

    public void removeIgnoreCase(final Collection<String> values, final String search) {
        values.removeIf(value -> value.equalsIgnoreCase(search));
    }

    public List<String> collectionToLowerCase(final Collection<String> values) {
        final List<String> newValues = new ArrayList<>();
        for (final String value : values) {
            newValues.add(value.toLowerCase());
        }

        return newValues;
    }
}
