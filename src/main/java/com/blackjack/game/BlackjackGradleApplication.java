package com.blackjack.game;

import com.blackjack.game.GameModels.GameManager;
import com.blackjack.game.GameModels.PlayerManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@ComponentScan("com.blackjack.game.Controllers")
public class BlackjackGradleApplication {

	@Bean
	public GameManager gameManager() {
		return new GameManager();
	}

	@Bean
	public PlayerManager playerManager() {
		return new PlayerManager();
	}

	public static void main(String[] args) {
		SpringApplication.run(BlackjackGradleApplication.class, args);
	}
}
