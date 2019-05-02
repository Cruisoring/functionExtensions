package io.github.cruisoring.logger;

import io.github.cruisoring.utility.StringHelper;

/**
 * Interface to define capabilities to make log messages to be colorful
 */
public interface IWithColor {

    //Default keywords to be matched with the template to see if message means success
    String[] DefaultSuccessKeywords = new String[]{"success", "passed", "pass"};
    //Default keywords to be matched with the template to see if message means failure
    String[] DefaultFailedKeywords = new String[]{"fail", "error", "exception", "wrong", "mistake", "problem"};

    /**
     * Wrap the text with color asscciated with the given LogLevel.
     *
     * @param level the <code>LogLevel</code> of the final log containing the given <code>text</code>
     * @param text  the text to be displayed with the color of the associated <code>level</code>
     * @return the modified text which would be displayed with color of the associated <code>level</code>
     */
    String withColor(LogLevel level, String text);

    /**
     * Provide replacement for Regex matchings to indicate fail result.
     *
     * @return the replacement of Regex matching that indicate it as fail.
     */
    String failPlaceholder();

    /**
     * Provide replacement for Regex matchings to indicate success result.
     *
     * @return the replacement of Regex matching that indicate it as success.
     */
    String successPlaceholder();

    /**
     * Provide replacement for Regex matchings to indicate neither success nor fail result.
     *
     * @return the replacement of Regex matching that indicate it as neither success nor fail.
     */
    String normalPlaceholder();

    /**
     * Eveluate if the message created from the given <code>format</code> is failed by matching the <code>DefaultFailedKeywords</code>
     *
     * @param format the format string used by String.format() to generate final message.
     * @return <code>true</code> if the format containing any fail keyword in the <code>DefaultFailedKeywords</code> case-insensitive
     */
    default boolean isFailed(String format) {
        return StringHelper.containsAnyIgnoreCase(format, DefaultFailedKeywords);
    }

    /**
     * Eveluate if the message created from the given <code>format</code> is success by matching the <code>DefaultSuccessKeywords</code>
     *
     * @param format the format string used by String.format() to generate final message.
     * @return <code>true</code> if the format containing any success keyword in the <code>DefaultSuccessKeywords</code> case-insensitive
     */
    default boolean isSuccess(String format) {
        return StringHelper.containsAnyIgnoreCase(format, DefaultSuccessKeywords);
    }

    /**
     * Default implementation to highlight the argument placeholders in given format with fail, success or normal colors.
     *
     * @param format the format string used by String.format() to generate final message.
     * @return the modified format with its arguments highlighted with fail, success or normal colors.
     */
    default String highlightArgs(String format) {
        String highlighted;

        if (isFailed(format)) {
            highlighted = format.replaceAll("%(\\d*$)?(\\d*)?\\S", failPlaceholder());
        } else if (isSuccess(format)) {
            highlighted = format.replaceAll("%(\\d*$)?(\\d*)?\\S", successPlaceholder());
        } else {
            highlighted = format.replaceAll("%(\\d*$)?(\\d*)?\\S", normalPlaceholder());
        }
        return highlighted;
    }

}
