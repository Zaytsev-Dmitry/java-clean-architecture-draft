package com.clean.architecture.application.prepared.base;

public interface BusinessBasePrepareData<REQ> {
    REQ prepare(REQ input);
    String getName();
}