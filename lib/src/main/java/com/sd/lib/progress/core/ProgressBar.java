package com.sd.lib.progress.core;

public interface ProgressBar extends ProgressView
{
    Integer getStartProgress();

    boolean setStartProgress(Integer progress);
}
