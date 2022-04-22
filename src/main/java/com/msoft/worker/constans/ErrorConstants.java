package com.msoft.worker.constans;

import java.net.URI;

public class ErrorConstants {

    public static final String PROBLEM_BASE_URL = "https://www.reshuffle.com/problem";

    public static final URI ACCOUNT_ALREADY_EXIST = URI.create(PROBLEM_BASE_URL + "/account-exist");

    public static final URI ACCOUNT_NOT_ACTIVATED = URI.create(PROBLEM_BASE_URL + "/account-not-activated");

    public static final URI ACCOUNT_NOT_FOUND = URI.create(PROBLEM_BASE_URL + "/account-not-found");

    public static final URI SOMETHING_NOT_FOUND = URI.create(PROBLEM_BASE_URL + "/account-not-found");

    public static final URI NOT_LOGIN = URI.create(PROBLEM_BASE_URL + "/not-login");
    public static final URI ENTITY_NOT_FOUND = URI.create(PROBLEM_BASE_URL + "/entity-not-found");
}
