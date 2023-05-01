package com.njkongelf.tictactoe.config;

import com.njkongelf.tictactoe.repository.GameDB;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class  Config{

    @Bean
    public GameDB setupInMemoryDB(){
    return new GameDB();
    }
}
