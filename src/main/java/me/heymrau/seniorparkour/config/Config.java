package me.heymrau.seniorparkour.config;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ninja.leaping.configurate.ConfigurationNode;

@Getter
@RequiredArgsConstructor
public class Config {

    private final String name;
    private final String path;
    private final ConfigurationNode node;

}
