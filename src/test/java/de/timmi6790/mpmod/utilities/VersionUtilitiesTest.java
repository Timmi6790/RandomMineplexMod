package de.timmi6790.mpmod.utilities;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class VersionUtilitiesTest {
    @ParameterizedTest
    @ValueSource(strings = {"2", "200000", "1.1.1", "1.0.0.1", "10.0.0", "1.0.1", "1.0.a"})
    void hasNewVersionHigherVersion(final String higherVersion) {
        assertThat(VersionUtilities.hasNewVersion("1.0.0", higherVersion)).isTrue();
    }

    @Test
    void hasNewVersionEqual() {
        assertThat(VersionUtilities.hasNewVersion("1.0.0.", "1.0.0")).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "0.9.9", "0.0.0.0.0.0.0.1"})
    void hasNewVersionLowerVersion(final String lowerVersion) {
        assertThat(VersionUtilities.hasNewVersion("1.0.0", lowerVersion)).isFalse();
    }
}