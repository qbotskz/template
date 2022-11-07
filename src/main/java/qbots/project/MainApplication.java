package qbots.project;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import qbots.project.configuration.Bot;

@SpringBootApplication
@Slf4j
public class MainApplication implements CommandLineRunner {
	public static void main(String[] args) {
		System.out.println("HEllo");
		SpringApplication.run(MainApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		log.info("ApiContextInitializer.InitNormal()");
		TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
		Bot bot = new Bot();
		try {
			telegramBotsApi.registerBot(bot);
			log.info("Bot was registered: " + bot.getBotUsername());
		} catch (TelegramApiRequestException e) {
			log.error("Error in main class", e);
		}
	}

}
