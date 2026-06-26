package io.github.biopuri.astroplanner.core.search;

import io.github.biopuri.astroplanner.core.domain.SkyCondition;

/**
 * Evaluates whether the actual sky condition satisfies
 * the requested sky condition constraint.
 *
 * @author seijime
 */
public class SkyConditionEvaluator {

    /**
     * Checks whether the actual sky condition is dark enough
     * for the requested condition.
     *
     * @param actual actual calculated sky condition.
     * @param required required sky condition from search request.
     * @return {@code true} if the actual sky condition satisfies the requirement.
     */
    public boolean matches(
            SkyCondition actual,
            SkyCondition required
    ) {
        return actual.ordinal() >= required.ordinal();
    }
}
