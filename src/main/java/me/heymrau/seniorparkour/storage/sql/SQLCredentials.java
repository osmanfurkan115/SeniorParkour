package me.heymrau.seniorparkour.storage.sql;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
public class SQLCredentials {

    private final String host;
    private final String database;
    private final String username;
    private final String password;
}
