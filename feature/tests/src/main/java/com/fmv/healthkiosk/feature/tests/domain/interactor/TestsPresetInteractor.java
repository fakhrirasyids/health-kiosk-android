package com.fmv.healthkiosk.feature.tests.domain.interactor;

import com.fmv.healthkiosk.feature.tests.domain.model.TestItem;
import com.fmv.healthkiosk.feature.tests.domain.repo.TestsRepository;
import com.fmv.healthkiosk.feature.tests.domain.usecase.TestsPresetUseCase;

import java.util.List;

import javax.inject.Inject;

public class TestsPresetInteractor implements TestsPresetUseCase {
    private final TestsRepository testsRepository;

    @Inject
    public TestsPresetInteractor(TestsRepository testsRepository) {
        this.testsRepository = testsRepository;
    }

    @Override
    public List<TestItem> getTestItems(String testsPreset) {
        return testsRepository.getTestItems(testsPreset);
    }
}
