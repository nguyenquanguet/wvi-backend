package com.vnmo.backend.service;

import java.util.Locale;

public interface MessageService {

    public String getMessage(String key, Object[] args);
    public String getMessage(String key, Object[] args, Locale locale);
}
