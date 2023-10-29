package racingcar.controller;

import camp.nextstep.edu.missionutils.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import racingcar.domain.Car;
import racingcar.domain.Validator;
import racingcar.view.UserOutput;

public class RacingGame {
    private static RacingGame instance; // 게임을 진행할 싱글톤 인스턴스
    private final List<Car> cars;

    private RacingGame() {
        cars = new ArrayList<>();
    }

    private static RacingGame getInstance() {
        if (instance == null) {
            instance = new RacingGame();
        }
        return instance;
    }

    public static void run(){

        UserOutput.askForCarNames();
        String carNames = Console.readLine();

        if(Validator.isEmpty(carNames)){
            throw new IllegalArgumentException("자동차 이름을 입력해야 합니다.");
        }

        RacingGame game = RacingGame.getInstance();
        game.addCar(carNames);

        UserOutput.askForTryCount();
        String rounds = Console.readLine();

        if(Validator.isNotNumber(rounds)){
            throw new IllegalArgumentException("숫자만 입력해야 합니다.");
        }
        game.play(rounds);
        String result = game.getWinners();
        UserOutput.showWinners(result);

        Console.close();
    }

    private void addCar(String input) {

        String[] carNames = input.split(",");

        for (String name : carNames) {
            if (Validator.isValidCarName(name)) {
                throw new IllegalArgumentException("자동차 이름은 5자 이하만 가능합니다.");
            }
            Car car = new Car(name);
            cars.add(car);
        }
    }

    private void play(String round) {

        int rounds = Integer.parseInt(round);

        for (int i = 0; i < rounds; i++) {
            playRound();
        }
        UserOutput.printNewLine();
    }

    private void playRound() {
        moveAllCars();
        displayAllCars();
        UserOutput.printNewLine();
    }

    private void moveAllCars() {
        for (Car car : cars) {
            car.move();
        }
    }

    private void displayAllCars() {
        for (Car car : cars) {
            car.display();
        }
    }

    private String getWinners() {
        List<String> winners = cars.stream()
                .filter(car -> car.getPosition() == getMaxPosition())
                .map(Car::getName)
                .collect(Collectors.toList());

        return String.join(", ", winners);
    }

    private int getMaxPosition(){
        return cars.stream()
                .mapToInt(Car::getPosition)
                .max()
                .orElse(-1);
    }

}
